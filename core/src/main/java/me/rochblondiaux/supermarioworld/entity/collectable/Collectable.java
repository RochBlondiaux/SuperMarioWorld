package me.rochblondiaux.supermarioworld.entity.collectable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.physics.box2d.Body;

import me.rochblondiaux.supermarioworld.entity.Entity;
import me.rochblondiaux.supermarioworld.entity.EntityType;
import me.rochblondiaux.supermarioworld.entity.living.Player;
import me.rochblondiaux.supermarioworld.graphics.animation.AnimationController;
import me.rochblondiaux.supermarioworld.level.Level;

public abstract class Collectable extends Entity {

    private final AnimationController<Collectable> animations;

    public Collectable(Level level, EntityType type, Body body, RectangleMapObject source, String atlasPath) {
        super(level, type, body, source);
        this.animations = new AnimationController<>(this, new TextureAtlas(Gdx.files.internal(atlasPath)), false);
    }

    public abstract void collect(Player player);

    @Override
    public void update(float delta) {
        super.update(delta);
        this.animations.update(delta);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        this.animations.render(batch);
    }

    @Override
    public void dispose() {
        super.dispose();
        this.animations.dispose();
    }
}
