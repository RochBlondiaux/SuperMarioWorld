package me.rochblondiaux.supermarioworld.entity.collectable;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.physics.box2d.Body;

import me.rochblondiaux.supermarioworld.entity.EntityType;
import me.rochblondiaux.supermarioworld.entity.factory.EntityFactory;
import me.rochblondiaux.supermarioworld.entity.living.Player;
import me.rochblondiaux.supermarioworld.level.Level;

public class Coin extends Collectable {

    public static final EntityFactory<Coin> FACTORY = Coin::new;

    public Coin(Level level, Body body, RectangleMapObject source) {
        super(level, EntityType.COIN, body, source, "sprites/collectable/coin.txt");
    }

    @Override
    public void collect(Player player) {
        player.addCoin();
    }
}
