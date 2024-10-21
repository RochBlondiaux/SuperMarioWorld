package me.rochblondiaux.supermarioworld.entity;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import me.rochblondiaux.supermarioworld.graphics.animation.AnimationController;

public class Player extends LivingEntity {
    public Player(World world, Body body, int maxHealth, AnimationController<?> controller) {
        super(world, body, maxHealth, controller);
    }
}
