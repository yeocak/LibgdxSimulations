package com.yeocak.gdxsim.watersurface

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.yeocak.gdxsim.generalutils.noise.OpenSimplexNoiseKotlin
import kotlin.random.Random

class WaterSurfaceMain: ApplicationAdapter() {

    private lateinit var renderer: ShapeRenderer
    private lateinit var camera: OrthographicCamera
    private lateinit var viewport: Viewport

    private val COLUMN = 100f
    private val ROW = 100f

    private lateinit var noise: OpenSimplexNoiseKotlin
    private var noiseZ = 0f

    private var noiseSpeed = 0.02f
    private var noiseZoom = 0.05f
    private var density = 0f

    override fun create() {
        camera = OrthographicCamera(COLUMN, ROW)
        camera.setToOrtho(false, COLUMN, ROW)

        renderer = ShapeRenderer()
        viewport = StretchViewport(COLUMN, ROW, camera)

        camera.update()

        val noiseSeed = Random.nextLong()
        noise = OpenSimplexNoiseKotlin(noiseSeed)
    }

    override fun render() {
        ScreenUtils.clear(Color.WHITE)

        draw()
    }

    private fun draw() {
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        renderer.begin(ShapeRenderer.ShapeType.Filled)
        renderer.projectionMatrix = camera.combined

        for(a in 1..COLUMN.toInt()){
            for(e in 1..ROW.toInt()){
                val alpha = (noise.random3D(e*noiseZoom,a*noiseZoom,noiseZ)+density)

                renderer.color = Color(0.01f,0.3f,1f,alpha)

                renderer.rectLine(a-1f,e-1f,a.toFloat(),e-1f,1f)
            }
        }

        renderer.end()

        noiseZ += noiseSpeed
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

    override fun dispose() {
        renderer.dispose()
    }

}