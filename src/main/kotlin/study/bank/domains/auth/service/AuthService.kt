package study.bank.domains.auth.service

import org.slf4j.Logger
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import study.bank.common.exception.CustomException
import study.bank.common.exception.ErrorCode
import study.bank.common.jwt.JwtProvider
import study.bank.common.logging.Logging
import study.bank.interfaces.OAuthService

@Service
class AuthService(
  private val oAuth2Services: Map<String, OAuthService>,
  private val jwtProvider: JwtProvider,
  private val logger : Logger = Logging.getLogger(AuthService::class.java)
) {

  @Transactional
  fun handleAuth(state: String, code: String): String = Logging.logFor(logger) {
    
    val provider = state.lowercase()
    val callService = oAuth2Services[provider] ?: throw CustomException(ErrorCode.PROVIDER_NOT_FOUND, provider)

    val accessToken = callService.getToken(code)
    val userInfo = callService.getUserInfo(accessToken.accessToken)
    val token = jwtProvider.createToken(provider, userInfo.email, userInfo.name, userInfo.id)
    
    
    
  }

}
