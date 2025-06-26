plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.graalvm.buildtools.native)
    alias(libs.plugins.spotless)
    alias(libs.plugins.license)
    kotlin("jvm") version "1.9.23" // Add this line
    kotlin("plugin.spring") version "1.9.23" // For Spring support
}

group = property("group") as String
version = property("version") as String

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(property("javaVersion").toString().toInt())
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    implementation(platform(libs.spring.modulith.bom))
    implementation(libs.spring.modulith.starter.core)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.actuator)
    implementation("org.springframework.modulith:spring-modulith-core")
    implementation(libs.gson)
    implementation(libs.fastutil)
    implementation(libs.annotations)
    implementation(libs.commons.lang3)

    compileOnly(libs.lombok)

    developmentOnly(libs.spring.boot.devtools)

    annotationProcessor(libs.spring.boot.configuration.processor)
    annotationProcessor(libs.lombok)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.modulith.starter.test)

    testRuntimeOnly(libs.junit.platform.launcher)
}

spotless {
    java {
        endWithNewline()
        removeUnusedImports()
        toggleOffOn()
        indentWithTabs()

        // Pin version to 4.31 due to Spotless bug https://github.com/diffplug/spotless/issues/1992
        eclipse("4.31").configFile(rootProject.file("codeformat/formatter-config.xml"))

        importOrder()
        custom("jetbrainsNullable") { fileContents: String ->
            fileContents.replace("javax.annotation.Nullable", "org.jetbrains.annotations.Nullable")
        }

        bumpThisNumberIfACustomStepChanges(2)
    }
}

license {
    header = rootProject.file("HEADER")
    include("**/*.java")
    strictCheck = true

    mapping("java", "SLASHSTAR_STYLE")

    skipExistingHeaders = false
}

tasks.withType<Test> {
    useJUnitPlatform()
}