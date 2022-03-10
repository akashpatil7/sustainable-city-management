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
        data = { }
        return Response.send_json_200(data)
