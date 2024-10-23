package me.rochblondiaux.supermarioworld.physics.implementation;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;

import me.rochblondiaux.supermarioworld.physics.BodyFactory;
import me.rochblondiaux.supermarioworld.utils.Constants;

public class LivingEntityBodyFactory implements BodyFactory {

    @Override
    public Body make(World world, Rectangle rectangle) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(rectangle.getX() / Constants.PPM, rectangle.getY() / Constants.PPM);
        bodyDef.fixedRotation = true;
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(rectangle.getWidth() / 2 / Constants.PPM, rectangle.getHeight() / 2 / Constants.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0;
        body.createFixture(fixtureDef);
        shape.dispose();

        return body;
    }

}
