package com.yeocak.gdxsim.flow

import com.badlogic.gdx.math.Vector2

data class FlowParticle(
    var x: Float,
    var y: Float,
    var oldX: Float,
    var oldY: Float,
    val speed: Vector2
) {
    fun updateCoordinates(newX: Float, newY: Float){
        oldX = x
        oldY = y

        x = newX
        y = newY
    }
}