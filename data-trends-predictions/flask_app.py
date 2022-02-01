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
	

@app.route("/getHourlyAverageForStation", methods=['GET'])
def getHourlyAverageForStation():
	
	station = request.args.get('station')

	print(f"\n\nGetting the view:AvgHourlyAvailability from MongoDB for station '{station}'")
	AvgHourlyAvailability = client.city_dashboard.AvgHourlyAvailability

	stationFilter = { '_id.name': station }
	print("Filter: ", stationFilter)

	data = AvgHourlyAvailability.find(filter = stationFilter)
	print("Returning data ... ")

	return dumps(list(data))

if __name__ == "__main__":
    app.run(host='0.0.0.0', port = rest_port)