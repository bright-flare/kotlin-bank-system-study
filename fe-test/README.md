# /callback 테스트용 FE

`GET /api/v1/auth/callback?code=&state=` 을 검증하기 위한 정적 페이지 + 프록시 서버.
의존성 없음(Node 내장 모듈만), Node 18 이상 필요.

## 실행

```bash
node fe-test/server.js       # http://localhost:3000
PORT=3001 node fe-test/server.js
```

백엔드는 `:9090`(application.yml `server.port`)에서 따로 띄워둔다.

## 사용법

1. **프록시 호출** — Node 서버가 대신 호출하며 redirect 를 따라가지 않는다.
   302 상태코드, `Location`, `Set-Cookie` 를 가공 없이 확인할 수 있다.
   (브라우저 fetch 로는 이 둘을 스크립트에서 읽을 수 없어서 프록시를 둔다.)
2. **브라우저 직접 호출** — 실제 리다이렉트가 `http://localhost:3000` 으로 돌아오는지,
   쿠키가 실제로 저장되는지 확인. `authToken` 은 httpOnly 라 DevTools > Application > Cookies 에서 본다.
3. **실제 OAuth 로그인** — client_id 를 넣고 공급자 인가 화면부터 태운다.

## 확인해볼 점

- `application.yml` 의 `redirect-uri` 가 `http://localhost:8080/callback/github` 인데,
  실제 엔드포인트는 `http://localhost:9090/api/v1/auth/callback` 이다. 공급자에 등록한 값과 셋이 모두 일치해야 한다.
- 쿠키 `path` 가 `/callback` 이라 `/api/v1/auth/**` 요청에는 브라우저가 쿠키를 자동으로 붙이지 않는다.
