import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
   id("com.netflix.dgs.codegen") version "5.0.6"
   id("org.springframework.boot") version "2.5.4"
   id("io.spring.dependency-management") version "1.0.11.RELEASE"
   kotlin("jvm") version "1.5.21"
   kotlin("plugin.spring") version "1.5.21"
   kotlin("kapt") version "1.5.31"
}


group = "com.ultish"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
   mavenCentral()
   mavenLocal()
}

dependencies {
   implementation(platform("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:latest.release"))
   implementation("com.netflix.graphql.dgs:graphql-dgs-spring-boot-starter")

   implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
   implementation("com.querydsl:querydsl-mongodb:5.0.0")
   implementation("com.querydsl:querydsl-apt:5.0.0")

   implementation("org.springframework.boot:spring-boot-starter-web")
   implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
   implementation("org.jetbrains.kotlin:kotlin-reflect")
   implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

   // TODO bug when using kotlin generation with spring-data, reflectionUtil
   //  on static final attribute issue.
//    kapt("com.querydsl:querydsl-kotlin-codegen:5.0.0")
   kapt("com.querydsl:querydsl-apt:5.0.0:general")

   testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<com.netflix.graphql.dgs.codegen.gradle.GenerateJavaTask> {
   generateClient = true
   packageName = "com.ultish.generated"
   schemaPaths = listOf(
      "src/main/resources/schema"
   ).toMutableList()
   language = "kotlin"

}

tasks.withType<KotlinCompile> {
   kotlinOptions {
      freeCompilerArgs = listOf("-Xjsr305=strict")
      jvmTarget = "1.8"
   }
   dependsOn("generateJava")
}

tasks.withType<Test> {
   useJUnitPlatform()
}
