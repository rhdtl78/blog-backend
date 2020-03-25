#!/usr/bin/env/ bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
soruce ${ABSDIR}/profile.sh

REPOSITORY=/home/ec2-user/app/step3
PROJECT_NAME=springboot-practice

echo "> Build 파일 복사"
echo "> cp $REPOSITORY/zip/*.jar $REPOSITORY"

cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> 새 application 배포"
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name $JAR_NAME"
echo "> 실행권한 부여 --> $JAR_NAME"

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

IDLE_PROFILE=$(find_idle_profile)

echo "  TARGET :: $JAR_NAME"
echo "  PROFILE :: $IDLE_PROFILE"

nohup java -jar \
    -Dspring.config.location=classpath:/application.yml,classpath:/application-$IDLE_PROFILE.yml,/home/ec2-user/app/application-oauth.yml,/home/ec2-user/app/application-real-db.yml \
    -Dspring.profiles.active=$IDLE_PROFILE \
    $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
