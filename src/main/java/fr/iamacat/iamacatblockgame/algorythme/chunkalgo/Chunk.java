package fr.iamacat.iamacatblockgame.algorythme.chunkalgo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Frustum;

import java.util.HashMap;
import java.util.Map;

public class Chunk {
    private int width;
    private int height;
    private int length;
    private Map<BlockPosition, Block> blocks;
    private OrthographicCamera cam;
    private Frustum frustum;
    private Map<Float, Block> singletons;

    public Chunk(int width, int height, int length) {
        this.width = width;
        this.height = height;
        this.length = length;

        blocks = new HashMap<>();
        singletons = new HashMap<>();
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
        Block block = blocks.get(new BlockPosition(x, y, z));
        return block != null ? block.getHeight() : 0.0f;
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
        BlockPosition position = new BlockPosition(x, y, z);
        Block block = blocks.get(position);

        if (block != null) {
            block.setHeight(height);
        } else {
            block = getOrCreateBlock(height);
            blocks.put(position, block);
        }
    }

    private Block getOrCreateBlock(float height) {
        Block block = singletons.get(height);
        if (block == null) {
            block = new Block(height);
            singletons.put(height, block);
        }
        return block;
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
            for (Map.Entry<BlockPosition, Block> entry : blocks.entrySet()) {
                Block block = entry.getValue();
                // Replace 'render' with the correct method for rendering your blocks
                block.render(batch);
            }
            batch.end();
        }
    }

    public void dispose() {
        // Dispose of singleton blocks
        for (Block block : singletons.values()) {
            block.dispose();
        }
        singletons.clear();
        blocks.clear();
    }

    private static class BlockPosition {
        private int x;
        private int y;
        private int z;

        public BlockPosition(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + x;
            result = prime * result + y;
            result = prime * result + z;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            BlockPosition other = (BlockPosition) obj;
            if (x != other.x)
                return false;
            if (y != other.y)
                return false;
            if (z != other.z)
                return false;
            return true;
        }
    }
}