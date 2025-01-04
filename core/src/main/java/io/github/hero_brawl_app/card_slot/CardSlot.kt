package io.github.hero_brawl_app.card_slot

import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import io.github.hero_brawl_app.HeroBrawl
import io.github.hero_brawl_app.board.Board
import io.github.hero_brawl_app.unit.MiniPekka
import kotlin.math.abs
import kotlin.math.min

class CardSlot(
    private var game: HeroBrawl,
    private var board: Board,
    private val notify: (CardSlot) -> Unit,
    private var startX:Float,
    private var startY:Float
): InputProcessor {
    // position
    private var tp: Vector3 = Vector3(startX, startY, 0F)
    // card bounds
    private val shapeBounds = Rectangle()
    private val WIDTH = 80F
    private val HEIGHT = 80F
    private fun updateShapeBounds() {
        shapeBounds.set(tp.x, tp.y, WIDTH, HEIGHT)
    }
    // preview
    // TODO make dynamic
    private var preview: Texture = Texture("mini_pekka.png")
    private val PREVIEW_WIDTH = 34F
    private val PREVIEW_HEIGHT = 37F
    // other state
    private var dragging = false
    // for calculating offsets
    private var offsetX = 0F
    private var offsetY = 0F
    private fun setOffsets(screenX:Int, screenY:Int){
        val touchPos = Vector3(screenX.toFloat(), screenY.toFloat(), 0f)
        game.camera.unproject(touchPos)
        offsetX = touchPos.x - tp.x
        offsetY = touchPos.y - tp.y
    }

    // calculated based on finger position
    private fun isBelowBoard() = tp.y + offsetY < board.boardBounds.y

    private fun getCurrentCell() = board.placeableCells.find { cell -> cell.contains(tp.x + offsetX, tp.y+offsetY)}

    fun render() {

        // show either card or preview
        if(isBelowBoard()){
           game.shape.setProjectionMatrix(game.camera.combined);
           game.shape.begin(ShapeType.Line);
           game.shape.color = Color.GREEN
           // TODO scale card size
           game.shape.rect(tp.x, tp.y, WIDTH, HEIGHT);
           game.shape.end();
        } else {
            // TODO render card name above preview
            game.batch.setProjectionMatrix(game.camera.combined)
            game.batch.begin()
            // ensure it's right under your finger
            //game.batch.draw(preview, tp.x + offsetX - (PREVIEW_WIDTH/2), tp.y + offsetY - (PREVIEW_HEIGHT/2))
             // snap to board grid
            val cell = getCurrentCell()
            if(cell != null) {
                game.batch.draw(preview, cell.x, cell.y)
            } else {
                // TODO make it snap to the closest otherwise
                game.batch.draw(preview, tp.x + offsetX - (PREVIEW_WIDTH/2), tp.y + offsetY - (PREVIEW_HEIGHT/2))
            }
            game.batch.end()
        }

    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        // ignore if its not left mouse button or first touch pointer
        if (button != Input.Buttons.LEFT || pointer > 0) return false;

        // ignore if outside bounds
        val touchPos = Vector3(screenX.toFloat(), screenY.toFloat(), 0f)
        game.camera.unproject(touchPos)

        updateShapeBounds()

        if (!shapeBounds.contains(touchPos.x, touchPos.y)) {
           return false
        }

        // calculate offsets from card box
        setOffsets(screenX, screenY)
        dragging = true;
        return true;
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (button != Input.Buttons.LEFT || pointer > 0) return false;

        // send to board if on a cell, otherwise snap back
        val cell = getCurrentCell()
        if(cell != null) {
            // TODO pick unit logic
            board.placeUnit(MiniPekka(game, cell.x, cell.y))
            // remove from screen
            notify(this)
        } else {
            tp.set(startX, startY, 0F)
        }

        dragging = false;
        return true;
    }


    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        if (!dragging) return false;
        // Convert screen coordinates to world coordinates
        val touchPos = Vector3(screenX.toFloat(), screenY.toFloat(), 0f)
        game.camera.unproject(touchPos)

        val maxHeight = board.boardBounds.y + board.boardBounds.height - offsetY
        // Update the shape's position based on the touch point and stored offset
        tp.set(touchPos.x - offsetX, min(touchPos.y - offsetY, maxHeight), 0F)
        updateShapeBounds() // Update bounds after moving

        return true
    }


    override fun touchCancelled(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = false
    override fun mouseMoved(screenX: Int, screenY: Int): Boolean = false
    override fun scrolled(amountX: Float, amountY: Float): Boolean = false
    override fun keyDown(keycode: Int): Boolean = false
    override fun keyUp(keycode: Int): Boolean = false
    override fun keyTyped(character: Char): Boolean = false
}
