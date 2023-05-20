package fr.iamacat.iamacatblockgame.worldgen.core;

import com.badlogic.gdx.math.MathUtils;
import fr.iamacat.iamacatblockgame.algorythme.chunkalgo.Block;
import fr.iamacat.iamacatblockgame.algorythme.chunkalgo.Chunk;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class WorldGenerator {
    private final int worldWidth;
    private final int worldHeight;
    public final int chunkWidth;
    public final int chunkHeight;
    private final int chunkLength;

    public WorldGenerator(int worldWidth, int worldHeight, int chunkWidth, int chunkHeight, int chunkLength) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.chunkWidth = chunkWidth;
        this.chunkHeight = chunkHeight;
        this.chunkLength = chunkLength;
    }

    public Chunk[][] generateChunks(Block[][][] blocks) {
        int numChunksX = blocks.length / chunkWidth;
        int numChunksY = blocks[0].length / chunkHeight;
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        AtomicBoolean modelDirty = new AtomicBoolean(false);

        Chunk[][] chunks = new Chunk[numChunksX][numChunksY];

        for (int chunkX = 0; chunkX < numChunksX; chunkX++) {
            for (int chunkY = 0; chunkY < numChunksY; chunkY++) {
                int startX = chunkX * chunkWidth;
                int startY = chunkY * chunkHeight;
                int endX = startX + chunkWidth;
                int endY = startY + chunkHeight;

                List<List<Block>> chunkBlocks = new ArrayList<>();
                for (int x = startX; x < endX; x++) {
                    List<Block> row = new ArrayList<>();
                    for (int y = startY; y < endY; y++) {
                        for (int z = 0; z < chunkLength; z++) {
                            row.add(blocks[x][y][z]);
                        }
                    }
                    chunkBlocks.add(row);
                }

                Chunk chunk = new Chunk(chunkX, chunkY, chunkWidth, chunkHeight, chunkBlocks);
                chunks[chunkX][chunkY] = chunk;

                if (!chunk.isLoaded() && chunk.getLoadingTask() == null) {
                    chunk.setLoadingTask(executorService.submit(() -> {
                        // Load and generate the chunk data here

                        // Example: Simulate loading time by sleeping for 2 seconds
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // Once the chunk data is loaded, update the loaded flag and mark the model as dirty
                        chunk.setLoaded(true);
                        modelDirty.set(true);

                        return null; // Return null explicitly
                    }));
                }
            }
        }

        return chunks;
    }

    public Block[][][] generateBlocks() {
        ByteBuffer heightMapBuffer = generateHeightMap();

        int numChunksX = worldWidth / chunkWidth;
        int numChunksY = worldHeight / chunkHeight;

        Block[][][] blocks = new Block[numChunksX * chunkWidth][numChunksY * chunkHeight][chunkLength];
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        try {
            for (int chunkX = 0; chunkX < numChunksX; chunkX++) {
                for (int chunkY = 0; chunkY < numChunksY; chunkY++) {
                    int startX = chunkX * chunkWidth;
                    int startY = chunkY * chunkHeight;
                    int endX = startX + chunkWidth;
                    int endY = startY + chunkHeight;

                    for (int x = startX; x < endX; x++) {
                        for (int y = startY; y < endY; y++) {
                            int heightMapIndex = y * worldWidth + x;
                            float height = heightMapBuffer.getFloat(heightMapIndex * 4); // 4 bytes per float

                            for (int z = 0; z < chunkLength; z++) {
                                if (z < height * chunkLength) {
                                    blocks[x][y][z] = new Block(height); // Pass the height value to the Block constructor
                                } else {
                                    blocks[x][y][z] = new Block(0.0f); // Create empty block for remaining layers
                                }
                            }
                        }
                    }
                }
            }
        } finally {
            executor.shutdown(); // Shutdown the executor to release resources
        }

        return blocks;
    }

    private ByteBuffer generateHeightMap() {
        int bufferSize = worldWidth * worldHeight * 4; // 4 bytes per float
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

        for (int x = 0; x < worldWidth; x++) {
            for (int y = 0; y < worldHeight; y++) {
                float height = MathUtils.random();
                buffer.putFloat(height);
            }
        }

        buffer.flip();
        return buffer;
    }
}
