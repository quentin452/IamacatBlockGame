package fr.iamacat.iamacatblockgame.worldgen.core;

import com.badlogic.gdx.math.MathUtils;
import fr.iamacat.iamacatblockgame.algorythme.chunkalgo.Block;
import fr.iamacat.iamacatblockgame.algorythme.chunkalgo.Chunk;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

                    executor.submit(() -> {
                        for (int x = startX; x < endX; x++) {
                            for (int y = startY; y < endY; y++) {
                                int heightMapIndex = y * worldWidth + x;
                                float height = heightMapBuffer.getFloat(heightMapIndex * 4); // 4 bytes per float

                                for (int z = 0; z < chunkLength; z++) {
                                    if (z < height * chunkLength) {
                                        blocks[x][y][z] = new Block(height); // Pass the height value to the Block constructor
                                    }
                                }
                            }
                        }
                    });
                }
            }
        } finally {
            executor.shutdown();

            try {
                // Wait for all tasks to complete
                executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return blocks;
    }

    public Chunk[][] generateChunks(Block[][][] blocks) {
        int numChunksX = blocks.length / chunkWidth;
        int numChunksY = blocks[0].length / chunkHeight;

        Chunk[][] chunks = new Chunk[numChunksX][numChunksY];

        for (int chunkX = 0; chunkX < numChunksX; chunkX++) {
            for (int chunkY = 0; chunkY < numChunksY; chunkY++) {
                int startX = chunkX * chunkWidth;
                int startY = chunkY * chunkHeight;
                int endX = startX + chunkWidth;
                int endY = startY + chunkHeight;

                Block[][] chunkBlocks = new Block[chunkWidth][chunkHeight];
                for (int x = startX; x < endX; x++) {
                    for (int y = startY; y < endY; y++) {
                        chunkBlocks[x - startX][y - startY] = blocks[x][y][0]; // Assuming chunkLength is 1
                    }
                }

                chunks[chunkX][chunkY] = new Chunk(chunkWidth, chunkHeight, 1, chunkBlocks);
            }
        }

        return chunks;
    }

    private ByteBuffer generateHeightMap() {
        int bufferSize = worldWidth * worldHeight * 4; // 4 bytes per float
        ByteBuffer buffer = ByteBuffer.allocateDirect(bufferSize);

        for (int x = 0; x < worldWidth; x++) {
            for (int y = 0; y < worldHeight; y++) {
                // Generate a random height value between 0 and 1
                float height = MathUtils.random();
                buffer.putFloat(height);
            }
        }

        buffer.flip(); // Prepare the buffer for reading

        return buffer;
    }
}