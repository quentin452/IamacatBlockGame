package fr.iamacat.iamacatblockgame.gamescreen;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
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
    public void update() {
        updateTitleScreen();
        updateButtons();
        updateInput();
        GLFW.glfwSwapBuffers(window);
        GLFW.glfwPollEvents();
    }
    private void bindTextureId(int textureId) {
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, textureId);
    }
    private void enableBlending() {
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void disableBlending() {
        GL46.glDisable(GL46.GL_BLEND);
    }
    private void updateTitleScreen() {
        renderTitleScreen();
    }

    private void updateButtons() {
        renderButtons();
    }

    private void updateInput() {
        handleButtonClicks();
    }
    private void loadTitleScreenTexture() {
        String titleScreenTexturePath = "textures/gamescreen/titlescreen.png";

        int textureID = GL46.glGenTextures();

        GL46.glActiveTexture(GL46.GL_TEXTURE0);
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, textureID);

        // Mipmapping
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_LINEAR);
        GL46.glGenerateMipmap(GL46.GL_TEXTURE_2D);

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(titleScreenTexturePath)) {
            if (inputStream == null) {
                throw new RuntimeException("Failed to load texture: " + titleScreenTexturePath);
            }

            BufferedImage image = ImageIO.read(inputStream);
            titleScreenTextureID = loadTexture(image);
            System.out.println("Title screen texture ID: " + titleScreenTextureID);
        } catch (IOException e) {
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
            return loadTexture(image);
        } catch (IOException e) {
            System.err.println("Failed to load texture: " + path);
            e.printStackTrace();
            throw new RuntimeException("Failed to load texture: " + path, e);
        }
    }

    private int loadTexture(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4); // 4 for RGBA components

        for (int pixel : pixels) {
            buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red component
            buffer.put((byte) ((pixel >> 8) & 0xFF));  // Green component
            buffer.put((byte) (pixel & 0xFF));         // Blue component
            buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha component
        }

        buffer.flip();

        int textureID = GL46.glGenTextures();
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, textureID);

        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_WRAP_S, GL46.GL_REPEAT);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_WRAP_T, GL46.GL_REPEAT);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_LINEAR);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_LINEAR);

        GL46.glTexImage2D(GL46.GL_TEXTURE_2D, 0, GL46.GL_RGBA, width, height, 0, GL46.GL_RGBA, GL46.GL_UNSIGNED_BYTE, buffer);

        GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);

        return textureID;
    }


    private void setupMesh() {

        float[] vertices = {
                -1.0f, -1.0f, 0.0f,
                1.0f, -1.0f, 0.0f,
                1.0f, 1.0f, 0.0f,
                -1.0f,  1.0f, 0.0f
        };

        float[] texCoords = {
                0.0f, 0.0f,
                1.0f, 0.0f,
                1.0f, 1.0f,
                0.0f, 1.0f
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
        GL46.glVertexAttribPointer(0, 3, GL46.GL_FLOAT, false, 0, 0);

        // Create the texture coordinate VBO and bind it
        texCoordVBOID = GL46.glGenBuffers();
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, texCoordVBOID);
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, texCoords, GL46.GL_STATIC_DRAW);
        GL46.glVertexAttribPointer(1, 2, GL46.GL_FLOAT, false, 0, 0);

        // Create the index VBO and bind it
        indexVBOID = GL46.glGenBuffers();
        GL46.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexVBOID);
        GL46.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL46.GL_STATIC_DRAW);

        // Unbind the VAO
        GL46.glBindVertexArray(0);

    }

    private void renderTitleScreen() {
        GL46.glActiveTexture(GL46.GL_TEXTURE0);

        bindTextureId(titleScreenTextureID);

        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);

        GL46.glBindVertexArray(vaoID);
        GL46.glEnableVertexAttribArray(0);
        GL46.glEnableVertexAttribArray(1); // Enable texture coordinates attribute

        GL46.glDrawElements(GL46.GL_TRIANGLES, 6, GL46.GL_UNSIGNED_INT, 0);

        GL46.glDisableVertexAttribArray(0);
        GL46.glDisableVertexAttribArray(1);
        GL46.glBindVertexArray(0);
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);

        GL46.glDisable(GL46.GL_BLEND);
    }

    private void renderButtons() {
        enableBlending();

        for (Button button : buttons) {
            bindTextureId(button.getTextureID());

            GL46.glBindVertexArray(vaoID);
            GL46.glEnableVertexAttribArray(0);
            GL46.glEnableVertexAttribArray(1); // Enable texture coordinates attribute

            GL46.glDrawElements(GL46.GL_TRIANGLES, 6, GL46.GL_UNSIGNED_INT, 0);

            GL46.glDisableVertexAttribArray(0);
            GL46.glDisableVertexAttribArray(1);
            GL46.glBindVertexArray(0);
            GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);
        }

        disableBlending();
    }


    private void handleButtonClicks() {
        double mouseX;
        double mouseY;

        double[] xpos = new double[1];
        double[] ypos = new double[1];

        GLFW.glfwGetCursorPos(window, xpos, ypos);
        mouseX = xpos[0];
        mouseY = ypos[0];
        // Check for mouse press
        int mouseButton = GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_LEFT);
        if (mouseButton == GLFW.GLFW_PRESS) {
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
}