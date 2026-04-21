package study.bank.domains.auth.service

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import okhttp3.FormBody
import org.springframework.stereotype.Service
import study.bank.common.exception.CustomException
import study.bank.common.exception.ErrorCode
import study.bank.common.httpClient.CallClient
import study.bank.config.OAuth2Config
import study.bank.interfaces.OAuth2TokenResponse
import study.bank.interfaces.OAuth2UserResponse
import study.bank.interfaces.OAuthServiceInterface

private const val key = "github"


@Service(key)
class GithubAuthService(
  private val config: OAuth2Config,
  private val httpClient: CallClient,
) : OAuthServiceInterface {

  private val oAuthInfo = config.providers[key] ?: throw CustomException(ErrorCode.AUTH_CONFIG_NOT_FOUND, key)
  override val providerName: String = key
  private val tokenURL = "https://github.com/login/oauth/access_token"
  private val userInfoURL = "https://api.github.com/user"

  override fun getToken(code: String): OAuth2TokenResponse {
    var body = FormBody.Builder()
      .add("code", code)
      .add("client_id", oAuthInfo.clientId)
      .add("client_secret", oAuthInfo.clientSecret)
      .add("redirect_uri", oAuthInfo.redirectUri)
      .add("grant_type", "authorization_code")
      .build()
    
    val headers = mapOf("Accept" to "application/json")
    val json = httpClient.POST(tokenURL, headers, body)
    
  }
  
  override fun getUserInfo(accessToken: String): OAuth2UserResponse {
    TODO("Not yet implemented")
  }
  
}


@Serializable
data class GoogleTokenResponse(
  @SerialName("access_token") override val accessToken: String,
) : OAuth2TokenResponse

@Serializable
data class GoogleUserResponse(
  override val id: String,
  override val name: String,
  override val email: String,
) : OAuth2UserResponse