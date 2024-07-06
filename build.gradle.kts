plugins {
    idea
    java
    application
    alias(libs.plugins.shadow)
    alias(libs.plugins.names)
}

group = "fr.rakambda"
description = "YouTubeStatistics"

dependencies {
    implementation(platform(libs.jacksonBom))

    implementation(libs.slf4j)
    implementation(libs.bundles.log4j2)

    implementation(libs.bundles.jackson)

    implementation(libs.picocli)
    implementation(libs.progressbar)

    implementation(libs.threeten)

    implementation(libs.bundles.google)

    compileOnly(libs.jetbrainsAnnotations)
    compileOnly(libs.lombok)

    annotationProcessor(libs.lombok)
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
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}
