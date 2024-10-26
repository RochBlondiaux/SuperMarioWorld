package me.rochblondiaux.supermarioworld.physics;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

@FunctionalInterface
public interface BodyFactory<T extends MapObject> {

    Body make(World world, T source);

}
