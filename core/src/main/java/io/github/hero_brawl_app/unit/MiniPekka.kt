package io.github.hero_brawl_app.unit

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle
import io.github.hero_brawl_app.HeroBrawl
import java.util.UUID

class MiniPekka(private val game:HeroBrawl, x:Float, y:Float, id:String? = null): Unit(x, y, id) {
    private val FRONT_IMAGE = Texture("mini_pekka.png")

    override val name = "mini_pekka"

    override fun render() {
        game.batch.draw(FRONT_IMAGE, super.positionX, super.positionY)
    }
}
