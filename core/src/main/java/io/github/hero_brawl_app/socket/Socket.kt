package io.github.hero_brawl_app.socket

import com.badlogic.gdx.utils.Json
import com.github.czyzby.websocket.WebSocket
import com.github.czyzby.websocket.WebSockets
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.JsonWriter.OutputType
import io.github.hero_brawl_app.HeroBrawl
import io.github.hero_brawl_app.model.Model
import io.github.hero_brawl_app.unit.Unit
import java.util.UUID

class Socket(game: HeroBrawl, model: Model) {
    private val socket: WebSocket = WebSockets.newSocket("ws://${game.host}:5000/battle")
    private val json = Json()

    data class Body(val id:String, val type: String, val x: Float, val y: Float)

    data class Event(val type: String, val data: Body)

    init {
        json.setOutputType(OutputType.json)
        // onConnectionReady
        fun sendPlayerId() = socket.send(game.playerId)
        // settings
        socket.setSendGracefully(true)
        socket.addListener(BattleSocketListener(model, json, ::sendPlayerId))
        socket.connect()
    }

    fun placeUnit(unit: Unit) {
       socket.send(json.toJson(Event("unit_placed", Body(unit.id, unit.name, unit.positionX, unit.positionY))))
    }
}
