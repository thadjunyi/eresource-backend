The frontend and backend of the application is hosted on the IDE, while the elasticsearch, minio and mongodb is hosted on containers using local dockers.

# Dockers
# Elasticsearch (localhost:9200)
docker pull docker.elastic.co/elasticsearch/elasticsearch:7.7.1
docker run -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" --name elasticsearch docker.elastic.co/elasticsearch/elasticsearch:7.7.1

## Minio (localhost:9000 minioadmin)
docker pull minio/minio
docker run -p 9000:9000 --name minio minio/minio server /data

## MongoDB (localhost:27019)
docker pull mongo
docker run -d -p 27017-27019:27017-27019 --name mongodb mongo:latest

## Spring Boot (localhost:8080)
start the application

## Vue (localhost:8081)
npm run serve

# Steps to initialized the database
1. Create audio bucket in minio:
	- POST http://localhost:8080/api/audio/createbucket
	
2. Create video bucket in minio:
	- POST http://localhost:8080/api/video/createbucket

3. Create database "mongodb" in MongoDB

4. Create collection "Documents" in database "mongodb"

5. Create collection "Audio" in database "mongodb"

6. Create collection "Video" in database "mongodb"

7. Insert multiple documents using Postman:
	- POST http://localhost:8080/api/documents/insert
		{
			"title": <_title_>,
			"content": <_content_)
		}
		
8. Insert multiple audio in http://localhost:8081/#/audio:
	- Click on the '+' icon at the bottom right.
	
9. Insert multiple video in http://localhost:8081/#/video:
	- Click on the '+' icon at the bottom right.