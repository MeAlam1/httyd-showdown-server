plugins {
    java
    alias(libs.plugins.graalvm.buildtools.native)
    alias(libs.plugins.spotless)
    alias(libs.plugins.license)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
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
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)

    implementation(libs.javalin)
    implementation(libs.slf4j.simple)
    implementation(libs.jackson)
    implementation(libs.jackson.datatype.jsr310)

    implementation(libs.gson)
    implementation(libs.fastutil)
    implementation(libs.annotations)
    implementation(libs.commons.lang3)
    implementation(libs.dotenv.java)

    implementation(libs.mysql)

    testImplementation(libs.junit.api)
    testImplementation(libs.junit.params)
    testRuntimeOnly(libs.junit.engine)

    testImplementation(libs.javalin.testtools)

    testImplementation(libs.mockito.core)
}


spotless {
    java {
        leadingSpacesToTabs()
        endWithNewline()
        removeUnusedImports()
        toggleOffOn()

        // Pin version to 4.31 due to Spotless bug https://github.com/diffplug/spotless/issues/1992
        eclipse("4.31").configFile(rootProject.file("codeformat/formatter-config.xml"))

        importOrder()

        bumpThisNumberIfACustomStepChanges(3)
    }
}

license {
    header = rootProject.file("HEADER")
    include("**/*.java")
    strictCheck = true

    mapping("java", "SLASHSTAR_STYLE")

    skipExistingHeaders = false
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()

    maxParallelForks = Runtime.getRuntime().availableProcessors()

    forkEvery = 50

    systemProperty("junit.jupiter.execution.parallel.enabled", "true")
    systemProperty("junit.jupiter.execution.parallel.mode.default", "concurrent")

    systemProperty("junit.jupiter.execution.parallel.config.strategy", "fixed")
    systemProperty("junit.jupiter.execution.parallel.config.fixed.parallelism", "4")
}