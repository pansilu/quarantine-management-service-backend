#!/bin/bash

deploy_services(){

	#. ${WORKSPACE}/Jenkins/${ENVIRONMENT}/env

    
    echo "docker rm -f qms_backend"
    docker rm -f qms_backend
	
	echo "docker run --restart always --name qms_backend -d -p 8085:8080 -v /data/logs/qms-backend:/logs qms_backend:v0.${BUILD_NUMBER}"
    docker run --restart always --name qms_backend -d -p 8085:8080 -v /data/logs/qms-backend:/logs qms_backend:v0.${BUILD_NUMBER}
}

deploy_services