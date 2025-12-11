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
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    
    implementation(libs.javalin)
    implementation("org.slf4j:slf4j-simple:2.0.16")

    implementation(libs.gson)
    implementation(libs.fastutil)
    implementation(libs.annotations)
    implementation(libs.commons.lang3)
    implementation(libs.dotenv.java)
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

tasks.withType<Test> {
    useJUnitPlatform()
}