package com.yeocak.gdxsim.flow

import com.badlogic.gdx.Application
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.yeocak.gdxsim.generalutils.noise.OpenSimplexNoiseKotlin
import kotlin.math.*
import kotlin.random.Random

class FlowMain: ApplicationAdapter() {
    private lateinit var batch: SpriteBatch
    private lateinit var renderer: ShapeRenderer
    private lateinit var camera: OrthographicCamera
    private lateinit var viewport: Viewport

    private val WIDTH = 1000f
    private val HEIGHT = 1000f

    private val showVectorsAndParticles = false

    private val column = 100
    private val row = 100

    private val particleCount = 20

    private var vectorList = Array(row){ Array(column) { Vector2(0f,0f) } }
    private var particleList = Array(particleCount) { FlowParticle(0f,0f, 0f, 0f, Vector2(0f,0f)) }

    private var perlinZSeed: Double = 0.1
    private var updateCounterZSeed = 0
    private lateinit var noiseX: OpenSimplexNoiseKotlin

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG

        batch = SpriteBatch()
        renderer = ShapeRenderer()

        camera = OrthographicCamera(WIDTH, HEIGHT)
        camera.setToOrtho(false, WIDTH, HEIGHT)
        camera.position.x = 0f
        camera.position.y = 0f
        camera.update()

        viewport = StretchViewport(WIDTH, HEIGHT, camera)

        val noiseSeed: Long = Random.nextLong()
        noiseX = OpenSimplexNoiseKotlin(noiseSeed)

        settingPoints()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width,height)
    }

    override fun render() {
        settingVectors()
        calculateParticles()
        if(showVectorsAndParticles){
            ScreenUtils.clear(Color.WHITE)
            drawVectors()
            drawPoints()
        } else{
            drawWaves()
        }
    }

    private fun settingVectors(){
        for(a in 0 until row){
            for(e in 0 until column){
                val newPerlin = noiseX.random3D(a * 0.08, e * 0.08, perlinZSeed)
                val vectorAngle = newPerlin * PI

                val vectorX = cos(vectorAngle) * 4f
                val vectorY = sin(vectorAngle) * 4f
                val newVector = Vector2(vectorX.toFloat(), vectorY.toFloat())
                vectorList[a][e].set(newVector)
            }
        }

        if(updateCounterZSeed >= 10000){
            perlinZSeed += 0.0001f
            updateCounterZSeed = -10000
        } else{
            updateCounterZSeed += 1
        }
    }

    private fun drawVectors(){
        for(a in 0 until row){
            for(e in 0 until column){
                val ratioX = WIDTH / column
                val ratioY = HEIGHT / row

                val centerY = ratioY / 2 + a * ratioY - HEIGHT / 2
                val centerX = ratioX / 2 + e * ratioX - WIDTH / 2

                renderer.begin(ShapeRenderer.ShapeType.Filled)
                renderer.projectionMatrix = camera.combined
                renderer.color = Color.WHITE

                renderer.rectLine(centerX, centerY, centerX+vectorList[a][e].x*30, centerY+vectorList[a][e].y*30, 3f)

                renderer.end()
            }
        }
    }

    private fun settingPoints(){
        particleList.forEach {
            it.updateCoordinates(Random.nextFloat() * WIDTH - (WIDTH / 2),
                Random.nextFloat() * HEIGHT - (HEIGHT / 2))
        }
    }

    private fun calculateParticles(){
        for(a in particleList.indices) {
            if(particleList[a].x >= WIDTH / 2){
                particleList[a].x -= WIDTH
                particleList[a].oldX = particleList[a].x
            }
            if(particleList[a].x <= -WIDTH / 2){
                particleList[a].x += WIDTH
                particleList[a].oldX = particleList[a].x
            }
            if(particleList[a].y <= -HEIGHT / 2){
                particleList[a].y += HEIGHT
                particleList[a].oldY = particleList[a].y
            }
            if(particleList[a].y >= HEIGHT / 2){
                particleList[a].y -= HEIGHT
                particleList[a].oldY = particleList[a].y
            }
        }

        for(a in particleList.indices) {
            val particleColumn = ((particleList[a].x + (WIDTH / 2)) / (WIDTH / column) - 1).toInt()
            val particleRow = ((particleList[a].y + (HEIGHT / 2)) / (HEIGHT / row) - 1).toInt()

            val currentVector = vectorList[particleRow][particleColumn]

            particleList[a].speed.add(currentVector)
        }

        for(a in particleList.indices) {
            val maxSpeed = 15f

            if(particleList[a].speed.x > maxSpeed || particleList[a].speed.x < -maxSpeed){
                particleList[a].speed.x /= 5f
            }
            if(particleList[a].speed.y > maxSpeed || particleList[a].speed.y < -maxSpeed){
                particleList[a].speed.y /= 5f
            }

            particleList[a].updateCoordinates(particleList[a].speed.x + particleList[a].x,
                particleList[a].speed.y + particleList[a].y)
        }
    }

    private fun drawPoints(){
        particleList.forEach {
            renderer.begin(ShapeRenderer.ShapeType.Filled)
            renderer.projectionMatrix = camera.combined
            renderer.color = Color.WHITE

            renderer.circle(it.x, it.y, 4f)

            renderer.end()
        }
    }

    private fun drawWaves(){
        particleList.forEach {
            Gdx.gl.glEnable(GL20.GL_BLEND)
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            renderer.begin(ShapeRenderer.ShapeType.Filled)
            renderer.projectionMatrix = camera.combined
            renderer.color = Color(1f,1f,1f, 0.1f)

            renderer.rectLine(it.oldX,it.oldY, it.x, it.y, 2f)

            renderer.end()
        }
    }

    override fun dispose() {
        batch.dispose()
        renderer.dispose()
    }
}