package study.bank.domains.auth.service

import com.auth0.jwt.JWT
import org.springframework.stereotype.Service
import study.bank.common.exception.CustomException
import study.bank.common.exception.ErrorCode
import study.bank.interfaces.OAuthServiceInterface

@Service
class AuthService(
  private val oAuth2Services: Map<String, OAuthServiceInterface>
) {

  fun handleAuth(state: String, code: String): String {

    val provider = state.lowercase()
    val callService = oAuth2Services[provider] ?: throw CustomException(ErrorCode.PROVIDER_NOT_FOUND, provider)

    val accessToken = callService.getToken(code)
    val userInfo = callService.getUserInfo(accessToken.accessToken)

    
  }

}
