from bson.json_util import dumps
from flask import make_response

class Response:

	@staticmethod
	def send_json_response_200(data):
		print("Returning data ... ")
		response = make_response(dumps(list(data)))
		response.status_code = 200
		response.headers["Content-Type"] = "application/json"
		return response