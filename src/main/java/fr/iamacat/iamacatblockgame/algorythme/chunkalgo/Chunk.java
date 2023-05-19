package fr.iamacat.iamacatblockgame.algorythme.chunkalgo;

import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class Chunk {
    private int width;
    private int height;
    private int length;
    private Block[][] chunkBlocks;
    private ModelInstance modelInstance;
    public ModelInstance getModelInstance() {
        return modelInstance;
    }
    public Chunk(int width, int height, int length, Block[][] chunkBlocks) {
        this.width = width;
        this.height = height;
        this.length = length;
        this.chunkBlocks = chunkBlocks;

        ModelBuilder modelBuilder = new ModelBuilder();

        modelBuilder.begin();
        MeshPartBuilder meshPartBuilder = modelBuilder.part("blocks", GL30.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates, new Material());

        for (int z = 0; z < length; z++) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Block block = chunkBlocks[x][y];
                    if (block != null) {
                        Vector3 position = new Vector3(x, y, z);
                        meshPartBuilder.setVertexTransform(new Matrix4().trn(position));
                        meshPartBuilder.addMesh(block.getModel().meshParts.get(0).mesh);
                    }
                }
            }
        }

        Model model = modelBuilder.end();
        modelInstance = new ModelInstance(model);
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