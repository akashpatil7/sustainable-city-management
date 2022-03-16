import math
import queue
import time
from src.common.response import Response
from enum import Enum


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

    def get_avg_delay(self, bus):
        total_delay = 0
        index = 0
        for stop in bus["stopSequence"]:
            total_delay += stop["arrivalDelay"]
            index+=1
        return total_delay / index
    
    def get_highest_aqi(self):
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
        highest_aqi_station = list(filter(lambda x: x["aqi"] != "-", highest_aqi_station))
        highest_aqi_station.sort(key=lambda x: int(x["aqi"]), reverse=True)
        highest_aqi_station = highest_aqi_station[0:5]
        return highest_aqi_station
    
    def get_most_polluted(self, buses, aqis):
        most_polluted = {}
        for aqi in aqis:
            closest_stop_dist = float('inf')
            closest_stop = None

            checked_buses = {}
            for bus in buses:
                if bus["routeLong"] in checked_buses:
                    continue
                route = bus["routeLong"]
                checked_buses[route] = 1
                for stop in bus["stopSequence"]:
                    lat_dist = (float(stop['stopLat']) - float(aqi['latitude'])) ** 2
                    lon_dist = (float(stop['stopLon']) - float(aqi['longitude'])) ** 2
                    distance = math.sqrt(lat_dist + lon_dist);
                    if distance < closest_stop_dist:
                        closest_stop_dist = distance
                        closest_stop = bus
            most_polluted[closest_stop['routeLong']] = aqi['aqi']
        return most_polluted
            

    def get_recommendations(self):
        print("[Bus Recommendations] Get")
        bus = self.db.get_collection("DBus_Historical")
        
        yesterday = time.time() - 86400

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
                }).sort([
                    ("startTimestamp", -1), ("routeLong", -1)
                ]))
            
        most_delayed = queue.PriorityQueue(maxsize=5)
        checked_buses = {}
        for bus in buses:
            if bus["routeLong"] in checked_buses:
                continue
            route = bus["routeLong"]
            checked_buses[route] = 1
            avg_delay = self.get_avg_delay(bus)
            if most_delayed.full():
                delay, _ = most_delayed.queue[0]
                if delay < avg_delay:
                    most_delayed.get()
                    most_delayed.put((avg_delay, bus["routeLong"]))
            else:
                most_delayed.put((avg_delay, bus["routeLong"]))

        most_delayed = most_delayed.queue
        highest_aqi = self.get_highest_aqi()
        most_polluted = self.get_most_polluted(buses, highest_aqi)

        data = {'mostDelayed': most_delayed, 'mostPolluted': most_polluted}
        return Response.send_json_200(data)
