package study.bank.common.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import study.bank.common.exception.CustomException
import study.bank.common.exception.ErrorCode

object Logging {
  fun <T : Any> getLogger(clazz: Class<T>): Logger = LoggerFactory.getLogger(clazz)

  fun <T> logFor(log: Logger, function: () -> T?): T {
    val logInfo = mutableMapOf<String, Any>()
    
    logInfo["startTime"] = now()
    
    val result = function.invoke()
    
    logInfo["endTime"] = now()
    
    log.info("logInfo: {}", logInfo)
    
    return result ?: throw CustomException(ErrorCode.FAILED_TO_INVOKE_IN_LOGGER)
  }

  private fun now(): Long = System.currentTimeMillis()
  
}