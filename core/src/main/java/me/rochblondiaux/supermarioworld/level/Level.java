package me.rochblondiaux.supermarioworld.level;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import lombok.Getter;
import me.rochblondiaux.supermarioworld.SuperMarioWorld;
import me.rochblondiaux.supermarioworld.entity.Entity;
import me.rochblondiaux.supermarioworld.entity.Player;
import me.rochblondiaux.supermarioworld.model.Renderable;
import me.rochblondiaux.supermarioworld.model.Updatable;
import me.rochblondiaux.supermarioworld.utils.Constants;

@Getter
public class Level implements Renderable, Updatable, Disposable {

    private final SuperMarioWorld game;
    private final World world;
    private final AssetManager assets;
    private Player player;
    private final Set<Entity> entities = new HashSet<>();

    public Level(SuperMarioWorld game, World world) {
        this.game = game;
        this.world = world;
        this.assets = new AssetManager();
    }

    @Override
    public void render(SpriteBatch batch) {
        // Entities
        this.entities.forEach(entity -> entity.render(batch));
    }

    @Override
    public void update(float delta) {
        // World
        this.world.step(1 / 60f, 6, 2);

        // Camera
        this.updateCamera();

        // Entities
        this.entities.forEach(entity -> entity.update(delta));
    }

    private void updateCamera() {
        Camera camera = this.game.camera();
        Vector3 position = camera.position;
        position.x = Math.round(player.body().getPosition().x * Constants.PPM * 10) / 10f;
        position.y = Math.round(player.body().getPosition().y * Constants.PPM * 10) / 10f;

        camera.position.set(position);
        camera.update();
    }

    @Override
    public void dispose() {
        this.assets.dispose();
        this.entities.forEach(Entity::dispose);
        this.entities.clear();
        this.world.dispose();
    }
}
