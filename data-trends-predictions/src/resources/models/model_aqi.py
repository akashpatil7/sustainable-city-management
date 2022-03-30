import numpy as np
from sklearn.linear_model import LinearRegression
import pickle
from collections import defaultdict
import time
from datetime import datetime
from datetime import timedelta
from src.utils import top_aqi_locations
from collections import OrderedDict
import json
from src.common.response import Response
from enum import Enum

class EndPointMethods(Enum):
    getAqiPredictions = "get_aqi_predictions"
    trainModel = "train_aqi_model"
    getAqiRecommendationFromPrediction = "get_aqi_recommendation_from_prediction"
    getAqiRecommendationFromFuturePrediction = "get_aqi_recommendation_from_future_prediction"

class AqiModel():
    def __init__(self, db):
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
    
    def get_testing_data_using_epoch(self, currentTime):
        epoch_times = []
        for i in range(len(self.STATION_TO_ID.values())):
            epoch_times.append(currentTime)
        
        return np.column_stack((list(self.STATION_TO_ID.values()), epoch_times))


    def train_aqi_model(self):
        # get data from db
        collection = self.db.get_collection("Aqi")
        data = collection.find({})

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

            # train model with x features being the station name and the time, and the y being the api
            model = LinearRegression()
            model.fit(X, y)

            to_db = pickle.dumps(model)

            date = datetime.now()
            new_entry = {"$set": {"model": to_db, "date_of_training": date}}

            info = self.db.update_one({"indicator": "aqi"}, new_entry)
            print("Aqi Model saved to DB")
            print(info)
            return Response.send_json_200(info)
        
        else:
            return None

    def get_predictions(self):
        collection = self.db.get_collection("predictive_models")
        model_ = collection.find_one({"indicator": "aqi"})['model']

        model = pickle.loads(model_)

        x_test = self.get_testing_data_using_epoch(time.time())
        preds = model.predict(x_test)

        predictions_ = defaultdict(dict)

        # assign the predictions to each staion
        for x, p in zip(x_test, preds):
            index_of_loc = list(self.STATION_TO_ID.values()).index(x[0])
            loc = list(self.STATION_TO_ID.keys())[index_of_loc]
            predictions_[loc] = p
        return predictions_

    def get_aqi_predictions(self):
        predictions_ = self.get_predictions()
        return Response.send_json_200(predictions_)

    def get_aqi_recommendation_from_prediction(self):
        predictions_ = self.get_predictions()
        dict(sorted(predictions_.items(), key=lambda item: item[1]))
        highest_aqi_station_data = list(predictions_.items())[:5]
        lowest_aqi_station_data = list(predictions_.items())[-5:]
        lowest_aqi_station_data.reverse()

        data = {
            'highestAqiStationData': highest_aqi_station_data,
            'lowestAqiStationData': lowest_aqi_station_data
        }
        return Response.send_json_200(data)

    def get_aqi_recommendation_from_future_prediction(self):
        collection = self.db.get_collection("predictive_models")
        model_ = collection.find_one({"indicator": "aqi"})['model']

        model = pickle.loads(model_)

        dtime = datetime.now() + timedelta(minutes=10)
        unixtime = time. mktime(dtime.timetuple())

        x_test = self.get_testing_data_using_epoch(unixtime)
        preds = model.predict(x_test)

        predictions_ = defaultdict(dict)

        # assign the predictions to each staion
        for x, p in zip(x_test, preds):
            index_of_loc = list(self.STATION_TO_ID.values()).index(x[0])
            loc = list(self.STATION_TO_ID.keys())[index_of_loc]
            predictions_[loc] = p

        dict(sorted(predictions_.items(), key=lambda item: item[1]))
        highest_aqi_station_data = list(predictions_.items())[:5]
        lowest_aqi_station_data = list(predictions_.items())[-5:]
        lowest_aqi_station_data.reverse()

        data = {
            'highestAqiStationData': highest_aqi_station_data,
            'lowestAqiStationData': lowest_aqi_station_data
        }

        return Response.send_json_200(data)