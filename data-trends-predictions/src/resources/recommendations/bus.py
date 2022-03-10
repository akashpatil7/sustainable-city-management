import datetime
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

    def get_recommendations(self):
        print("[Bus Recommendations] Get")
        bus = self.db.get_collection("DBus_Historical")
        
        yesterday = time.time() - 86400

        buses = list(
            bus.find(
                {
                    'scheduleRelationship': 'Scheduled',
                    'stopSequence.arrivalDelay': {
                        '$gt': 0
                    },
                    'startTimestamp': {
                        '$gt': yesterday
                    },
                }, {
                    'routeLong': True,
                    'stopSequence': True,
                }).sort([
                    ("routeLong", -1),
                ]))

        def get_avg_delay(bus):
            total_delay = 0;
            index = 0
            for stop in bus["stopSequence"]:
                total_delay += stop["arrivalDelay"]
                index+=1
            return total_delay / index
            
        most_delayed = queue.PriorityQueue()
        for bus in buses:
            avg_delay = get_avg_delay(bus)
            if most_delayed.full():
                delay, _ = most_delayed.queue[0]
                if delay < avg_delay:
                    most_delayed.get()
                    most_delayed.put((avg_delay, bus["routeLong"]))
            else:
                most_delayed.put((avg_delay, bus["routeLong"]))

        most_delayed = most_delayed.queue
        data = {'mostDelayed': most_delayed}
        return Response.send_json_200(data)
