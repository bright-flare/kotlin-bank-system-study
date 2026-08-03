package study.bank.domains.auth.controller

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletResponseWrapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.PathMatcher
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import study.bank.domains.auth.service.AuthService
import java.net.URI
import kotlin.math.max

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(private val authService: AuthService, private val pathMatcher: PathMatcher) {

    /*
    * GET - callback API 개발
    * Req param code, state 받음.
    * auth service 로 토큰 받음
    * HttpservletRespose , 쿠키에 authToken 을 Response, path: /callback, isHttponly true, maxAge 60, 60, 24
    * ResponseEntity.status found.location() -> URI.create("") -> localhost:3000 으로
    *
    * GET - verify-token API 개발
    * Authorization 헤더값
    *
    * AuthService -> verifyToken -> jwtProvider, require로 검증
    * */

    @GetMapping("/callback")
    fun callback(
        @RequestParam code: String,
        @RequestParam state: String,
        response: HttpServletResponse
    ): ResponseEntity<Map<String, String>> {

        val token = authService.handleAuth(state = state, code = code)
        response.addCookie(
            Cookie("authToken", token).apply {
                isHttpOnly = true
                path = "/callback"
                maxAge = 60 * 60 * 24
            }
        )

        return ResponseEntity
            .status(HttpStatus.FOUND)
            .location(URI.create("http://localhost:3000")).build()
    }
}