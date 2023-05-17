package fr.iamacat.iamacatblockgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.iamacat.iamacatblockgame.gamescreen.TitleScreen;


public class Main extends Game {
    private SpriteBatch batch;
    private TitleScreen titleScreen;

    @Override
    public void create() {
        batch = new SpriteBatch();
        titleScreen = new TitleScreen(batch);
        setScreen(titleScreen);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "IamAcat Block Game";
        config.width = 1280;
        config.height = 720;
        config.addIcon("textures/gamescreen/icon.png", Files.FileType.Internal); // Set the path to your icon file

        new LwjglApplication(new Main(), config);
    }
}