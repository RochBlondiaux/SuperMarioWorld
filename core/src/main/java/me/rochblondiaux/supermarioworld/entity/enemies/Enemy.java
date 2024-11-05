package me.rochblondiaux.supermarioworld.entity.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import me.rochblondiaux.supermarioworld.entity.EntityType;
import me.rochblondiaux.supermarioworld.entity.living.LivingEntity;
import me.rochblondiaux.supermarioworld.graphics.animation.AnimationController;
import me.rochblondiaux.supermarioworld.level.Level;
import me.rochblondiaux.supermarioworld.model.FacingDirection;
import me.rochblondiaux.supermarioworld.utils.Constants;
import me.rochblondiaux.supermarioworld.utils.PhysicsUtils;

public abstract class Enemy extends LivingEntity {

    protected Vector2 boundingBox;

    public Enemy(Level level, EntityType type, Body body, RectangleMapObject source, int maxHealth, String atlasPath) {
        super(level, type, body, source, maxHealth);
        this.animations = new AnimationController<>(this, new TextureAtlas(Gdx.files.internal(atlasPath)), false);
        this.boundingBox = new Vector2(source.getRectangle().width / 2 / Constants.PPM, source.getRectangle().height / 2 / Constants.PPM);
    }

    public boolean canMoveTo(float x, float y) {
        boolean currentLocationSolid = PhysicsUtils.isSolid(this, this.level.world(), x, y, this.boundingBox.x, this.boundingBox.y);
        boolean groundSolid = PhysicsUtils.isSolid(this, this.level.world(), x, y - 1f, this.boundingBox.x, this.boundingBox.y);
        boolean directionSolid = PhysicsUtils.isSolid(this, this.level.world(), x + (direction.equals(FacingDirection.LEFT) ? -this.boundingBox.x : this.boundingBox.x), y, this.boundingBox.x, this.boundingBox.y);

        return !currentLocationSolid
               && groundSolid
               && !directionSolid;
    }


}
