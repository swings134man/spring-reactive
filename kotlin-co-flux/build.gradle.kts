plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.4.7"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.lucas"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.8.6") // swagger-ui
	developmentOnly("org.springframework.boot:spring-boot-devtools")

	// DB
	implementation("org.postgresql:r2dbc-postgresql:1.0.3.RELEASE")
//	runtimeOnly("io.r2dbc:r2dbc-postgresql:0.8.11.RELEASE") // 위와 같은 버전
	implementation("io.r2dbc:r2dbc-h2") // Test H2 DB

	// Runtime
	runtimeOnly ("io.netty:netty-resolver-dns-native-macos:4.1.104.Final:osx-aarch_64") // Netty Dns Resolver For ARM Mac Error

	// Test dependencies
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
	testImplementation("io.kotest:kotest-runner-junit5:5.8.0") // kotest
	testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.2") // kotest-extensions
	testImplementation("io.mockk:mockk:1.13.8") // MockK
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
