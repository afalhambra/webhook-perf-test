#!/bin/bash

########
# Deploy webhook-perf-test application to Minikube
########

eval $(minikube -p minikube docker-env)

kubectl create namespace performance

mvn \
  -f "$( dirname "$0" )/../../pom.xml" \
  clean package -DskipTests -Pminikube