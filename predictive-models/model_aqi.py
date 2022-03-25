from sklearn.model_selection import train_test_split
import numpy as np
from sklearn.linear_model import LinearRegression
import pickle
from collections import defaultdict
import time

STATION_TO_ID = {'Marino, Dublin 3, Ireland': 1, 'Ballymun Library, Dublin 9, Ireland': 2, "St. Anne's Park, Dublin 5, Ireland": 3, 
'Sandymount Green, Dublin 4, Ireland': 4, 'Amiens Street, Dublin 1, Ireland': 5, 'St. John’s Road, Kilmainham, Dublin 8, Ireland': 6, 
'Custom House Quay, Dublin 1, Ireland': 7, 'Davitt Road, Inchicore, Dublin 12, Ireland': 8, 'Weaver Park, Dublin 8, Ireland': 9, 
'Drumcondra Library, Dublin 9, Ireland': 10, 'Coolock, Dublin 5, Ireland': 11, 'Clonskeagh, Ireland': 12, 'Ringsend, Dublin 4, Ireland': 13, 
'Dublin Port, Dublin 1, Ireland': 14, 'Finglas, Dublin 11, Ireland': 15, 'Rathmines, Ireland': 16, 'Lord Edward Street, Dublin 2, Ireland': 17, 
'Walkinstown Library, Dublin 12, Ireland': 18}

def get_testing_data_using_epoch():
    epoch_times = []
    for i in range(len(STATION_TO_ID.values())):
        epoch_times.append(time.time())
    
    return np.column_stack((list(STATION_TO_ID.values()), epoch_times))


def train_aqi_model(db):
    # get data from db
    collection = db.Aqi
    data = collection.find()

    if data != []:
        stations = []
        aqi = []
        epoch_time = []
        for doc in data:
            if doc['aqi'] != '-':
                stations.append(STATION_TO_ID[doc['stationName']])
                aqi.append(int(doc['aqi']))
                epoch_time.append(doc['lastUpdatedTime'])
        
        
        X = np.column_stack((stations, epoch_time))
        y = np.array(aqi)

        # train model with x features being the station name and the time, and the y being the api
        model = LinearRegression()
        model.fit(X, y)

        to_db = pickle.dumps(model)
        return to_db
    
    else:
        return None

def get_aqi_predictions(model_, db):
    model = pickle.loads(model_)

    x_test = get_testing_data_using_epoch()
    preds = model.predict(x_test)

    predictions_ = defaultdict(dict)

    # assign the predictions to each staion
    for x, p in zip(x_test, preds):
        index_of_loc = list(STATION_TO_ID.values()).index(x[0])
        loc = list(STATION_TO_ID.keys())[index_of_loc]
        predictions_[loc] = p

    return predictions_
