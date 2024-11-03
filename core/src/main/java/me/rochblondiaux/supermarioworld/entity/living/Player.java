package me.rochblondiaux.supermarioworld.entity.living;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import lombok.Getter;
import lombok.Setter;
import me.rochblondiaux.supermarioworld.entity.EntityType;
import me.rochblondiaux.supermarioworld.entity.factory.EntityFactory;
import me.rochblondiaux.supermarioworld.graphics.animation.AnimationController;
import me.rochblondiaux.supermarioworld.level.Level;
import me.rochblondiaux.supermarioworld.screen.implementation.GameScreen;
import me.rochblondiaux.supermarioworld.utils.Constants;

@Getter
@Setter
public class Player extends LivingEntity {

    public static final EntityFactory<Player> FACTORY = Player::new;

    private int jumps;
    private int coins;
    private int lives = 5;

    // Assets
    private Sound coinSound;
    private Sound jumpSound;

    public Player(Level level, Body body, RectangleMapObject source) {
        super(level, EntityType.PLAYER, body, source, 20);
        this.animations = new AnimationController<>(
            this,
            new TextureAtlas(Gdx.files.internal("sprites/player.txt")),
            true
        );
        this.speed = 4;
        this.size = new Vector2(16, 16);

        // Sounds
        this.coinSound = Gdx.audio.newSound(Gdx.files.internal("sounds/coin.wav"));
        this.jumpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/jump.wav"));
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        // Health
        if (this.body.getPosition().y < 0) {
            if (this.level.game().screens().currentScreen() instanceof GameScreen) {
                GameScreen gameScreen = (GameScreen) this.level.game().screens().currentScreen();
                gameScreen.setDead();
                return;
            }
        }

        this.inputPoll();
    }

    private void inputPoll() {
        velocity.x = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            velocity.x = 1;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            velocity.x = -1;

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && jumps < 2) {
            float force = body.getMass() * Constants.JUMP_FORCE;
            body.setLinearVelocity(body.getLinearVelocity().x, 0);
            body.applyLinearImpulse(new Vector2(0, force), body.getWorldCenter(), true);
            jumps++;

            // Play jump sound
            jumpSound.play();
        }

        if (body.getLinearVelocity().y == 0) {
            jumps = 0;
        }

        body.setLinearVelocity(velocity.x * speed, body.getLinearVelocity().y < 12 ? body.getLinearVelocity().y : 12);
    }

    public void addCoin() {
        coins++;
        coinSound.play();
    }

    @Override
    public void dispose() {
        super.dispose();

        coinSound.dispose();
        jumpSound.dispose();
    }
}
