import unittest
from src.app import app
from src.resources.trends.bike import EndPointMethods as BikeTrendsEndPoints

class AnalysisTests(unittest.TestCase):
	def testBikeTrendsEndPoints(self):
		for end_point_name, member in BikeTrendsEndPoints.__members__.items():
			print("[TEST] Bike Trends End Point: " + end_point_name)
			response = app.test_client().get('trends/bike/' + end_point_name)
			assert response.status_code == 200

	