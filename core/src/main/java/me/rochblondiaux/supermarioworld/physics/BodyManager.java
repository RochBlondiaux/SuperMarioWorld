package me.rochblondiaux.supermarioworld.physics;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class BodyManager {

    private final Map<Class<?>, BodyFactory> factories = new HashMap<>();

    public void register(Class<?> clazz, BodyFactory factory) {
        factories.put(clazz, factory);
    }

    public BodyFactory getFactory(Class<?> clazz) {
        BodyFactory factory = factories.get(clazz);
        if (factory == null)
            factory = factories.entrySet()
                .stream()
                .filter(entry -> entry.getKey().isAssignableFrom(clazz))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
        return factory;
    }

    public Body make(Class<?> type, World world, Rectangle rectangle) {
        BodyFactory factory = getFactory(type);
        if (factory == null)
            throw new IllegalArgumentException("No factory found for " + type.getSimpleName());

        return factory.make(world, rectangle);
    }
}
