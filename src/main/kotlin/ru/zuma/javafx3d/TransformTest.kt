package ru.zuma.javafx3d

import javafx.scene.transform.Rotate
import ru.zuma.javafx3d.engine.euler
import ru.zuma.javafx3d.engine.multiply

fun main() {
    println("x = ${Rotate.Y_AXIS.multiply(Rotate.Z_AXIS)}")
    println("y = ${Rotate.Z_AXIS.multiply(Rotate.X_AXIS)}")
    println("z = ${Rotate.X_AXIS.multiply(Rotate.Y_AXIS)}")

    val euler = Rotate(30.0, 0.0, 0.0, 0.0, Rotate.X_AXIS).euler()

    println()
    println("$euler")
}