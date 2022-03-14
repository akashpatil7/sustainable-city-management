from src.common.Rresponse import Response
from enum import Enum

class EndPointMethods(Enum):
    getRecommendations = "get_recommendations"


class Pedestrian():
    def __init__(self, db):
        self.db = db
        print("Initiating Pedestrian Recommendations")

    def perform_action(self, action):
        try:
            return getattr(self, EndPointMethods[action].value)()
        except KeyError:
            print("[Pedestrian Recommendations] EndPoint not found")
        except AttributeError:
            print("[Pedestrian Recommendations] EndPoint cannot be resolved")
        return Response.not_found_404("Recommendations pedestrian: " + action + " not found")

    def get_recommendations(self):
        print("[Pedestrian Recommendations] Get")
        aqi = self.db.get_collection("Pedestrian")

        lowest_count_pedestrian_data = list(
            aqi.find({}, {
                'street': True,
                'count': True,
                'streetLatitude': True,
                'streetLongitude': True,
                'time': True,
            }).sort([
                ("count", -1),
                ("time",1)
            ]).limit(5))

        highest_count_pedestrian_data = list(
            aqi.find({}, {
                'count': True,
                'street': True,
            }).sort([
                ("count", 1),
                ("time",1)
            ]).limit(5))

        data = {
            'lowestCountPedestrianData': lowest_count_pedestrian_data,
            'highestCountPedestrianData': highest_count_pedestrian_data
        }
        return Response.send_json_200(data)
