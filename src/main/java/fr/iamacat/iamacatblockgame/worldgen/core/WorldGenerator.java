package fr.iamacat.iamacatblockgame.worldgen.core;
public class WorldGenerator {
    private final int worldWidth;
    private final int worldHeight;

    public WorldGenerator(int worldWidth, int worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    public float[][] generateHeightMap() {
        float[][] heightMap = new float[worldWidth][worldHeight];

        // Create a Perlin noise module as the basis for the terrain
        Gradient basis = new Gradient();

        // Create a fractal module to add complexity to the terrain
        Billow fractal = new Billow();

        // Combine the basis and fractal modules
        Add addModule = new Add();
        addModule.setSource1(basis);
        addModule.setSource2(fractal);

        // Scale and clamp the height map values
        ScaleBias scaleBias = new ScaleBias();
        scaleBias.setSource(addModule);
        scaleBias.setScale(0.5);
        scaleBias.setBias(0.5);
        Clamp clamp = new Clamp();
        clamp.setSource(scaleBias);
        clamp.setLowerBound(0);
        clamp.setUpperBound(1);

        // Generate the height map
        for (int x = 0; x < worldWidth; x++) {
            for (int y = 0; y < worldHeight; y++) {
                float value = clamp.getValue(x, y, 0);
                heightMap[x][y] = value;
            }
        }

        return heightMap;
    }
}