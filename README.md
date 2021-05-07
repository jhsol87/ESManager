# ESManager
0. 준비
- 로컬에 9200 포트로 Elasticsearch 구동되어 있어야 함.

1. INDEX 생성
- 인덱스명과 shard 개수만 지정하면, name 필드를 가진 인덱스 생성

POST 127.0.0.1:8080/create
{
    "name" : "product",
    "numShard" : 1,
    "numReplica" : 0
}

2. 샘플 데이터 색인
- 인덱스명을 지정하면, 5개의 샘플 데이터 bulk 로 색인

POST 127.0.0.1:8080/insert
{
    "name" : "product"
}

3. 검색
- 검색어를 지정하면, 검색 소요 시간과 결과를 리턴

POST 127.0.0.1:8080/search
{
    "keyword" : "나"
}

Response
{
    "result": [
        "나이키",
        "반다나",
        "나이키 골프",
        "운동화 나이키",
        "브랜드 나이키"
    ],
    "took": 24
}
