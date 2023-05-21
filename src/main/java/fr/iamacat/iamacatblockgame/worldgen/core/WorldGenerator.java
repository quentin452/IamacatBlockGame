package fr.iamacat.iamacatblockgame.worldgen.core;

import com.badlogic.gdx.math.MathUtils;
import fr.iamacat.iamacatblockgame.algorythme.chunkalgo.Block;
import fr.iamacat.iamacatblockgame.algorythme.chunkalgo.Chunk;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

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

    public Chunk[][] generateChunks(Block[][][] blocks, PrintStream logStream) {
        int numChunksX = blocks.length / chunkWidth;
        int numChunksY = blocks[0].length / chunkHeight;
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        ExecutorService progressPrinter = Executors.newSingleThreadExecutor();
        AtomicBoolean modelDirty = new AtomicBoolean(false);

        Chunk[][] chunks = new Chunk[numChunksX][numChunksY];

        int totalChunks = numChunksX * numChunksY;
        AtomicInteger generatedChunks = new AtomicInteger();

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

                Chunk chunk = new Chunk(chunkWidth, chunkHeight, chunkLength, chunkX, chunkY, chunkBlocks);
                chunks[chunkX][chunkY] = chunk;

                if (!chunk.isLoaded() && chunk.getLoadingTask() == null) {
                    chunk.setLoadingTask(executorService.submit(() -> {
                        // Load and generate the chunk data here

                        // Example: Simulate loading time by sleeping for 2 seconds
                        try {
                            Thread.sleep(200);
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

        generatedChunks.getAndIncrement();
        // Progress printer task
        progressPrinter.submit(() -> {
            try (PrintWriter writer = new PrintWriter(logStream, true)) {
                while (generatedChunks.get() < totalChunks) {
                    float totalProgress = generatedChunks.get() / (float) totalChunks * 100;
                    writer.println(totalProgress + "% world generated");

                    // Update the generatedChunks counter atomically
                    generatedChunks.getAndIncrement();

                    try {
                        Thread.sleep(50); // Print progress every 50 milliseconds
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                // Ensure the progress reaches 100% when all chunks are generated
                writer.println("100% world generated");
            }
        });

        executorService.shutdown();
        progressPrinter.shutdown();

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
            progressPrinter.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return chunks;
    }

    public Block[][][] generateBlocks() {
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
                            for (int z = 0; z < chunkLength; z++) {
                                float height = MathUtils.random();
                                if (z < height * chunkLength) {
                                    blocks[x][y][z] = new Block(height);
                                } else {
                                    blocks[x][y][z] = new Block(0.0f);
                                }
                            }
                        }
                    }
                }
            }
        } finally {
            executor.shutdown();
            try {
                executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return blocks;
    }
}
