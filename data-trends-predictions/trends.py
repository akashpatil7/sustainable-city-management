from flask import Blueprint

trends = Blueprint('trends', __name__)

@trends.route('/')
def index():
    return "This is trends"