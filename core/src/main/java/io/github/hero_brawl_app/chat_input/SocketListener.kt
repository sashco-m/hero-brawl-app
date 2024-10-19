package io.github.hero_brawl_app.chat_input

import com.badlogic.gdx.Gdx
import com.github.czyzby.websocket.WebSocket
import com.github.czyzby.websocket.WebSocketListener

class SocketLogger: WebSocketListener {
    override fun onOpen(webSocket: WebSocket?): Boolean {
        Gdx.app.log("#INFO", "socket Opened")
        return true
    }

    override fun onClose(webSocket: WebSocket?, closeCode: Int, reason: String?): Boolean {
        Gdx.app.log("#INFO", "socket Closed")
        return true
    }

    override fun onMessage(webSocket: WebSocket?, packet: String?): Boolean {
        Gdx.app.log("#INFO", "Received $packet")
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
