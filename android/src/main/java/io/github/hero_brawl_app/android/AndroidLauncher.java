package io.github.hero_brawl_app.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.github.czyzby.websocket.CommonWebSockets;

import io.github.hero_brawl_app.HeroBrawl;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
        configuration.useImmersiveMode = true; // Recommended, but not required.
        configuration.useAccelerometer = false;
        configuration.useCompass = false;
        // websocket
        CommonWebSockets.initiate();
        // game
        initialize(new HeroBrawl(), configuration);
    }
}
