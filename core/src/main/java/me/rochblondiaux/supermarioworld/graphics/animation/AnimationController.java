package me.rochblondiaux.supermarioworld.graphics.animation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Predicate;

import me.rochblondiaux.supermarioworld.entity.Entity;
import me.rochblondiaux.supermarioworld.model.Renderable;
import me.rochblondiaux.supermarioworld.model.Updatable;

public class AnimationController<T extends Entity> implements Updatable, Disposable, Renderable {

    private final T entity;
    private final TextureAtlas atlas;
    private final boolean canMove;
    private final float frameDuration;
    private final Map<String, Animation<TextureRegion>> animations = new HashMap<>();
    private final Map<String, Predicate<T>> rules = new HashMap<>();
    private String currentAnimation;
    private float stateTime;

    public AnimationController(T entity, TextureAtlas atlas, boolean canMove) {
        this(entity, atlas, canMove, 1f / 5f);
    }

    public AnimationController(T entity, TextureAtlas atlas, boolean canMove, float frameDuration) {
        this.entity = entity;
        this.atlas = atlas;
        this.canMove = canMove;
        this.frameDuration = frameDuration;

        this.load();
        this.setCurrentAnimation("idle");
    }

    private void load() {
        for (TextureAtlas.AtlasRegion region : atlas.getRegions()) {
            if (this.animations.containsKey(region.name))
                continue;

            Animation<TextureRegion> animation = new Animation<>(this.frameDuration, atlas.findRegions(region.name), Animation.PlayMode.LOOP);
            this.animations.put(region.name, animation);
        }
    }

    @Override
    public void update(float deltaTime) {
        // Update state time
        stateTime += deltaTime;

        if (!canMove)
            return;

        Body body = entity.body();

        // Rules
        for (Map.Entry<String, Predicate<T>> entry : rules.entrySet()) {
            if (entry.getValue().evaluate(entity)) {
                setCurrentAnimation(entry.getKey());
                break;
            }
        }

        // Animation update
        if (body.getLinearVelocity().y > 0.01) {
            if (this.animations.containsKey("jump"))
                setCurrentAnimation("jump");
        } else if (body.getLinearVelocity().y < -0.01) {
            if (this.animations.containsKey("fall"))
                setCurrentAnimation("fall");
        } else if (body.getLinearVelocity().x != 0) {
            setCurrentAnimation("walk");
        } else {
            setCurrentAnimation("idle");
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (currentAnimation == null)
            return;

        Animation<TextureRegion> currentAnimation = animations.get(this.currentAnimation);
        if (currentAnimation == null)
            return;

        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);

        // Flip texture
        if (entity.velocity().x < 0) {
            if (currentFrame.isFlipX()) {
                currentFrame.flip(true, false);
            }
        } else if (entity.velocity().x > 0) {
            if (!currentFrame.isFlipX()) {
                currentFrame.flip(true, false);
            }
        }

        // Draw texture
        batch.draw(currentFrame, entity.position().x - entity.size().x / 2, entity.position().y - entity.size().y / 2, entity.size().x, entity.size().y);
    }

    public void removeAnimation(String name) {
        animations.remove(name);
    }

    public void addAnimation(String name, Animation<TextureRegion> animation) {
        animations.put(name, animation);
    }

    public void setCurrentAnimation(String currentAnimation) {
        if (!animations.containsKey(currentAnimation))
            throw new IllegalArgumentException("Animation not found: " + currentAnimation);
        else if (Objects.equals(this.currentAnimation, currentAnimation))
            return;

        this.currentAnimation = currentAnimation;
    }

    public Animation<TextureRegion> getAnimation(String name) {
        return animations.get(name);
    }

    public void addRule(String name, Predicate<T> rule) {
        rules.put(name, rule);
    }

    public void removeRule(String name) {
        rules.remove(name);
    }

    @Override
    public void dispose() {
        this.atlas.dispose();
        this.animations.clear();
    }

}
