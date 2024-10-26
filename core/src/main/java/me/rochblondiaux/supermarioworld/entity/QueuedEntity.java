package me.rochblondiaux.supermarioworld.entity;

import java.util.concurrent.CompletableFuture;

import com.badlogic.gdx.math.Rectangle;

import lombok.Data;

@Data
public class QueuedEntity {

    private final EntityType type;
    private final Rectangle rectangle;
    private final CompletableFuture<Entity> future;

}
