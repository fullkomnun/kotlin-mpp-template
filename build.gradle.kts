allprojects {
    group = System.getenv("GITHUB_REPOSITORY")?.split('/')?.first()?.plus(".mpp") ?: "mpp"
    version = System.getenv("GITHUB_REF")?.split('/')?.last() ?: "1.development"
}

val xcodeproj: String by project

val clean by tasks.registering(Delete::class) {
    with(arrayOf(buildDir, file("$xcodeproj/app.xcworkspace"), file("$xcodeproj/Podfile.lock"),
        file("$xcodeproj/Pods"))) {
        destroyables.register(this)
        delete(this)
    }
}