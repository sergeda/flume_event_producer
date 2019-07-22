## Random Events producer solution

### Description
Implement Random Events producer
Use Java, Scala or Python to implement event producer. Each event message describes single product purchase.
Producer should connect to Flume socket (see below) and send events in a CSV format, one event per line.
Product purchase event properties:
propertie	distibution type requirement	data requirement
product name	uniform	
product price	gaussian	
purchase date	time - gaussian	1 week range
	date - uniform
product category	uniform	
client IP address	uniform	IPv4 random adresses
Note
Hive CSV SerDe uses OpenCSV library, so you could try it as well.
3. Configure Flume to consume events using NetCat Source
Flume should put events to HDFS directory events/${year}/${month}/${day}
Try to put more than 3000 events to HDFS in several batches


### To run with default configuration
By default it will try to connect to localhost and port 5454 and generate 10 events

sbt run

### To run with custom config
1. Create custom config and override any values you find in application.conf
2. Run it like this: 
sbt -Dconfig.file=/path/to/config/custom.conf run
