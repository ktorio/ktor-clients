import org.gradle.jvm.tasks.Jar

val sourcesJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles sources JAR"
    classifier = "sources"
    println("Configuring sources for $project")
    from(project.convention.getByType(SourceSetContainer::class.java).getByName("main").allSource)
}

artifacts.add("archives", sourcesJar)

extensions.configure<PublishingExtension>("publishing") {
    repositories {
        maven {
            val name = "e5l"
            val repo = "ktor"
            val artifact = "ktor-clients"

            url = uri("https://api.bintray.com/maven/$name/$repo/$artifact/;publish=1")

            credentials {
                username = System.getenv("BINTRAY_USER")
                password = System.getenv("BINTRAY_API_KEY")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            artifact(sourcesJar)

            from(components["java"])

            groupId = "${project.group}"
            artifactId = project.name
            version = "${project.version}"

            pom {
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
            }
        }
    }
}
