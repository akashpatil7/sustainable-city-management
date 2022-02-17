import unittest
from data_app import app

class AnalysisTests(unittest.TestCase):
    def testCurrentHourAverages(self):
        response = app.test_client().get('/getCurrentHourAverages')
        assert response.status_code == 200