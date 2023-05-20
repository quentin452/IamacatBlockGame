package fr.iamacat.iamacatblockgame.worldgen.core;

import com.badlogic.gdx.math.MathUtils;
import fr.iamacat.iamacatblockgame.algorythme.chunkalgo.Block;
import fr.iamacat.iamacatblockgame.algorythme.chunkalgo.Chunk;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
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

    public Chunk[][] generateChunks(Block[][][] blocks, int viewDistance, int playerChunkX, int playerChunkY) {
        int numChunksX = blocks.length / chunkWidth;
        int numChunksY = blocks[0].length / chunkHeight;

        int startChunkX = MathUtils.clamp(playerChunkX - viewDistance, 0, numChunksX - 1);
        int endChunkX = MathUtils.clamp(playerChunkX + viewDistance, 0, numChunksX - 1);
        int startChunkY = MathUtils.clamp(playerChunkY - viewDistance, 0, numChunksY - 1);
        int endChunkY = MathUtils.clamp(playerChunkY + viewDistance, 0, numChunksY - 1);

        Chunk[][] chunks = new Chunk[endChunkX - startChunkX + 1][endChunkY - startChunkY + 1];

        for (int chunkX = startChunkX; chunkX <= endChunkX; chunkX++) {
            for (int chunkY = startChunkY; chunkY <= endChunkY; chunkY++) {
                int startX = chunkX * chunkWidth;
                int startY = chunkY * chunkHeight;
                int endX = startX + chunkWidth;
                int endY = startY + chunkHeight;

                List<List<Block>> chunkBlocks = new ArrayList<>();
                for (int x = startX; x < endX; x++) {
                    List<Block> row = new ArrayList<>();
                    for (int y = startY; y < endY; y++) {
                        row.add(blocks[x][y][0]); // Use the first layer of the block
                    }
                    chunkBlocks.add(row);
                }

                chunks[chunkX - startChunkX][chunkY - startChunkY] = new Chunk(chunkX, chunkY, chunkWidth, chunkHeight, chunkBlocks);
            }
        }

        return chunks;
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
