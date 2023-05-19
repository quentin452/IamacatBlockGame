package fr.iamacat.iamacatblockgame.algorythme.chunkalgo;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Frustum;

public class Chunk {
    private int width;
    private int height;
    private int length;
    private Block[] blocks;
    private OrthographicCamera cam;
    private Frustum frustum;

    public Chunk(int width, int height, int length, Block[][] chunkBlocks) {
        System.out.println("Chunk constructor called");
        this.width = width;
        this.height = height;
        this.length = length;
        blocks = new Block[width * height * length];

        // Initialize the blocks using chunkBlocks parameter
        int index = 0;
        for (int z = 0; z < length; z++) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Block block = chunkBlocks[x][y];
                    blocks[index] = block;
                    index++;
                }
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLength() {
        return length;
    }

    public float getBlockHeight(int x, int y, int z) {
        int index = getIndex(x, y, z);
        Block block = blocks[index];
        return block != null ? block.getHeight() : 0.0f;
    }

    public void generate() {
        System.out.println("Chunk generate method called");
        // Generate terrain here
        updateUI();
    }

    public void setBlockHeight(int x, int y, int z, float height) {
        int index = getIndex(x, y, z);
        Block block = blocks[index];

        if (block != null) {
            block.setHeight(height);
        } else {
            block = new Block(height);
            blocks[index] = block;
        }
    }

    private void updateUI() {
        // Update UI if needed
    }

    private boolean isValidBlockCoordinate(int x, int y, int z) {
        return x >= 0 && x < width && y >= 0 && y < height && z >= 0 && z < length;
    }

    public void render(SpriteBatch batch) {
        System.out.println("Rendering Chunks...");
        if (frustum.boundsInFrustum(cam.position.x, cam.position.y, 0,
                cam.viewportWidth, cam.viewportHeight, 0)) {
            batch.begin();
            for (Block block : blocks) {
                if (block != null) {
                    // Replace 'render' with the correct method for rendering your blocks
                    block.render(batch);
                }
            }
            batch.end();
        }
    }

    public void dispose() {
        // Dispose of blocks and associated resources
        for (Block block : blocks) {
            if (block != null) {
                block.dispose();
            }
        }
        blocks = null;
    }

    private int getIndex(int x, int y, int z) {
        return x + y * width + z * width * height;
    }
}