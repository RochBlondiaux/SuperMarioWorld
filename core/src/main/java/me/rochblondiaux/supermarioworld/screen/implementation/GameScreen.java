package me.rochblondiaux.supermarioworld.screen.implementation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import lombok.Getter;
import me.rochblondiaux.supermarioworld.SuperMarioWorld;
import me.rochblondiaux.supermarioworld.entity.living.Player;
import me.rochblondiaux.supermarioworld.graphics.Hud;
import me.rochblondiaux.supermarioworld.level.Level;
import me.rochblondiaux.supermarioworld.screen.ScreenType;
import me.rochblondiaux.supermarioworld.utils.Constants;

@Getter
public class GameScreen implements Screen {

    private final SuperMarioWorld game;
    private final Box2DDebugRenderer debugRenderer;
    private final Hud hud;
    private Level level;
    private boolean paused;

    // Death
    private volatile boolean dead;
    private Actor fadeActor;
    private ShapeRenderer shapeRenderer;

    // Assets
    private final Sound gameOverSound;
    private final Sound lostLifeSound;
    private final Music music;

    public GameScreen(SuperMarioWorld game) {
        this.game = game;
        this.debugRenderer = new Box2DDebugRenderer();
        this.hud = new Hud(this);

        World world = new World(
            new Vector2(0, Constants.GRAVITY),
            true
        );
        this.level = new Level(
            game,
            world,
            "maps/level-0.tmx"
        );

        // Death
        this.fadeActor = new Actor();
        this.shapeRenderer = new ShapeRenderer(8);

        // Assets
        this.gameOverSound = Gdx.audio.newSound(Gdx.files.internal("sounds/game_over.wav"));
        this.lostLifeSound = Gdx.audio.newSound(Gdx.files.internal("sounds/lost_a_life.wav"));
        this.music = Gdx.audio.newMusic(Gdx.files.internal("music/overworld.wav"));
        this.music.setVolume(0.5f);
    }

    public void setDead() {
        Player player = this.level.player();
        player.lives(player.lives() - 1);

        // Game over
        if (player.lives() <= 0) {
            this.gameOverSound.play();

            this.game.screens().setCurrentScreen(ScreenType.GAME_OVER);
            // TODO: handle that better
            return;
        }

        this.dead = true;

        // Sound
        this.music.stop();
        this.lostLifeSound.play();

        fadeActor.clearActions();
        fadeActor.setColor(Color.CLEAR);
        fadeActor.addAction(Actions.sequence(
            Actions.color(Color.BLACK, 2f, Interpolation.fade),
            Actions.run(() -> {
                player.body().setTransform(level.checkpoint(), 0);
                player.body().setLinearVelocity(0, 0);

                this.music.play();

                this.dead = false;
            }),
            Actions.color(Color.CLEAR, 2f, Interpolation.fade)
        ));
    }

    @Override
    public void show() {
        this.hud.loadAssets();

        this.music.setLooping(true);
        this.music.play();
    }

    @Override
    public void render(float delta) {
        if (dead) {
            fadeActor.act(delta);
            float alpha = fadeActor.getColor().a;
            if (alpha != 0) {
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(0, 0, 0, alpha);
                shapeRenderer.rect(-1, -1, 2, 2);
                shapeRenderer.end();
            }
            return;
        }

        SpriteBatch batch = this.game.batch();

        // Start rendering
        batch.begin();

        // Hud
        this.hud.render(batch);

        // End rendering
        batch.end();

        // Update the world
        if (!this.paused)
            this.level.update(delta);

        // Render the world
        this.level.render(null);

        // Debug
        debugRenderer.render(this.level.world(), this.game.camera().combined.scl(Constants.PPM));

        // Inputs
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            this.game.screens().setCurrentScreen(ScreenType.PAUSE);
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        this.paused = true;
    }

    @Override
    public void resume() {
        this.paused = false;
    }

    @Override
    public void hide() {
        this.music.stop();
    }

    @Override
    public void dispose() {
        this.level.dispose();
        this.debugRenderer.dispose();
        this.hud.dispose();

        this.gameOverSound.dispose();
        this.lostLifeSound.dispose();
        this.music.dispose();
    }
}
