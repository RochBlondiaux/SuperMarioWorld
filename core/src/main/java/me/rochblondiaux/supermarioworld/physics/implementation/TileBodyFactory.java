package me.rochblondiaux.supermarioworld.physics.implementation;

import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import me.rochblondiaux.supermarioworld.physics.BodyFactory;
import me.rochblondiaux.supermarioworld.utils.Constants;

public class TileBodyFactory implements BodyFactory<PolygonMapObject> {

    @Override
    public Body make(World world, PolygonMapObject source) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bodyDef);
        Shape shape = createPolygonShape(source);
        body.createFixture(shape, 1000);
        shape.dispose();

        return body;
    }

    private Shape createPolygonShape(PolygonMapObject object) {
        float[] vertices = object.getPolygon().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; i++) {
            Vector2 current = new Vector2(vertices[i * 2] / Constants.PPM, vertices[i * 2 + 1] / Constants.PPM);
            worldVertices[i] = current;
        }
        PolygonShape shape = new PolygonShape();
        shape.set(worldVertices);
        return shape;
    }
}
