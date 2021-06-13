package com.yeocak.gdxsim.fractaltree

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.badlogic.gdx.utils.viewport.Viewport
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.round
import kotlin.math.sin

class FractalTreeMain : ApplicationAdapter() {
    private lateinit var renderer: ShapeRenderer
    private lateinit var camera: OrthographicCamera
    private lateinit var viewport: Viewport
    private lateinit var stage: Stage

    private val WIDTH = 1000f
    private val HEIGHT = 1000f

    private var length: Float = 200f
    private var angle: Float = PI.toFloat()/6
    private var stepCount = 10
    private var reduction = 0.7f

    override fun create() {
        camera = OrthographicCamera(WIDTH, HEIGHT)
        camera.setToOrtho(false, WIDTH, HEIGHT)

        renderer = ShapeRenderer()
        viewport = StretchViewport(WIDTH, HEIGHT, camera)
        stage = Stage(viewport)

        camera.position.x = 0f
        camera.position.y = HEIGHT/2
        camera.update()

        settingPanel()
    }

    override fun render() {
        ScreenUtils.clear(Color.BLACK)

        draw()

        stage.draw()
        stage.act()
    }

    private fun settingPanel(){
        val skin = Skin(Gdx.files.internal("default/skin/uiskin.json"))

        //Reduction Slider
        val reductionSlider = Slider(0f,1f,0.01f,false,skin)
        reductionSlider.x = -WIDTH/2 + 5f
        reductionSlider.y = HEIGHT - 50f
        reductionSlider.value = reduction
        stage.addActor(reductionSlider)

        val reductionTopText = Label(round(reduction*100).toInt().toString(),skin)
        reductionTopText.color = Color.WHITE
        reductionTopText.x = -WIDTH/2 + 62f
        reductionTopText.y = HEIGHT - 35f
        stage.addActor(reductionTopText)

        val reductionBottomText = Label("Reduction coefficient",skin)
        reductionBottomText.color = Color.WHITE
        reductionBottomText.x = -WIDTH/2
        reductionBottomText.y = HEIGHT - 80f
        stage.addActor(reductionBottomText)

        reductionSlider.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                reduction = reductionSlider.value
                reductionTopText.setText(round(reduction*100).toInt().toString())
            }
        })

        //Length Slider
        val lengthSlider = Slider(20f,300f,1f,false,skin)
        lengthSlider.x = -WIDTH/2 + 150f
        lengthSlider.y = HEIGHT - 50f
        lengthSlider.value = length
        stage.addActor(lengthSlider)

        val lengthTopText = Label(lengthSlider.value.toInt().toString(),skin)
        lengthTopText.color = Color.WHITE
        lengthTopText.x = -WIDTH/2 + 205f
        lengthTopText.y = HEIGHT - 35f
        stage.addActor(lengthTopText)

        val lengthBottomText = Label("Length",skin)
        lengthBottomText.color = Color.WHITE
        lengthBottomText.x = -WIDTH/2 + 205f
        lengthBottomText.y = HEIGHT - 80f
        stage.addActor(lengthBottomText)

        lengthSlider.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                length = round(lengthSlider.value)
                lengthTopText.setText(lengthSlider.value.toInt().toString())
            }
        })

        //Angle Slider
        val angleSlider = Slider(0f, 180f,1f,false,skin)
        angleSlider.x = -WIDTH/2 + 300f
        angleSlider.y = HEIGHT - 50f
        angleSlider.value = angle / PI.toFloat() * 180
        stage.addActor(angleSlider)

        val angleTopText = Label(angleSlider.value.toInt().toString(),skin)
        angleTopText.color = Color.WHITE
        angleTopText.x = -WIDTH/2 + 356f
        angleTopText.y = HEIGHT - 35f
        stage.addActor(angleTopText)

        val angleBottomText = Label("Angle",skin)
        angleBottomText.color = Color.WHITE
        angleBottomText.x = -WIDTH/2 + 346f
        angleBottomText.y = HEIGHT - 80f
        stage.addActor(angleBottomText)

        angleSlider.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                angle = angleSlider.value/180f*PI.toFloat()
                angleTopText.setText(angleSlider.value.toInt().toString())
            }
        })

        //Step Slider
        val stepSlider = Slider(1f, 20f,1f,false,skin)
        stepSlider.x = -WIDTH/2 + 440f
        stepSlider.y = HEIGHT - 50f
        stepSlider.value = stepCount.toFloat()
        stage.addActor(stepSlider)

        val stepTopText = Label(stepSlider.value.toInt().toString(),skin)
        stepTopText.color = Color.WHITE
        stepTopText.x = -WIDTH/2 + 492f
        stepTopText.y = HEIGHT - 35f
        stage.addActor(stepTopText)

        val stepBottomText = Label("Step Count",skin)
        stepBottomText.color = Color.WHITE
        stepBottomText.x = -WIDTH/2 + 470f
        stepBottomText.y = HEIGHT - 80f
        stage.addActor(stepBottomText)

        stepSlider.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                stepCount = round(stepSlider.value).toInt()
                stepTopText.setText(round(stepSlider.value).toInt().toString())
            }
        })

        Gdx.input.inputProcessor = stage
    }

    private fun findPointByAngle(newAngle: Float, length: Float): com.yeocak.gdxsim.generalutils.Point{
        val circleY = length * sin(newAngle)
        val circleX = length * cos(newAngle)

        return com.yeocak.gdxsim.generalutils.Point(circleX,circleY)
    }

    private fun draw(){
        renderer.begin(ShapeRenderer.ShapeType.Filled)
        renderer.projectionMatrix = camera.combined
        renderer.color = Color.WHITE
        renderer.rectLine(0f,0f,0f,length,3f)

        drawSingleDuo(com.yeocak.gdxsim.generalutils.Point(0f,length),PI.toFloat()/2,length, 1)

        renderer.end()
    }

    private fun drawSingleDuo(coordinate: com.yeocak.gdxsim.generalutils.Point, beforeAngle: Float, beforeLength: Float, step: Int){
        //Calculating Right
        val newRightAngle = beforeAngle-angle
        val newRightLine = findPointByAngle(newRightAngle, beforeLength * reduction)
        val rightFarPoint = com.yeocak.gdxsim.generalutils.Point(newRightLine.x+coordinate.x, newRightLine.y+coordinate.y)

        renderer.rectLine(coordinate.x,coordinate.y,rightFarPoint.x, rightFarPoint.y, 3f)

        //Calculating Left
        val newLeftAngle = beforeAngle+angle
        val newLeftLine = findPointByAngle(newLeftAngle, beforeLength * reduction)
        val leftFarPoint = com.yeocak.gdxsim.generalutils.Point(newLeftLine.x+coordinate.x, newLeftLine.y+coordinate.y)

        renderer.rectLine(coordinate.x,coordinate.y,leftFarPoint.x, leftFarPoint.y, 3f)

        if(step == stepCount) return

        drawSingleDuo(rightFarPoint,newRightAngle,beforeLength * reduction,step+1)
        drawSingleDuo(leftFarPoint,newLeftAngle,beforeLength * reduction,step+1)
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

    override fun dispose() {
        renderer.dispose()
        stage.dispose()
    }

}