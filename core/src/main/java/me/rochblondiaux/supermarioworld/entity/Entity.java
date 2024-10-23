package me.rochblondiaux.supermarioworld.entity;

import java.util.UUID;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import lombok.Data;
import me.rochblondiaux.supermarioworld.model.FacingDirection;
import me.rochblondiaux.supermarioworld.model.Renderable;
import me.rochblondiaux.supermarioworld.model.Updatable;
import me.rochblondiaux.supermarioworld.utils.Constants;

@Data
public abstract class Entity implements Disposable, Renderable, Updatable {

    // Definition
    protected final UUID uniqueId;
    protected final World world;
    protected Body body;
    protected float speed = 1f;

    // Physics
    protected Vector2 position;
    protected Vector2 size;
    protected Vector2 velocity;

    // State
    protected boolean alive;
    protected boolean onGround;
    protected float airTicks;
    protected FacingDirection direction = FacingDirection.RIGHT;

    public Entity(World world, Body body) {
        this.uniqueId = UUID.randomUUID();
        this.world = world;
        this.body = body;
        this.create();
    }

    public void create() {
        this.alive = true;
        this.onGround = true;
        this.velocity = new Vector2();
        this.position = new Vector2(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
        this.size = new Vector2(body.getFixtureList().get(0).getShape().getRadius() * 2 * Constants.PPM, body.getFixtureList().get(0).getShape().getRadius() * 2 * Constants.PPM);
        this.body.setUserData(this.uniqueId);
    }

    public void size(Vector2 size) {
        this.size = size;
        this.body.getFixtureList().get(0).getShape().setRadius(size.x / 2 / Constants.PPM);
    }

    @Override
    public void render(SpriteBatch batch) {

    }

    @Override
    public void update(float delta) {
        // Update position
        this.position = new Vector2(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);

        // Update direction
        if (velocity.x > 0)
            direction = FacingDirection.RIGHT;
        if (velocity.x < 0)
            direction = FacingDirection.LEFT;

        // Ground
        onGround = body.getLinearVelocity().y == 0;

        // Air ticks
        if (!onGround)
            airTicks++;
        else
            airTicks = 0;
    }

    public void updatePosition() {
        // Update the body position
        float xVelocity = Math.min(Constants.MAX_X_SPEED, Math.abs(velocity.x));
        float yVelocity = Math.min(Constants.MAX_Y_SPEED, Math.abs(velocity.y));
        body.setLinearVelocity(xVelocity, yVelocity);
    }

    public boolean isFalling() {
        return body.getLinearVelocity().y < 0 && !onGround;
    }

    public boolean isJumping() {
        return body.getLinearVelocity().y > 0 && !onGround;
    }


    @Override
    public void dispose() {
    }
}
