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
import fr.iamacat.iamacatblockgame.settings.WorldSettings;

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
        // Limit the view distance to a maximum value
        int currentViewDistance = Math.min(viewDistance, WorldSettings.MAX_VIEW_DISTANCE);
        // Check if chunk is within view distance
        if (!isChunkWithinViewDistance(playerPosition, currentViewDistance)) {
            unloadChunk();
            return;
        }

        if (model == null) {
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
        }

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

    public void unloadChunk() {
        if (!loaded) {
            return; // Chunk is already unloaded
        }

        if (modelInstance != null) {
            modelInstance.model.dispose();
            modelInstance = null; // Set the model instance to null
        }

        // Dispose of blocks and associated resources
        for (List<Block> row : chunkBlocks) {
            for (Block block : row) {
                if (block != null) {
                    block.dispose();
                }
            }
            row.clear();
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

    public Vector3 getCenterPoint() {
        return new Vector3((float) width / 2, (float) height / 2, (float) length / 2);
    }

    @Override
    public void render(float delta) {
        // Nothing to render in this screen
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
                block.setHeight(height);
            } else {
                block = new Block(height);
                row.set(x, block);
            }
            modelDirty = true; // Mark the model as dirty when a block's height changes
        }
    }

    private boolean isValidBlockCoordinate(int x, int y, int z) {
        // Limit the block coordinates to within the desired world width and height
        return x >= 0 && x < WorldSettings.DESIRED_WORLD_WIDTH && y >= 0 && y < WorldSettings.DESIRED_WORLD_HEIGHT && z >= 0 && z < length;
    }

    @Override
    public void show() {
        // No additional actions needed when showing the screen
    }

    @Override
    public void resize(int width, int height) {
        // No additional actions needed when resizing the screen
    }

    @Override
    public void pause() {
        // No additional actions needed when pausing the screen
    }

    @Override
    public void resume() {
        // No additional actions needed when resuming the screen
    }

    @Override
    public void hide() {
        // No additional actions needed when hiding the screen
    }

    public void dispose() {
        if (modelInstance != null) {
            modelInstance.model.dispose();
            modelInstance = null;
        }

        // Dispose of blocks and associated resources
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
