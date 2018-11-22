buildscript {
    val kotlin_version = "1.3.10"
    repositories {
        jcenter()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}

val kotlin_version = "1.3.10"
val kotlin_coroutines = "1.0.2-debug"

allprojects {
    repositories {
        maven { setUrl("https://dl.bintray.com/kotlin/ktor") }
        maven { setUrl("https://dl.bintray.com/kotlin/kotlinx") }
        maven { setUrl("https://dl.bintray.com/palantir/releases") }
        maven { setUrl("https://dl.bintray.com/qwwdfsad/kotlinx/") }
        jcenter()
    }

//    apply plugin: "kotlin"
//    apply plugin: "maven"
//    apply plugin: "maven-publish"

//    group "io.ktor.client"
//    version "0.1.0"

    sourceSets {
        main.kotlin.srcDirs "src"
        test.kotlin.srcDirs "test"
        main.resources.srcDirs "resources"
    }

    dependencies {
        extra.properties
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinx_coroutines")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-test:$kotlinx_coroutines")
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


    task(type: Jar, "javadocJar") {
        classifier = "javadoc"
        from "build/docs/javadoc"
    }

    task(type: Jar, "sourcesJar") {
        classifier = "sources"
        from sourceSets.main.allSource
    }

//    artifacts {
//        archives javadocJar
//        archives sourcesJar
//    }
//
//    publishing {
//        publications {
//            MyPublication(MavenPublication) {
//                from components.java
//                groupId project.group
//                artifactId project.name
//                version "$project.version"
//            }
//        }
//    }
}
