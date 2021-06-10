package com.yeocak.gdxsim.generalutils

import kotlin.math.pow
import kotlin.math.sqrt

fun distanceOfTwoPoints(x1: Float, y1: Float, x2: Float, y2: Float) = sqrt((x1 - x2).pow(2f) + (y1 - y2).pow(2f))

fun reRange(value: Float, oldMin: Float, oldMax: Float, newMin: Float, newMax: Float) =
    (((value - oldMin) * (newMax - newMin)) / (oldMax - oldMin)) + newMin