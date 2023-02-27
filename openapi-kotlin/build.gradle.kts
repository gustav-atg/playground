import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.3-SNAPSHOT"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.8.10"
    kotlin("plugin.spring") version "1.8.10"
    id("org.openapi.generator").version("6.4.0")
}

group = "com.example.openapi"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.4")
    implementation("io.swagger.core.v3:swagger-annotations:2.2.8")
    implementation("io.swagger.core.v3:swagger-models:2.2.8")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")
    implementation("org.springdoc:springdoc-openapi-webflux-ui:1.6.14")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

apply(plugin = "org.openapi.generator")

task("openApiGenerateAll") {
    val suffix = ".openapi.yaml"
    val source = fileTree("$rootDir/api").filter {
        it.isFile && it.name.endsWith(suffix)
    }
    dependsOn(
        source.map { file ->
            val spec = file.name.removeSuffix(suffix)
            tasks.create("openApiGenerate${spec.capitalize()}", org.openapitools.generator.gradle.plugin.tasks.GenerateTask::class) {
                generatorName.set("kotlin-spring")
                inputSpec.set(file.path)
                outputDir.set("$buildDir/generated")
                packageName.set("${rootProject.group}")
                apiPackage.set("${rootProject.group}.generated.api")
                modelPackage.set("${rootProject.group}.generated.api.dto")
                configFile.set("$rootDir/api/config.json")
            }
        },
    )
}

tasks.compileKotlin {
    dependsOn("openApiGenerateAll")
}

sourceSets {
    main {
        java {
            srcDir("$rootDir/src/main/kotlin")
            srcDir("$rootDir/build/generated/src/main/kotlin")
        }
    }
}
