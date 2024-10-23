package me.rochblondiaux.supermarioworld.physics.implementation;

import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import me.rochblondiaux.supermarioworld.physics.BodyFactory;
import me.rochblondiaux.supermarioworld.utils.Constants;

public class TileBodyFactory implements BodyFactory {

    @Override
    public Body make(World world, Rectangle rectangle) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bodyDef);
        Shape shape = createPolygonShape(rectangle);
        body.createFixture(shape, 1000);
        shape.dispose();

        return body;
    }

    private static Shape createPolygonShape(Rectangle rectangle) {
        float[] vertices = new float[]{
            rectangle.x, rectangle.y,                             // Bottom-left corner
            rectangle.x + rectangle.width, rectangle.y,           // Bottom-right corner
            rectangle.x + rectangle.width, rectangle.y + rectangle.height, // Top-right corner
            rectangle.x, rectangle.y + rectangle.height      // Top-left corner
        };
        PolygonMapObject object = new PolygonMapObject(vertices);
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
