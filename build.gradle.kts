import org.jetbrains.gradle.ext.Gradle
import org.jetbrains.gradle.ext.RunConfigurationContainer

plugins {
    id("java-library")
    id("maven-publish")
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.7"
    id("eclipse")
    id("com.gtnewhorizons.retrofuturagradle") version "1.2.3"
}

// Project properties
group = "mods.betterwithpatches"
version = "1.0.0"

// Set the toolchain version to decouple the Java we run Gradle with from the Java used to compile and run the mod
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
        // Azul covers the most platforms for Java 8 toolchains, crucially including MacOS arm64
        vendor.set(JvmVendorSpec.AZUL)
    }
    // Generate sources and javadocs jars when building and publishing
    withSourcesJar()
    withJavadocJar()
}

// Most RFG configuration lives here, see the JavaDoc for com.gtnewhorizons.retrofuturagradle.MinecraftExtension
minecraft {
    mcVersion.set("1.7.10")

    // Username for client run configurations
    username.set("bingus")

    // Generate a field named VERSION with the mod version in the injected Tags class
    injectedTags.put("VERSION", project.version)

    // If you need the old replaceIn mechanism, prefer the injectTags task because it doesn't inject a javac plugin.
    // tagReplacementFiles.add("RfgExampleMod.java")

    // Enable assertions in the mod's package when running the client or server
    extraRunJvmArguments.add("-ea:${project.group}")

    // If needed, add extra tweaker classes like for mixins.
    extraTweakClasses.add("org.spongepowered.asm.launch.MixinTweaker")

    // Exclude some Maven dependency groups from being automatically included in the reobfuscated runs
    groupsToExcludeFromAutoReobfMapping.addAll("com.diffplug", "com.diffplug.durian", "net.industrial-craft")
}

// Generates a class named rfg.examplemod.Tags with the mod version in it, you can find it at
tasks.injectTags.configure {
    outputClassName.set("${project.group}.Tags")
}

// Put the version from gradle into mcmod.info
tasks.processResources.configure {
    inputs.property("version", project.version)

    filesMatching("mcmod.info") {
        expand(mapOf("version" to project.version))
    }
}

// Create a new dependency type for runtime-only dependencies that don't get included in the maven publication
val runtimeOnlyNonPublishable: Configuration by configurations.creating {
    description = "Runtime only dependencies that are not published alongside the jar"
    isCanBeConsumed = false
    isCanBeResolved = false
}

listOf(configurations.runtimeClasspath, configurations.testRuntimeClasspath).forEach {
    it.configure {
        extendsFrom(
                runtimeOnlyNonPublishable
        )
    }
}

// Add an access tranformer
// tasks.deobfuscateMergedJarToSrg.configure {accessTransformerFiles.from("src/main/resources/META-INF/mymod_at.cfg")}

// Dependencies
repositories {
  flatDir {
    dirs("lib")
  }
  maven {
      name = "OvermindDL1 Maven"
      url = uri("https://gregtech.overminddl1.com/")
      mavenContent {
          excludeGroup("net.minecraftforge") // missing the `universal` artefact
        }
    }
  maven {
      name = "GTNH Maven"
      url = uri("http://jenkins.usrv.eu:8081/nexus/content/groups/public/")
      isAllowInsecureProtocol = true
    }
  maven {
      name = "jitpack"
      url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation ("mcp.mobius.waila:Waila:1.5.11-RC2-NONEI_1.7.10:dev")
    implementation ("codechicken:CodeChickenLib:1.7.10-1.1.3.140:dev")
    implementation ("codechicken:CodeChickenCore:1.7.10-1.0.7.47:dev")
    implementation ("codechicken:NotEnoughItems:1.7.10-1.0.5.120:dev")
    // Adds NotEnoughItems and its dependencies (CCL&CCC) to runClient/runServer
    //compileOnly("com.github.GTNewHorizons:NotEnoughItems:2.3.39-GTNH:dev")
    compileOnly("betterwithmods:BetterWithMods:0.6.0:dev")
    runtimeOnlyNonPublishable("betterwithmods:BetterWithMods:0.6.0")
    annotationProcessor("lib:_unimixins-mixin-1.7.10:0.1.5+unimix.0.12.0-mixin.0.8.5")
    implementation("lib:_unimixins-mixin-1.7.10:0.1.5+unimix.0.12.0-mixin.0.8.5")
    implementation("lib:_unimixins-compat-1.7.10:0.1.5")
    implementation("lib:_unimixins-spongemixins-1.7.10:0.1.5+gtnh.2.0.1")
    implementation("lib:_unimixins-gtnhmixins-1.7.10:0.1.5+2.1.9")
    implementation("lib:_unimixins-mixingasm-1.7.10:0.1.5+0.2.2")
    implementation("lib:CraftTweaker-1.7.10:3.1.0-legacy")
}

// IDE Settings
eclipse {
    classpath {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
        inheritOutputDirs = true // Fix resources in IJ-Native runs
    }
    project {
        this.withGroovyBuilder {
            "settings" {
                "runConfigurations" {
                    val self = this.delegate as RunConfigurationContainer
                    self.add(Gradle("1. Run Client").apply {
                        setProperty("taskNames", listOf("runClient"))
                    })
                    self.add(Gradle("2. Run Server").apply {
                        setProperty("taskNames", listOf("runServer"))
                    })
                    self.add(Gradle("3. Run Obfuscated Client").apply {
                        setProperty("taskNames", listOf("runObfClient"))
                    })
                    self.add(Gradle("4. Run Obfuscated Server").apply {
                        setProperty("taskNames", listOf("runObfServer"))
                    })
                }
                "compiler" {
                    val self = this.delegate as org.jetbrains.gradle.ext.IdeaCompilerConfiguration
                    afterEvaluate {
                        self.javac.moduleJavacAdditionalOptions = mapOf(
                                (project.name + ".main") to
                                        tasks.compileJava.get().options.compilerArgs.map { '"' + it + '"' }.joinToString(" ")
                        )
                    }
                }
            }
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

tasks.processIdeaSettings.configure {
    dependsOn(tasks.injectTags)
}

apply("spongemixins.gradle")
