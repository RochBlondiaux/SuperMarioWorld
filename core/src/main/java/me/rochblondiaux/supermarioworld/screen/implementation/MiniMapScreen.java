package me.rochblondiaux.supermarioworld.screen.implementation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;

import lombok.RequiredArgsConstructor;
import me.rochblondiaux.supermarioworld.SuperMarioWorld;
import me.rochblondiaux.supermarioworld.configuration.LevelInformation;

@RequiredArgsConstructor
public class MiniMapScreen implements Screen {

    private final SuperMarioWorld game;
    private LevelInformation level;

    // Dimensions
    private float width;
    private float height;

    // Assets
    private Texture background;
    private TextureAtlas playerAtlas;
    private TextureRegion playerIdle;

    // Map
    private TiledMap map;
    private TiledMapRenderer mapRenderer;


    @Override
    public void show() {
        // Get current level
        this.level = this.game.levels().currentLevel();

        // Map
        this.map = new TmxMapLoader().load(this.level.minimap());
        this.mapRenderer = new OrthogonalTiledMapRenderer(this.map);

        // Dimensions
        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();

        // Load assets
        this.background = new Texture(Gdx.files.internal("minimap/background.png"));
        this.playerAtlas = new TextureAtlas(Gdx.files.internal("sprites/player.txt"));
        this.playerIdle = this.playerAtlas.findRegion("walk");
    }

    @Override
    public void render(float delta) {
        SpriteBatch batch = this.game.batch();
        batch.begin();

        // Background
        batch.draw(this.background, 0, 0, width, height);

        // Player
        float playerWidth = playerIdle.getRegionWidth() * 2;
        float playerHeight = playerIdle.getRegionHeight() * 2;
        batch.draw(this.playerIdle, 100 + playerWidth / 2, height - 85 - playerHeight / 2, playerWidth, playerHeight);

        // Draw level name
        this.game.font().setColor(0, 0, 0, 1);
        this.game.font().draw(batch, this.level.name().toUpperCase(), 400, height - 80);
        this.game.font().setColor(1, 1, 1, 1);

        float scaleX = 5f;
        float scaleY = 2.77f;

        float centerX = width - 1191;
        float centerY = height / 2f - 224;
        Matrix4 mapMatrix = this.game.camera()
            .combined
            .cpy()
            .translate(centerX, centerY, 0)
            .scale(scaleX, scaleY, 1);

        this.mapRenderer.setView(mapMatrix, 0, 0, width, height);
        this.mapRenderer.render();

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
        this.background.dispose();
        this.playerAtlas.dispose();
        this.map.dispose();
    }
}
