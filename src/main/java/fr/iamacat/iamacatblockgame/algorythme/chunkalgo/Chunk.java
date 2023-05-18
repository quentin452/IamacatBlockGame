package fr.iamacat.iamacatblockgame.algorythme.chunkalgo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.utils.async.AsyncTask;


public class Chunk {

    private final int width;
    private final int height;
    private final int length;

    // Use your own Block class instead of 'Block' from com.jcraft.jorbis
    private Block[][][] blocks;

    private OrthographicCamera cam;
    private Frustum frustum;

    public Chunk(int width, int height, int length) {
        this.width = width;
        this.height = height;
        this.length = length;
        blocks = new Block[width][height][length];
    }

    public void generate() {
        // Generate on background thread
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Generate terrain here

                // Call updateUI on the UI thread after generation is complete
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        updateUI();
                    }
                });
            }
        });

        thread.start();
    }

    public void setBlockHeight(int x, int y, int z, float height) {
        blocks[x][y][z].setHeight(height);
    }


    private void updateUI() {
        // Update UI if needed
    }

    public void render(SpriteBatch batch) {
        if (frustum.boundsInFrustum(cam.position.x, cam.position.y, 0,
                cam.viewportWidth, cam.viewportHeight, 0)) {
            batch.begin();
            for (int z = 0; z < length; z++) {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        Block block = blocks[x][y][z];
                        // Replace 'render' with the correct method for rendering your blocks
                        block.render(batch);
                    }
                }
            }
            batch.end();
        }
    }
}