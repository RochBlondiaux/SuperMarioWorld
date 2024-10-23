package me.rochblondiaux.supermarioworld.screen.implementation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;

import me.rochblondiaux.supermarioworld.SuperMarioWorld;
import me.rochblondiaux.supermarioworld.level.Level;
import me.rochblondiaux.supermarioworld.utils.Constants;

public class GameScreen implements Screen {

    private final SuperMarioWorld game;
    private final Level level;
    private final Box2DDebugRenderer debugRenderer;

    public GameScreen(SuperMarioWorld game) {
        this.game = game;
        this.debugRenderer = new Box2DDebugRenderer();

        World world = new World(
            new Vector2(0, Constants.GRAVITY),
            true
        );
        this.level = new Level(
            game,
            world,
            Gdx.files.internal("maps/level-1.ldtk")
        );
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // Clear the screen
        ScreenUtils.clear(0, 0, 0, 1);

        SpriteBatch batch = this.game.batch();

        // Start rendering
        batch.begin();

        // End rendering
        batch.end();


        // Update the world
        this.level.update(delta);

        // Render the world
        this.level.render(null);

        // Debug
        debugRenderer.render(this.level.world(), this.game.camera().combined.scl(Constants.PPM));
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
        this.level.dispose();
    }
}
