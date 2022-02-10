import unittest
from flask_app import Analysis

# TODO(all): Write legitimate tests for flask_app.py.

class AnalysisTests(unittest.TestCase):
    def testCurrentHourAverages(self):
        analysis = Analysis()
        hourAverages = analysis.getCurrentHourAverages()
        testHourAverages = analysis.getCurrentHourAverages()
        self.assertEqual(hourAverages, testHourAverages)
        