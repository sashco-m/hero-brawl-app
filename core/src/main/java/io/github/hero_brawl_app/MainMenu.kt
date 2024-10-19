package io.github.hero_brawl_app

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.ScreenUtils

class MainMenu(private var game: HeroBrawl) : Screen {

    override fun show() {}

    override fun render(delta: Float) {
        ScreenUtils.clear(0F, 0F, 0.2f, 1F);

        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Welcome to Hero Brawl!!! ", 100F, 150F);
        game.font.draw(game.batch, "Tap anywhere to begin!", 100F, 100F);
        game.batch.end();

        if (Gdx.input.isTouched) {
            game.setScreen(Battle(game));
            dispose();
        }
    }

    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {}
}
