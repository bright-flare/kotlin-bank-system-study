package study.bank.common.httpClient

import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.springframework.stereotype.Component
import study.bank.common.exception.CustomException
import study.bank.common.exception.ErrorCode

@Component
class CallClient(
  private val httpClient: OkHttpClient,
) {

  fun GET(url: String, headers: Map<String, String> = emptyMap()): String {
    val requestBuilder = Request.Builder().url(url)
    headers.forEach { (key, value) -> requestBuilder.addHeader(key, value) }
    val request = requestBuilder.build()
    
    
  }
  
  fun POST(url: String, headers: Map<String, String> = emptyMap(), body : RequestBody): String {
    val requestBuilder = Request.Builder().url(url).post(body)
    headers.forEach { (key, value) -> requestBuilder.addHeader(key, value) }
    val request = requestBuilder.build()
    
  }

  private fun requestHandler(response: Response): String {
    
    response.use {
      if (!it.isSuccessful) {
        val msg = "Http ${it.code}:  ${it.body?.string() ?: "Unknown error"}"
        throw CustomException(ErrorCode.FAILED_TO_CALL_CLIENT, msg)
      }
      
      return it.body?.string() ?: throw CustomException(ErrorCode.))
    }
    
  }
  
}