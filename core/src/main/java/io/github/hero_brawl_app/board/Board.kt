package io.github.hero_brawl_app.board

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Json
import com.github.czyzby.websocket.WebSocket
import io.github.hero_brawl_app.HeroBrawl
import io.github.hero_brawl_app.http.BattleREST
import io.github.hero_brawl_app.model.Model
import io.github.hero_brawl_app.socket.Socket
import io.github.hero_brawl_app.unit.Unit


class Board(
    private var game: HeroBrawl,
    private val rest: BattleREST,
    private val model: Model
) {
    val BOARD_X_OFFSET = 20F
    val BOARD_Y_OFFSET = 200F
    val ENEMY_BOARD_Y_OFFSET = 500F
    val HEIGHT = 15 //cells
    val WIDTH = 18 //cells
    val CELL_WIDTH = 24.44F //px
    val CELL_HEIGHT = 18F //px

    private val board = (0..<HEIGHT).map { i -> buildRow(i) }

    val boardBounds = Rectangle(0F, BOARD_Y_OFFSET, 480F, HEIGHT * CELL_HEIGHT)

    val cellBounds = board.mapIndexed { i, row ->  row.mapIndexed { j, cell ->
        if(!cell){
            null
        }else {
            Rectangle(
                BOARD_X_OFFSET + j * CELL_WIDTH,
                BOARD_Y_OFFSET + i * CELL_HEIGHT,
                CELL_WIDTH,
                CELL_HEIGHT
            )
        }
    } }.flatten().filterNotNull()
    val enemyCellBounds = board.reversed().mapIndexed { i, row ->  row.mapIndexed { j, cell ->
        if(!cell){
            null
        }else {
            Rectangle(
                BOARD_X_OFFSET + j * CELL_WIDTH,
                ENEMY_BOARD_Y_OFFSET + i * CELL_HEIGHT,
                CELL_WIDTH,
                CELL_HEIGHT
            )
        }
    } }.flatten().filterNotNull()

    private fun buildRow(index:Int): BooleanArray {
        val row = BooleanArray(WIDTH) { true }
        val invalidSpots = mapOf(
            // first row
            (HEIGHT - 1) to listOf(0, WIDTH-1),
            // last row
            0 to ((0..5).toList() + (12..<WIDTH).toList())
        )
        invalidSpots[index]?.forEach { i -> row[i] = false }
        return row
    }

    fun placeUnit(unit: Unit) {
        // may be redundant if the state is updated fast enough. if so, remove this call and have
        // the caller use REST directly
        model.placeUnit(unit)
        //sock.send(UnitPlaced(unit.name, unit.positionX, unit.positionY))
        rest.addPiece(unit)
    }

    fun render() {
        // render board
        game.shape.setProjectionMatrix(game.camera.combined);
        game.shape.begin(ShapeType.Line);
        // ours
        game.shape.color = Color.GREEN
        for(cell in cellBounds){
            game.shape.rect(cell.x,cell.y,cell.width,cell.height)
        }
        // theirs
        game.shape.color = Color.RED
        for(cell in enemyCellBounds){
            game.shape.rect(cell.x,cell.y,cell.width,cell.height)
        }
        game.shape.end();

        // render units
        game.batch.setProjectionMatrix(game.camera.combined)
        game.batch.begin()
        val allUnits = model.friendlyUnits.plus(model.enemyUnits)
        for (unit in allUnits){
            unit.render()
        }
        game.batch.end()
    }

}
