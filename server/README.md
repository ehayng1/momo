
프로젝트 기본 정보
- 인텔리제이 기준
- JAVA VERSION
  - 1.8
- Plugin
  - lombok
  - mapstruct
- swagger url
  - http://localhost:8088/swagger-ui.html


--- 서버 구동 시 주의사항 ---

앱단 서버는 super 계정으로 꼭 실행할 것 -> 일반유저로 실행 시 블록체인 지갑 생성이 안됨

```
screen -x X 
```

입력하여 현재 super 계정으로 구동중인 서버 로그 확인가능

``` 
nohup java -jar -Dspring.profiles.active=local ./apimain.war &
```

서버 실행

```
tail -f ./nohup.out
```

로그 확인