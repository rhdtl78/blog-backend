#!/bin/bash

REPOSITORY=/home/ec2-user/app/step2
PROJECT_NAME=springboot-practice

echo pwd;

echo "> Copy build file to $REPOSITORY"

cp $REPOSITORY/zip/*.jar $REPOSITORY

echo "> Check pid of already running application"

CURRENT_PID=$(pgrep -fl ${PROJECT_NAME}.*.jar | grep jar | awk '{print $1}')

echo "  Currntly running application pid : $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
        echo "> No application running. skip kill process"
else
        echo "> Kill running application"
        echo "  kill -15 $CURRENT_PID"
        kill -15 $CURRENT_PID
        sleep 5
fi

echo "> Deploy new application"

JAR_NAME=$(ls -tr $REPOSITORY/ | grep *.jar | tail -n 1)

echo "> JAR Name : $JAR_NAME"

echo "> Give permission to JAR file $JAR_NAME"

chmod +x $JAR_NAME

echo "> Running new application : $JAR_NAME"

nohup java -jar \
        -Dspring.config.location=classpath:/application.yml,classpath:/application-real.yml/home/ec2-user/app/application-oauth.yml,/home/ec2-user/app/application-real-db.yml \
        -Dspring.profiles.active=real \
        $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &