package ru.zuma.javafx3d.engine

import javafx.scene.Node
import javafx.scene.transform.Rotate
import javafx.scene.transform.Transform

class RotatableTransform(private val node: Node) {
    private var rotation: Transform = Rotate()
    var directionZ = rotation.transform(Rotate.Z_AXIS)
    private val nodeTransformIndex: Int

    init {
        node.transforms.add(rotation)
        nodeTransformIndex = node.transforms.indexOf(rotation)
    }

    fun move(dx: Double = 0.0, dy: Double = 0.0, dz: Double = 0.0) {
        val d = rotation.transform(dx, dy, dz)
        println("move node: {$dx, $dy, $dz} -> {${d.x}, ${d.y}, ${d.z}}")
        node.translateX += d.x
        node.translateY += d.y
        node.translateZ += d.z
    }

    fun rotate(dx: Double = 0.0, dy: Double = 0.0, dz: Double = 0.0) {
        if (dx != 0.0) rotation = rotation.createConcatenation(Rotate(dx, 0.0, 0.0, 0.0, Rotate.X_AXIS))
        if (dy != 0.0) rotation = rotation.createConcatenation(Rotate(dy, 0.0, 0.0, 0.0, Rotate.Y_AXIS))
        if (dz != 0.0) rotation = rotation.createConcatenation(Rotate(dz, 0.0, 0.0, 0.0, Rotate.Z_AXIS))
        val old = directionZ
        val directionX = rotation.transform(Rotate.X_AXIS)
        val directionY = rotation.transform(Rotate.Y_AXIS)
        directionZ = rotation.transform(Rotate.Z_AXIS)
//        println("rotation z: {${old.x}, ${old.y}, ${old.z}} -> {${directionZ.x}, ${directionZ.y}, ${directionZ.z}}")
        println("angles: {${Rotate.X_AXIS.angle(directionX)}, ${Rotate.Y_AXIS.angle(directionY)}, ${Rotate.Z_AXIS.angle(directionZ)}}")
        node.transforms[nodeTransformIndex] = rotation
    }
}