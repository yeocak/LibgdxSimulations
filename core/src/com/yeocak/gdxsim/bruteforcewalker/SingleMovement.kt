package com.yeocak.gdxsim.bruteforcewalker

import com.badlogic.gdx.Gdx
import kotlin.random.Random

data class SingleMovement(
    var x: Float,
    var y: Float,
    var right: Boolean = true,
    var up: Boolean = true,
    var left: Boolean = true,
    var down: Boolean = true
) {
    private fun updateDirections(){
        BruteForceWalkerMain.movementList.find { it.x == this.x-1 &&  it.y == this.y}?.let{
            left = false
        }
        BruteForceWalkerMain.movementList.find { it.x == this.x+1 &&  it.y == this.y}?.let{
            right = false
        }
        BruteForceWalkerMain.movementList.find { it.x == this.x &&  it.y == this.y+1}?.let{
            up = false
        }
        BruteForceWalkerMain.movementList.find { it.x == this.x &&  it.y == this.y-1}?.let{
            down = false
        }

        if(x < 1f){
            left = false
        } else if(x > BruteForceWalkerMain.COLUMN-1){
            right = false
        }

        if(y < 1f){
            down = false
        } else if(y > BruteForceWalkerMain.ROW-1){
            up = false
        }
    }

    private fun howManyMove(): Int{
        var counter = 0
        if(right){
            counter += 1
        }
        if(left){
            counter += 1
        }
        if(down){
            counter += 1
        }
        if(up){
            counter += 1
        }
        return counter
    }

    fun addNext(){
        val howMany = howManyMove()
        if(howMany > 0){
            var moveCountRandom = Random.nextInt(howManyMove())+1

            if(right){
                if(moveCountRandom == 1){
                    newMovement(x+1,y)
                    return
                } else{
                    moveCountRandom -= 1
                }
            }
            if(up){
                if(moveCountRandom == 1){
                    newMovement(x,y+1)
                    return
                } else{
                    moveCountRandom -= 1
                }
            }
            if(left){
                if(moveCountRandom == 1){
                    newMovement(x-1,y)
                    return
                } else{
                    moveCountRandom -= 1
                }
            }
            if(down){
                if(moveCountRandom == 1){
                    newMovement(x,y-1)
                    return
                } else{
                    moveCountRandom -= 1
                }
            }
        } else{
            fun before() = BruteForceWalkerMain.movementList[BruteForceWalkerMain.movementList.lastIndex-1]

            when {
                before().x > x -> {
                    before().left = false
                }
                before().x < x -> {
                    before().right = false
                }
                before().y > y -> {
                    before().down = false
                }
                before().y < y -> {
                    before().up = false
                }
            }
            BruteForceWalkerMain.movementList.remove(this)
        }
    }

    private fun newMovement(x: Float, y: Float){
        val new = SingleMovement(x,y)
        new.updateDirections()
        BruteForceWalkerMain.movementList.add(new)
    }
}
