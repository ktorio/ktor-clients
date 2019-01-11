
extensions.configure<PublishingExtension>("publishing") {
    publications {
        create<MavenPublication>("maven") {
            groupId = "${project.group}"
            artifactId = project.name
            version = "${project.version}"


            from(components["java"])
        }
    }
}
