package me.rochblondiaux.supermarioworld.level;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lombok.Getter;
import me.rochblondiaux.supermarioworld.configuration.LevelInformation;

@Getter
public class LevelManager implements Disposable {

    private final Set<LevelInformation> informations = new HashSet<>();
    private final Gson gson = new Gson();
    private final Preferences preferences;
    private LevelInformation currentLevel;
    private Vector2 currentPosition;
    private String currentMap;

    public LevelManager() {
        this.preferences = Gdx.app.getPreferences("levels");
    }

    public void loadConfiguration() {
        FileHandle configurationFile = Gdx.files.internal("configuration/levels.json");

        // Load configuration
        Set<LevelInformation> levels = gson.fromJson(configurationFile.readString(), new TypeToken<Set<LevelInformation>>() {
        }.getType());
        this.informations.addAll(levels);

        // Get current level from preferences
        int currentLevelId = this.preferences.getInteger("currentLevel", -1);
        if (currentLevelId == -1) {
            currentLevelId = this.informations.iterator().next().id();
            this.preferences.putInteger("currentLevel", currentLevelId);
        }
        this.currentLevel = this.findFromId(currentLevelId).orElseThrow(() -> new IllegalStateException("Current level not found"));

        // Get current map from preferences
        this.currentMap = this.preferences.getString("currentMap", null);
        if (this.currentMap == null) {
            this.currentMap = this.currentLevel.maps().keySet().iterator().next();
            this.preferences.putString("currentMap", this.currentMap);
        } else
            this.currentMap = this.informations.stream()
                .map(level -> level.maps().get(this.currentMap))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Current map not found"));

        // Get current position in level
        float x = this.preferences.getFloat("currentPositionX", -1);
        float y = this.preferences.getFloat("currentPositionY", -1);
        if (x == -1 || y == -1)
            this.currentPosition = new Vector2(0, 0);
        else
            this.currentPosition = new Vector2(x, y);

        System.out.println("Loaded configuration");
        System.out.println("Current level: " + this.currentLevel);
        System.out.println("Current map: " + this.currentMap);
        System.out.println("Current position: " + this.currentPosition);

        // Save configuration
        this.preferences.flush();
    }

    public void setCurrentLevel(LevelInformation level) {
        this.currentLevel = level;
        this.preferences.putInteger("currentLevel", level.id());
        this.preferences.flush();
    }

    public void setCurrentPosition(Vector2 position) {
        this.currentPosition = position;
        this.preferences.putFloat("currentPositionX", position.x);
        this.preferences.putFloat("currentPositionY", position.y);
        this.preferences.flush();
    }

    public void setCurrentMap(String map) {
        this.currentMap = map;
        this.preferences.putString("currentMap", map);
        this.preferences.flush();
    }

    public Optional<LevelInformation> findFromId(int id) {
        return this.informations.stream()
            .filter(level -> level.id() == id)
            .findFirst();
    }

    @Override
    public void dispose() {
        this.preferences.flush();
    }
}
