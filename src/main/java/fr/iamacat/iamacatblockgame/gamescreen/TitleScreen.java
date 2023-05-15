package fr.iamacat.iamacatblockgame.gamescreen;

import fr.iamacat.iamacatblockgame.textures.TextureLoader;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class TitleScreen {
    private static final Logger logger = LogManager.getLogger(TitleScreen.class);

    private boolean gameStarted;
    private long window;
    private List<Button> buttons;
    private int titleScreenTextureID;
    private int vaoID;
    private int vertexVBOID;
    private int texCoordVBOID;
    private int indexVBOID;

    public TitleScreen(long window) {
        this.window = window;
        buttons = new ArrayList<>();
        createButtons();
        loadTitleScreenTexture();
        setupMesh();
        gameStarted = false;
    }

    private void loadTitleScreenTexture() {
        String titleScreenTexturePath = "textures/gamescreen/titlescreen.png";

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(titleScreenTexturePath)) {
            if (inputStream == null) {
                throw new RuntimeException("Failed to load texture: " + titleScreenTexturePath);
            }

            BufferedImage image = ImageIO.read(inputStream);
            TextureLoader textureLoader = new TextureLoader();
            titleScreenTextureID = textureLoader.loadTexture(image);

            System.out.println("Texture loaded successfully. Texture ID: " + titleScreenTextureID);
        } catch (IOException e) {
            System.err.println("Failed to load texture: " + titleScreenTexturePath);
            e.printStackTrace();
            throw new RuntimeException("Failed to load texture: " + titleScreenTexturePath, e);
        }
    }

    private void createButtons() {
        String playButtonTexturePath = "textures/gamescreen/button/playbutton.png";
        int playButtonTextureID = loadTextureFromResource(playButtonTexturePath);
        String quitButtonTexturePath = "textures/gamescreen/button/quitbutton.png";
        int quitButtonTextureID = loadTextureFromResource(quitButtonTexturePath);
        int buttonWidth = 100;
        int buttonHeight = 50;
        Button playButton = new Button("Join", 100, 100, buttonWidth, buttonHeight, playButtonTextureID);
        Button quitButton = new Button("Exit", 100, 200, buttonWidth, buttonHeight, quitButtonTextureID);
        buttons.add(playButton);
        buttons.add(quitButton);
    }

    private int loadTextureFromResource(String path) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new RuntimeException("Failed to load texture: " + path);
            }

            BufferedImage image = ImageIO.read(inputStream);
            TextureLoader textureLoader = new TextureLoader();
            return textureLoader.loadTexture(image);
        } catch (IOException e) {
            System.err.println("Failed to load texture: " + path);
            e.printStackTrace();
            throw new RuntimeException("Failed to load texture: " + path, e);
        }
    }

    private void setupMesh() {
        float[] vertices = {
                -1.0f, -1.0f, 0.0f,
                1.0f, -1.0f, 0.0f,
                1.0f, 1.0f, 0.0f,
                -1.0f,  1.0f, 0.0f
        };

        // Set up the texture coordinates
        float[] texCoords = {
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f,
                0.0f, 0.0f
        };

        // Set up the indices for the quad
        int[] indices = {
                0, 1, 2,
                2, 3, 0
        };

        // Create and bind the VAO
        vaoID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoID);

        // Create the vertex VBO and bind it
        vertexVBOID = GL46.glGenBuffers();
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vertexVBOID);
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, vertices, GL46.GL_STATIC_DRAW);
        GL46.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

        // Create the texture coordinate VBO and bind it
        texCoordVBOID = GL46.glGenBuffers();
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, texCoordVBOID);
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, texCoords, GL46.GL_STATIC_DRAW);
        GL46.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);

        // Create the index VBO and bind it
        indexVBOID = GL46.glGenBuffers();
        GL46.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexVBOID);
        GL46.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL46.GL_STATIC_DRAW);

        // Unbind the VAO
        GL46.glBindVertexArray(0);
    }

    public void update() {
        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT);
        renderTitleScreen();
        renderButtons();
        handleButtonClicks();
        GLFW.glfwSwapBuffers(window);
        GLFW.glfwPollEvents();
    }

    private void renderTitleScreen() {
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);

        GL46.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT);

        GL46.glBindTexture(GL46.GL_TEXTURE_2D, titleScreenTextureID);
        GL46.glBindVertexArray(vaoID);

        GL46.glEnableVertexAttribArray(0);
        GL46.glEnableVertexAttribArray(1);

        GL46.glDrawElements(GL46.GL_TRIANGLES, 6, GL46.GL_UNSIGNED_INT, 0);

        GL46.glDisableVertexAttribArray(0);
        GL46.glDisableVertexAttribArray(1);

        GL46.glBindVertexArray(0);
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);

        GL46.glDisable(GL46.GL_BLEND);
    }

    private void renderButtons() {
        for (Button button : buttons) {
            GL46.glBindTexture(GL46.GL_TEXTURE_2D, button.getTextureID());
            button.render();
        }
    }

    private void handleButtonClicks() {
        double mouseX;
        double mouseY;

        double[] xpos = new double[1];
        double[] ypos = new double[1];

        GLFW.glfwGetCursorPos(window, xpos, ypos);
        mouseX = xpos[0];
        mouseY = ypos[0];

        if (!gameStarted) {
            for (Button button : buttons) {
                if (button.isClicked(mouseX, mouseY)) {
                    if (button.getText().equals("Join")) {
                        System.out.println("Starting the game...");
                        gameStarted = true;
                    } else if (button.getText().equals("Exit")) {
                        GLFW.glfwSetWindowShouldClose(window, true);
                    }
                }
            }
        }
    }
}