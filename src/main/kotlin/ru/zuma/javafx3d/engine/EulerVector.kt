package ru.zuma.javafx3d.engine

class EulerVector (
    var precession: Double = 0.0,
    var nutation: Double = 0.0,
    var intrinsicRotation: Double = 0.0) {

    override fun toString(): String {
        return "EulerVector(precession=$precession, nutation=$nutation, intrinsicRotation=$intrinsicRotation)"
    }
}