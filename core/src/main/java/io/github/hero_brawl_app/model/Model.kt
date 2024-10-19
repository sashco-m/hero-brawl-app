package io.github.hero_brawl_app.model

import com.badlogic.gdx.Gdx
import io.github.hero_brawl_app.HeroBrawl
import io.github.hero_brawl_app.socket.GameStateUpdate
import io.github.hero_brawl_app.unit.MiniPekka
import io.github.hero_brawl_app.unit.Unit

class Model(val game: HeroBrawl) {
    // TODO potentially replace with libgdx array
    // TODO add player information
    val enemyUnits = ArrayList<Unit>()
    val friendlyUnits = ArrayList<Unit>()

    // TODO figure out why enemy units aren't displayed right away

    // parses an update
    // need to translate the enemy's local perspective into the current user's perspective
    fun update(newState: GameStateUpdate) {
        for(player in newState.players?.entries!!){
            val isCurrentPlayer = player.key == game.playerId
            Gdx.app.log("#INFO", "Updating for player ${player.key}")

            for(piece in player.value.pieces!!){
                val allUnits = friendlyUnits.plus(enemyUnits)
                val unit = allUnits.find { u -> u.id == piece.id }
                Gdx.app.log("#INFO", "Updating piece ${piece.id} for player ${player.key}")

                // add new units (from enemy only for now, likely local too)
                if(unit == null) {
                    enemyUnits.add(
                        MiniPekka(game, piece.x, 970 - piece.y, piece.id)
                    )
                } else {

                    // update position of existing units
                    unit.positionX = piece.x
                    // TODO flesh out, use constants
                    unit.positionY = if (isCurrentPlayer) piece.y else 970 - piece.y
                }
            }
        }
    }

    // TODO we can probably remove this and rely on server updates
    fun placeUnit(unit: Unit){
        friendlyUnits.add(unit)
    }

}
