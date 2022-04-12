from src.common.response import Response
from enum import Enum

class Pedestrian():
    def __init__(self, db):
        self.db = db
        print("Initiating Pedestrian Recommendations")

    """ Endpoint
    """
    def getRecommendations(self):
        print("[Pedestrian Recommendations] Get")
        ped = self.db.get_collection("Pedestrian")
        latest_time = 0
        pedestrian_time = list(
            ped.find({}, {
                'time': True,
            }).sort([("time", -1)]).limit(1))
        for x in pedestrian_time:
            latest_time = x["time"]

        lowest_count_pedestrian_data = list(
            ped.find({
                'time': {
                    '$gte': latest_time
                },
            }, {
                'street': True,
                'count': True,
                'streetLatitude': True,
                'streetLongitude': True,
                'time': True,
            }).sort([("count", -1)]).limit(5))

        highest_count_pedestrian_data = list(
            ped.find({}, {
                'count': True,
                'street': True,
            }).sort([("count", 1)]).limit(5))

        data = {
            'lowestCountPedestrianData': lowest_count_pedestrian_data,
            'highestCountPedestrianData': highest_count_pedestrian_data
        }
        return Response.send_json_200(data)
