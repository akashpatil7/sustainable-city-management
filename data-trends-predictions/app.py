from flask import Flask, Blueprint
import py_eureka_client.eureka_client as eureka_client
from pymongo import MongoClient

from trends import trends

rest_port = 8050
eureka_client.init(eureka_server="http://localhost:8761/eureka",
                   app_name="py-data-trends-predictions-service",
                   instance_port=rest_port)
client = MongoClient(
    "mongodb+srv://admin:admin@cluster1.varva.mongodb.net/city_dashboard")

recommendations = Blueprint('recommendations', __name__)

app = Flask(__name__)
app.register_blueprint(trends, url_prefix="/trends")


app.register_blueprint(recommendations, url_prefix="/recommendations")

if __name__ == '__main__':
	app.run(host='0.0.0.0', port=8050)