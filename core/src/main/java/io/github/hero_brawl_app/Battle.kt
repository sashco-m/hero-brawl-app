package io.github.hero_brawl_app

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ScreenUtils
import io.github.hero_brawl_app.board.Board
import io.github.hero_brawl_app.card_slot.CardSlot
import io.github.hero_brawl_app.card_slot.CardSlotController
import io.github.hero_brawl_app.http.BattleREST
import io.github.hero_brawl_app.model.Model
import io.github.hero_brawl_app.socket.Socket
import io.github.hero_brawl_app.unit.Unit

class Battle(private var game: HeroBrawl) : Screen {
    // state
    private val model = Model(game)

    // socket
    private var socket = Socket(game, model)

    // REST
    private var rest = BattleREST(game)

    private var board = Board(game, rest, socket, model)


    private var cards = arrayListOf(
        CardSlot(game, board, ::notify, 50F, 30F),
        CardSlot(game, board, ::notify, 200F, 30F),
        CardSlot(game, board, ::notify, 350F, 30F)
    )

    // notify card was placed
    private fun notify(card: CardSlot){
       cards.remove(card)
    }


    override fun show() {
        Gdx.input.inputProcessor = CardSlotController(cards)
    }

    override fun render(delta: Float) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f)
        game.camera.update()
        // board
        board.render()
        // card slots at bottom
        for(card in cards){
            card.render()
        }
        // images
    }

    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {}
}
