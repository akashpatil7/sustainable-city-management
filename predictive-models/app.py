from flask import Flask
from apscheduler.schedulers.background import BackgroundScheduler
from pymongo import MongoClient
from model_pedestrians import train_pedestrian_model, get_pedestrian_predictions
from model_aqi import train_aqi_model, get_aqi_predictions
from datetime import datetime
from bikes_model import create_bikes_model, get_bike_predictions

app = Flask(__name__)

sched = BackgroundScheduler(daemon=True)

# SCM db
eureka_db_string = 'mongodb+srv://admin:admin@cluster1.varva.mongodb.net/city_dashboard'
client = MongoClient(eureka_db_string)
eureka_db = client.city_dashboard
eureka_collection = eureka_db.predictive_models

def save_pedestrian_model():
    ped_model = train_pedestrian_model(eureka_db)
    date = datetime.now()
    new_entry = {"$set": {"model": ped_model, "date_of_training": date}}

    info = eureka_collection.update_one({"indicator": "pedestrian"}, new_entry)
    print("Pedestrian Model saved to DB")
    print(info)


def save_bike_model():
    bike_model = create_bikes_model()
    current_date = datetime.now()
    new_bike_entry = {"$set": {"model": bike_model, "date_of_training": current_date}}
    model_info = eureka_collection.update_one({"indicator": "dublin_bikes"}, new_bike_entry)
    print("Bikes Model saved to DB")
    print(model_info)

def save_aqi_model():
    aqi_model = train_aqi_model(eureka_db)
    date = datetime.now()
    new_entry = {"$set": {"model": aqi_model, "date_of_training": date}}

    info = eureka_collection.update_one({"indicator": "aqi"}, new_entry)
    print("Aqi Model saved to DB")
    print(info)

@app.route("/get_pedestrian_predictions", methods=['GET'])
def load_pedestrian_model():
    ped_model = eureka_collection.find_one({"indicator": "pedestrian"})

    pred = get_pedestrian_predictions(ped_model['model'])
    return pred

@app.route("/get_aqi_predictions", methods=['GET'])
def load_aqi_model():
    aqi_model = eureka_collection.find_one({"indicator": "aqi"})

    pred = get_aqi_predictions(aqi_model['model'], eureka_db)
    return pred
    
@app.route("/get_bike_predictions", methods=['GET'])
def load_bike_model():
    bike_pred_model = eureka_collection.find_one({"indicator": "dublin_bikes"})

    pred = get_bike_predictions(bike_pred_model['model'])
    return str(pred)


if __name__ == "__main__":
    # Set "hour" and/or "minute" to the time we want the scheduler to run the jobs.
    # e.g. hour=12 means the scheduler will run at 12 every day

    #sched.add_job(save_pedestrian_model, 'cron', hour='12')

    # sched.add_job(save_pedestrian_model, 'cron', minute='*')
    # sched.start()
    # sched.add_job(save_bike_model, 'cron', minute='20')
    # sched.start()

    # sched.add_job(save_pedestrian_model, 'cron', minute='*')
    # sched.add_job(save_aqi_model, 'cron', minute='*')

    # sched.start()

    # print('Scheduler Running...')
    app.run()