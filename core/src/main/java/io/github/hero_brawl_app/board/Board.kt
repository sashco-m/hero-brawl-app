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
    private val sock: Socket,
    private val model: Model
) {
    val BOARD_X_OFFSET = 20F
    val BOARD_Y_OFFSET = 200F
    val HEIGHT = 16 //cells
    val WIDTH = 18 //cells
    val CELL_WIDTH = 24.44F //px
    val CELL_HEIGHT = 18F //px


    val boardBounds = Rectangle(0F, BOARD_Y_OFFSET, 480F, HEIGHT * CELL_HEIGHT)

    private val activeCells = (0..<HEIGHT).map { i -> buildRow(i) }.toTypedArray()

    private fun buildRow(index:Int): kotlin.Array<Boolean> {
        val row = BooleanArray(WIDTH) { true }

        val kingTowerCells = (7..10).toList()
        val princessTowerCells = listOf(2,3,4) + listOf(13, 14, 15)

        val invalidSpots = mapOf(
            // 1st half of bridge
            (HEIGHT - 1) to (listOf(0,1) + (5..<13).toList() + listOf(16, 17)),
            // first row
            (HEIGHT - 2) to listOf(0, WIDTH-1),
            // princess tower
            7 to princessTowerCells,
            6 to princessTowerCells,
            5 to princessTowerCells,
            // king tower
            4 to kingTowerCells,
            3 to kingTowerCells,
            2 to kingTowerCells,
            1 to kingTowerCells,
            // last row
            0 to ((0..5).toList() + (12..<WIDTH).toList())
        )
        invalidSpots[index]?.forEach { i -> row[i] = false }
        return row.toTypedArray()
    }

    private val renderedCells = listOf(*activeCells, *(activeCells.reversed().toTypedArray()))
        .mapIndexed { i, row -> row.mapIndexed { j, cell ->
        if(!cell){
            null
        } else {
            Rectangle(
                BOARD_X_OFFSET + j * CELL_WIDTH,
                BOARD_Y_OFFSET + i * CELL_HEIGHT,
                CELL_WIDTH,
                CELL_HEIGHT
            )
        }
    }}.flatten().filterNotNull()

    val placeableCells = renderedCells.slice((0..221))
    val bridgeCells = renderedCells.slice((placeableCells.size..placeableCells.size+11))

    fun placeUnit(unit: Unit) {
        // may be redundant if the state is updated fast enough. if so, remove this call and have
        // the caller use REST directly
        model.placeUnit(unit)
         //sock.send(UnitPlaced(unit.name, unit.positionX, unit.positionY))
        sock.placeUnit(unit)
         //rest.addPiece(unit)
    }

    fun render() {
        // render board
        game.shape.setProjectionMatrix(game.camera.combined);

        game.shape.begin(ShapeType.Line);
        // each players + bridge
        for ((index, cell) in renderedCells.withIndex()){
            if(index < placeableCells.size){
                game.shape.color = Color.GREEN
            } else if(index < placeableCells.size + bridgeCells.size){
                game.shape.color = Color.YELLOW
            } else {
                game.shape.color = Color.RED
            }
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
