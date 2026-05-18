package study.bank.domains.auth.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import study.bank.types.entity.User

interface AuthUserRepository : JpaRepository<User, String> {
  
  @Modifying
  @Query("UPDATE User SET accessToken = :accessToken WHERE username = :username")
  fun updateAccessTokenByUsername(username: String, accessToken: String)
  
  fun existsByUsername(username: String): Boolean
}