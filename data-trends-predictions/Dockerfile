FROM python:3.8-slim-buster

EXPOSE 8050

WORKDIR /app

COPY requirements.txt requirements.txt
RUN pip3 install -r requirements.txt

COPY . .

ENTRYPOINT [ "python" ]

CMD [ "data_app.py","--env", "eureka"]
