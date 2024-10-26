package me.rochblondiaux.supermarioworld.screen.implementation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

import lombok.RequiredArgsConstructor;
import me.rochblondiaux.supermarioworld.SuperMarioWorld;
import me.rochblondiaux.supermarioworld.screen.ScreenType;

@RequiredArgsConstructor
public class PauseMenuScreen implements Screen {

    private final SuperMarioWorld game;

    // Texture
    private final TextureAtlas atlas;
    private final TextureRegion title;

    public PauseMenuScreen(SuperMarioWorld game) {
        this.game = game;

        // Load textures
        this.atlas = new TextureAtlas(Gdx.files.internal("ui/main/main.txt"));
        this.title = atlas.findRegion("title");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        SpriteBatch batch = this.game.batch();
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        // Clear screen
        ScreenUtils.clear(0.207f, 0.207f, 0.207f, 1);

        this.game.camera().update();
        batch.setProjectionMatrix(this.game.camera().combined);
        batch.begin();

        // Title
        float titleWidth = title.getRegionWidth() * 1.5f;
        float titleHeight = title.getRegionHeight() * 1.5f;
        batch.draw(title, width / 2f - titleWidth / 2f, height / 1.40f - titleHeight / 2f, titleWidth, titleHeight);

        // Buttons
        //  game.font.draw(game.batch, "Press ESCAPE to unpause", width / 2f - 100, height / 2f);

        batch.end();

        // Unpause
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            this.game.screens().setCurrentScreen(ScreenType.GAME);
        }
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
