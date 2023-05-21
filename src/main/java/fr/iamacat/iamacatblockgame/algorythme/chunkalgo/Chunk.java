package fr.iamacat.iamacatblockgame.algorythme.chunkalgo;

import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import fr.iamacat.iamacatblockgame.settings.WorldSettings;

import java.util.List;
import java.util.concurrent.Future;

public class Chunk {

    private boolean loaded;
    private int width;
    private int height;
    private int length;
    private int chunkX;
    private int chunkY;
    private List<List<Block>> chunkBlocks;
    private ModelInstance modelInstance;
    private boolean modelDirty;
    private Model model;
    private Future<Void> loadingTask;
    private BitmapFont font;
    private SpriteBatch spriteBatch;

    public Chunk(int width, int height, int length, int chunkX, int chunkY, List<List<Block>> chunkBlocks) {
        this.width = width;
        this.height = height;
        this.length = length;
        this.chunkX = chunkX;
        this.chunkY = chunkY;
        this.chunkBlocks = chunkBlocks;
        this.loaded = false;
        this.modelDirty = true;
        this.font = new BitmapFont();
        this.spriteBatch = new SpriteBatch();
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkY() {
        return chunkY;
    }

    public ModelInstance getModelInstance() {
        if (modelInstance == null) {
            Vector3 playerPosition = new Vector3(0, 0, 0);
            int viewDistance = 10;
            createModelInstance(playerPosition, viewDistance);
        }
        return modelInstance;
    }

    public void createModelInstance(Vector3 playerPosition, int viewDistance) {
        if (!modelDirty || !isChunkWithinViewDistance(playerPosition, viewDistance)) {
            return;
        }

        if (model != null) {
            model.dispose();
        }

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();

        MeshPartBuilder meshPartBuilder = modelBuilder.part("blocks", GL30.GL_TRIANGLES,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates, new Material());

        int startX = Math.max((int) (playerPosition.x - viewDistance), 0);
        int startY = Math.max((int) (playerPosition.y - viewDistance), 0);
        int startZ = Math.max((int) (playerPosition.z - viewDistance), 0);

        int endX = Math.min((int) (playerPosition.x + viewDistance), width - 1);
        int endY = Math.min((int) (playerPosition.y + viewDistance), height - 1);
        int endZ = Math.min((int) (playerPosition.z + viewDistance), length - 1);

        for (int z = startZ; z <= endZ; z++) {
            for (int y = startY; y <= endY; y++) {
                List<Block> row = chunkBlocks.get(y);
                for (int x = startX; x <= endX; x++) {
                    Block block = row.get(x);
                    if (block != null) {
                        Vector3 position = new Vector3(x, y, z);
                        meshPartBuilder.setVertexTransform(new Matrix4().trn(position));
                        meshPartBuilder.addMesh(block.getModel().meshParts.get(0).mesh);
                    }
                }
            }
        }

        model = modelBuilder.end();

        if (model != null) {
            modelInstance = new ModelInstance(model);
            loaded = true;
            modelDirty = false;
        } else {
            unloadChunk();
        }
    }

    private boolean isChunkWithinViewDistance(Vector3 playerPosition, int viewDistance) {
        int startX = Math.max((int) (playerPosition.x - viewDistance), 0);
        int startY = Math.max((int) (playerPosition.y - viewDistance), 0);
        int startZ = Math.max((int) (playerPosition.z - viewDistance), 0);

        int endX = Math.min((int) (playerPosition.x + viewDistance), width - 1);
        int endY = Math.min((int) (playerPosition.y + viewDistance), height - 1);
        int endZ = Math.min((int) (playerPosition.z + viewDistance), length - 1);

        return startX <= endX && startY <= endY && startZ <= endZ;
    }

    public void unloadChunk() {
        if (!loaded) {
            return;
        }
        if (loadingTask != null && !loadingTask.isDone()) {
            loadingTask.cancel(true);
        }
        if (modelInstance != null) {
            modelInstance.model.dispose();
            modelInstance = null;
        }

        for (List<Block> row : chunkBlocks) {
            for (Block block : row) {
                if (block != null) {
                    block.dispose();
                }
            }
            row.clear();
        }

        loaded = false;
        modelDirty = false;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public Future<Void> getLoadingTask() {
        return loadingTask;
    }

    public void setLoadingTask(Future<Void> loadingTask) {
        this.loadingTask = loadingTask;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public Vector3 getCenterPoint() {
        return new Vector3((float) width / 2, (float) height / 2, (float) length / 2);
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
        if (isValidBlockCoordinate(x, y, z)) {
            Block block = chunkBlocks.get(y).get(x);
            return block != null ? block.getHeight() : 0.0f;
        }
        return 0.0f;
    }

    public void setBlockHeight(int x, int y, int z, float height) {
        if (isValidBlockCoordinate(x, y, z)) {
            List<Block> row = chunkBlocks.get(y);
            Block block = row.get(x);
            if (block != null) {
                if (block.getHeight() != height) {
                    block.setHeight(height);
                    modelDirty = true;
                }
            } else {
                block = new Block(height);
                row.set(x, block);
                modelDirty = true;
            }
        }
    }

    private boolean isValidBlockCoordinate(int x, int y, int z) {
        return x >= 0 && x < WorldSettings.DESIRED_WORLD_WIDTH &&
                y >= 0 && y < WorldSettings.DESIRED_WORLD_HEIGHT &&
                z >= 0 && z < length;
    }

    public void dispose() {
        if (modelInstance != null) {
            modelInstance.model.dispose();
            modelInstance = null;
        }

        for (List<Block> row : chunkBlocks) {
            for (Block block : row) {
                if (block != null) {
                    block.dispose();
                }
            }
            row.clear();
        }
        chunkBlocks = null;

        if (font != null) {
            font.dispose();
            font = null;
        }

        if (spriteBatch != null) {
            spriteBatch.dispose();
            spriteBatch = null;
        }
    }

    private int getIndex(int x, int y, int z) {
        return x + y * width + z * width * height;
    }
}