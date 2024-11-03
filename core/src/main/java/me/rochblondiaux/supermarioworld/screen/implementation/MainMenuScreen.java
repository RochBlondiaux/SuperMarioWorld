package me.rochblondiaux.supermarioworld.screen.implementation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import me.rochblondiaux.supermarioworld.SuperMarioWorld;
import me.rochblondiaux.supermarioworld.screen.ScreenType;

public class MainMenuScreen implements Screen {

    private final SuperMarioWorld game;

    // Stage
    private final Stage stage;

    // Textures
    private final TextureAtlas atlas;
    private final TextureRegion copyright;
    private final TextureRegion title;

    private float lastBlink;
    private int selected = 0;
    private Image selector;
    private Table table;

    // Sounds
    private final Music music;

    public MainMenuScreen(SuperMarioWorld game) {
        this.game = game;

        // Stage
        this.stage = new Stage(new ScreenViewport(), this.game.batch());

        // Skin
        Skin skin = new Skin(Gdx.files.internal("skin.json"));

        // Inputs
        Gdx.input.setInputProcessor(this.stage);

        // Textures
        this.atlas = new TextureAtlas(Gdx.files.internal("ui/main/main.txt"));
        this.title = atlas.findRegion("title");
        this.copyright = atlas.findRegion("copyright");

        // Music
        this.music = Gdx.audio.newMusic(Gdx.files.internal("music/title_screen.wav"));

        // Setup stage
        table = new Table();
        table.setFillParent(true);
        table.setSkin(skin);

        // Frame
        table.background(new Image(new Texture(Gdx.files.internal("ui/main/frame.png"))).getDrawable());

        // Title
        table.add(new Image(this.title))
            .center()
            .size(this.title.getRegionWidth() * 3, this.title.getRegionHeight() * 3)
            .padBottom(100)
            .colspan(2);

        // Selection (1 Player, 2 Players, Options, Quit)
        table.row().padTop(25);
        table.add("1  PLAYER  GAME").center().colspan(2);
        table.row().padTop(10);
        table.add("2  PLAYER  GAME").center().colspan(2);

        // Copyright
        table.row().padTop(100);
        table.add(new Image(this.copyright))
            .size(this.copyright.getRegionWidth() * 2, this.copyright.getRegionHeight() * 2)
            .colspan(2);

        this.stage.addActor(table);

        // Selector
        this.selector = new Image(new Texture(Gdx.files.internal("ui/main/selector.png")));
        selector.setSize(16, 16);
        this.stage.addActor(selector);
    }

    @Override
    public void show() {
        this.music.setLooping(true);
        this.music.play();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1);

        this.stage.act(delta);

        // selector
        Actor actor = table.getChild(1 + selected);
        selector.setPosition(actor.getX() - 50, actor.getY());

        // Make the selector blink
        lastBlink += delta;
        if (lastBlink > 0.25f) {
            lastBlink = 0;
            selector.setVisible(!selector.isVisible());
        }

        this.stage.draw();

        // Selector input
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            select(true);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            select(false);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            // TODO: Implement the game saves system
            this.game.screens().setCurrentScreen(ScreenType.GAME);
        }
    }

    private void select(boolean up) {
        this.selected = this.selected + (up ? 1 : -1);
        if (this.selected < 0)
            this.selected = 1;
        else if (this.selected > 1)
            this.selected = 0;
    }

    @Override
    public void resize(int width, int height) {
        this.stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        this.music.stop();
    }

    @Override
    public void dispose() {
        this.atlas.dispose();
        this.stage.dispose();
        this.music.dispose();
    }
}
