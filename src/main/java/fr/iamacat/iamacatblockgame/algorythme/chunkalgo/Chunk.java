package fr.iamacat.iamacatblockgame.algorythme.chunkalgo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.utils.async.AsyncTask;


public class Chunk {
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLength() {
        return length;
    }
    private int width;
    private int height;
    private int length;

    private Block[][][] blocks;

    private OrthographicCamera cam;
    private Frustum frustum;

    public Chunk(int width, int height, int length) {
        this.width = width;
        this.height = height;
        this.length = length;

        // Initialize the blocks array with the desired dimensions
        blocks = new Block[width][height][length];

        // Populate the blocks array with valid Block objects
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < length; z++) {
                    // Create and assign a new Block object to the blocks array
                    blocks[x][y][z] = new Block(height);
                }
            }
        }
    }

    public float getBlockHeight(int x, int y, int z) {
        Block block = blocks[x][y][z];
        return block.getHeight();
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
    private boolean isValidBlockCoordinate(int x, int y, int z) {
        return x >= 0 && x < width && y >= 0 && y < height && z >= 0 && z < length;
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