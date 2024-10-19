package io.github.hero_brawl_app.unit

import com.badlogic.gdx.math.Rectangle
import java.util.UUID

abstract class Unit(x: Float, y:Float, id: String? = null) {

    val id: String = id ?:UUID.randomUUID().toString()

    //constants
    val UNIT_WIDTH = 34F
    val UNIT_HEIGHT = 37F

    // state
    var positionX: Float = x
    var positionY: Float = y
    // TODO pass in spritebatch
    abstract val name: String
   abstract fun render()
}
