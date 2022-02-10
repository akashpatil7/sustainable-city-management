import unittest
from flask_app import Analysis

class AnalysisTests(unittest.TestCase):
    def testCurrentHourAverages(self):
        analysis = Analysis()
        hourAverages = analysis.getCurrentHourAverages()
        testHourAverages = analysis.getCurrentHourAverages()
        self.assertEqual(hourAverages, testHourAverages)