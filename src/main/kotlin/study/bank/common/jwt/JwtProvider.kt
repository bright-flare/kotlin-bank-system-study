package study.bank.common.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.AlgorithmMismatchException
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import study.bank.common.exception.CustomException
import study.bank.common.exception.ErrorCode
import java.util.Date

@Component
class JwtProvider(
  @Value("\${jwt.secret-key}") private val secretKey: String,
  @Value("\${jwt.time}") private val time: Long,
) {

  private val ONE_MINUTE_MILLIS: Long = 60 * 1000

  fun createToken(platform: String, email: String, name: String, id: String): String {

    return JWT.create()
      .withSubject("{$platform - $email - $name - $id}")
      .withIssuedAt(Date())
      .withExpiresAt(Date(System.currentTimeMillis() + time * ONE_MINUTE_MILLIS))
      .sign(Algorithm.HMAC256(secretKey))

  }

  fun verifyToken(token: String): DecodedJWT {

    try {
      return JWT
        .require(Algorithm.HMAC256(secretKey))
        .build()
        .verify(token)

    } catch (e: AlgorithmMismatchException) {
      throw CustomException(ErrorCode.TOKEN_IS_INVALID, e.message)
    }
  }
  
}

