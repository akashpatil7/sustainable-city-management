import numpy as np
from sklearn.linear_model import LinearRegression
from sklearn.ensemble import RandomForestRegressor
import pickle
import time
from datetime import datetime
from datetime import timedelta
from src.utils import get_testing_data_using_epoch
from src.common.response import Response
from enum import Enum

class EndPointMethods(Enum):
    getPedestrianPredictions = "get_pedestrian_predictions"
    getPedestrianRecommendationFromPrediction = "get_recommendation_pedestrian_predictions"
    getPedestrianRecommendationFromFuturePrediction = "get_recommendation_pedestrian_future_predictions"
    trainModel = 'train_pedestrian_model'

class PedestrianModel():
    def __init__(self, db):
        print("Initialising Pedestrian Model")
        self.db = db
        self.LOCATION_TO_ID = {'Westmoreland Street West/Carrolls': 1, 'Henry Street/Coles Lane/Dunnes': 2, "O'Connell st/Princes st North": 3, 'College Green/Church Lane': 4, 'Dawson Street/Molesworth': 5, 
        'Talbot st/Guineys': 6, 'Grafton Street / Nassau Street / Suffolk Street': 7, 'North Wall Quay/Samuel Beckett bridge West': 8, 'Grafton Street/CompuB': 9, 'Mary st/Jervis st': 10, 'Dame Street/Londis': 11, 
        'Newcomen Bridge/Charleville mall inbound': 12, 'College st/Westmoreland st': 13, 'Bachelors walk/Bachelors way': 14, "D'olier st/Burgh Quay": 15, 'Richmond st south/Portabello Harbour inbound': 16, 
        'Westmoreland Street East/Fleet street': 17, 'Grand Canal st upp/Clanwilliam place/Google': 18, 'Grand Canal st upp/Clanwilliam place': 19, 'Baggot st lower/Wilton tce inbound': 20, 
        'Phibsborough Rd/Munster St': 21, 'North Wall Quay/Samuel Beckett bridge East': 22, 'Grafton st/Monsoon': 23, 'Baggot st upper/Mespil rd/Bank': 24, 'Talbot st/Murrays Pharmacy': 25, 
        "O'Connell St/Parnell St/AIB": 26, 'Phibsborough Rd/Enniskerry Road': 27, 'College Green/Bank Of Ireland': 28, 'Capel st/Mary street': 29}


    def perform_action(self, action):
            try:
                return getattr(self, EndPointMethods[action].value)()
            except KeyError:
                print("[Pedestrian Model] EndPoint not found")
            except AttributeError:
                print("[Pedestrian Model] EndPoint cannot be resolved")
            return Response.not_found_404("Pedestrian Model: " + action +
                                        " not found")


    def train_pedestrian_model(self):
        # get data from db
        collection = self.db.get_collection("Pedestrian")
        data = collection.find()
        
        if data != []:
            streets = []
            count = []
            time = []
            for doc in data:
                streets.append(self.LOCATION_TO_ID[doc['street']])
                count.append(doc['count'])
                time.append(doc['time'])

            X = np.column_stack((streets, time))
            y_count = np.array(count)

            # train model with x features being the street name and the time, and the y being the count
            model = RandomForestRegressor(n_estimators=15, max_depth=10, criterion='mse')
            model.fit(X, y_count)

            to_db = pickle.dumps(model)

            date = datetime.now()
            new_entry = {"$set": {"model": to_db, "date_of_training": date}}

            info = self.db.get_collection('predictive_models').update_one({"indicator": "pedestrian"}, new_entry)
            print("Pedestrian Model saved to DB")
            print(info)

            return Response.send_json_200(info._UpdateResult__raw_result)


    def get_predictions(self, unixTime):
        collection = self.db.get_collection("predictive_models")
        model_ = collection.find_one({"indicator": "pedestrian"})['model']

        model = pickle.loads(model_)

        x_test = get_testing_data_using_epoch(self.LOCATION_TO_ID, unixTime)
        preds = model.predict(x_test)

        response_predictions = []

        # assign the predictions to each location
        for x, p in zip(x_test, preds):
            index_of_loc = list(self.LOCATION_TO_ID.values()).index(x[0])
            loc = list(self.LOCATION_TO_ID.keys())[index_of_loc]

            doc = self.db.get_collection("Pedestrian").find_one({"street": loc})
            obj = {"count": round(p), "street": loc, "streetLatitude": doc['streetLatitude'], "streetLongitude": doc['streetLongitude'], "time": x[1], "id": doc['_id']}
            response_predictions.append(obj)
        return response_predictions

    def get_pedestrian_predictions(self):
        response_predictions = self.get_predictions(time.time())
        return Response.send_json_200(response_predictions)

    def get_recommendation_pedestrian_predictions(self):
        response_predictions = self.get_predictions(time.time())
        sortedList = sorted(response_predictions, key=lambda d: d["count"])
        lowest_count_pedestrian_data = sortedList[:5]
        highest_count_pedestrian_data = sortedList[-5:]
        highest_count_pedestrian_data.reverse()

        data = {
            'lowestCountPedestrianData': lowest_count_pedestrian_data,
            'highestCountPedestrianData': highest_count_pedestrian_data
        }
        return Response.send_json_200(data)

    def get_recommendation_pedestrian_future_predictions(self):
        dtime = datetime.now() + timedelta(minutes=10)
        unixtime = time.mktime(dtime.timetuple())

        response_predictions = self.get_predictions(unixtime)
        sortedList = sorted(response_predictions, key=lambda d: d["count"])
        lowest_count_pedestrian_data = sortedList[:5]
        highest_count_pedestrian_data = sortedList[-5:]
        highest_count_pedestrian_data.reverse()

        data = {
            'lowestCountPedestrianData': lowest_count_pedestrian_data,
            'highestCountPedestrianData': highest_count_pedestrian_data
        }
        return Response.send_json_200(data)

