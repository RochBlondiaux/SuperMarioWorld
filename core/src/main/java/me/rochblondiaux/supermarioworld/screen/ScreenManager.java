package me.rochblondiaux.supermarioworld.screen;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Disposable;

public class ScreenManager implements Disposable {

    private final Map<ScreenType, Screen> screens = new HashMap<>();
    private ScreenType currentScreen;

    public void register(ScreenType screenType, Screen screen) {
        this.screens.put(screenType, screen);
    }

    public Screen get(ScreenType screenType) {
        return this.screens.get(screenType);
    }

    public Screen getCurrentScreen() {
        return this.screens.get(this.currentScreen);
    }

    public void render(float delta) {
        Screen screen = this.screens.get(this.currentScreen);
        if (screen == null)
            return;

        screen.render(delta);
    }

    public void resize(int width, int height) {
        Screen screen = this.screens.get(this.currentScreen);
        if (screen == null)
            return;

        screen.resize(width, height);
    }

    public void setCurrentScreen(ScreenType screenType) {
        Screen screen = this.screens.get(screenType);
        if (screen == null)
            return;
        this.currentScreen = screenType;
    }

    public void pause() {
        Screen screen = this.screens.get(this.currentScreen);
        if (screen == null)
            return;

        screen.pause();

        if (this.currentScreen == ScreenType.GAME)
            this.setCurrentScreen(ScreenType.PAUSE);
    }

    public void resume() {
        Screen screen = this.screens.get(this.currentScreen);
        if (screen == null)
            return;

        screen.resume();

        if (this.currentScreen == ScreenType.PAUSE)
            this.setCurrentScreen(ScreenType.GAME);
    }

    @Override
    public void dispose() {
        this.screens.values().forEach(Screen::dispose);
    }
}
