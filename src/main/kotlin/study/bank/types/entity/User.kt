package study.bank.types.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "account")
data class User(

  @Id
  @Column(name = "ulid", length = 12, nullable = false)
  val ulid: String,

  @Column(name = "platform", nullable = false, length = 25)
  var platform: String,

  @Column(name = "username", nullable = false, unique = true, length = 50)
  val username: String,

  @Column(name = "access_token", length = 255)
  var accessToken: String?,

  @Column(name = "created_at", nullable = false, updatable = false)
  val createdAt: LocalDateTime = LocalDateTime.now(),
  
  @Column(name = "updated_at", nullable = false, updatable = false)
  val updatedAt: LocalDateTime = LocalDateTime.now(),

)