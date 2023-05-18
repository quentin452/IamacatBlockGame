package fr.iamacat.iamacatblockgame.worldgen.core;

import fr.iamacat.iamacatblockgame.algorythme.chunkalgo.Chunk;

public class WorldGenerator {
    private final int worldWidth;
    private final int worldHeight;
    private final int chunkWidth;
    private final int chunkHeight;
    private final int chunkLength;

    public WorldGenerator(int worldWidth, int worldHeight, int chunkWidth, int chunkHeight, int chunkLength) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.chunkWidth = chunkWidth;
        this.chunkHeight = chunkHeight;
        this.chunkLength = chunkLength;
    }

    public Chunk[][] generateChunks() {
        float[][] heightMap = generateHeightMap();

        int numChunksX = worldWidth / chunkWidth;
        int numChunksY = worldHeight / chunkHeight;

        Chunk[][] chunks = new Chunk[numChunksX][numChunksY];

        for (int chunkX = 0; chunkX < numChunksX; chunkX++) {
            for (int chunkY = 0; chunkY < numChunksY; chunkY++) {
                int startX = chunkX * chunkWidth;
                int startY = chunkY * chunkHeight;
                int endX = startX + chunkWidth;
                int endY = startY + chunkHeight;

                Chunk chunk = new Chunk(chunkWidth, chunkHeight, chunkLength);

                for (int x = startX; x < endX; x++) {
                    for (int y = startY; y < endY; y++) {
                        // Calculate terrain height for the chunk using heightMap
                        float height = heightMap[x][y];
                        // Assign the height to the corresponding block in the chunk
                        chunk.setBlockHeight(x - startX, y - startY, 0, height);
                    }
                }

                // Generate the chunk's terrain asynchronously
                chunk.generate();

                chunks[chunkX][chunkY] = chunk;
            }
        }

        return chunks;
    }

    private float[][] generateHeightMap() {
        // Create and populate the height map
        float[][] heightMap = new float[worldWidth][worldHeight];
        // Generate the height values for each coordinate in the height map
        // ...

        return heightMap;
    }
}