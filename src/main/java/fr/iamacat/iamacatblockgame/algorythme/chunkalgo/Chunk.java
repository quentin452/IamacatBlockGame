package fr.iamacat.iamacatblockgame.algorythme.chunkalgo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Chunk implements Screen {

    private boolean loaded;
    private int width;
    private int height;
    private int length;
    private List<List<Block>> chunkBlocks;
    private ModelInstance modelInstance;
    private BitmapFont font;
    private SpriteBatch spriteBatch;
    private boolean modelDirty; // Flag indicating whether the model needs regeneration
    private Model model; // Cached model for the chunk
    private Future<Void> loadingTask; // Task for loading the chunk asynchronously

    public Chunk(int width, int height, int length, int chunkHeight, List<List<Block>> chunkBlocks) {
        this.width = width;
        this.height = height;
        this.length = length;
        this.chunkBlocks = chunkBlocks;
        this.loaded = false;
        this.modelDirty = true;
        // Create font and sprite batch for the screen
        font = new BitmapFont();
        spriteBatch = new SpriteBatch();
    }

    public ModelInstance getModelInstance() {
        if (modelDirty) {
            Vector3 playerPosition = new Vector3(0, 0, 0);
            int viewDistance = 10;
            createModelInstance(playerPosition, viewDistance);
        }
        return modelInstance;
    }

    public void createModelInstance(Vector3 playerPosition, int viewDistance) {
        if (!modelDirty) {
            return;
        }
        int currentViewDistance = (int) (double) viewDistance;
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
                for (int x = startX; x <= endX; x++) {
                    Block block = chunkBlocks.get(x).get(y);
                    if (block != null) {
                        Vector3 position = new Vector3(x, y, z);
                        meshPartBuilder.setVertexTransform(new Matrix4().trn(position));
                        meshPartBuilder.addMesh(block.getModel().meshParts.get(0).mesh);
                    }
                }
            }
        }
        // Add the following check to skip generating the model if the chunk is outside the view distance
        if (!isChunkWithinViewDistance(playerPosition, viewDistance)) {
            unloadChunk();
            return;
        }
        model = modelBuilder.end();
        modelInstance = new ModelInstance(model);
        modelDirty = false;
    }

    private boolean isChunkWithinViewDistance(Vector3 playerPosition, int viewDistance) {
        int startX = Math.max((int) (playerPosition.x - viewDistance), 0);
        int startY = Math.max((int) (playerPosition.y - viewDistance), 0);
        int startZ = Math.max((int) (playerPosition.z - viewDistance), 0);

        int endX = Math.min((int) (playerPosition.x + viewDistance), width - 1);
        int endY = Math.min((int) (playerPosition.y + viewDistance), height - 1);
        int endZ = Math.min((int) (playerPosition.z + viewDistance), length - 1);

        // Check if any corner of the chunk is within the view distance
        return startX <= endX && startY <= endY && startZ <= endZ;
    }

    private void unloadChunk() {
        if (!loaded) {
            return; // Chunk is already unloaded
        }

        modelInstance.model.dispose();
        modelInstance = null; // Set the model instance to null

        // Dispose of blocks and associated resources
        for (int z = 0; z < length; z++) {
            for (int y = 0; y < height; y++) {
                Block block = chunkBlocks.get(z).get(y);
                if (block != null) {
                    block.dispose();
                    chunkBlocks.get(z).set(y, null);
                }
            }
        }

        loaded = false; // Set the loaded flag to false
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

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);

        spriteBatch.begin();
        spriteBatch.end();
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
            Block block = chunkBlocks.get(x).get(y);
            return block != null ? block.getHeight() : 0.0f;
        }
        return 0.0f;
    }

    public void setBlockHeight(int x, int y, int z, float height) {
        if (isValidBlockCoordinate(x, y, z)) {
            Block block = chunkBlocks.get(x).get(y);
            if (block != null) {
                block.setHeight(height);
            } else {
                block = new Block(height);
                chunkBlocks.get(x).set(y, block);
            }
            modelDirty = true; // Mark the model as dirty when a block's height changes
        }
    }

    private boolean isValidBlockCoordinate(int x, int y, int z) {
        return x >= 0 && x < width && y >= 0 && y < height && z >= 0 && z < length;
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

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

    public void dispose() {
        if (modelInstance != null) {
            modelInstance.model.dispose();
            modelInstance = null;
        }

        // Dispose of blocks and associated resources
        for (int z = 0; z < length; z++) {
            for (int y = 0; y < height; y++) {
                Block block = chunkBlocks.get(z).get(y);
                if (block != null) {
                    block.dispose();
                    chunkBlocks.get(z).set(y, null);
                }
            }
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
