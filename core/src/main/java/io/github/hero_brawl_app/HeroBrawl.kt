package io.github.hero_brawl_app

import com.badlogic.gdx.Application
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import io.github.hero_brawl_app.chat_input.ChatInput
import java.util.UUID

class HeroBrawl: Game() {
    lateinit var batch: SpriteBatch
    lateinit var shape: ShapeRenderer
    lateinit var font: BitmapFont
    lateinit var camera: OrthographicCamera
    val playerId = UUID.randomUUID().toString()
    // EMULATOR
    //val host = "10.0.2.2"
    // PHYSICAL
    val host = "localhost"

    override fun create() {
        batch = SpriteBatch()
        font = BitmapFont()
        camera = OrthographicCamera()
        camera.setToOrtho(false, 480F, 800F,)
        shape = ShapeRenderer()

        this.setScreen(MainMenu(this))

        Gdx.app.logLevel = Application.LOG_DEBUG
    }

    override fun dispose() {
        batch.dispose()
        font.dispose()
    }
}
