package study.bank.domains.auth.service

import org.springframework.stereotype.Service
import study.bank.config.OAuth2Config
import study.bank.interfaces.OAuth2TokenResponse
import study.bank.interfaces.OAuth2UserResponse
import study.bank.interfaces.OAuthServiceInterface

private const val key = "github"


@Service(key)
class GithubAuthService(
  private val config: OAuth2Config
) : OAuthServiceInterface {

  private val oAuthInfo = config.providers[key] ?: throw TODO("custom exception")
  override val providerName: String = key

  override fun getToken(code: String): OAuth2TokenResponse {
    TODO("Not yet implemented")
  }
  
  override fun getUserInfo(accessToken: String): OAuth2UserResponse {
    TODO("Not yet implemented")
  }
  
}