package me.rochblondiaux.supermarioworld.utils;

import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.redsponge.ldtkgdx.LDTKTile;

public class BodyHelper {

    public static Body createBody(float x, float y, float width, float height, boolean isStatic, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = isStatic ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / Constants.PPM, y / Constants.PPM);
        bodyDef.fixedRotation = true;
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / Constants.PPM, height / 2 / Constants.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0;
        body.createFixture(fixtureDef);
        shape.dispose();

        return body;
    }

    public static void createStaticBody(World world, Vector2 tileSize, LDTKTile tile) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bodyDef);
        Shape shape = createPolygonShape(tileSize, tile);
        body.createFixture(shape, 1000);
        shape.dispose();
    }

    private static float[] getVertices(Vector2 tileSize, LDTKTile tile) {
        // Create a float array of the vertices that match the tile dimensions
        return new float[]{
            tile.getX(), tile.getY(),                             // Bottom-left corner
            tile.getX() + tileSize.x, tile.getY(),           // Bottom-right corner
            tile.getX() + tileSize.x, tile.getY() + tileSize.y, // Top-right corner
            tile.getX(), tile.getY() + tileSize.y      // Top-left corner
        };
    }


    private static Shape createPolygonShape(Vector2 layer, LDTKTile tile) {
        PolygonMapObject object = new PolygonMapObject(getVertices(layer, tile));

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
