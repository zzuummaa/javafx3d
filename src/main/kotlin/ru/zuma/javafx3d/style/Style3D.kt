package ru.zuma.javafx3d.style

import tornadofx.*

class Style3D: Stylesheet() {
    companion object {
        val topLevelStyle by cssclass()
    }

    init {
        topLevelStyle {
            prefWidth = 950.0.px
            prefHeight = 560.0.px
        }
    }
}