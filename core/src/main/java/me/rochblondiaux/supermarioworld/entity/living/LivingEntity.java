package me.rochblondiaux.supermarioworld.entity.living;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import lombok.Getter;
import lombok.Setter;
import me.rochblondiaux.supermarioworld.entity.Entity;
import me.rochblondiaux.supermarioworld.entity.EntityType;
import me.rochblondiaux.supermarioworld.graphics.animation.AnimationController;

@Getter
@Setter
public class LivingEntity extends Entity {

    // Animation
    protected AnimationController<?> animations;

    // State
    private int health;
    private int maxHealth;

    public LivingEntity(World world, EntityType type, Body body, int maxHealth) {
        super(world, type, body);
        this.health = maxHealth;
        this.maxHealth = maxHealth;
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
