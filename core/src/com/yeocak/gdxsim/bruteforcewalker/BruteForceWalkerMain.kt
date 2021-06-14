package com.yeocak.gdxsim.bruteforcewalker

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.badlogic.gdx.utils.viewport.Viewport

class BruteForceWalkerMain : ApplicationAdapter() {
    private lateinit var renderer: ShapeRenderer
    private lateinit var camera: OrthographicCamera
    private lateinit var viewport: Viewport

    companion object{
        val COLUMN = 5f
        val ROW = 5f

        val movementList = mutableListOf<SingleMovement>()
    }

    private var isDone = false

    override fun create() {
        camera = OrthographicCamera(COLUMN, ROW)
        camera.setToOrtho(false, COLUMN, ROW)

        renderer = ShapeRenderer()
        viewport = StretchViewport(COLUMN, ROW, camera)

        camera.update()

        movementList.clear()

        val starter = SingleMovement(0.5f,0.5f, left = false, down = false)
        movementList.add(starter)
    }

    override fun render() {
        ScreenUtils.clear(Color.BLACK)

        draw()
        if(!isDone){
            calculateNext()
        }
    }

    private fun draw() {
        renderer.begin(ShapeRenderer.ShapeType.Filled)
        renderer.projectionMatrix = camera.combined
        renderer.color = Color.WHITE

        for(a in 0 until movementList.size) {
            val current = movementList[a]
            renderer.circle(current.x, current.y, 0.2f, 30)

            if(a != movementList.size-1){
                renderer.rectLine(current.x,current.y,movementList[a+1].x,movementList[a+1].y,0.01f)
            }
        }

        renderer.end()
    }

    private fun calculateNext(){
        if(movementList.size == (ROW * COLUMN).toInt()){
            isDone = true
        } else{
            movementList[movementList.lastIndex].addNext()
        }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

    override fun dispose() {
        renderer.dispose()
    }

}