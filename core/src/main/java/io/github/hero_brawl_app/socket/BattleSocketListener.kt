package io.github.hero_brawl_app.socket

import com.badlogic.gdx.Gdx
import com.github.czyzby.websocket.WebSocket
import com.github.czyzby.websocket.WebSocketListener
import com.badlogic.gdx.utils.Json
import io.github.hero_brawl_app.model.Model

class BattleSocketListener(private val model: Model, private val json: Json, private val onConnectionReady: () -> Unit): WebSocketListener {

    override fun onOpen(webSocket: WebSocket?): Boolean {
        Gdx.app.log("#INFO", "socket Opened")
        // send the client ID when the socket is opened
        onConnectionReady()
        return true
    }

    override fun onClose(webSocket: WebSocket?, closeCode: Int, reason: String?): Boolean {
        Gdx.app.log("#INFO", "socket Closed")
        return true
    }

    override fun onMessage(webSocket: WebSocket?, packet: String?): Boolean {
        Gdx.app.log("#INFO", "New State")
        Gdx.app.log("#INFO", json.prettyPrint(packet))

        Gdx.app.log("#INFO", "Old State")
        Gdx.app.log("#INFO", "${model.friendlyUnits[0].positionY}")

        // TODO revert
        // val test = "{\"players\": {\"19bffa52-8768-4eb2-825a-489482c3afee\": {\"id\": \"19bffa52-8768-4eb2-825a-489482c3afee\",\"pieces\": [] },\"3f46042b-8507-43ae-888d-4f87090e8496\": {\"id\": \"3f46042b-8507-43ae-888d-4f87090e8496\",\"pieces\": [{\"id\": \"3e8f5833-8b3f-4a49-97b7-d3df8113aa7c\",\"x\": 142.20001,\"y\": 974 }] } } }"
        // val newStateTest = json.fromJson(GameStateUpdate::class.java, test)
        // Gdx.app.log("#INFO", "WORKS!")
        // Gdx.app.log("#INFO", "${newStateTest.players?.map { p -> p.key }}")

        val newState = json.fromJson(GameStateUpdate::class.java, packet)
        //Gdx.app.log("#INFO", "deserialized ${newState.pieces?.joinToString { p -> "Piece ${p.id}: ${p.x} ${p.y}" }}")
        Gdx.app.log("#INFO", "HERE: ${newState}")
        model.update(newState)

        return true
    }

    override fun onMessage(webSocket: WebSocket?, packet: ByteArray?): Boolean {
        Gdx.app.log("#INFO", "Received $packet")
        return true
    }

    override fun onError(webSocket: WebSocket?, error: Throwable?): Boolean {
        Gdx.app.error("#ERROR", "Received ${error?.message}")
        return true
    }
}
