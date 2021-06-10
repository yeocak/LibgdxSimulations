package com.yeocak.gdxsim.generalutils.hitbox

import com.yeocak.gdxsim.generalutils.Point

interface ObjectHitbox {
    infix fun isIncluding(point: Point): Boolean
}