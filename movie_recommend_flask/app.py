from flask import Flask, request
import json
import pymysql

import pandas as pd
import numpy as np
from tqdm.notebook import tqdm
from sklearn.metrics.pairwise import euclidean_distances
from sklearn.preprocessing import StandardScaler

# 데이터베이스 연결
db = pymysql.connect(
    host="database-1.crcwt5epkeh5.ap-northeast-2.rds.amazonaws.com", # AWS Endpoint 주소
    port=3306,
    user='admin',
    passwd='1q2w3e4r', # 비밀번호
    db='movie_db',
    charset='utf8'
)

sql = "SELECT * FROM movie_tbl;"
# SELECT 쿼리 실행 및 결과를 movie_df에 저장
movie_df = pd.read_sql(sql, db)

# 모든 행의 synopsys_vector 컬럼을 numpy 배열로 변환해서 synopsys_vector_numpy 컬럼에 대입
movie_df.loc[:, "synopsys_vector_numpy"] = \
    movie_df.loc[:, "synopsys_vector"].apply(lambda x: np.fromstring(x, dtype="float32"))

# 데이터의 각 열을 표준화
scaler = StandardScaler()
scaler.fit(np.array(movie_df["synopsys_vector_numpy"].tolist()))

movie_df["synopsys_vector_numpy_scale"] = \
    scaler.transform(np.array(movie_df["synopsys_vector_numpy"].tolist())).tolist()

# synopsys_vector_numpy_scale 컬럼의 유클리드 거리 계산
sim_score = euclidean_distances(
    movie_df["synopsys_vector_numpy_scale"].tolist(),
    movie_df["synopsys_vector_numpy_scale"].tolist()
)

# sim_score를 데이터프레임으로 변환
sim_df = pd.DataFrame(data=sim_score)

# sim_df의 인덱스에 영화 제목 대입
sim_df.index = movie_df["title"]

# sim_df의 컬럼명에 영화 제목 대입
sim_df.columns = movie_df["title"]

app = Flask(__name__)

# POST 방식으로 movie_recommend URL일 때 실행
@app.route('/movie_recommend', methods=["POST"])
def hello_world():
    req_title = request.form["title"] # 입력한 제목 리턴
    result = sim_df[req_title].sort_values()[1:4] # 입력한 영화 제목과 가장 가까운 영화 3편을 result에 대입
    result = result.index.to_list() # 영화 제목이 저장된 result.index를 리스트로 변환
    
    # JSON 문자열로 변환
    result = json.dumps(result, ensure_ascii=False)
    
    return result


if __name__ == '__main__':
    app.run(host='0.0.0.0') # 플라스크 시작 host='0.0.0.0' 다른 컴퓨터에서 접속 가능