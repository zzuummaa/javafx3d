package ru.zuma.javafx3d.engine

import javafx.geometry.Point3D
import javafx.scene.transform.Rotate
import javafx.scene.transform.Transform

const val DOUBLE_EPSILON = 1E-6

fun Point3D.multiply(p: Point3D) = Point3D(
        y * p.z - z * p.y,
        - x * p.z + z * p.x,
        x * p.y - y * p.x
)

/**
 * @return point of euler angles [ precession, nutation, intrinsic rotation ]
 */
fun Transform.euler(): EulerVector {
    val xh = transform(Rotate.X_AXIS)
    val yh = transform(Rotate.Y_AXIS)
    val zh = transform(Rotate.Z_AXIS)

    // Calculate line of nodes
    val n = Rotate.Y_AXIS.multiply(yh)
    println("n = ${n}, xh = $xh, yh = $yh, zh = $zh")

    return if (n.x < DOUBLE_EPSILON && n.y < DOUBLE_EPSILON && n.z < DOUBLE_EPSILON) {
        EulerVector(
                0.0,
                Rotate.Y_AXIS.angle(yh),
                zh.angle(Rotate.Z_AXIS)
        )
    } else {
        EulerVector(
                Rotate.Z_AXIS.angle(n),
                Rotate.Y_AXIS.angle(yh),
                zh.angle(n)
        )
    }
}