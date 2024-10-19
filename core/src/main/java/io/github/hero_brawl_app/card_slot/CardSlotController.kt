package io.github.hero_brawl_app.card_slot

import com.badlogic.gdx.InputProcessor
import io.github.hero_brawl_app.HeroBrawl

// TODO multitouch
class CardSlotController(private var cards: ArrayList<CardSlot>): InputProcessor {

    private var currentDraggingCard: CardSlot? = null

    override fun keyDown(keycode: Int): Boolean = false
    override fun keyUp(keycode: Int): Boolean = false
    override fun keyTyped(character: Char): Boolean = false

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        for (card in cards) {
            if (card.touchDown(screenX, screenY, pointer, button)) {
                currentDraggingCard = card
                return true
            }
        }
        return false
    }
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        currentDraggingCard?.touchUp(screenX, screenY, pointer, button)
        currentDraggingCard = null
        return true
    }

     override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        currentDraggingCard?.touchDragged(screenX, screenY, pointer)
        return true
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean = false
    override fun scrolled(amountX: Float, amountY: Float): Boolean = false
    override fun touchCancelled(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = false
}
