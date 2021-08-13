plugins {
    idea
    java
    application
    id("com.github.johnrengelman.shadow") version ("7.0.0")
    id("com.github.ben-manes.versions") version ("0.39.0")
    id("io.freefair.lombok") version ("6.1.0")
}

group = "fr.raksrinana"
description = "YouTubeStatistics"

dependencies {
    implementation(libs.slf4j)
    implementation(libs.bundles.log4j2)

    implementation(libs.bundles.jackson)

    implementation(libs.picocli)
    implementation(libs.progressbar)

    implementation(libs.threeten)

    implementation(libs.bundles.google)

    compileOnly(libs.jetbrainsAnnotations)
}

repositories {
    mavenCentral()
}

tasks {
    processResources {
        expand(project.properties)
    }

    compileJava {
        val moduleName: String by project
        inputs.property("moduleName", moduleName)

        options.encoding = "UTF-8"
        options.isDeprecation = true
    }

    test {
        useJUnitPlatform()
    }

    shadowJar {
        archiveBaseName.set(project.name)
        archiveClassifier.set("shaded")
        archiveVersion.set("")
    }

    wrapper {
        val wrapperVersion: String by project
        gradleVersion = wrapperVersion
    }
}

application {
    val moduleName: String by project
    val className: String by project

    mainModule.set(moduleName)
    mainClass.set(className)
}

java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}
