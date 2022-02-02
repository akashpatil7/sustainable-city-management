from flask import Flask, request
import pandas as pd
import py_eureka_client.eureka_client as eureka_client
from pymongo import MongoClient
from datetime import datetime

rest_port = 8050
eureka_client.init(eureka_server="http://localhost:8761/eureka",
                   app_name="py-data-trends-predictions-service",
                   instance_port=rest_port)
client = MongoClient("mongodb+srv://admin:admin@cluster1.varva.mongodb.net/city_dashboard")
app = Flask(__name__)

@app.route("/getDailyAverages", methods=['GET'])
def getDailyAverages():
	""" data = request.json
	
	df1 = pd.DataFrame(data['1'])
	df1 = df1[['name', 'available_bike_stands', 'bike_stands', 'available_bikes', 'harvest_time']]
	df1['harvest_time']= pd.to_datetime(df1['harvest_time'])
	df1 = df1.groupby("name", as_index=False).agg(
		available_bike_stands=pd.NamedAgg(column='available_bike_stands', aggfunc="mean"),
		bike_stands=pd.NamedAgg(column='bike_stands', aggfunc="mean"),
		available_bikes=pd.NamedAgg(column='available_bikes', aggfunc="mean"),
		harvest_time=pd.NamedAgg(column='harvest_time', aggfunc="max"))

	df2 = pd.DataFrame(data['2'])
	df2 = df2[['name', 'available_bike_stands', 'bike_stands', 'available_bikes', 'harvest_time']]
	df2['harvest_time']= pd.to_datetime(df2['harvest_time'])
	df2 = df2.groupby("name", as_index=False).agg(
		available_bike_stands=pd.NamedAgg(column='available_bike_stands', aggfunc="mean"),
		bike_stands=pd.NamedAgg(column='bike_stands', aggfunc="mean"),
		available_bikes=pd.NamedAgg(column='available_bikes', aggfunc="mean"),
		harvest_time=pd.NamedAgg(column='harvest_time', aggfunc="max"))

	df = pd.merge(df1,df2,on='name')
	df["available_bike_stands"] = (df["available_bike_stands_x"] + df["available_bike_stands_y"]) / 2
	df["bike_stands"] = (df["bike_stands_x"] + df["bike_stands_y"]) / 2
	df["available_bikes"] = (df["available_bikes_x"] + df["available_bikes_y"]) / 2
	df["harvest_time"] = df[["harvest_time_x", "harvest_time_y"]].max(axis = 1)
	df = df[['name', 'bike_stands', 'available_bikes', 'available_bike_stands', 'harvest_time']]

 """	
	print("Getting DublinBikes data from MongoDB")
	Dublin_Bikes = client.city_dashboard.Dublin_Bikes
	data = Dublin_Bikes.find(
		{ },
		{ 'name': True, 'harvestTime': True, 'availableBikeStands': True, 'bikeStands': True, 'availableBikes': True, '_id': False }
	)

	print("Calculating averages")
	df = pd.DataFrame(list(data))
	df['harvestTime']= pd.to_datetime(df['harvestTime'], unit='s')
	df['hour'] = pd.to_datetime(df['harvestTime']).dt.hour
	df = df.groupby(['name', 'hour'], as_index=False).mean()
	df['avg_hourly_bike_availability'] = round((df['availableBikes'] / df['bikeStands']) * 100, 1)
	df = df[['name', 'hour', 'avg_hourly_bike_availability']]

	print("Returning data ... ")
	return df.to_json(orient = 'records')

@app.route("/getRecommendations", methods=['GET'])
def getRecommendations():
	print("Getting DublinBikes data from MongoDB")
	Dublin_Bikes = client.city_dashboard.Dublin_Bikes
	mostEmptyBikeStationData = Dublin_Bikes.find(
		{ },
		{ 'name': True, 'harvestTime': True, 'availableBikeStands': True, 'bikeStands': True, 'availableBikes': True, '_id': False }
	).sort([("harvestTime", -1), ("availableBikeStands", 1)]).limit(5)

	mostAvailableBikeStationData = Dublin_Bikes.find(
		{ },
		{ 'name': True, 'harvestTime': True, 'availableBikeStands': True, 'bikeStands': True, 'availableBikes': True, '_id': False }
	).sort([("harvestTime", -1), ("availableBikeStands", -1)]).limit(5)

	print("Calculating recommendations")
	df1 = pd.DataFrame(list(mostEmptyBikeStationData))
	df2 = pd.DataFrame(list(mostAvailableBikeStationData))
	return df1.append(df2).to_json(orient = 'records')

if __name__ == "__main__":
    app.run(host='0.0.0.0', port = rest_port)