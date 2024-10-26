package me.rochblondiaux.supermarioworld.registry;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.rochblondiaux.supermarioworld.physics.BodyFactory;
import me.rochblondiaux.supermarioworld.physics.implementation.EntityBodyFactory;
import me.rochblondiaux.supermarioworld.physics.implementation.LivingEntityBodyFactory;
import me.rochblondiaux.supermarioworld.physics.implementation.TileBodyFactory;

@RequiredArgsConstructor
@Getter
public enum CollisionRegistry {
    TILES(new TileBodyFactory()),
    STATIC_ENTITIES(new EntityBodyFactory()),
    DYNAMIC_ENTITIES(new LivingEntityBodyFactory());

    private final BodyFactory factory;

}
