package me.rochblondiaux.supermarioworld.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

import lombok.RequiredArgsConstructor;
import me.rochblondiaux.supermarioworld.entity.living.Player;
import me.rochblondiaux.supermarioworld.model.Renderable;
import me.rochblondiaux.supermarioworld.screen.implementation.GameScreen;

@RequiredArgsConstructor
public class Hud implements Renderable, Disposable {

    private final GameScreen gameScreen;

    // Dimensions
    private float width;
    private float height;

    // Textures
    private Texture coins;
    private TextureAtlas smallNumbers;
    private Texture marioName;

    public void loadAssets() {
        coins = new Texture(Gdx.files.internal("ui/hud/coins.png"));
        smallNumbers = new TextureAtlas(Gdx.files.internal("ui/hud/fonts/small-numbers.txt"));
        marioName = new Texture(Gdx.files.internal("ui/hud/player/mario.png"));

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
    }

    @Override
    public void render(SpriteBatch batch) {
        Player player = this.gameScreen.level().player();
        if (player == null)
            return;

        // Coins
        renderCoins(player, batch);

        // Player name
        renderPlayerName(batch);
    }


    private void renderPlayerName(SpriteBatch batch) {
        float marioNameWidth = marioName.getWidth() * 2.25f;
        float marioNameHeight = marioName.getHeight() * 2.25f;

        batch.draw(marioName, 15, this.height - marioNameHeight - 5, marioNameWidth, marioNameHeight);
    }

    private void renderCoins(Player player, SpriteBatch batch) {
        // Coins
        float coinsWidth = this.coins.getWidth() * 2.25f;
        float coinsHeight = this.coins.getHeight() * 2.5f;
        float coinX = this.width - coinsWidth * 4;
        float coinY = this.height - coinsHeight - 5;
        batch.draw(coins, coinX, coinY, coinsWidth, coinsHeight);

        // Draw number of coins
        this.drawNumber(batch, player.coins(), coinX + coinsWidth + 5, coinY, 2.25f);
    }

    private void drawNumber(SpriteBatch batch, int number, float x, float y, float scale) {
        String numberString = String.valueOf(number);
        for (int i = 0; i < numberString.length(); i++) {
            TextureRegion numberRegion = smallNumbers.findRegion(String.valueOf(numberString.charAt(i)));

            double width = numberRegion.getRegionWidth() * scale;
            double height = numberRegion.getRegionHeight() * scale;

            batch.draw(numberRegion, (float) (x + i * width), y, (float) width, (float) height);
        }
    }

    @Override
    public void dispose() {
        coins.dispose();
        smallNumbers.dispose();
        marioName.dispose();
    }
}
