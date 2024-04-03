plugins {
    kotlin("jvm") version "1.9.22"
    `java-library`
    jacoco
    application
}


group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("skipped", "failed")
        afterSuite(
            // spell
            KotlinClosure2({ desc: TestDescriptor, result: TestResult ->
                // Only execute on the outermost suite
                if (desc.parent == null) {
                    println(" **** Result: ${result.resultType} ****")
                    println("  >    Tests: ${result.testCount}")
                    println("  >   Passed: ${result.successfulTestCount}")
                    println("  >   Failed: ${result.failedTestCount}")
                    println("  >  Skipped: ${result.skippedTestCount}")
                }
            })
        )
    }

    reports {
        junitXml.required = true
    }

    finalizedBy(tasks.jacocoTestReport)
}

tasks.named<JacocoReport>("jacocoTestReport") {
    dependsOn(tasks.test)
    reports {
        csv.required  = true
        xml.required = false
        html.required = false
    }
}

kotlin {
    jvmToolchain(21)
}
