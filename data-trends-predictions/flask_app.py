from flask import Flask, request
import pandas as pd
import py_eureka_client.eureka_client as eureka_client
from pymongo import MongoClient
from datetime import datetime
from bson.json_util import dumps

rest_port = 8050
eureka_client.init(eureka_server="http://localhost:8761/eureka",
                   app_name="py-data-trends-predictions-service",
                   instance_port=rest_port)
client = MongoClient("mongodb+srv://admin:admin@cluster1.varva.mongodb.net/city_dashboard")
app = Flask(__name__)

@app.route("/getCurrentHourAverages", methods=['GET'])
def getCurrentHourAverages():
	
	print("\n\nGetting the view:AvgHourlyAvailability from MongoDB")
	AvgHourlyAvailability = client.city_dashboard.AvgHourlyAvailability

	hourFilter = { '_id.hour': datetime.now().hour }
	print("Filter: ", hourFilter)

	data = AvgHourlyAvailability.find(filter = hourFilter)
	print("Returning data ... ")

	return dumps(list(data))
	

@app.route("/getHourlyAverageForAllStation", methods=['GET'])
def getHourlyAverageForAllStation():
	
	print('\n\nGetting the view:AvgHourlyAvailability from MongoDB for all stations')
	AvgHourlyAvailability = client.city_dashboard.AvgHourlyAvailability

	data = AvgHourlyAvailability.find()
	print("Returning data ... ")

	return dumps(list(data))

@app.after_request
def after_request(response):
	header = response.headers
	header['Access-Control-Allow-Origin'] = '*'
	print("After Request triggered")
	return response

@app.route("/getRecommendations", methods=['GET'])
def getRecommendations():
	print("Getting DublinBikes data from MongoDB")
	Dublin_Bikes = client.city_dashboard.Dublin_Bikes
	mostEmptyBikeStationData = list(Dublin_Bikes.find(
		{ },
		{ 'name': True, 'harvestTime': True, 'availableBikeStands': True, 'bikeStands': True, 'availableBikes': True, '_id': False }
	).sort([("harvestTime", -1), ("availableBikeStands", 1)]).limit(5))

	mostAvailableBikeStationData = list(Dublin_Bikes.find(
		{ },
		{ 'name': True, 'harvestTime': True, 'availableBikeStands': True, 'bikeStands': True, 'availableBikes': True, '_id': False }
	).sort([("harvestTime", -1), ("availableBikeStands", -1)]).limit(5))

	print("Calculating recommendations")

	return dumps({
		'mostEmptyBikeStationData': mostEmptyBikeStationData,
		'mostAvailableBikeStationData': mostAvailableBikeStationData
	})

if __name__ == "__main__":
    app.run(host='0.0.0.0', port = rest_port)