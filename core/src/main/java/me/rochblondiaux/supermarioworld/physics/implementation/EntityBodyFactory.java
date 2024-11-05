package me.rochblondiaux.supermarioworld.physics.implementation;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;

import lombok.RequiredArgsConstructor;
import me.rochblondiaux.supermarioworld.physics.BodyFactory;
import me.rochblondiaux.supermarioworld.utils.Constants;

@RequiredArgsConstructor
public class EntityBodyFactory implements BodyFactory<RectangleMapObject> {

    private final BodyDef.BodyType type;

    public EntityBodyFactory() {
        this(BodyDef.BodyType.KinematicBody);
    }

    @Override
    public Body make(World world, RectangleMapObject source) {
        Rectangle rectangle = source.getRectangle();
        float x = rectangle.getX() + rectangle.getWidth() / 2;
        float y = rectangle.getY() + rectangle.getHeight() / 2;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = this.type;
        bodyDef.position.set(x / Constants.PPM, y / Constants.PPM);
        bodyDef.fixedRotation = true;
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(rectangle.getWidth() / 2 / Constants.PPM, rectangle.getHeight() / 2 / Constants.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = Constants.BIT_ENTITIES;
        fixtureDef.shape = shape;
        fixtureDef.friction = 0;
        body.createFixture(fixtureDef);

        shape.dispose();

        return body;
    }
}
