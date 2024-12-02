package app;

import com.almasb.fxgl.app.GameSettings;

public class SimulatorConfig {
    public static void configureSettings(GameSettings settings) {
        settings.setWidth(950);
        settings.setHeight(545);
        settings.setTitle("Restaurant-Gourmet");
        settings.setVersion("1.1");
        settings.setMainMenuEnabled(false);
        settings.setGameMenuEnabled(false);
    }
}