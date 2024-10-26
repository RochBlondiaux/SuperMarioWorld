package me.rochblondiaux.supermarioworld.entity.interactable;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Timer;

import me.rochblondiaux.supermarioworld.entity.EntityType;
import me.rochblondiaux.supermarioworld.entity.factory.EntityFactory;
import me.rochblondiaux.supermarioworld.entity.living.Player;
import me.rochblondiaux.supermarioworld.level.Level;

public class QuestionBlock extends Interactable {

    public static final EntityFactory<QuestionBlock> FACTORY = QuestionBlock::new;

    private boolean empty;

    public QuestionBlock(Level level, Body body, RectangleMapObject source) {
        super(level, EntityType.QUESTION_BLOCK, body, source, "sprites/interactable/question-block.txt");
    }

    @Override
    public void interact(Player player) {
        if (player.position().y > this.position.y || this.empty)
            return;

        // Update state
        this.empty = true;
        this.animations.setCurrentAnimation("empty");

        // Spawn loot
        // TODO: randomize the loot
        //EntityType entityType = RandomUtils.getRandomIn(EntityType.COIN, EntityType.MUSHROOM, EntityType.YOSHI_EGG);
        EntityType entityType = EntityType.COIN;

        Rectangle rectangle = new Rectangle(
            this.position.x - 8,
            this.position.y,
            16,
            16
        );

        this.level.addEntity(entityType, rectangle)
            .thenAccept(entity -> {
                entity.body().setLinearVelocity(0, 1);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        entity.body().setLinearVelocity(0, 0);
                    }
                }, 0.25f);
            });
    }
}
