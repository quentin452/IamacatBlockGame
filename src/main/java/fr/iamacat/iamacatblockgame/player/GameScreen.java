package fr.iamacat.iamacatblockgame.player;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.iamacat.iamacatblockgame.algorythme.chunkalgo.Chunk;
import fr.iamacat.iamacatblockgame.algorythme.chunkalgo.WorldGeneratorScene;
import fr.iamacat.iamacatblockgame.gamescreen.TitleScreen;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private Player player;
    private Camera camera;
    private WorldGeneratorScene worldGeneratorScene;

    public GameScreen(Chunk[][] chunks) {
        batch = new SpriteBatch();
        player = new Player();
        camera = new Camera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Create an instance of WorldGeneratorScene with the generated chunks
        worldGeneratorScene = new WorldGeneratorScene(chunks);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // Update player position (example)
        player.setPosition(100, 100);

        // Update camera position
        camera.update(player);

        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        // Set projection matrix
        batch.setProjectionMatrix(camera.getCamera().combined);

        // Render player
        player.render(batch, camera);
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

    // Other methods in the Screen interface
}