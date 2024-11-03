package me.rochblondiaux.supermarioworld.entity.collectable;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.physics.box2d.Body;

import me.rochblondiaux.supermarioworld.entity.EntityType;
import me.rochblondiaux.supermarioworld.entity.factory.EntityFactory;
import me.rochblondiaux.supermarioworld.entity.living.Player;
import me.rochblondiaux.supermarioworld.level.Level;

public class Apple extends Collectable {

    public static final EntityFactory<Apple> FACTORY = Apple::new;

    public Apple(Level level, Body body, RectangleMapObject source) {
        super(level, EntityType.APPLE, body, source, "sprites/collectable/apple.txt");
    }

    @Override
    public void collect(Player player) {

    }
}
