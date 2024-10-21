package me.rochblondiaux.supermarioworld.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import lombok.Getter;
import lombok.Setter;
import me.rochblondiaux.supermarioworld.graphics.animation.AnimationController;

@Getter
@Setter
public class LivingEntity extends Entity {

    // Animation
    private final AnimationController<?> animations;

    // State
    private int health;
    private int maxHealth;

    public LivingEntity(World world, Body body, int maxHealth, AnimationController<?> controller) {
        super(world, body);
        this.health = maxHealth;
        this.maxHealth = maxHealth;
        this.animations = controller;
    }

    public void damage(int damage) {
        this.health -= damage;
        if (this.health <= 0)
            this.die();
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        // Animations
        this.animations.render(batch);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        // Update animations
        this.animations.update(delta);
    }

    protected void die() {
        this.alive = false;
    }

    @Override
    public void dispose() {
        super.dispose();

        // Dispose animations
        this.animations.dispose();
    }
}
