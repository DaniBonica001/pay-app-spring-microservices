#!/bin/bash

#Create docker newtwork

echo 'Creating docker network distribuidos'
docker network create distribuidos

#Deploy consul and app-config
echo 'Deploying consul and app-config'
docker-compose -f discovery-and-config.yml up -d

#Deploy kafka
echo 'Deploying kafka'
docker-compose -f kafka.yml up -d

#Deploy databases
echo 'Deploying databases'
docker-compose -f storage.yml up -d

#Deploy services
sleep 10
echo 'Deploying services'
docker-compose -f services.yml up -d

sleep 10
echo 'Creating load balancer and api gateway'
docker-compose -f loadbalancer-apigw.yml up -d