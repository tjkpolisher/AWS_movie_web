# 운영체제 설정
FROM ubuntu:22.04
# 작업할 폴더 생성
WORKDIR /app
# 작업했던 파일들을 복사해서 my_boot에 복사
COPY . .

# 리눅스 bash 쉘에서 명령들이 실행되도록 설정
# 도커는 기본적으로 /bin/sh 창에서 명령들이 실행됨
SHELL ["/bin/bash", "-c"]

RUN apt-get update
# 파이썬 설치
RUN apt-get install software-properties-common -y
# pip 설치
RUN apt-get -y install python3-pip
# requirements.txt에 저장한 파이썬 라이브러리 설치
RUN pip3 install -r requirements.txt

# 5000 포트 사용
EXPOSE 5000

# 플라스크 실행
# 외부 접속 가능 --host=0.0.0.0
ENTRYPOINT [ "python3", "-m", "flask", "run", "--host=0.0.0.0" ]