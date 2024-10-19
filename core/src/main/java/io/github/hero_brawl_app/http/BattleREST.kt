package io.github.hero_brawl_app.http

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Net.HttpMethods
import com.badlogic.gdx.Net.HttpResponse
import com.badlogic.gdx.Net.HttpResponseListener
import com.badlogic.gdx.net.HttpRequestBuilder
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonWriter.OutputType
import io.github.hero_brawl_app.HeroBrawl
import io.github.hero_brawl_app.unit.Unit

class BattleREST(val game: HeroBrawl) {
    private val requestBuilder = HttpRequestBuilder()
    private val json = Json()

    init {
        json.setOutputType(OutputType.json)
    }

    fun addPiece(unit: Unit) {
        // TODO when deployed, set https and remove the network_security_config
        val url = "http://${game.host}:5000/battle/add-piece"

        Gdx.app.log("#INFO", "Sending to $url")

        data class Body(val id:String, val type: String, val x: Float, val y: Float)

        val httpRequest = requestBuilder
            .newRequest()
            .method(HttpMethods.POST)
            .url(url)
            .header("Content-Type", "application/json")
            .header("PlayerID", game.playerId)
            .content(json.toJson(Body(unit.id, unit.name, unit.positionX, unit.positionY)))
            .build()

        Gdx.net.sendHttpRequest(httpRequest, object: HttpResponseListener {
            override fun cancelled() {
                Gdx.app.error("#ERROR", "Cancelled")
            }
            override fun failed(t: Throwable?) {
                Gdx.app.error("#ERROR", "Received ${t?.message}")
            }
            override fun handleHttpResponse(httpResponse: HttpResponse) {
                Gdx.app.log("#INFO", "Received ${httpResponse.status.statusCode}")
            }
        })
    }
}
