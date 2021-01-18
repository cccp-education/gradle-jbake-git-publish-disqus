import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlin.text.Charsets.UTF_8

plugins {
    id("org.jbake.site") version ("5.3.0")
    //uncomment version if running outside workspace project
    id("org.ajoberstar.git-publish") version ("3.0.0")
}

buildscript {
    dependencies {
        classpath("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.1")
        classpath("io.kotlintest:kotlintest-runner-junit5:3.4.2")
    }
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/asciidoctor/maven")
}



data class JbakeBlogConf(
        val blogDestDirName: String,
        val blogSrcPath: String,
        val useCname: Boolean,
        val cname: String
)

data class GitRepoConf(
        val name: String,
        val repository: String,
        val branch: String,
        val from: String,
        val to: String,
        val message: String,
        val user: String,
        val password: String
)


data class GitPublishConfig(val repos: List<GitRepoConf>)

val cdn: GitRepoConf = jacksonObjectMapper().readValue<GitPublishConfig>(
        File(properties["blog_git_publish_conf_path"] as String)
                .apply {
                    require(exists()) {
                        "TODO: utiliser la tache createConfig pour creer une repo config"
                    }
                }
                .readText(UTF_8)
).repos.filter {
    it.name.equals("cdn")
}.first()

val blog: JbakeBlogConf = jacksonObjectMapper()
        .readValue<JbakeBlogConf>(
                File(properties["blog_config_path"] as String).apply {
                    require(exists()) {
                        "TODO: utiliser la tache createBlogConfig pour creer une blog config"
                    }
                }
                        .readText(UTF_8)

        )

val source: GitRepoConf = jacksonObjectMapper().readValue<GitPublishConfig>(
        File(properties["blog_git_publish_conf_path"] as String)
                .apply {
                    require(exists()) {
                        "TODO: utiliser la tache createConfig pour creer une repo config"
                    }
                }
                .readText(UTF_8)
).repos.filter {
    it.name.equals("source")
}.first()


tasks.register("publishBlog") {
    description = "Publish Blog Online."
    dependsOn("bake")
    if (blog.useCname) addCname()
    finalizedBy("gitPublishPush")
}

fun addCname() = file("build").run {
    if (!exists()) assert(mkdir())
    else if (!isDirectory) {
        assert(delete())
        assert(mkdir())
    }
    assert(exists() && isDirectory)
    file("build/jbake").run {
        if (!exists()) assert(mkdir())
        else if (!isDirectory) {
            assert(delete())
            assert(mkdir())
        }
        assert(exists() && isDirectory)
        file("build/jbake/CNAME").run {
            if (!exists()) {
                assert(createNewFile())
                appendText(blog.cname, UTF_8)
            } else if (isDirectory) {
                assert(deleteRecursively())
                assert(createNewFile())
                appendText(blog.cname, UTF_8)
            }
            assert(exists() && !isDirectory)
        }
    }
}

tasks.register("displayBlogConf") {
    println("displayBlogConf")
    println(
            jacksonObjectMapper()
                    .readValue<JbakeBlogConf>(
                            File(properties["blog_config_path"] as String)
                    )
    )
}

task<Exec>("publishSource") {
    commandLine(
            "./gradlew", "publishSourceCommandLine",
            "-PGRGIT_USER=${source.user}",
            "-PGRGIT_PASS=${source.password}"
    )
}

tasks.register("publishSourceCommandLine") {
    gitPublish {
        repoUri.set(source.repository)
        branch.set(source.branch)
        contents {
            from(file(source.from)) {
                into(source.to)
                exclude(*File(".gitignore")
                        .readLines(kotlin.text.Charsets.UTF_8)
                        .filter { !it.startsWith("#") }
                        .toTypedArray())
            }
        }
        commitMessage.set(source.message)
    }
}

jbake {
    srcDirName = blog.blogSrcPath
    destDirName = blog.blogDestDirName
}

gitPublish {
    repoUri.set(cdn.repository)
    branch.set(cdn.branch)
    contents {
        from(file(cdn.from)) {
            into(cdn.to)
        }
    }
    commitMessage.set(cdn.message)
}