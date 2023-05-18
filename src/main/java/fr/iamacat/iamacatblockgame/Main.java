package fr.iamacat.iamacatblockgame;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.iamacat.iamacatblockgame.algorythme.chunkalgo.Block;
import fr.iamacat.iamacatblockgame.algorythme.chunkalgo.Chunk;
import fr.iamacat.iamacatblockgame.gamescreen.TitleScreen;
import fr.iamacat.iamacatblockgame.worldgen.core.WorldGenerator;


public class Main extends Game {
    private WorldGenerator worldGenerator;
    private Chunk[][] chunks; // Store the generated chunks
    private SpriteBatch batch;
    private TitleScreen titleScreen;

    public static void main(String[] args) {
        // Configure Log4j
        Log4jConfiguration.configure();

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "IamAcat Block Game";
        config.width = 1280;
        config.height = 720;
        config.addIcon("textures/gamescreen/icon.png", Files.FileType.Internal); // Set the path to your icon file

        new LwjglApplication(new Main(), config);
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        titleScreen = new TitleScreen(batch);
        setScreen(titleScreen);

        // Create an instance of WorldGenerator
        worldGenerator = new WorldGenerator(1000, 1000, 16, 16, 64);

        // Generate the chunks
        Block[][][] blocks = worldGenerator.generateBlocks();
        chunks = generateChunksFromBlocks(blocks);

    }

    private Chunk[][] generateChunksFromBlocks(Block[][][] blocks) {
        int numChunksX = blocks.length / worldGenerator.chunkWidth;
        int numChunksY = blocks[0].length / worldGenerator.chunkHeight;

        Chunk[][] chunks = new Chunk[numChunksX][numChunksY];

        for (int chunkX = 0; chunkX < numChunksX; chunkX++) {
            for (int chunkY = 0; chunkY < numChunksY; chunkY++) {
                int startX = chunkX * worldGenerator.chunkWidth;
                int startY = chunkY * worldGenerator.chunkHeight;
                int endX = startX + worldGenerator.chunkWidth;
                int endY = startY + worldGenerator.chunkHeight;

                Block[][] chunkBlocks = new Block[worldGenerator.chunkWidth][worldGenerator.chunkHeight];
                for (int x = startX; x < endX; x++) {
                    for (int y = startY; y < endY; y++) {
                        chunkBlocks[x - startX][y - startY] = blocks[x][y][0]; // Assuming chunkLength is 1
                    }
                }

                chunks[chunkX][chunkY] = new Chunk(worldGenerator.chunkWidth, worldGenerator.chunkHeight, 1, chunkBlocks);
            }
        }

        return chunks;
    }

    @Override
    public void render() {
        clearScreen();
        super.render();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void dispose() {
        batch.dispose();
        // Dispose textures and other disposable objects
    }
}