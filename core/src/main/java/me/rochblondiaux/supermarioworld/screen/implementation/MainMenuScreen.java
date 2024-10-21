package me.rochblondiaux.supermarioworld.screen.implementation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

import me.rochblondiaux.supermarioworld.SuperMarioWorld;
import me.rochblondiaux.supermarioworld.screen.ScreenType;

public class MainMenuScreen implements Screen {

    private final SuperMarioWorld game;

    // Textures
    private final TextureAtlas atlas;
    private final TextureRegion background;
    private final TextureRegion frame;
    private final TextureRegion copyright;
    private final TextureRegion title;

    public MainMenuScreen(SuperMarioWorld game) {
        this.game = game;

        // Textures
        this.atlas = new TextureAtlas(Gdx.files.internal("ui/main/main.txt"));
        this.background = atlas.findRegion("background");
        this.frame = atlas.findRegion("frame");
        this.title = atlas.findRegion("title");
        this.copyright = atlas.findRegion("copyright");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // Switch to game screen
        // TODO: make a selection menu instead
        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            this.game.screens().setCurrentScreen(ScreenType.GAME);
            return;
        }

        SpriteBatch batch = this.game.batch();

        // Clear screen
        ScreenUtils.clear(0, 0, 0.2f, 1);

        // Update camera
        this.game.camera().update();
        batch.setProjectionMatrix(this.game.camera().combined);
        batch.begin();

        float width = this.game.camera().viewportWidth;
        float height = this.game.camera().viewportHeight;

        // Background
        batch.draw(background, 0, 0, width, height);

        // Frame
        batch.draw(frame, 0, 0, width, height);

        // Title
        float titleWidth = title.getRegionWidth() * 1.5f;
        float titleHeight = title.getRegionHeight() * 1.5f;
        batch.draw(title, width / 2f - titleWidth / 2f, height / 1.40f - titleHeight / 2f, titleWidth, titleHeight);

        // Copyright
        float copyrightWidth = copyright.getRegionWidth() * 1.5f;
        float copyrightHeight = copyright.getRegionHeight() * 1.5f;
        batch.draw(copyright, width / 2f - copyrightWidth / 2f, copyrightHeight * 6, copyrightWidth, copyrightHeight);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        this.atlas.dispose();
    }
}
