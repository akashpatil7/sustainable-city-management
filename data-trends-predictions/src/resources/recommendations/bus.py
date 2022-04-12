import time
from src.common.response import Response
from enum import Enum
from src.utils import get_most_polluted, top_aqi_locations, most_delayed_buses

# -----------------------------------------------------------
# Creates a Bus class and functions to get poluted bus routes
# compared with AQI data
# -----------------------------------------------------------

class EndPointMethods(Enum):
    getRecommendations = "get_recommendations"


class Bus():
    def __init__(self, db):
        self.db = db
        print("Initiating Bus Recommendations")

    def perform_action(self, action):
        try:
            return getattr(self, EndPointMethods[action].value)()
        except KeyError:
            print("[Bus Recommendations] EndPoint not found")
        except AttributeError:
            print("[Bus Recommendations] EndPoint cannot be resolved")
        return Response.not_found_404("Recommendations bus: " + action +
                                      " not found")

    def get_recommendations(self):
        """Get the most poluted areas for bus and AQI data."""
        print("[Bus Recommendations] Get")
        bus = self.db.get_collection("DBus_Historical")

        yesterday = time.time() - 86400

        # get and sort the bus routes by route
        buses = list(
            bus.find(
                {
                    'scheduleRelationship': 'Scheduled',
                    'startTimestamp': {
                        '$gt': yesterday
                    },
                }, {
                    'routeLong': True,
                    'stopSequence': True,
                    'stopLat': True,
                    'stopLon': True,
                }).sort([("startTimestamp", -1), ("routeLong", -1)]))

        # get the most delayed buses
        most_delayed = most_delayed_buses(buses)
        
        # get the most poluted AQI locations
        aqi = self.db.get_collection("Aqi")
        highest_aqi_station = list(
            aqi.find({}, {
                'aqi': True,
                'stationName': True,
                'latitude': True,
                'longitude': True,
            }).sort([
                ("aqi", -1),
            ]))
            
        highest_aqi = top_aqi_locations(highest_aqi_station, 5, True)
        most_polluted = get_most_polluted(buses, highest_aqi)

        data = {'mostDelayed': most_delayed, 'mostPolluted': most_polluted}
        return Response.send_json_200(data)
