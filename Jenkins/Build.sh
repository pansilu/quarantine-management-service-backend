#!/bin/bash

build_services(){

	#. ${WORKSPACE}/Jenkins/${ENVIRONMENT}/env

    
    echo 'spring.profiles.active=production' > ${WORKSPACE}/src/main/resources/application.properties
	#sed -i 's+MYSQL_HOST+'${MYSQL_HOST}'+g' ${WORKSPACE}/src/main/resources/application-production.properties
	
	#cp Jmeter/apache-jmeter-5.2.1.tar.gz Docker
                                                            
    #mvn clean install -DskipTests
    gradle clean build -x test
    
    if [ $? -ne 0 ]
	then
		echo "gradle build failed"
		exit 1
	fi
    cp ${WORKSPACE}/build/libs/qms-0.0.1-SNAPSHOT.jar ${WORKSPACE}/Docker
	cp ${WORKSPACE}/src/main/resources/application-production.properties ${WORKSPACE}/Docker/application.properties
    
    cd ${WORKSPACE}/Docker
	
	whoami
	ls
	pwd
	
    docker build -t qms_backend:v0.${BUILD_NUMBER} .
    if [ $? -ne 0 ]
	then
		echo "docker build failed"
		exit 1
	fi
	
    #docker push $DOCKER_REGISTRY/tms
    #docker rmi -f $DOCKER_REGISTRY/tms

}

build_services
