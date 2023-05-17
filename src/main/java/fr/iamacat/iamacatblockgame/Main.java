package fr.iamacat.iamacatblockgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jme3.renderer.opengl.GL3;
import fr.iamacat.iamacatblockgame.gamescreen.TitleScreen;
import fr.iamacat.iamacatblockgame.multithreadingandbatching.GLCALLMultithreadedandbatched;

import java.io.IOException;


public class Main extends ApplicationAdapter {
    GLCALLMultithreadedandbatched gl;
    private SpriteBatch batch;
    private TitleScreen titleScreen;

    @Override
    public void create() {
        batch = new SpriteBatch();
        titleScreen = new TitleScreen(batch);
        gl = new GLCALLMultithreadedandbatched(); // Assign value to the instance variable
    }

    @Override
    public void render() {
        gl.executeGLCall(() -> {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        });

        titleScreen.update();
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