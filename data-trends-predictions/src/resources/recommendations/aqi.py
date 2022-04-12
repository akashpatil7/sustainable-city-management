from src.common.response import Response
from enum import Enum
from src.utils import top_aqi_locations

# -----------------------------------------------------------
# Creates an AQI class and functions to get the highest and 
# lowest AQI values
# -----------------------------------------------------------

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
        """Get the lowest and highest AQI values."""
        print("[Aqi Recommendations] Get")
        aqi = self.db.get_collection("Aqi")

        # get top 5 lowest AQI values from the DB
        lowest_aqi_station_data = list(
            aqi.find({}, {
                'aqi': True,
                'lastUpdatedTime': True,
                'stationName': True,
                'latitude': True,
                'longitude': True,
            }).sort([
                ("aqi", 1),
            ]))
        lowest_aqi_station_data = top_aqi_locations(lowest_aqi_station_data, 5,
                                                    False)
        # get top 5 highest AQI values from the DB                                            
        highest_aqi_station_data = list(
            aqi.find({}, {
                'aqi': True,
                'stationName': True,
            }).sort([
                ("aqi", -1),
            ]))

        highest_aqi_station_data = top_aqi_locations(highest_aqi_station_data,
                                                     5, True)
        data = {
            'highestAqiStationData': highest_aqi_station_data,
            'lowestAqiStationData': lowest_aqi_station_data
        }
        return Response.send_json_200(data)
