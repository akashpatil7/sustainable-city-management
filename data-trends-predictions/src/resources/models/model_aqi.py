import numpy as np
from sklearn.ensemble import RandomForestRegressor
import pickle
import time
from datetime import timedelta
from datetime import datetime
import time
from src.utils import get_testing_data_using_epoch
from src.common.response import Response
from enum import Enum

class EndPointMethods(Enum):
    getAqiPredictions = "get_aqi_predictions"
    trainModel = "train_aqi_model"
    getAqiRecommendationFromPrediction = "get_aqi_recommendation_from_prediction"
    getAqiRecommendationFromFuturePrediction = "get_aqi_recommendation_from_future_prediction"

class AqiModel():
    def __init__(self, db):
        print("Initialising Aqi Model")
        self.db = db
        self.STATION_TO_ID = {'Marino, Dublin 3, Ireland': 1, 'Ballymun Library, Dublin 9, Ireland': 2, "St. Anne's Park, Dublin 5, Ireland": 3, 
        'Sandymount Green, Dublin 4, Ireland': 4, 'Amiens Street, Dublin 1, Ireland': 5, 'St. John’s Road, Kilmainham, Dublin 8, Ireland': 6, 
        'Custom House Quay, Dublin 1, Ireland': 7, 'Davitt Road, Inchicore, Dublin 12, Ireland': 8, 'Weaver Park, Dublin 8, Ireland': 9, 
        'Drumcondra Library, Dublin 9, Ireland': 10, 'Coolock, Dublin 5, Ireland': 11, 'Clonskeagh, Ireland': 12, 'Ringsend, Dublin 4, Ireland': 13, 
        'Dublin Port, Dublin 1, Ireland': 14, 'Finglas, Dublin 11, Ireland': 15, 'Rathmines, Ireland': 16, 'Lord Edward Street, Dublin 2, Ireland': 17, 
        'Walkinstown Library, Dublin 12, Ireland': 18}

    def perform_action(self, action):
            try:
                return getattr(self, EndPointMethods[action].value)()
            except KeyError:
                print("[Aqi Model] EndPoint not found")
            except AttributeError:
                print("[Aqi Model] EndPoint cannot be resolved")
            return Response.not_found_404("Aqi Model: " + action +
                                        " not found")
    
    def train_aqi_model(self):
        # get data from db
        collection = self.db.get_collection("Aqi")
        
        data = list(collection.find())

        # build training dataset using stations, aqi level and the epoch times
        if data != []:
            stations = []
            aqi = []
            epoch_time = []
            for doc in data:
                if doc['aqi'] != '-':
                    stations.append(self.STATION_TO_ID[doc['stationName']])
                    aqi.append(int(doc['aqi']))
                    epoch_time.append(doc['lastUpdatedTime'])
            
            
            X = np.column_stack((stations, epoch_time))
            y = np.array(aqi)

            # train model with x features being the station name and the time, and the y being the aqi level
            model = RandomForestRegressor(n_estimators=15, max_depth=10, criterion='mse')
            model.fit(X, y)

            # convert model to byte object
            to_db = pickle.dumps(model)

            # store object in db
            date = datetime.now()
            new_entry = {"$set": {"model": to_db, "date_of_training": date}}

            info = self.db.get_collection('predictive_models').update_one({"indicator": "aqi"}, new_entry)
            print("Aqi Model saved to DB")
            print(dir(info))

            return Response.send_json_200(info._UpdateResult__raw_result)
    
    def get_predictions(self, unixTime):
        # get model from db
        collection = self.db.get_collection("predictive_models")
        model_ = collection.find_one({"indicator": "aqi"})['model']

        # load model from byte object
        model = pickle.loads(model_)

        # generate testing data
        x_test = get_testing_data_using_epoch(self.STATION_TO_ID, unixTime)

        # get predictions
        preds = model.predict(x_test)

        response_predictions = []

        # assign the predictions to each staion
        for x, p in zip(x_test, preds):
            index_of_loc = list(self.STATION_TO_ID.values()).index(x[0])
            loc = list(self.STATION_TO_ID.keys())[index_of_loc] 
            doc = self.db.get_collection("Aqi").find_one({"stationName": loc})
            obj = {"aqi": round(p), "stationName" : loc, "latitude" : doc['latitude'], "longitude" : doc['longitude']}
            response_predictions.append(obj)
        return response_predictions

    def get_aqi_predictions(self):
        response_predictions = self.get_predictions(time.time())
        return Response.send_json_200(response_predictions)

    def get_aqi_recommendation_from_prediction(self):
        response_predictions = self.get_predictions(time.time())
        sortedList = sorted(response_predictions, key=lambda d: d["aqi"])
        lowest_aqi_station_data = sortedList[:5]
        highest_aqi_station_data = sortedList[-5:]
        highest_aqi_station_data.reverse()

        data = {
            'lowestAqiStationData': lowest_aqi_station_data,
            'highestAqiStationData': highest_aqi_station_data
        }
        return Response.send_json_200(data)

    def get_aqi_recommendation_from_future_prediction(self):
        dtime = datetime.now() + timedelta(minutes=10)
        unixtime = time.mktime(dtime.timetuple())

        response_predictions = self.get_predictions(unixtime)
        sortedList = sorted(response_predictions, key=lambda d: d["aqi"])
        lowest_aqi_station_data = sortedList[:5]
        highest_aqi_station_data = sortedList[-5:]
        highest_aqi_station_data.reverse()

        data = {
            'lowestAqiStationData': lowest_aqi_station_data,
            'highestAqiStationData': highest_aqi_station_data
        }
        return Response.send_json_200(data)

