package io.github.hero_brawl_app.socket

// basically just a copy of what's on the python side

class Piece() {
    val id: String? = null
    val x: Float = 0F
    val y: Float = 0F
}

class Player() {
    val id: String? = null
    val pieces: ArrayList<Piece>? = arrayListOf()
}

class GameStateUpdate() {
    val players: HashMap<String, Player>? = null
}
