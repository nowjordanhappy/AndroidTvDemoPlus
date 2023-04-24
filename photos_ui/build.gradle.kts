apply {
    from("$rootDir/xml-module.gradle")
}
//apply(plugin = "org.jetbrains.kotlin.android")

dependencies {
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.coreUi))
    "implementation"(project(Modules.photosDomain))
    "implementation"(project(Modules.photosData))

    "implementation"(Coil.coilKotlin)
    "implementation"(Glide.glide)
}