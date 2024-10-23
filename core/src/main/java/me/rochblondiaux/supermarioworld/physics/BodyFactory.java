package me.rochblondiaux.supermarioworld.physics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

@FunctionalInterface
public interface BodyFactory {

    Body make(World world, Rectangle rectangle);

}
