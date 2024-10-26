package me.rochblondiaux.supermarioworld.physics.implementation;

import com.badlogic.gdx.physics.box2d.BodyDef;

public class LivingEntityBodyFactory extends EntityBodyFactory {

    public LivingEntityBodyFactory() {
        super(BodyDef.BodyType.DynamicBody);
    }
}
