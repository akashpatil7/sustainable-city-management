from collections import defaultdict
from sklearn.model_selection import train_test_split
import numpy as np
from sklearn.linear_model import LinearRegression
import pickle
import time

LOCATION_TO_ID = {'Westmoreland Street West/Carrolls': 1, 'Henry Street/Coles Lane/Dunnes': 2, "O'Connell st/Princes st North": 3, 'College Green/Church Lane': 4, 'Dawson Street/Molesworth': 5, 
'Talbot st/Guineys': 6, 'Grafton Street / Nassau Street / Suffolk Street': 7, 'North Wall Quay/Samuel Beckett bridge West': 8, 'Grafton Street/CompuB': 9, 'Mary st/Jervis st': 10, 'Dame Street/Londis': 11, 
'Newcomen Bridge/Charleville mall inbound': 12, 'College st/Westmoreland st': 13, 'Bachelors walk/Bachelors way': 14, "D'olier st/Burgh Quay": 15, 'Richmond st south/Portabello Harbour inbound': 16, 
'Westmoreland Street East/Fleet street': 17, 'Grand Canal st upp/Clanwilliam place/Google': 18, 'Grand Canal st upp/Clanwilliam place': 19, 'Baggot st lower/Wilton tce inbound': 20, 
'Phibsborough Rd/Munster St': 21, 'North Wall Quay/Samuel Beckett bridge East': 22, 'Grafton st/Monsoon': 23, 'Baggot st upper/Mespil rd/Bank': 24, 'Talbot st/Murrays Pharmacy': 25, 
"O'Connell St/Parnell St/AIB": 26, 'Phibsborough Rd/Enniskerry Road': 27, 'College Green/Bank Of Ireland': 28, 'Capel st/Mary street': 29}

def get_testing_data_using_epoch():
    epoch_times = []
    for i in range(len(LOCATION_TO_ID.values())):
        epoch_times.append(time.time())
    
    return np.column_stack((list(LOCATION_TO_ID.values()), epoch_times))

def train_pedestrian_model(db):
    # get data from db
    collection = db.Pedestrian
    data = collection.find()
    
    if data != []:
        streets = []
        count = []
        time = []
        for doc in data:
            streets.append(LOCATION_TO_ID[doc['street']])
            count.append(doc['count'])
            time.append(doc['time'])

        X = np.column_stack((streets, time))
        y_count = np.array(count)

        # train model with x features being the street name and the time, and the y being the count
        model = LinearRegression()
        model.fit(X, y_count)

        to_db = pickle.dumps(model)
        return to_db
    
    else:
        return None


def get_pedestrian_predictions(model_):
    model = pickle.loads(model_)

    x_test = get_testing_data_using_epoch()
    preds = model.predict(x_test)

    predictions_ = defaultdict(dict)
    
    # assign the predictions to each location
    for x, p in zip(x_test, preds):
        index_of_loc = list(LOCATION_TO_ID.values()).index(x[0])
        loc = list(LOCATION_TO_ID.keys())[index_of_loc]
        predictions_[loc] = p   

    return predictions_
