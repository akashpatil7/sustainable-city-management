import pandas as pd
import numpy as np
import math, sys
from sklearn.preprocessing import PolynomialFeatures
from sklearn.model_selection import train_test_split
from sklearn.linear_model import Ridge
from sklearn.model_selection import KFold
from sklearn.metrics import mean_squared_error
import pickle
from datetime import datetime, timedelta

def create_bikes_model():

    y = last_3_months_bikes_data_processed()
    q=1
    lag=6; stride=1
    w=math.floor(7*24*60*60/dt) # number of samples per week
    length = y.size-w-lag*w-q
    XX=y[q:q+length:stride]
    for i in range(1,lag):
        X=y[i*w+q:i*w+q+length:stride]
        XX=np.column_stack((XX,X))
    d=math.floor(24*60*60/dt) # number of samples per day
    for i in range(0,lag):
        X=y[i*d+q:i*d+q+length:stride]
        XX=np.column_stack((XX,X))
    yy=y[lag*w+w+q:lag*w+w+q+length:stride]
    # tt=t[lag*w+w+q:lag*w+w+q+length:stride]

    mean_error=[]; std_error=[]
    degree = [1,2,3,4]
    for d in degree:
        poly = PolynomialFeatures(d)
        X_poly = poly.fit_transform(XX)
        model = Ridge()
        temp=[]
        kf = KFold(n_splits=5, shuffle=True)
        for train, test in kf.split(yy):
            model.fit(X_poly[train], yy[train])
            ypred = model.predict(X_poly[test])
            score = mean_squared_error(yy[test],ypred, squared=False)
            print(score)
            temp.append(score)
        mean_error.append(np.array(temp).mean())
        std_error.append(np.array(temp).std())
        print("----------------------------")
    best_degree = degree[mean_error.index(min(mean_error))]

    poly2 = PolynomialFeatures(best_degree)
    X_poly_2 = poly2.fit_transform(XX)

    mean_error=[]; std_error=[]
    Ci_range = [0.0001, 0.001, 0.01, 0.1, 1, 10]
    for Ci in Ci_range:
        model = Ridge(alpha=1/(2*Ci))
        temp=[]
        kf = KFold(n_splits=5, shuffle=True)
        for train, test in kf.split(X_poly_2):
            model.fit(X_poly_2[train], yy[train])
            score = mean_squared_error(yy[test],model.predict(X_poly_2[test]), squared=False)
            print(score)
            temp.append(score)
        mean_error.append(np.array(temp).mean())
        std_error.append(np.array(temp).std())
        print("----------------------------")
    best_Ci = Ci_range[mean_error.index(min(mean_error))]

    train, test = train_test_split(np.arange(0,yy.size),test_size=0.1)
    Ci=best_Ci
    model = Ridge(alpha=1/(2*Ci))
    model.fit(X_poly_2[train], yy[train])
    print(mean_squared_error(yy[test],model.predict(X_poly_2[test]), squared=False))
    return pickle.dumps(model)

def get_bike_predictions(model_):
    model = pickle.loads(model_)
    #TODO : Fetch these values form DB
    # test_array = [[ 9, 20, 25, 29, 18, 28,  9,  0, 19,  6,  7, 15]]
    test_array = get_prediction_array()
    test_poly = PolynomialFeatures(4)
    test_X_poly_2 = test_poly.fit_transform(test_array)
    prediction = model.predict(test_X_poly_2)
    return math.ceil(prediction[0])

def last_3_months_bikes_data_processed():
    df = pd.read_csv("sls_bikes_data.csv",usecols = [1,2,3])
    t_full=pd.array(pd.DatetimeIndex(df.iloc[:,1]).astype(np.int64))/1000000000
    dt = t_full[1] - t_full[0]
    start=pd.to_datetime('01−01−2020',format='%d−%m−%Y')
    end=pd.to_datetime('31−03−2020',format='%d−%m−%Y')
    t_start = pd.DatetimeIndex([start]).astype(np.int64)/1000000000
    t_end = pd.DatetimeIndex([end]).astype(np.int64)/1000000000
    t = np.extract([(t_full>=t_start[0]) and (t_full<=t_end[0])], t_full)
    t=(t-t[0])/60/60/24 # convert timestamp to days
    y = np.extract([(t_full>=t_start[0]) and (t_full<=t_end[0])], df.iloc[:,2]).astype(np.int64)
    return y

def get_prediction_array():
    y = last_3_months_bikes_data_processed()
    # previous_day = datetime.now() - timedelta(days=1)
    # previous_day_last_instance = datetime.now().replace(hour=23, minute=55, second =0) - timedelta(days=1)
    previous_day_last_instance = datetime(2020,3,31,23,55,0,0)
    previous_day = datetime(2020,3,31,datetime.now().hour,datetime.now().minute,0,0)
    diff = previous_day_last_instance - previous_day
    minutes = diff.total_seconds()/60
    instances_to_go_back = int((minutes + (5-(minutes%5)))/5)
    lag = 6
    print(instances_to_go_back)
    prediction_array = []
    y_length = len(y)
    prediction_array.append(y[y_length - 1 - instances_to_go_back])
    for i in range(1, lag):
        prediction_array.append(y[y_length -1 - instances_to_go_back - (i*2016)])
    prediction_array.append(y[y_length - 1 - instances_to_go_back])
    for i in range(1, lag):
        prediction_array.append(y[y_length -1 - instances_to_go_back - (i*288)])
    return [prediction_array]



