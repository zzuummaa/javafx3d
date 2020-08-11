package ru.zuma.javafx3d.view

import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.PerspectiveCamera
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.paint.Color
import javafx.scene.paint.PhongMaterial
import javafx.scene.shape.Box
import javafx.scene.transform.Rotate
import ru.zuma.javafx3d.engine.RotatableTransform
import ru.zuma.javafx3d.style.Style3D
import tornadofx.*
import java.awt.Robot
import java.util.*

class View3D: View() {
    private val camera = PerspectiveCamera(true)
    private val cameraController = RotatableTransform(camera)
    private var isMouseStick = true
    private var isMovedByCode = true

    private val rotateSpeed = 0.2
    private var mouseXOld = 0.0
    private var mouseYOld = 0.0

    private val timer = Timer()

    override fun onDock() {
        root.scene.camera = camera
        camera.farClip = 50000.0
        camera.translateZ = -1000.0

        currentStage!!.focusedProperty().addListener(ChangeListener { observable, oldValue, newValue ->
            if (!newValue) isMouseStick = false
//            print("focused: $newValue")
        })

        currentStage!!.addEventHandler(KeyEvent.KEY_PRESSED) { event ->
            val speed = 10.0
            when (event.code) {
                KeyCode.D           -> cameraController.move(dx = speed)
                KeyCode.A           -> cameraController.move(dx = -speed)
                KeyCode.W           -> cameraController.move(dz = speed)
                KeyCode.S           -> cameraController.move(dz = -speed)
                KeyCode.SPACE       -> cameraController.move(dy = -speed)
                KeyCode.CONTROL     -> cameraController.move(dy = speed)
                KeyCode.Q           -> cameraController.rotate(dz = -rotateSpeed * 8.0)
                KeyCode.E           -> cameraController.rotate(dz =  rotateSpeed * 8.0)
                else -> {}
            }

//                println("pressed: " + event.code)
        }

        timer.schedule(object: TimerTask() {
            override fun run() {
                try {
                    Platform.runLater {
//                        if (isMouseStick) moveMouseToCenter()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }, 1000, 2000)

        moveMouseToCenter()
    }

    override val root = stackpane {
        addClass(Style3D.topLevelStyle)
        onMouseClicked = EventHandler { event ->
            println("on mouse click")
            if (!isMouseStick) {
                isMouseStick = true
                isMovedByCode = true
                moveMouseToCenter()
            }
        }
        onMouseMoved = EventHandler { event ->
//            println("mouse x: ${event.sceneX}, mouse y: ${event.sceneY}")
            if (isMovedByCode) {
                isMovedByCode = false
//                println("on moved by code")
                return@EventHandler
            }

            if (isMouseStick) {
//                println("on center mouse")
                isMovedByCode = true
                moveMouseToCenter()

                val mouseXNew = event.screenX
                val mouseYNew = event.screenY

                cameraController.rotate(dy =  (mouseXNew - mouseXOld) * rotateSpeed,
                                        dx = -(mouseYNew - mouseYOld) * rotateSpeed)

                mouseXOld = mouseXNew
                mouseYOld = mouseYNew
            }
        }

        group {
//            subscene {
//                fill = Color.ALICEBLUE
//                camera = this@View3D.camera
//            }
            addChildIfPossible(camera)
            addChildIfPossible(with (Box(200.0, 200.0, 200.0)) {
                material = with (PhongMaterial()) {
                    specularColor = Color.RED
                    diffuseColor = Color.RED
                    this
                }
                this
            })

        }
    }

    fun moveMouseToCenter() {
        val x = (root.scene.window.x + root.scene.window.width / 2).toInt()
        val y = (root.scene.window.y + root.scene.window.height / 2).toInt()
        Robot().mouseMove(x, y)
        mouseXOld = x.toDouble()
        mouseYOld = y.toDouble()
    }
}