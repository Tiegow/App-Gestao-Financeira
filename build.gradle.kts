plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidMultiplatformLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    ktlint {
        version.set("1.3.1")
        filter {
            exclude { element ->
                val path = element.file.absolutePath.lowercase()
                path.contains("build") || path.contains("generated")
            }
        }
    }
    
    tasks.matching { it.name.startsWith("ktlint") }.configureEach {
        if (this is org.gradle.api.tasks.SourceTask) {
            exclude("**/generated/**", "**/build/**")
        }
    }
}
