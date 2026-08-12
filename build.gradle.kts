plugins {
  kotlin("jvm") version "2.2.21"
  kotlin("plugin.spring") version "2.2.21"
  id("org.springframework.boot") version "4.0.5"
  id("io.spring.dependency-management") version "1.1.7"
  
  kotlin("plugin.serialization") version "1.8.0"
  kotlin("plugin.jpa") version "1.9.22"
}

group = "bright-flare-study"
version = "0.0.1-SNAPSHOT"
description = "kotlin-bank-system-study"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-webmvc")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("tools.jackson.module:jackson-module-kotlin")
  implementation("com.squareup.okhttp3:okhttp:4.12.0")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.10.0")

  // redis
  implementation("org.springframework.boot:spring-boot-starter-data-redis")
  implementation("org.redisson:redisson-spring-boot-starter:3.36.0")

  // mysql
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  
  // mongoDB
  implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
  
  // jwt
  implementation("com.auth0:java-jwt:3.12.0")
  
  // ulid
  implementation("com.github.f4b6a3:ulid-creator:5.2.3")

  runtimeOnly("com.mysql:mysql-connector-j")
  runtimeOnly("com.h2database:h2")

  developmentOnly("org.springframework.boot:spring-boot-docker-compose")

  testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}
