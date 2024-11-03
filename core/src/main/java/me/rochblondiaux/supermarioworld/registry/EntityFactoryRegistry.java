package me.rochblondiaux.supermarioworld.registry;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.rochblondiaux.supermarioworld.entity.EntityType;
import me.rochblondiaux.supermarioworld.entity.collectable.Apple;
import me.rochblondiaux.supermarioworld.entity.collectable.Coin;
import me.rochblondiaux.supermarioworld.entity.factory.EntityFactory;
import me.rochblondiaux.supermarioworld.entity.interactable.QuestionBlock;
import me.rochblondiaux.supermarioworld.entity.interactable.Trampoline;
import me.rochblondiaux.supermarioworld.entity.living.Player;

@RequiredArgsConstructor
@Getter
public enum EntityFactoryRegistry {
    PLAYER("player", EntityType.PLAYER, Player.FACTORY, CollisionRegistry.DYNAMIC_ENTITIES),
    COIN("coin", EntityType.COIN, Coin.FACTORY, CollisionRegistry.STATIC_ENTITIES),
    QUESTION_BLOCK("qblock", EntityType.QUESTION_BLOCK, QuestionBlock.FACTORY, CollisionRegistry.STATIC_ENTITIES),
    APPLE("apple", EntityType.APPLE, Apple.FACTORY, CollisionRegistry.STATIC_ENTITIES),
    TRAMPOLINE("trampoline", EntityType.TRAMPOLINE, Trampoline::new, CollisionRegistry.STATIC_ENTITIES);

    private final String id;
    private final EntityType type;
    private final EntityFactory factory;
    private final CollisionRegistry collision;

    public static EntityFactoryRegistry findById(String id) {
        for (EntityFactoryRegistry registry : values()) {
            if (id.equalsIgnoreCase(registry.id)) {
                return registry;
            }
        }
        return null;
    }

    public static EntityFactoryRegistry findByType(EntityType type) {
        for (EntityFactoryRegistry registry : values()) {
            if (type.equals(registry.type)) {
                return registry;
            }
        }
        return null;
    }
}
