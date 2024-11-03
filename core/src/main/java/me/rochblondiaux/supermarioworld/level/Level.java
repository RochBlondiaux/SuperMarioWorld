package me.rochblondiaux.supermarioworld.level;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArraySet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import lombok.Getter;
import lombok.Setter;
import me.rochblondiaux.supermarioworld.SuperMarioWorld;
import me.rochblondiaux.supermarioworld.entity.Entity;
import me.rochblondiaux.supermarioworld.entity.EntityType;
import me.rochblondiaux.supermarioworld.entity.QueuedEntity;
import me.rochblondiaux.supermarioworld.entity.living.Player;
import me.rochblondiaux.supermarioworld.model.Renderable;
import me.rochblondiaux.supermarioworld.model.Updatable;
import me.rochblondiaux.supermarioworld.physics.listener.CollectableListener;
import me.rochblondiaux.supermarioworld.physics.listener.InteractableListener;
import me.rochblondiaux.supermarioworld.registry.CollisionRegistry;
import me.rochblondiaux.supermarioworld.registry.EntityFactoryRegistry;
import me.rochblondiaux.supermarioworld.utils.Constants;
import net.dermetfan.gdx.physics.box2d.ContactMultiplexer;

@Getter
public class Level implements Renderable, Updatable, Disposable {

    private final SuperMarioWorld game;
    private final World world;
    private final AssetManager assets;
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final SpriteBatch batch;
    private final ContactMultiplexer contactMultiplexer;

    private final Set<Entity> entities = new CopyOnWriteArraySet<>();
    private final Queue<QueuedEntity> queuedEntities = new ArrayDeque<>();

    private Vector2 spawnPoint;
    @Setter
    private Vector2 checkpoint;
    private Player player;

    private Texture background;

    public Level(SuperMarioWorld game, World world, String mapPath) {
        this.game = game;
        this.world = world;
        this.assets = new AssetManager();
        this.batch = new SpriteBatch();

        // Map
        this.map = new TmxMapLoader().load(mapPath);
        this.mapRenderer = new OrthogonalTiledMapRenderer(this.map);

        // Contact
        this.contactMultiplexer = new ContactMultiplexer();
        this.contactMultiplexer.add(new CollectableListener(this));
        this.contactMultiplexer.add(new InteractableListener(this));
        this.world.setContactListener(this.contactMultiplexer);

        // Assets
        // TODO: use the assets manager
        this.background = new Texture(Gdx.files.internal("backgrounds/1.png"));
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        this.loadEntities();
        this.loadCollisions();
    }

    private void loadCollisions() {
        MapObjects objects = this.map.getLayers().get("collisions").getObjects();
        for (MapObject object : objects) {
            if (!(object instanceof PolygonMapObject))
                continue;
            PolygonMapObject polygon = (PolygonMapObject) object;
            CollisionRegistry.TILES.factory().make(world, polygon);
        }
    }

    private void loadEntities() {
        MapObjects objects = this.map.getLayers().get("entities").getObjects();
        for (MapObject object : objects) {
            if (!(object instanceof RectangleMapObject))
                continue;
            String entityId = object.getName();
            EntityFactoryRegistry definition = EntityFactoryRegistry.findById(entityId.split("_")[0]);
            if (definition == null)
                continue;

            RectangleMapObject rectangle = (RectangleMapObject) object;
            Body body = definition.collision().factory().make(world, rectangle);
            Entity entity = definition.factory().make(this, body, rectangle);
            this.entities.add(entity);
            if (entity instanceof Player) {
                this.player = (Player) entity;
                this.spawnPoint = new Vector2(entity.body().getPosition());
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        // Background
        SpriteBatch gameBatch = this.game.batch();
        gameBatch.begin();

        // Background
        gameBatch.draw(this.background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        gameBatch.end();


        this.batch.begin();

        // Map
        this.mapRenderer.render();

        // Entities
        this.entities.forEach(entity -> entity.render(this.batch));

        this.batch.end();
    }

    @Override
    public void update(float delta) {
        // World
        this.world.step(1 / 60f, 6, 2);

        // Dead entities
        if (!this.world.isLocked())
            this.entities.stream()
                .filter(entity -> !entity.alive())
                .forEach(entity -> {
                    entity.dispose();
                    this.world.destroyBody(entity.body());
                    this.entities.remove(entity);
                });

        // Queued entities
        if (!this.world.isLocked()) {
            while (!this.queuedEntities.isEmpty()) {
                QueuedEntity queuedEntity = this.queuedEntities.poll();
                EntityFactoryRegistry factory = EntityFactoryRegistry.findByType(queuedEntity.type());
                if (factory == null)
                    continue;
                RectangleMapObject rectangle = new RectangleMapObject();
                rectangle.getRectangle().set(queuedEntity.rectangle());
                Body body = factory.collision().factory().make(this.world, rectangle);
                Entity entity = factory.factory().make(this, body, rectangle);
                this.entities.add(entity);
                queuedEntity.future().complete(entity);
            }
        }

        // Camera
        this.updateCamera();

        // Batch
        this.batch.setProjectionMatrix(this.game.camera().combined);

        // Map
        this.mapRenderer.setView(this.game.camera());

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
        this.mapRenderer.dispose();
        this.batch.dispose();
    }

    public Vector2 checkpoint() {
        return this.checkpoint == null ? this.spawnPoint : this.checkpoint;
    }

    public <T extends Entity> CompletableFuture<T> addEntity(EntityType type, Rectangle rectangle) {
        CompletableFuture<Entity> future = new CompletableFuture<>();
        this.queuedEntities.add(new QueuedEntity(type, rectangle, future));
        return future.thenApply(entity -> (T) entity);
    }

    public Optional<Entity> findById(UUID uniqueId) {
        return this.entities.stream()
            .filter(entity -> entity.uniqueId().equals(uniqueId))
            .findFirst();
    }
}
