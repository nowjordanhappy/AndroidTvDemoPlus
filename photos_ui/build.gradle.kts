apply {
    from("$rootDir/xml-module.gradle")
}
apply(plugin = "org.jetbrains.kotlin.android")

dependencies {
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.coreUi))
    "implementation"(project(Modules.photos))

    "implementation"(Coil.coilKotlin)
}