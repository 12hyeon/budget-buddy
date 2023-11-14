#!/usr/bin/env bash

# Redis가 이미 실행 중인지 확인
REDIS_RUNNING=$(pgrep redis-server)

# Redis가 이미 실행 중이지 않으면 실행
if [ -z "$REDIS_RUNNING" ]; then
    echo "Redis가 실행 중이지 않습니다. Redis를 시작합니다."
    redis-server &
else
    echo "Redis가 이미 실행 중입니다. 기존 프로세스 ID: $REDIS_RUNNING"
fi

PROJECT_ROOT="/home/ubuntu/app"
JAR_FILE="$PROJECT_ROOT/spring-webapp.jar"

APP_LOG="$PROJECT_ROOT/application.log"
ERROR_LOG="$PROJECT_ROOT/error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)

# build 파일 복사
echo "$TIME_NOW > $JAR_FILE 파일 복사" >> $DEPLOY_LOG
cp $PROJECT_ROOT/build/libs/*.jar $JAR_FILE

# Jar 파일에 실행 권한 주기
chmod +x $JAR_FILE

# jar 파일 실행 (백그라운드에서 실행)
echo "$TIME_NOW > $JAR_FILE 파일 실행" >> $DEPLOY_LOG
nohup java -jar $JAR_FILE > $APP_LOG 2> $ERROR_LOG &

CURRENT_PID=$(pgrep -f $JAR_FILE)
echo "$TIME_NOW > 실행된 프로세스 아이디 $CURRENT_PID 입니다." >> $DEPLOY_LOG
