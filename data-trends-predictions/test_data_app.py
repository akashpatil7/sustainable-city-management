import pytest
from src.app import create_app
from src.resources.trends.bike import EndPointMethods as BikeTrendsEndPoints

@pytest.fixture
def client(monkeypatch):
	monkeypatch.setenv('FLASK_RUN_PORT', '8050')
	client = create_app().test_client()
	yield client

def test_bike_trends_end_points(client):
	for end_point_name, member in BikeTrendsEndPoints.__members__.items():
		print("[TEST] Bike Trends End Point: " + end_point_name)
		response = client.get('trends/bike/' + end_point_name)
		assert response.status_code == 200