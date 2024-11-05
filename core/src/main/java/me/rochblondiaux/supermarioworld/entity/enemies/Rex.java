package me.rochblondiaux.supermarioworld.entity.enemies;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.physics.box2d.Body;

import me.rochblondiaux.supermarioworld.entity.EntityType;
import me.rochblondiaux.supermarioworld.entity.living.Player;
import me.rochblondiaux.supermarioworld.level.Level;
import me.rochblondiaux.supermarioworld.model.Direction;
import me.rochblondiaux.supermarioworld.model.FacingDirection;

public class Rex extends Enemy {

    private boolean squished = false;
    private FacingDirection walkingDirection = FacingDirection.RIGHT;

    public Rex(Level level, Body body, RectangleMapObject source) {
        super(level, EntityType.REX, body, source, 2, "sprites/enemies/rex.txt");
    }


    @Override
    public void update(float delta) {
        super.update(delta);

        // Check if the Rex needs to turn around
        if (!this.canMoveTo(this.body.getPosition().x, this.body.getPosition().y))
            this.walkingDirection = this.walkingDirection.opposite();

        if (this.walkingDirection.equals(FacingDirection.LEFT)) {
            velocity.x = -1;
        } else {
            velocity.x = 1;
        }


        this.updatePosition();
    }

    public void onCollision(Player player, Direction direction) {
        // Player is above the Rex
        if (direction == Direction.UP) {
            if (squished) {
                this.die();
                return;
            }

            this.animations.setCurrentAnimation("flat");
            this.squished = true;
            return;
        }

        // Player is on the side of the Rex
        player.damage(1);
    }
}
