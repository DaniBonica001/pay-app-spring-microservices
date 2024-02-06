#!/bin/bash

#Create docker newtwork

docker network create distribuidos

#Deploy consul and app-config

docker-compose -f discovery-and-config.yml up -d

#Deploy kafka

docker-compose -f kafka.yml up -d

#Deploy databases

docker-compose -f storage.yml up -d

#Deploy services

docker-compose -f services.yml up -d