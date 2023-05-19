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

public class Chunk implements Screen {
    private int width;
    private int height;
    private int length;
    private Block[][] chunkBlocks;
    private ModelInstance modelInstance;
    private BitmapFont font;
    private SpriteBatch spriteBatch;

    private int blockCount; // Counter for the number of generated blocks

    public Chunk(int width, int height, int length, int chunkHeight, Block[][] chunkBlocks) {
        this.width = width;
        this.height = height;
        this.length = length;
        this.chunkBlocks = chunkBlocks;
        this.blockCount = 0; // Initialize the block count to 0
        // Create font and sprite batch for the screen
        font = new BitmapFont();
        spriteBatch = new SpriteBatch();
    }
    public void renderBlockCount() {
        spriteBatch.begin();
        font.draw(spriteBatch, "Block count: " + blockCount, 10, Gdx.graphics.getHeight() - 10);
        spriteBatch.end();
    }
    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    public void createModelInstance(Vector3 playerPosition, int viewDistance) {
        ModelBuilder modelBuilder = new ModelBuilder();

        modelBuilder.begin();
        MeshPartBuilder meshPartBuilder = modelBuilder.part("blocks", GL30.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates, new Material());

        int startX = Math.max((int) (playerPosition.x - viewDistance), 0);
        int startY = Math.max((int) (playerPosition.y - viewDistance), 0);
        int startZ = Math.max((int) (playerPosition.z - viewDistance), 0);

        int endX = Math.min((int) (playerPosition.x + viewDistance), width - 1);
        int endY = Math.min((int) (playerPosition.y + viewDistance), height - 1);
        int endZ = Math.min((int) (playerPosition.z + viewDistance), length - 1);

        for (int z = startZ; z <= endZ; z++) {
            for (int y = startY; y <= endY; y++) {
                for (int x = startX; x <= endX; x++) {
                    Block block = chunkBlocks[x][y];
                    if (block != null) {
                        Vector3 position = new Vector3(x, y, z);
                        meshPartBuilder.setVertexTransform(new Matrix4().trn(position));
                        meshPartBuilder.addMesh(block.getModel().meshParts.get(0).mesh);
                        incrementBlockCount(); // Increment the block count when a block is generated
                        renderBlockCount(); // Render the block count on the screen
                    }
                }
            }
        }

        Model model = modelBuilder.end();
        modelInstance = new ModelInstance(model);
    }

    public int getBlockCount() {
        return blockCount;
    }

    private void incrementBlockCount() {
        blockCount++;
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);

        spriteBatch.begin();
        renderBlockCount(); // Render the block count on the screen
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
            Block block = chunkBlocks[x][y];
            return block != null ? block.getHeight() : 0.0f;
        }
        return 0.0f;
    }

    public void setBlockHeight(int x, int y, int z, float height) {
        if (isValidBlockCoordinate(x, y, z)) {
            Block block = chunkBlocks[x][y];
            if (block != null) {
                block.setHeight(height);
            } else {
                block = new Block(height);
                chunkBlocks[x][y] = block;
            }
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
        modelInstance.model.dispose();
        // Dispose of blocks and associated resources
        for (int z = 0; z < length; z++) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Block block = chunkBlocks[x][y];
                    if (block != null) {
                        block.dispose();
                    }
                }
            }
        }
        chunkBlocks = null;
    }

    private int getIndex(int x, int y, int z) {
        return x + y * width + z * width * height;
    }
}