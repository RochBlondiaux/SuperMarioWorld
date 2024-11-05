package me.rochblondiaux.supermarioworld.utils;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

import lombok.experimental.UtilityClass;
import me.rochblondiaux.supermarioworld.entity.Entity;

@UtilityClass
public class PhysicsUtils {

    public static boolean isSolid(Entity entity, World world, float x, float y, float width, float height) {
        List<Fixture> fixtures = new ArrayList<>();
        world.QueryAABB(fixture -> {
            if (Objects.equals(fixture.getBody().getUserData(), entity.uniqueId()))
                return true;


            if (fixture.getFilterData().categoryBits == Constants.BIT_GROUND
                || fixture.getFilterData().categoryBits == Constants.BIT_ENTITIES)
                fixtures.add(fixture);
            return false;
        }, x, y, x + width, y + height);
        return !fixtures.isEmpty();
    }

    public static boolean isGround(World world, float x, float y, float width, float height) {
        List<Fixture> fixtures = new ArrayList<>();
        world.QueryAABB(fixture -> {
            if (fixture.getFilterData().categoryBits == Constants.BIT_GROUND) {
                fixtures.add(fixture);
            }
            return false;
        }, x, y, x + width, y + height);
        return !fixtures.isEmpty();
    }
}
