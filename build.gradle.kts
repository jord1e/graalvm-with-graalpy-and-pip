plugins {
    application
    id("org.graalvm.buildtools.native") version "0.9.28"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass.set("org.example.Main")
}

graalvmNative {
    toolchainDetection.set(true)
}

dependencies {
    implementation("org.graalvm.polyglot:polyglot:23.1.0")
    runtimeOnly("org.graalvm.polyglot:python-community:23.1.0")
    runtimeOnly("org.graalvm.polyglot:llvm-community:23.1.0")
}

sourceSets {
    main {
        resources {
            srcDirs(".")
            include("venv/**")
        }
    }
}
