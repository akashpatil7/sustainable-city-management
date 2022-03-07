from flask import Flask

def create_app():
	# INIT Flask App
	app = Flask(__name__)

	# INIT Configs
	app.config.from_pyfile('settings.py')
	DB_CONNECTION_STRING = app.config.get('DB_CONNECTION_STRING')
	DB_NAME = app.config.get('DB_NAME')
	FLASK_RUN_PORT = app.config.get('FLASK_RUN_PORT')
	EUREKA_SERVER_HOST = app.config.get('EUREKA_SERVER_HOST')
	EUREKA_REGISTERED_APP_NAME = app.config.get('EUREKA_REGISTERED_APP_NAME')
	EUREKA_SERVER_NAME = app.config.get('EUREKA_SERVER_NAME')
	print(f'FLASK_RUN_PORT: {FLASK_RUN_PORT}\nEUREKA_REGISTERED_APP_NAME: {EUREKA_REGISTERED_APP_NAME}\nEUREKA_SERVER_NAME: {EUREKA_SERVER_NAME}')

	# INIT Eureka Client
	import py_eureka_client.eureka_client as eureka_client
	eureka_client.init(
		eureka_server = EUREKA_SERVER_NAME,
		app_name = EUREKA_REGISTERED_APP_NAME,
		instance_port = FLASK_RUN_PORT
	)

	# INIT Cors
	from flask_cors import CORS
	cors = CORS(app, resources={r"/*": {"origins": "*"}})

	# INIT RESTFul API 
	from flask_restful import Api
	api = Api(app, catch_all_404s=True)

	# INIT DB Object
	from src.common.database import Database
	db = Database(connection_string = DB_CONNECTION_STRING, database = DB_NAME)

	# ADD Routes
	route_args = {'db': db}

	from src.resources.recommendations import Recommendations
	api.add_resource(Recommendations, '/recommendations/<string:data_indicator>/<string:action>', resource_class_kwargs=route_args)

	from src.resources.trends import Trends
	api.add_resource(Trends, '/trends/<string:data_indicator>/<string:action>', resource_class_kwargs=route_args)

	return app