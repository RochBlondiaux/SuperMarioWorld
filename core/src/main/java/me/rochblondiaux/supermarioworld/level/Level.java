package me.rochblondiaux.supermarioworld.level;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.redsponge.ldtkgdx.*;

import lombok.Getter;
import me.rochblondiaux.supermarioworld.SuperMarioWorld;
import me.rochblondiaux.supermarioworld.entity.Entity;
import me.rochblondiaux.supermarioworld.entity.Player;
import me.rochblondiaux.supermarioworld.model.Renderable;
import me.rochblondiaux.supermarioworld.model.Updatable;
import me.rochblondiaux.supermarioworld.utils.BodyHelper;
import me.rochblondiaux.supermarioworld.utils.Constants;

@Getter
public class Level implements Renderable, Updatable, Disposable {

    private final SuperMarioWorld game;
    private final World world;
    private final AssetManager assets;
    private final LDTKLevel map;
    private final Set<Entity> entities = new HashSet<>();
    private final SpriteBatch batch;

    private Player player;

    public Level(SuperMarioWorld game, World world, FileHandle map) {
        this.game = game;
        this.world = world;
        this.assets = new AssetManager();
        this.batch = new SpriteBatch();
        this.map = this.loadMap(map);
    }

    private LDTKLevel loadMap(FileHandle fileHandle) {
        LDTKTypes types = new LDTKTypes();
        if (fileHandle == null)
            throw new IllegalArgumentException("FileHandle cannot be null : " + fileHandle.path());
        LDTKMap map = new LDTKMap(types, fileHandle);
        LDTKLevel level = map.getLevel("Level_0");

        // Load entities
        // TODO: automatic loading & parsing of entities
        for (LDTKEntityLayer entityLayer : level.getEntityLayers()) {
            Array<LDTKEntity> players = entityLayer.getEntitiesOfType("Player");
            for (LDTKEntity player : players) {
                Body body = BodyHelper.createBody(
                    player.getX() + 8,
                    player.getY() + 8,
                    16,
                    16,
                    false,
                    this.world
                );
                this.player = new Player(world, body);
                this.entities.add(this.player);
            }
        }

        // Collisions
        LDTKLayer groundLayer = level.getLayerByName("Ground");
        if (groundLayer instanceof LDTKTileLayer) {
            LDTKTileLayer tileLayer = (LDTKTileLayer) groundLayer;
            Vector2 tileSize = new Vector2(tileLayer.getGridSize(), tileLayer.getGridSize());
            for (LDTKTile region : tileLayer.getRegions()) {
                BodyHelper.createStaticBody(world, tileSize, region);
            }
        }

        return level;
    }

    @Override
    public void render(SpriteBatch batch) {
        this.batch.begin();

        // Level
        this.map.render(this.batch);

        // Entities
        this.entities.forEach(entity -> entity.render(this.batch));

        this.batch.end();
    }

    @Override
    public void update(float delta) {
        // World
        this.world.step(1 / 60f, 6, 2);

        // Camera
        this.updateCamera();

        // Batch
        this.batch.setProjectionMatrix(this.game.camera().combined);

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
        this.map.dispose();
        this.assets.dispose();
        this.entities.forEach(Entity::dispose);
        this.entities.clear();
        this.world.dispose();
    }
}
