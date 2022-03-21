import queue
import time
from src.common.response import Response
from enum import Enum
from src.utils import get_most_polluted, get_highest_aqi, get_avg_delay


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
                }).sort([("startTimestamp", -1), ("routeLong", -1)]))

        most_delayed = queue.PriorityQueue(maxsize=5)
        checked_buses = {}
        for bus in buses:
            if bus["routeLong"] in checked_buses:
                continue
            route = bus["routeLong"]
            checked_buses[route] = 1
            avg_delay = get_avg_delay(bus)
            if most_delayed.full():
                delay, _ = most_delayed.queue[0]
                if delay < avg_delay:
                    most_delayed.get()
                    most_delayed.put((avg_delay, bus["routeLong"]))
            else:
                most_delayed.put((avg_delay, bus["routeLong"]))

        most_delayed = most_delayed.queue
        highest_aqi = get_highest_aqi(self.db)
        most_polluted = get_most_polluted(buses, highest_aqi)

        data = {'mostDelayed': most_delayed, 'mostPolluted': most_polluted}
        return Response.send_json_200(data)
