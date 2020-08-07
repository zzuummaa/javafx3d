package ru.zuma.javafx3d.app

import ru.zuma.javafx3d.style.Style3D
import ru.zuma.javafx3d.view.View3D
import tornadofx.*

class App3D: App(View3D::class, Style3D::class)

fun main(args: Array<String>) {
    launch<App3D>(args)
}