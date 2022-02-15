from datetime import datetime
import py_eureka_client.eureka_client as eureka_client
from pymongo import MongoClient
from flask import Flask
from flask_cors import CORS, cross_origin
from bson.json_util import dumps


rest_port = 8050
eureka_client.init(eureka_server="http://eureka:8761/eureka",
                   app_name="trends",
                   instance_port=rest_port)
client = MongoClient(
    "mongodb+srv://admin:admin@cluster1.varva.mongodb.net/city_dashboard")
app = Flask(__name__)
cors = CORS(app)
app.config['CORS_HEADERS'] = 'Content-Type'


@app.route("/getCurrentHourAverages", methods=['GET'])
@cross_origin()
def getCurrentHourAverages():

    print("Testing change to Github branch")
    print("\n\nGetting the view:AvgHourlyAvailability from MongoDB")
    AvgHourlyAvailability = client.city_dashboard.AvgHourlyAvailability

    hourFilter = {'_id.hour': datetime.now().hour}
    print("Filter: ", hourFilter)

    data = AvgHourlyAvailability.find(filter=hourFilter)
    print("Returning data ... ")

    return dumps(list(data))

@app.route("/getHourlyAverageForAllStation", methods=['GET'])
@cross_origin()
def getHourlyAverageForAllStation():

    print(
        '\n\nGetting the view:AvgHourlyAvailability from MongoDB for all stations'
    )
    AvgHourlyAvailability = client.city_dashboard.AvgHourlyAvailability

    data = AvgHourlyAvailability.find()
    print("Returning data ... ")

    return dumps(list(data))

@app.route("/getRecommendations", methods=['GET'])
@cross_origin()
def getRecommendations():
    print("Getting DublinBikes data from MongoDB")
    Dublin_Bikes = client.city_dashboard.Dublin_Bikes
    mostEmptyBikeStationData = list(
        Dublin_Bikes.find({}, {
            'name': True,
            'harvestTime': True,
            'availableBikeStands': True,
            'bikeStands': True,
            'availableBikes': True,
            '_id': False
        }).sort([("harvestTime", -1),
                    ("availableBikeStands", 1)]).limit(5))

    mostAvailableBikeStationData = list(
        Dublin_Bikes.find({}, {
            'name': True,
            'harvestTime': True,
            'availableBikeStands': True,
            'bikeStands': True,
            'availableBikes': True,
            '_id': False
        }).sort([("harvestTime", -1),
                    ("availableBikeStands", -1)]).limit(5))

    print("Calculating recommendations")

    return dumps({
        'mostEmptyBikeStationData':
        mostEmptyBikeStationData,
        'mostAvailableBikeStationData':
        mostAvailableBikeStationData
    })


if __name__ == "__main__":
    app.run(host='0.0.0.0', port=rest_port)

