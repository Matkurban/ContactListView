import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.mavenPublish)
}

group = "io.github.matkurban"
version = libs.versions.library.get()

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    coordinates("io.github.matkurban", "contactlistview", version.toString())
    pom {
        name.set("ContactListView")
        description.set("Compose Multiplatform contact list with A-Z index navigation and sticky section headers")
        url.set("https://github.com/Matkurban/ContactListView")
        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
            }
        }
        developers {
            developer {
                id.set("matkurban")
                name.set("Matkurban")
            }
        }
        scm {
            url.set("https://github.com/Matkurban/ContactListView")
            connection.set("scm:git:git://github.com/Matkurban/ContactListView.git")
            developerConnection.set("scm:git:ssh://git@github.com/Matkurban/ContactListView.git")
        }
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ContactListView"
            isStatic = true
        }
    }

    jvm()

    js {
        browser()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    android {
        namespace = "io.github.matkurban.contactlistview"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }

        withHostTest {}
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.ui)
            implementation(libs.compose.material3)
            implementation(libs.kotlinx.coroutines.core)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.compose.foundation)
            implementation(libs.compose.ui)
        }
    }
}
