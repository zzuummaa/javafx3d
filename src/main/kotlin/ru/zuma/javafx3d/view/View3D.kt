package ru.zuma.javafx3d.view

import bio.singa.javafx.viewer.XForm
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.PerspectiveCamera
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.shape.Cylinder
import javafx.scene.transform.Rotate
import tornadofx.*
import java.awt.Robot

class View3D: View() {
    private val camera = PerspectiveCamera(true)
    private val xForm = XForm()
    private var isMouseStick = true
    private var isMovedByCode = false

    private val rotateX = Rotate(0.0, 0.0, 0.0, 0.0, Rotate.X_AXIS)
    private val rotateY = Rotate(0.0, 0.0, 0.0, 0.0, Rotate.Y_AXIS)

    private var mouseXOld = 0.0
    private var mouseYOld = 0.0

    override fun onDock() {
        root.scene.camera = camera
        camera.farClip = 50000.0
        camera.translateZ = -1000.0
        camera.transforms.addAll(rotateX, rotateY)

        currentStage!!.focusedProperty().addListener(ChangeListener { observable, oldValue, newValue ->
            if (!newValue) isMouseStick = false
//            print("focused: $newValue")
        })

        moveMouseToCenter(root)
    }

    override val root = group {
        addChildIfPossible(camera)
        addChildIfPossible(Cylinder(100.0, 500.0))
        keyboard {
            addEventHandler(KeyEvent.KEY_PRESSED) { event ->
                val speed = 10.0
                when (event.code) {
                    KeyCode.D           -> camera.translateX = camera.translateX + speed
                    KeyCode.A           -> camera.translateX = camera.translateX - speed
                    KeyCode.W           -> camera.translateZ = camera.translateZ + speed
                    KeyCode.S           -> camera.translateZ = camera.translateZ - speed
                    KeyCode.SPACE       -> camera.translateY = camera.translateY - speed
                    KeyCode.CONTROL     -> camera.translateY = camera.translateY + speed
                    else -> {}
                }
//                println("pressed: " + event.code)
            }
            addEventHandler(KeyEvent.KEY_RELEASED) { event ->

            }
        }
        onMouseClicked = EventHandler { event ->
            if (!isMouseStick) {
                isMouseStick = true
                Platform.runLater {
                    moveMouseToCenter(this)
                    isMovedByCode = true
                }

                mouseXOld = event.sceneX
                mouseYOld = event.sceneY

                println("mouse x: ${event.sceneX}")
            }
        }
        onMouseMoved = EventHandler { event ->
            val rotateSpeed = 0.1

            if (isMovedByCode) {
                isMovedByCode = false
                mouseXOld = event.sceneX
                mouseYOld = event.sceneY
                return@EventHandler
            }

            if (isMouseStick) {
                Platform.runLater {
                    moveMouseToCenter(this)
                    isMovedByCode = true
                }

                val mouseXNew = event.sceneX
                val mouseYNew = event.sceneY

                val rotatePitch = rotateX.angle + (mouseYNew - mouseYOld) * rotateSpeed
                rotateX.angle = rotatePitch
                val rotateYaw = rotateY.angle - (mouseXNew - mouseXOld) * rotateSpeed
                rotateY.angle = rotateYaw

                mouseXOld = mouseXNew
                mouseYOld = mouseYNew

//                println("mouse x: ${event.sceneX}")
                println("rotate x: $rotatePitch\trotate y: $rotateYaw")
            }
        }
    }

    fun moveMouseToCenter(node: Node) {
        val screenBounds = node.localToScreen(node.boundsInLocal)
        val x = (screenBounds.minX + screenBounds.width / 2).toInt()
        val y = (screenBounds.minY + screenBounds.height / 2).toInt()
        Robot().mouseMove(x, y)
    }
}