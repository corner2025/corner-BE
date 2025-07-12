✅ EC2 서버 접속 가이드

1. .pem 키를 Git Bash에 복사하고 권한 설정

디렉토리에 pem 파일 있는 곳으로 이동
$ cd /c/corner2025_EC2/gitbash

$ chmod 400 corner2025.pem



2. SSH 접속
$ ssh -i "corner2025.pem" ec2-user@ec2-13-124-62-0.ap-northeast-2.compute.amazonaws.com

$ cd ~/corner-travel-db

3. 컨테이너 관리
# 컨테이너 상태 확인
docker ps

# 컨테이너 로그 확인
docker logs cornerTravelDb

# 컨테이너를 내렸다가 다시 올릴 경우
docker-compose down
docker-compose up -d

docker rm -f [백엔드 컨테이너 이름 또는 ID]
docker rmi [백엔드 이미지 이름 또는 ID]

4. MySQL 접속 테스트
docker exec -it cornerTravelDb mysql -u root -p / mysql -u cornerbe -p 
# 비밀번호: rootpass
# 비밀번호: cornerbe0720



- mysql 접속 후 TABLE 조회
SHOW DATABASES;
USE travel;
SHOW TABLES;
SELECT * FROM tourist_spot LIMIT 10;




✅ Backend Docker container
-기존 docker container 삭제
docker stop cornerbeserver-container
docker rm cornerbeserver-container
docker image rm cornerbeserver-image


exit; 후 재배포

scp -i /c/corner2025_EC2/gitbash/corner2025.pem -r /c/corner2025/corner-BE ec2-user@ec2-13-124-62-0.ap-northeast-2.compute.amazonaws.com:~/cornerBEServer
rm -rf ~/cornerBEServer
ssh접속 후 다시 빌드
cd ~/cornerBEServer
./gradlew build -x test
./gradlew clean
ls build/libs

도커 이미지 빌드
docker build -t cornerbeserver-image .
도커 이미지 확인
docker images

docker logs cornerbeserver-container


- //docker backend 띄우기 (cd ~/cornerBEServer)
$  vi Dockerfile
- Dockerfile
FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
- 
  scp -i /c/corner2025_EC2/gitbash/corner2025.pem -r /c/corner2025/corner-BE ec2-user@ec2-3-35-8-211.ap-northeast-2.compute.amazonaws.com:~/cornerBEServer

도커 이미지 만들기
docker build -t cornerbeserver-image .


-네트워크
$ docker network create corner-net
$ docker network ls
$ docker network rm corner-net  # 혹시 남아있는 기존 네트워크 있으면 삭제
$ docker network inspect corner-net

