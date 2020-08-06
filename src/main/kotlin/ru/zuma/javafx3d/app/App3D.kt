package ru.zuma.javafx3d.app

import ru.zuma.javafx3d.view.View3D
import tornadofx.App
import tornadofx.launch

class App3D: App(View3D::class)

fun main(args: Array<String>) {
    launch<App3D>(args)
}