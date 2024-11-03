package me.rochblondiaux.supermarioworld.entity.interactable;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Timer;

import me.rochblondiaux.supermarioworld.entity.EntityType;
import me.rochblondiaux.supermarioworld.entity.living.Player;
import me.rochblondiaux.supermarioworld.level.Level;
import me.rochblondiaux.supermarioworld.model.Direction;

public class Trampoline extends Interactable {

    private volatile boolean active = false;

    public Trampoline(Level level, Body body, RectangleMapObject source) {
        super(level, EntityType.TRAMPOLINE, body, source, "sprites/interactable/trampoline.txt");
    }

    @Override
    public void interact(Player player, Direction direction) {
        if (direction != Direction.UP)
            return;

        // Animation
        this.animations.setCurrentAnimation("active");
        this.active = true;

        // Apply a linear impulse to the player's body
        Body playerBody = player.body();
        playerBody.applyLinearImpulse(0, 10, playerBody.getWorldCenter().x, playerBody.getWorldCenter().y, true);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                // Reset the animation
                Trampoline.this.animations.setCurrentAnimation("idle");
                Trampoline.this.active = false;
            }
        }, 0.25f);
    }
}
