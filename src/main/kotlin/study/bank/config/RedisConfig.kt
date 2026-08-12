package study.bank.config

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.client.RedisClientConfig
import org.redisson.config.Config
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@Configuration
class RedisConfig {


    @Bean
    fun redisConnectionFactory(
        @Value("\${database.redis.host}") host: String,
        @Value("\${database.redis.port}") port: Int,
        @Value("\${database.redis.password}") password: String?,
        @Value("\${database.redis.database}") database: Int,
        @Value("\${database.redis.timeout}") timeout: Long,
    ): LettuceConnectionFactory {

        val config = RedisStandaloneConfiguration(host, port).apply {
            password?.let { setPassword(it) }
            setDatabase(database)
        }

        val clientConfig = LettuceClientConfiguration.builder()
            .commandTimeout(Duration.ofSeconds(timeout))
            .build()

        return LettuceConnectionFactory(config, clientConfig)
    }

    @Bean
    fun redisTemplate(factory: RedisConnectionFactory): RedisTemplate<String, String> {
        return RedisTemplate<String, String>().apply {
            connectionFactory = factory
            keySerializer = StringRedisSerializer()
            valueSerializer = StringRedisSerializer()
            hashKeySerializer = StringRedisSerializer()
            hashValueSerializer = StringRedisSerializer()
            afterPropertiesSet()
        }
    }

    @Bean
    fun redissonClient(
        @Value("\${database.redis.host}") host: String,
        @Value("\${database.redis.timeout}") timeout: Long,
        @Value("\${database.redis.password}") password: String?,
    ): RedissonClient {

        val config = Config()
        val singleServerConfig = config.useSingleServer()
            .setAddress(host)
            .setTimeout(timeout.toInt())

        if (!password.isNullOrBlank()) {
            singleServerConfig.setPassword(password)
        }

        return Redisson.create(config).also {
            println("redisson client success ")
        }
    }

}