✅ EC2 서버 접속 가이드

1. .pem 키를 Git Bash에 복사하고 권한 설정

디렉토리에 pem 파일 있는 곳으로 이동
$ cd /c/corner2025_EC2/gitbash

$ chmod 400 corner2025.pem

cd ~/corner-travel-db

2. SSH 접속
$ ssh -i "corner2025.pem" ec2-user@ec2-3-35-8-211.ap-northeast-2.compute.amazonaws.com



3. 컨테이너 관리
# 컨테이너 상태 확인
docker ps

# 컨테이너 로그 확인
docker logs cornerTravelDb

# 컨테이너를 내렸다가 다시 올릴 경우
docker-compose down
docker-compose up -d


4. MySQL 접속 테스트
docker exec -it cornerTravelDb mysql -u root -p / mysql -u cornerbe -p 
# 비밀번호: rootpass
# 비밀번호: cornerbe0720



- mysql 접속 후 TABLE 조회
SHOW DATABASES;
USE travel;
SHOW TABLES;
SELECT * FROM tourist_spot LIMIT 10;