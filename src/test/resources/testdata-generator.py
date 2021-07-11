from faker import Faker
from datetime import datetime, timedelta
import random

fake = Faker()
now = datetime.now()
datapointsPerSensor = 5*4*24 #5 days, each 15min

sourceFile = open('datapoints.sql', 'w')

print("INSERT INTO raw_data (moisture, created, mac_id) VALUES", file = sourceFile)
for intervalIndex in range(datapointsPerSensor):
  fiftyMinutesInterval = intervalIndex*15
  creationTS = now - timedelta(minutes=fiftyMinutesInterval)
  for sensorIndex in range(10):
    moisture = random.randint(1,100)
    lineEnding = ";" if (intervalIndex==datapointsPerSensor-1 and sensorIndex==9) else ","
    print("({}, '{}', {}){}".format(moisture, creationTS, sensorIndex+1, lineEnding), file = sourceFile)

sourceFile.close()