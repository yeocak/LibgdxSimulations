package com.yeocak.gdxsim.pendulum

import com.badlogic.gdx.*
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.yeocak.gdxsim.generalutils.*
import com.yeocak.gdxsim.generalutils.hitbox.CircleHitbox
import kotlin.math.PI
import kotlin.math.sin

class PendulumMain : ApplicationAdapter() {
    private lateinit var batch: SpriteBatch
    private lateinit var renderer: ShapeRenderer
    private lateinit var camera: OrthographicCamera
    private lateinit var viewport: Viewport

    private val friction = 0.003f

    private val WIDTH = 1000f
    private val HEIGHT = 1000f

    private val pendulum = SinglePendulum(CircleHitbox(0f,0f,30f),HEIGHT/2,0f,0f,0f)

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG

        batch = SpriteBatch()
        renderer = ShapeRenderer()

        camera = OrthographicCamera(WIDTH, HEIGHT)
        camera.setToOrtho(false, WIDTH, HEIGHT)
        camera.position.x = 0f
        camera.position.y = -HEIGHT/6
        camera.update()

        viewport = StretchViewport(WIDTH, HEIGHT, camera)

        pendulum.changeByAngle(30 * PI.toFloat() / 180)
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width,height)
    }

    override fun render() {
        ScreenUtils.clear(Color.BLACK)

        batch.begin()
        movementProcess()
        draw()

        batch.end()
    }

    private fun movementProcess(){
        if(Input.Buttons.LEFT.isButtonPressed()){
            val mouseX = (Gdx.input.x.toFloat()-Gdx.graphics.width/2) * (WIDTH / Gdx.graphics.width)
            val mouseY = -((Gdx.input.y.toFloat()-Gdx.graphics.height/2+Gdx.graphics.height/6)*(HEIGHT/Gdx.graphics.height))

            val mousePoint = Point(mouseX, mouseY)

            if(pendulum.hitbox.isIncluding(mousePoint) || pendulum.isHeld){
                pendulum.resetSpeed()
                pendulum.isHeld = true

                pendulum.changeByPoint(mouseX, mouseY)
            } else {
                pendulum.changeByForce(sin(pendulum.angle), friction)
            }
        } else{
            if(pendulum.isHeld){
                pendulum.changeSpeed(((pendulum.angle-pendulum.oldAngle)*0.3).toFloat())

                pendulum.isHeld = false
            }
            pendulum.changeByForce(sin(pendulum.angle), friction)
        }
    }

    private fun draw(){
        renderer.begin(ShapeRenderer.ShapeType.Filled)

        renderer.projectionMatrix = camera.combined
        renderer.color = Color.WHITE
        renderer.rectLine(0f,0f,pendulum.hitbox.x,pendulum.hitbox.y,3f)
        renderer.circle(pendulum.hitbox.x, pendulum.hitbox.y, pendulum.hitbox.radius)

        renderer.end()
    }

    override fun dispose() {
        batch.dispose()
        renderer.dispose()
    }
}
