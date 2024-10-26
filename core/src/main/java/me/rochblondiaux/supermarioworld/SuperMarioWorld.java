package me.rochblondiaux.supermarioworld;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import lombok.Getter;
import me.rochblondiaux.supermarioworld.screen.ScreenManager;
import me.rochblondiaux.supermarioworld.screen.ScreenType;
import me.rochblondiaux.supermarioworld.screen.implementation.GameScreen;
import me.rochblondiaux.supermarioworld.screen.implementation.MainMenuScreen;
import me.rochblondiaux.supermarioworld.screen.implementation.PauseMenuScreen;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
@Getter
public class SuperMarioWorld extends ApplicationAdapter {

    @Getter
    private static SuperMarioWorld instance;

    // Screen
    private ScreenManager screens;


    // Batch
    private SpriteBatch batch;

    // Camera
    private OrthographicCamera camera;

    @Override
    public void create() {
        instance = this;

        // Batch
        this.batch = new SpriteBatch();

        // Camera
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Screen
        this.screens = new ScreenManager();
        this.screens.register(ScreenType.MAIN, new MainMenuScreen(this));
        this.screens.register(ScreenType.GAME, new GameScreen(this));
        this.screens.register(ScreenType.PAUSE, new PauseMenuScreen(this));
        this.screens.setCurrentScreen(ScreenType.MAIN);
    }

    @Override
    public void resize(int width, int height) {
        this.screens.resize(width, height);
    }

    @Override
    public void pause() {
        this.screens.pause();
    }

    @Override
    public void resume() {
        this.screens.resume();
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        this.screens.render(delta);
    }

    @Override
    public void dispose() {
        this.screens.dispose();
        this.batch.dispose();

        instance = null;
    }
}
