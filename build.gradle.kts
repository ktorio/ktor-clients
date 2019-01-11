import org.jetbrains.dokka.gradle.*
import org.jetbrains.kotlin.utils.addToStdlib.*

val implementation = "implementation"
val testImplementation = "testImplementation"

buildscript {
    val kotlin_version = "1.3.11"
    val dokka_version = "0.9.17"

    repositories {
        jcenter()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:$dokka_version")
    }
}

val kotlin_version = "1.3.11"
val kotlinx_coroutines = "1.1.0"
val ktor_version = "1.1.1"

val junit_version = "4.12"
val hamkrest_version = "1.5.0.0"
val logback_version = "1.2.3"
val docker_compose_rule_junit = "0.34.0"

allprojects {
    group = "io.ktor.clients"
    version = "0.0.1"

    repositories {
        maven { setUrl("https://dl.bintray.com/ktorio/ktor") }
        maven { setUrl("https://dl.bintray.com/kotlin/kotlinx") }
        maven { setUrl("https://dl.bintray.com/palantir/releases") }
        jcenter()
    }

    apply(plugin = "kotlin")
    apply(plugin = "maven")
    apply(plugin = "maven-publish")
    apply(plugin = "org.jetbrains.dokka")

    configure<SourceSetContainer> {
        getByName("main") {
            java.srcDirs("src")
            resources.srcDirs("resources")
        }
        getByName("test") {
            java.srcDirs("test")
        }
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinx_coroutines")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-debug:$kotlinx_coroutines")
        implementation("io.ktor:ktor-network:$ktor_version")
        implementation("ch.qos.logback:logback-classic:$logback_version")

        testImplementation("io.ktor:ktor-server-core:$ktor_version")
        testImplementation("io.ktor:ktor-server-sessions:$ktor_version")
        testImplementation("junit:junit:$junit_version")
        testImplementation("com.natpryce:hamkrest:$hamkrest_version")
        testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
        testImplementation("com.palantir.docker.compose:docker-compose-rule-junit4:$docker_compose_rule_junit")
    }

    apply { from(rootProject.file("gradle/publish.gradle.kts")) }

    (tasks["dokka"] as DokkaTask).apply {
        outputFormat = "gfm"
        outputDirectory = "api"
    }
}
