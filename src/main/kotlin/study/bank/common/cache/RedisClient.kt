package study.bank.common.cache

import org.redisson.api.RedissonClient
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import study.bank.common.exception.CustomException
import study.bank.common.exception.ErrorCode
import study.bank.types.dto.Response
import java.util.concurrent.TimeUnit

@Component
class RedisClient(
    private val template: RedisTemplate<String, String>,
    private val redissonClient: RedissonClient
) {

    fun get(key: String): String? {
        return template.opsForValue().get(key)
    }

    fun <T> get(key: String, kSerializer: (Any) -> T?): T? {

        val value = template.opsForValue().get(key)

        return value?.let {
            return kSerializer(it)
        }
    }

    fun setIfNotExist(key: String, value: String): Boolean {
        return template.opsForValue().setIfAbsent(key, value) ?: false
    }

    fun <T> invokeWithMutex(key: String, function: () -> T?): Response<TransferResponse> {
        val lock = redissonClient.getLock(key)
        var lockAcquired = false

        try {
            lockAcquired = lock.tryLock(10, 15, TimeUnit.SECONDS) // lock 획득 시도 (가능한지 확인)

            if (!lockAcquired) {
                throw CustomException(ErrorCode.FAIL_TO_TRY_LOCK)
            }

            lock.lock(15, TimeUnit.SECONDS) // lock 획득

            return function.invoke()

        } catch (_: Exception) {
            throw CustomException(ErrorCode.FAIL_TO_MUTEX_INVOKE)
        }finally {

            if (lockAcquired && lock.isHeldByCurrentThread) {
                lock.unlock()
            }
        }

    }

}