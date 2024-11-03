package me.rochblondiaux.supermarioworld;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import lombok.Getter;
import me.rochblondiaux.supermarioworld.level.LevelManager;
import me.rochblondiaux.supermarioworld.screen.ScreenManager;
import me.rochblondiaux.supermarioworld.screen.ScreenType;
import me.rochblondiaux.supermarioworld.screen.implementation.GameScreen;
import me.rochblondiaux.supermarioworld.screen.implementation.MainMenuScreen;
import me.rochblondiaux.supermarioworld.screen.implementation.MiniMapScreen;
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

    // Level
    private LevelManager levels;

    // Batch
    private SpriteBatch batch;

    // Camera
    private OrthographicCamera camera;

    // Assets
    private BitmapFont font;

    @Override
    public void create() {
        instance = this;

        // Batch
        this.batch = new SpriteBatch();

        // Camera
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Font
        this.font = new BitmapFont(Gdx.files.internal("font/default.fnt"), Gdx.files.internal("font/default.png"), false);

        // Screen
        this.screens = new ScreenManager();
        this.screens.register(ScreenType.MAIN, new MainMenuScreen(this));
        this.screens.register(ScreenType.GAME, new GameScreen(this));
        this.screens.register(ScreenType.MINI_MAP, new MiniMapScreen(this));
        this.screens.register(ScreenType.PAUSE, new PauseMenuScreen(this));
        this.screens.setCurrentScreen(ScreenType.MAIN);

        // Level
        this.levels = new LevelManager();
        this.levels.loadConfiguration();
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

        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        this.screens.render(delta);

        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();

        this.screens.dispose();
        this.batch.dispose();
        this.levels.dispose();
        this.font.dispose();

        instance = null;
    }
}
