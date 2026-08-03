/**
 * /api/v1/auth/callback 테스트용 정적 서버 + 프록시.
 * 의존성 없음. Node 18+ (내장 fetch, res.headers.getSetCookie) 필요.
 *
 * 프록시가 필요한 이유: 브라우저 fetch는 302 Location 과 Set-Cookie 를
 * 스크립트에서 읽을 수 없어서, 서버 사이드에서 redirect 를 따라가지 않고 원본 응답을 그대로 보여준다.
 */
const http = require('http');
const fs = require('fs');
const path = require('path');

const PORT = Number(process.env.PORT || 3000);
const PUBLIC_DIR = path.join(__dirname, 'public');

const MIME = {
  '.html': 'text/html; charset=utf-8',
  '.js': 'text/javascript; charset=utf-8',
  '.css': 'text/css; charset=utf-8',
};

function sendFile(res, filePath) {
  fs.readFile(filePath, (err, buf) => {
    if (err) {
      res.writeHead(404, { 'Content-Type': 'text/plain; charset=utf-8' });
      res.end('not found');
      return;
    }
    res.writeHead(200, { 'Content-Type': MIME[path.extname(filePath)] || 'application/octet-stream' });
    res.end(buf);
  });
}

function sendJson(res, status, body) {
  const payload = JSON.stringify(body);
  res.writeHead(status, { 'Content-Type': 'application/json; charset=utf-8' });
  res.end(payload);
}

async function proxyCallback(url, res) {
  const backend = url.searchParams.get('backend') || 'http://localhost:9090';
  const code = url.searchParams.get('code') || '';
  const state = url.searchParams.get('state') || '';

  const target = `${backend.replace(/\/$/, '')}/api/v1/auth/callback`
    + `?code=${encodeURIComponent(code)}&state=${encodeURIComponent(state)}`;

  const startedAt = Date.now();
  try {
    const upstream = await fetch(target, { method: 'GET', redirect: 'manual' });
    const body = await upstream.text();
    sendJson(res, 200, {
      target,
      status: upstream.status,
      elapsedMs: Date.now() - startedAt,
      location: upstream.headers.get('location'),
      setCookie: upstream.headers.getSetCookie ? upstream.headers.getSetCookie() : [],
      headers: Object.fromEntries(upstream.headers.entries()),
      body,
    });
  } catch (e) {
    sendJson(res, 502, { target, error: String(e), cause: String(e.cause || '') });
  }
}

const server = http.createServer((req, res) => {
  const url = new URL(req.url, `http://localhost:${PORT}`);

  if (url.pathname === '/api/proxy-callback') {
    proxyCallback(url, res);
    return;
  }

  // OAuth 공급자가 FE 를 redirect_uri 로 잡은 경우 code/state 를 눈으로 확인하는 착지 페이지
  if (url.pathname === '/callback') {
    sendFile(res, path.join(PUBLIC_DIR, 'callback.html'));
    return;
  }

  const file = url.pathname === '/' ? 'index.html' : url.pathname.replace(/^\/+/, '');
  const resolved = path.join(PUBLIC_DIR, file);
  if (!resolved.startsWith(PUBLIC_DIR)) {
    res.writeHead(403);
    res.end();
    return;
  }
  sendFile(res, resolved);
});

server.listen(PORT, () => {
  console.log(`FE test server: http://localhost:${PORT}`);
});
