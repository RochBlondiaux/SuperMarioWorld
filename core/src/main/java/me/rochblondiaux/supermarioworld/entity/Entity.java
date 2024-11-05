package me.rochblondiaux.supermarioworld.entity;

import java.util.UUID;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import lombok.Data;
import me.rochblondiaux.supermarioworld.level.Level;
import me.rochblondiaux.supermarioworld.model.FacingDirection;
import me.rochblondiaux.supermarioworld.model.Renderable;
import me.rochblondiaux.supermarioworld.model.Updatable;
import me.rochblondiaux.supermarioworld.utils.Constants;

@Data
public abstract class Entity implements Disposable, Renderable, Updatable {

    // Definition
    protected final UUID uniqueId;
    protected final EntityType type;
    protected final Level level;
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

    public Entity(Level level, EntityType type, Body body, RectangleMapObject source) {
        this.uniqueId = UUID.randomUUID();
        this.type = type;
        this.level = level;
        this.world = level.world();
        this.body = body;
        this.body.setUserData(this.uniqueId);
        this.size = new Vector2(source.getRectangle().width, source.getRectangle().height);
        this.create();
    }

    public void create() {
        this.alive = true;
        this.onGround = true;
        this.velocity = new Vector2();
        this.position = new Vector2(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
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
        // TODO: cap velocity
        body.setLinearVelocity(velocity.x * speed, velocity.y);
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
