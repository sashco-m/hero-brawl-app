package io.github.hero_brawl_app.chat_input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.TextInputListener
import com.github.czyzby.websocket.WebSocket
import com.github.czyzby.websocket.WebSocketListener
import com.github.czyzby.websocket.WebSockets


class ChatInput : TextInputListener {
    private var socket: WebSocket = WebSockets.newSocket("ws://10.0.2.2:5000/echo")
    private var listener: WebSocketListener = SocketLogger()

    init {
        socket.setSendGracefully(true)
        socket.addListener(listener)
        socket.connect()
    }

    override fun input(text: String) {
        Gdx.app.log("#INFO", "Inside Listener")
        Gdx.app.log("#INFO", text)
        socket.send(text)
        socket.close()
    }

    override fun canceled() {
    }
}


