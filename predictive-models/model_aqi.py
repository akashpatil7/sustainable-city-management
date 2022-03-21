from sklearn.model_selection import train_test_split
import numpy as np
from sklearn.metrics import max_error
from sklearn.linear_model import LinearRegression
import pickle

x_test, y_test = [], []

STATION_TO_ID = {'Marino, Dublin 3, Ireland': 1, 'Ballymun Library, Dublin 9, Ireland': 2, "St. Anne's Park, Dublin 5, Ireland": 3, 
'Sandymount Green, Dublin 4, Ireland': 4, 'Amiens Street, Dublin 1, Ireland': 5, 'St. John’s Road, Kilmainham, Dublin 8, Ireland': 6, 
'Custom House Quay, Dublin 1, Ireland': 7, 'Davitt Road, Inchicore, Dublin 12, Ireland': 8, 'Weaver Park, Dublin 8, Ireland': 9, 
'Drumcondra Library, Dublin 9, Ireland': 10, 'Coolock, Dublin 5, Ireland': 11, 'Clonskeagh, Ireland': 12, 'Ringsend, Dublin 4, Ireland': 13, 
'Dublin Port, Dublin 1, Ireland': 14, 'Finglas, Dublin 11, Ireland': 15, 'Rathmines, Ireland': 16, 'Lord Edward Street, Dublin 2, Ireland': 17, 
'Walkinstown Library, Dublin 12, Ireland': 18}

def train_aqi_model(db):
    stations = []
    aqi = []
    epoch_time = []

    # get data from db
    collection = db.Aqi
    data = collection.find()
    for doc in data:
        if doc['aqi'] != '-':
            stations.append(STATION_TO_ID[doc['stationName']])
            aqi.append(int(doc['aqi']))
            epoch_time.append(doc['lastUpdatedTime'])
    
    
    X = np.column_stack((stations, epoch_time))
    y_count = np.array(aqi)

    # split into train/test datasets
    global x_test
    global y_test
    x_train, x_test, y_train, y_test = train_test_split(X, y_count, train_size=0.75, shuffle=True)

    # train model with x features being the station name and the time, and the y being the api
    model = LinearRegression()
    model.fit(x_train, y_train)

    to_db = pickle.dumps(model)
    return to_db

def get_aqi_predictions(model_):
    model = pickle.loads(model_)
    preds = model.predict(x_test)

    print(max_error(y_test, preds))
    return preds
