package com.yeocak.gdxsim.generalutils.hitbox

import com.yeocak.gdxsim.generalutils.Point
import kotlin.math.pow
import kotlin.math.sqrt

class CircleHitbox(var x: Float, var y: Float, val radius: Float): ObjectHitbox {
    override infix fun isIncluding(point: Point): Boolean {
        val pytha = (x - point.x).pow(2f) + (y - point.y).pow(2f)
        val goras = sqrt(pytha)

        return goras <= radius
    }
}