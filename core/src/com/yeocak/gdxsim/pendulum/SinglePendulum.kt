package com.yeocak.gdxsim.pendulum

import com.yeocak.gdxsim.generalutils.hitbox.CircleHitbox
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin

data class SinglePendulum(
    var hitbox: CircleHitbox,
    private val length: Float,
    var angle: Float,
    var oldAngle: Float = 0f,
    var speed: Float = 0f,
    private var acceleration: Float = 0f,
    var isHeld: Boolean = false
) {
    fun changeByPoint(newX: Float, newY: Float){
        oldAngle = angle
        angle = atan(-newX/newY)

        if(newY > 0){
            var reverseItem = PI.toFloat()
            if(newX < 0) reverseItem *= -1

            angle += reverseItem
        }

        changeByAngle(angle, false)
    }

    fun changeByAngle(newAngle: Float, updateOld: Boolean = true){
        angle = newAngle
        if(updateOld) {oldAngle = angle}

        hitbox.y = -length * cos(angle)
        hitbox.x = length * sin(angle)
    }

    fun changeByForce(force: Float, friction: Float){
        acceleration = -force / length
        speed += acceleration

        speed -= speed * friction

        changeByAngle(angle+speed)
    }

    fun resetSpeed() { speed = 0f }
    fun changeSpeed(newSpeed: Float) { speed = newSpeed }
}