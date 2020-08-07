package ru.zuma.javafx3d.view

import bio.singa.javafx.viewer.XForm
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.PerspectiveCamera
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.paint.PhongMaterial
import javafx.scene.shape.Box
import javafx.scene.transform.Rotate
import ru.zuma.javafx3d.style.Style3D
import tornadofx.*
import java.awt.Robot
import java.util.*

class View3D: View() {
    private val camera = PerspectiveCamera(true)
    private val xForm = XForm()
    private var isMouseStick = true
    private var isMovedByCode = true

    private val rotateSpeed = 0.1
    private val rotateX = Rotate(0.0, 0.0, 0.0, 0.0, Rotate.X_AXIS)
    private val rotateY = Rotate(0.0, 0.0, 0.0, 0.0, Rotate.Y_AXIS)

    private var mouseXOld = 0.0
    private var mouseYOld = 0.0

    private val timer = Timer()

    override fun onDock() {
        root.scene.camera = camera
        camera.farClip = 50000.0
        camera.translateZ = -1000.0
        camera.transforms.addAll(rotateX, rotateY)

        currentStage!!.focusedProperty().addListener(ChangeListener { observable, oldValue, newValue ->
            if (!newValue) isMouseStick = false
//            print("focused: $newValue")
        })

        currentStage!!.addEventHandler(MouseEvent.MOUSE_MOVED, { event ->

        })

        currentStage!!.addEventHandler(KeyEvent.KEY_PRESSED) { event ->
            val speed = 10.0
            when (event.code) {
                KeyCode.D           -> moveCamera(dx = speed)
                KeyCode.A           -> moveCamera(dx = -speed)
                KeyCode.W           -> moveCamera(dz = speed)
                KeyCode.S           -> moveCamera(dz = -speed)
                KeyCode.SPACE       -> moveCamera(dy = -speed)
                KeyCode.CONTROL     -> moveCamera(dy = speed)
                KeyCode.E           -> rotateX.angle += rotateSpeed * 4
                KeyCode.Q           -> rotateX.angle -= rotateSpeed * 4
                KeyCode.X           -> rotateY.angle += rotateSpeed * 4
                KeyCode.Z           -> rotateY.angle -= rotateSpeed * 4
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
                mouseXOld = event.x
                mouseYOld = event.y
            }
        }
        onMouseMoved = EventHandler { event ->
//            println("mouse x: ${event.sceneX}, mouse y: ${event.sceneY}")
            if (isMovedByCode) {
                isMovedByCode = false
//                println("on moved by code")
                mouseXOld = event.x
                mouseYOld = event.y
                return@EventHandler
            }

            if (isMouseStick) {
                isMovedByCode = true
                moveMouseToCenter()
//                println("on center mouse")

                val mouseXNew = event.x
                val mouseYNew = event.y

                rotateY.angle += (mouseXNew - mouseXOld) * rotateSpeed
                rotateX.angle -= (mouseYNew - mouseYOld) * rotateSpeed
                println("dx: ${mouseXNew - mouseXOld}, dy: ${mouseYNew - mouseYOld}")
//                println("x: $mouseXNew, y: $mouseYNew")

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
        Platform.runLater {
            val x = (root.scene.window.x + root.scene.window.width / 2).toInt()
            val y = (root.scene.window.y + root.scene.window.height / 2).toInt()
            Robot().mouseMove(x, y)
        }
    }

    fun moveCamera(dx: Double = 0.0, dy: Double = 0.0, dz: Double = 0.0) {
        val point = rotateY.transform(rotateX.transform(dx, dy, dz))
        println("moveCamera: {$dx, $dy, $dz} -> {${point.x}, ${point.y}, ${point.z}}")
        camera.translateX += point.x
        camera.translateY += point.y
        camera.translateZ += point.z
    }
}