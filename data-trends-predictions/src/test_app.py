import imp
import pytest
from src.app import create_app

@pytest.fixture
def client(monkeypatch):
	monkeypatch.setenv('FLASK_RUN_PORT', '8050')
	monkeypatch.setenv('DB_CONNECTION_STRING', 'mongodb+srv://admin:admin@cluster1.varva.mongodb.net/city_dashboard')
	monkeypatch.setenv('DB_NAME', 'city_dashboard')
	monkeypatch.setenv('EUREKA_SERVER_HOST', 'localhost')
	monkeypatch.setenv('EUREKA_REGISTERED_APP_NAME', 'trends')

	client = create_app().test_client()
	yield client

def test_bike_trends_end_points(client):
	trends_end_point_names = ['getCurrentHourAverages', 'getHourlyAverageForAllStation',
	 'getPopularityAverageHistorical', 'getPopularityAverageToday']
	for end_point_name in trends_end_point_names:
		print("[TEST] Bike Trends End Point: " + end_point_name)
		response = client.get('trends/bike/' + end_point_name)
		assert response.status_code == 200