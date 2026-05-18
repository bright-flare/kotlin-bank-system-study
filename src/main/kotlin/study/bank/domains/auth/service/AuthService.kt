package study.bank.domains.auth.service

import com.github.f4b6a3.ulid.UlidCreator
import org.slf4j.Logger
import org.springframework.stereotype.Service
import study.bank.common.exception.CustomException
import study.bank.common.exception.ErrorCode
import study.bank.common.jwt.JwtProvider
import study.bank.common.logging.Logging
import study.bank.common.transaction.Transactional
import study.bank.domains.auth.repository.AuthUserRepository
import study.bank.interfaces.OAuthService
import study.bank.types.entity.User

@Service
class AuthService(
  private val oAuth2Services: Map<String, OAuthService>,
  private val jwtProvider: JwtProvider,
  private val logger: Logger = Logging.getLogger(AuthService::class.java),
  private val transactional: Transactional,
  private val authUserRepository: AuthUserRepository,
) {

  fun handleAuth(state: String, code: String): String = Logging.logFor(logger) { logInfo ->
    
    val provider = state.lowercase()

    logInfo["provider"] = provider
    
    val callService = oAuth2Services[provider] ?: throw CustomException(ErrorCode.PROVIDER_NOT_FOUND, provider)

    val accessToken = callService.getToken(code)
    val userInfo = callService.getUserInfo(accessToken.accessToken)
    val token = jwtProvider.createToken(provider, userInfo.email, userInfo.name, userInfo.id)
    
    val username = userInfo.name ?: userInfo.email 
    
    transactional.run {
      val exist = authUserRepository.existsByUsername(username = username)
      
      if (exist) {

        authUserRepository.updateAccessTokenByUsername(username = username, accessToken = token)
        
      } else {
        val ulid = UlidCreator.getUlid().toString()
        val user = User(ulid = ulid, username = username, accessToken = token)
      }
    }

    return@logFor token
  }

}
