package fr.iamacat.iamacatblockgame.algorythme.chunkalgo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.iamacat.iamacatblockgame.gamescreen.TitleScreen;

public class WorldGeneratorScreen implements Screen {
    private SpriteBatch batch;
    private WorldGeneratorScene worldGeneratorScene;

    public WorldGeneratorScreen(SpriteBatch batch) {
        this.batch = batch;
        worldGeneratorScene = new WorldGeneratorScene();
        worldGeneratorScene.create(); // Initialize the scene
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        // Render the scene
        worldGeneratorScene.render();

        if (worldGeneratorScene.shouldExit()) {
            // Dispose the scene resources
            worldGeneratorScene.dispose();

            // Switch to the next screen (e.g., TitleScreen)
            ((Game) Gdx.app.getApplicationListener()).setScreen(new TitleScreen(batch));
        }
    }

    @Override
    public void resize(int width, int height) {
        // Update the scene's viewport
        worldGeneratorScene.resize(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        // Dispose the scene resources
        worldGeneratorScene.dispose();
    }
}