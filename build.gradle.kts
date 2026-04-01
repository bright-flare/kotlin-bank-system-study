plugins {
  kotlin("jvm") version "2.2.21"
  kotlin("plugin.spring") version "2.2.21"
  id("org.springframework.boot") version "4.0.5"
  id("io.spring.dependency-management") version "1.1.7"
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
  testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
  
  // mysql
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("mysql:mysql-connector-java")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  
  // mongoDB
  implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
  
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
