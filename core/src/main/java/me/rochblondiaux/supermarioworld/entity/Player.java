package me.rochblondiaux.supermarioworld.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import me.rochblondiaux.supermarioworld.graphics.animation.AnimationController;

public class Player extends LivingEntity {

    private int jumps;

    public Player(World world, Body body) {
        super(world, body, 20);
        this.animations = new AnimationController<>(
            this,
            new TextureAtlas(Gdx.files.internal("sprites/player.txt")),
            true
        );
        this.speed = 4;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        this.inputPoll();
    }

    private void inputPoll() {
        velocity.x = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            velocity.x = 1;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            velocity.x = -1;

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && jumps < 2) {
            float force = body.getMass() * 6;
            body.setLinearVelocity(body.getLinearVelocity().x, 0);
            body.applyLinearImpulse(new Vector2(0, force), body.getWorldCenter(), true);
            jumps++;
        }

        if (body.getLinearVelocity().y == 0) {
            jumps = 0;
        }

        body.setLinearVelocity(velocity.x * speed, body.getLinearVelocity().y < 12 ? body.getLinearVelocity().y : 12);
    }
}
