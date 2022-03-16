from src.common.response import Response
from enum import Enum


class EndPointMethods(Enum):
    getRecommendations = "get_recommendations"


class Aqi():
    def __init__(self, db):
        self.db = db
        print("Initiating Aqi Recommendations")

    def perform_action(self, action):
        try:
            return getattr(self, EndPointMethods[action].value)()
        except KeyError:
            print("[Aqi Recommendations] EndPoint not found")
        except AttributeError:
            print("[Aqi Recommendations] EndPoint cannot be resolved")
        return Response.not_found_404("Recommendations aqi: " + action +
                                      " not found")

    def get_recommendations(self):
        print("[Aqi Recommendations] Get")
        aqi = self.db.get_collection("Aqi")

        lowest_aqi_station_data = list(
            aqi.find({}, {
                'aqi': True,
                'lastUpdatedTime': True,
                'stationName': True,
                'latitude': True,
                'longitude': True,
            }).sort([
                ("aqi", -1),
            ]))
        lowest_aqi_station_data = list(filter(lambda x: x["aqi"] != "-", lowest_aqi_station_data))
        lowest_aqi_station_data.sort(key=lambda x: int(x["aqi"]))
        lowest_aqi_station_data = lowest_aqi_station_data[0:5]

        highest_aqi_station_data = list(
            aqi.find({}, {
                'aqi': True,
                'stationName': True,
            }).sort([
                ("aqi", 1),
            ]))
            
        highest_aqi_station_data = list(filter(lambda x: x["aqi"] != "-", highest_aqi_station_data))
        highest_aqi_station_data.sort(key=lambda x: int(x["aqi"]), reverse=True)
        highest_aqi_station_data = highest_aqi_station_data[0:5]

        data = {
            'highestAqiStationData': lowest_aqi_station_data,
            'lowestAqiStationData': highest_aqi_station_data
        }
        return Response.send_json_200(data)
