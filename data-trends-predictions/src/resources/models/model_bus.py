import numpy as np
from sklearn.ensemble import RandomForestRegressor
import pickle
import time
from datetime import timedelta
from datetime import datetime
from src.utils import get_testing_data_using_epoch, update_average_departure_delays_for_predictions
from src.common.response import Response
from enum import Enum

class EndPointMethods(Enum):
    getBusPredictions = "get_bus_predictions"
    trainModel = "train_bus_model"
    
class BusModel():
    def __init__(self, db):
        print("Initialising Bus Route 7 Model")
        self.db = db
        self.STOP_NUMBERS = [3072, 3073, 4, 3077, 3076, 3084, 7644, 4705, 4725, 3202, 3203, 3214, 
            3215, 3216, 3217, 3218, 3219, 3220, 1174, 3224, 3225, 3226, 3227, 3228, 3229, 3238, 3240, 
            273, 281, 4962, 480, 405, 408, 409, 410, 411, 412, 413, 414, 415, 416, 417, 418, 419, 420, 
            421, 422, 423, 424, 425, 426, 427, 428, 429, 5046, 5047, 470, 7639, 7640, 3033, 472, 3032, 
            476, 7641, 3034, 7642, 3036, 3037, 7645, 7646, 7643, 3038, 3039, 3041, 3042, 3047, 488, 485, 
            493, 494, 495, 2035, 2036, 2040, 2041, 3068, 3069, 3070, 3071]

    def perform_action(self, action):
            try:
                return getattr(self, EndPointMethods[action].value)()
            except KeyError:
                print("[Bus Model] EndPoint not found")
            except AttributeError:
                print("[Bus Model] EndPoint cannot be resolved")
            return Response.not_found_404("Bus Model: " + action +
                                        " not found")
    
    def train_bus_model(self):
        update_average_departure_delays_for_predictions(self.db)

        # get data from db
        data = self.db.get_collection('DBus_Historical').find({
            'routeShort': '7'
        })

        # build training dataset using stop numbers, route start times, departure delays & arrival delays
        if data != []:
            stop_numbers = []
            route_start_timestamp = []
            departure_delays = []
            arrival_delays = []
            
            for doc in data:
                for stop in doc['stopSequence']:
                    if stop['arrivalDelay'] != -1:
                        route_start_timestamp.append(doc['startTimestamp'])
                        departure_delays.append(stop['departureDelay'])
                        arrival_delays.append(stop['arrivalDelay'])

                        stop_ = self.db.get_collection('DBus_Stops').find_one({
                            'stop_id': stop['stopId']
                        })

                        stop_number_index = stop_['stop_name'].index('stop ') + 5

                        stop_numbers.append(int(stop_['stop_name'][stop_number_index:]))

            X = np.column_stack((stop_numbers, route_start_timestamp, departure_delays))
            y = np.array(arrival_delays)

            # train model 
            model = RandomForestRegressor(n_estimators=10, max_depth=5, criterion='mse')
            model.fit(X, y)
            
            # convert model to byte object
            to_db = pickle.dumps(model)

            # store in db
            new_entry = {"$set": {"model": to_db, "date_of_training": datetime.now()}}
            info = self.db.get_collection('predictive_models').update_one({'indicator': "bus", "route": '7'}, new_entry)
            return Response.send_json_200(info._UpdateResult__raw_result)

    def get_predictions(self, unixTime):
        # get average departure delays for each stop in route from db
        collection = self.db.get_collection('predictive_models')
        data = collection.find_one({'task': 'predictions'})
        
        # convert to dict
        stops = {int(stop['stop_number']): stop['average_departure_delay'] for stop in data['average_departure_delays']}
        
        # build testing data for predictions
        dep_delays = [stops[s] for s in self.STOP_NUMBERS]
        epoch = [unixTime for i in range(len(self.STOP_NUMBERS))]
        x_test = np.column_stack((self.STOP_NUMBERS, epoch, dep_delays))

        # load model
        model_ = collection.find_one({"indicator": "bus", "route": "7"})['model']
        model = pickle.loads(model_)

        # get predictions
        predictions = model.predict(x_test)
        return_obj = []
        for i in range(len(self.STOP_NUMBERS)):
            entry = {"simulation": True, "stop_number": self.STOP_NUMBERS[i], "arrival_delay": round(predictions[i])}
            return_obj.append(entry)

        return return_obj

    def get_bus_predictions(self):
        response_predictions = self.get_predictions(time.time())
        return Response.send_json_200(response_predictions)

