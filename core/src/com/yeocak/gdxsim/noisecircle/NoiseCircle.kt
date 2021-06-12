package com.yeocak.gdxsim.noisecircle

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.yeocak.gdxsim.generalutils.noise.OpenSimplexNoiseKotlin
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.round
import kotlin.math.sin
import kotlin.random.Random

class NoiseCircle : ApplicationAdapter() {
    private lateinit var renderer: ShapeRenderer
    private lateinit var camera: OrthographicCamera
    private lateinit var viewport: Viewport
    private lateinit var stage: Stage

    private val WIDTH = 1000f
    private val HEIGHT = 1000f

    private var circleX: Float = 0f
    private var circleY: Float = 0f
    private var circleAngle: Float = 0f
    private var circleLength: Float = 100f

    private lateinit var noise: OpenSimplexNoiseKotlin

    private var speed = 0.02f
    private var averageLength = 100
    private var cornerSharpness = 1f
    private var edgeNumber = 100

    private var noiseZSeed: Float = 0f

    override fun create() {
        camera = OrthographicCamera(WIDTH, HEIGHT)
        camera.setToOrtho(false, WIDTH, HEIGHT)

        renderer = ShapeRenderer()
        viewport = StretchViewport(WIDTH, HEIGHT, camera)
        stage = Stage(viewport)

        camera.position.x = 0f
        camera.position.y = HEIGHT / 8
        camera.update()

        val noiseSeed = Random.nextLong()
        noise = OpenSimplexNoiseKotlin(noiseSeed)

        createPanel()
    }

    private fun createPanel(){
        val skin = Skin(Gdx.files.internal("default/skin/uiskin.json"))

        // Left - Speed System
        val speedTopText = Label("Speed",skin)
        speedTopText.color = Color.BLACK
        speedTopText.x = -WIDTH/2 + 5f
        speedTopText.y = HEIGHT/2 - 25f + HEIGHT / 8
        stage.addActor(speedTopText)

        val speedSlider = Slider(0f,0.05f,0.001f,true,skin)
        speedSlider.x = -WIDTH/2 + 20f
        speedSlider.y = HEIGHT/2 - 165f + HEIGHT / 8
        speedSlider.value = 0.02f
        stage.addActor(speedSlider)

        val speedBottomText = Label((round(speed * 1000)).toInt().toString(),skin)
        speedBottomText.color = Color.BLACK
        speedBottomText.x = -WIDTH/2 + 10f
        speedBottomText.y = HEIGHT/2 - 190f + HEIGHT / 8
        stage.addActor(speedBottomText)

        speedSlider.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                speed = speedSlider.value
                speedBottomText.setText((round(speed * 1000).toInt()).toString())
            }
        })

        // Middle - Average Length System
        val averageLengthTopText = Label("Average Length",skin)
        averageLengthTopText.color = Color.BLACK
        averageLengthTopText.x = -WIDTH/2 + 15f + 60f
        averageLengthTopText.y = HEIGHT/2 - 25f + HEIGHT / 8
        stage.addActor(averageLengthTopText)

        val averageLengthSlider = Slider(0f,100f,1f,true,skin)
        averageLengthSlider.x = -WIDTH/2 + 60f + 60f
        averageLengthSlider.y = HEIGHT/2 - 165f + HEIGHT / 8
        averageLengthSlider.value = 100f
        stage.addActor(averageLengthSlider)

        val averageLengthBottomText = Label(averageLengthSlider.value.toInt().toString(),skin)
        averageLengthBottomText.color = Color.BLACK
        averageLengthBottomText.x = -WIDTH/2 + 52f + 60f
        averageLengthBottomText.y = HEIGHT/2 - 190f + HEIGHT / 8
        stage.addActor(averageLengthBottomText)

        averageLengthSlider.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                averageLength = averageLengthSlider.value.toInt()
                averageLengthBottomText.setText(averageLength.toString())
            }
        })

        // Right - Corner Sharpness System
        val cornerSharpnessTopText = Label("Corner Sharpness",skin)
        cornerSharpnessTopText.color = Color.BLACK
        cornerSharpnessTopText.x = -WIDTH/2 + 45f + 160f
        cornerSharpnessTopText.y = HEIGHT/2 - 25f + HEIGHT / 8
        stage.addActor(cornerSharpnessTopText)

        val cornerSharpnessSlider = Slider(0f,5f,0.05f,true,skin)
        cornerSharpnessSlider.x = -WIDTH/2 + 90f + 160f
        cornerSharpnessSlider.y = HEIGHT/2 - 165f + HEIGHT / 8
        cornerSharpnessSlider.value = 1f
        stage.addActor(cornerSharpnessSlider)

        val cornerSharpnessBottomText = Label(round(cornerSharpness * 20).toInt().toString(),skin)
        cornerSharpnessBottomText.color = Color.BLACK
        cornerSharpnessBottomText.x = -WIDTH/2 + 88f + 160f
        cornerSharpnessBottomText.y = HEIGHT/2 - 190f + HEIGHT / 8
        stage.addActor(cornerSharpnessBottomText)

        cornerSharpnessSlider.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                cornerSharpness = cornerSharpnessSlider.value
                cornerSharpnessBottomText.setText(round(cornerSharpness * 20).toInt().toString())
            }
        })

        // End - Number of edges System
        val edgeNumberTopText = Label("Number Of Edges",skin)
        edgeNumberTopText.color = Color.BLACK
        edgeNumberTopText.x = -WIDTH/2 + 350f
        edgeNumberTopText.y = HEIGHT/2 - 25f + HEIGHT / 8
        stage.addActor(edgeNumberTopText)

        val edgeNumberSlider = Slider(3f,edgeNumber.toFloat(),1f,true,skin)
        edgeNumberSlider.x = -WIDTH/2 + 380f
        edgeNumberSlider.y = HEIGHT/2 - 165f + HEIGHT / 8
        edgeNumberSlider.value = edgeNumber.toFloat()
        stage.addActor(edgeNumberSlider)

        val edgeNumberBottomText = Label(edgeNumber.toString(),skin)
        edgeNumberBottomText.color = Color.BLACK
        edgeNumberBottomText.x = -WIDTH/2 + 378f
        edgeNumberBottomText.y = HEIGHT/2 - 190f + HEIGHT / 8
        stage.addActor(edgeNumberBottomText)

        edgeNumberSlider.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                edgeNumber = round(edgeNumberSlider.value).toInt()
                edgeNumberBottomText.setText(edgeNumber.toString())
            }
        })

        Gdx.input.inputProcessor = stage
    }

    override fun render() {
        ScreenUtils.clear(Color.WHITE)
        drawCircle()

        stage.draw()
        stage.act()
    }

    private fun drawCircle(){
        renderer.begin(ShapeRenderer.ShapeType.Filled)
        renderer.color = Color.BLACK
        renderer.projectionMatrix = camera.combined

        for(a in -1..edgeNumber){
            val noiseLength = noise.random3D(sin(a.toFloat() / (edgeNumber) * 2 * PI).toFloat() * cornerSharpness ,
                cos(a.toFloat() / (edgeNumber) * 2 * PI).toFloat() * cornerSharpness, noiseZSeed) + 2
            circleLength = noiseLength * averageLength

            val oldX = circleX
            val oldY = circleY

            changeByAngle(circleAngle)
            circleAngle = 2 * PI.toFloat() * a.toFloat() * 1f/(edgeNumber)

            if(a > 0){
                renderer.rectLine(oldX,oldY,circleX,circleY, 2f)
            }
        }

        noiseZSeed += speed

        renderer.end()
    }

    private fun changeByAngle(newAngle: Float){
        circleAngle = newAngle

        circleY = circleLength * sin(circleAngle)
        circleX = circleLength * cos(circleAngle)
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width,height)
    }

    override fun dispose() {
        renderer.dispose()
        stage.dispose()
    }

}