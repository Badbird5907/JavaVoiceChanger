plugins {
    id("java")
}

group = "dev.badbird"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation("org.urish.openal:java-openal:1.0.0")
}

tasks.jar {
    //create a fat jar
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from({
        configurations.runtimeClasspath.get().map { if (it.isDirectory()) it else zipTree(it) }
    })
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
