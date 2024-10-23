package me.rochblondiaux.supermarioworld.entity.factory;

import com.badlogic.gdx.physics.box2d.Body;
import com.redsponge.ldtkgdx.LDTKEntity;

import me.rochblondiaux.supermarioworld.entity.Entity;
import me.rochblondiaux.supermarioworld.level.Level;

@FunctionalInterface
public interface EntityFactory<T extends Entity> {

    T make(Level level, Body body, LDTKEntity ldtkEntity);

}