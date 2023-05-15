package fr.iamacat.iamacatblockgame.gamescreen;

import fr.iamacat.iamacatblockgame.textures.TextureLoader;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

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
    }

    private void loadTitleScreenTexture() {
        String titleScreenTexturePath = "textures/gamescreen/titlescreen.png";
        titleScreenTextureID = loadTextureFromResource(titleScreenTexturePath);
    }

    private void createButtons() {
        // Create buttons and add them to the button list
        String playButtonTexturePath = "textures/gamescreen/button/playbutton.png";
        int playButtonTextureID = loadTextureFromResource(playButtonTexturePath);
        String quitButtonTexturePath = "textures/gamescreen/button/quitbutton.png";
        int quitButtonTextureID = loadTextureFromResource(quitButtonTexturePath);
        int buttonWidth = 100; // Set the desired width for the buttons
        int buttonHeight = 50; // Set the desired height for the buttons
        Button playButton = new Button("Join", 100, 100, buttonWidth, buttonHeight, playButtonTextureID);
        Button quitButton = new Button("Exit", 100, 200, buttonWidth, buttonHeight, quitButtonTextureID);
        buttons.add(playButton);
        buttons.add(quitButton);
    }

    private int loadTextureFromResource(String path) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
            if (inputStream == null) {
                throw new RuntimeException("Failed to load texture: " + path);
            }
            BufferedImage image = ImageIO.read(inputStream);
            TextureLoader textureLoader = new TextureLoader();
            return textureLoader.loadTexture(image);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load texture: " + path, e);
        }
    }

    private void setupMesh() {
        // Set up the vertices for a full-screen quad
        float[] vertices = {
                -1.0f, -1.0f, 0.0f,  // Bottom-left vertex
                1.0f, -1.0f, 0.0f,  // Bottom-right vertex
                1.0f,  1.0f, 0.0f,  // Top-right vertex
                -1.0f,  1.0f, 0.0f   // Top-left vertex
        };

        // Set up the texture coordinates
        float[] texCoords = {
                0.0f, 0.0f,  // Bottom-left texcoord
                1.0f, 0.0f,  // Bottom-right texcoord
                1.0f, 1.0f,  // Top-right texcoord
                0.0f, 1.0f   // Top-left texcoord
        };

        // Set up the indices for the quad
        int[] indices = {
                0, 1, 2,  // First triangle
                2, 3, 0   // Second triangle
        };

        // Create and bind the VAO
        vaoID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoID);

        // Create the vertex VBO and bind it
        vertexVBOID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexVBOID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(vertices.length).put(vertices).flip(), GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

        // Create the texture coordinate VBO and bind it
        texCoordVBOID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, texCoordVBOID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(texCoords.length).put(texCoords).flip(), GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);

        // Create the index VBO and bind it
        indexVBOID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexVBOID);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, BufferUtils.createIntBuffer(indices.length).put(indices).flip(), GL15.GL_STATIC_DRAW);

        // Unbind the VAO
        GL30.glBindVertexArray(0);
    }

    public void update() {
        logger.info("Updating title screen...");
        // Perform game logic and rendering for the title screen
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        renderTitleScreen();
        renderButtons();
        handleButtonClicks();
        GLFW.glfwSwapBuffers(window);
        GLFW.glfwPollEvents();
    }

    private void renderTitleScreen() {
        // Bind the texture and VAO
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, titleScreenTextureID);
        GL30.glBindVertexArray(vaoID);

        // Draw the quad
        GL11.glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_INT, 0);

        // Unbind the texture and VAO
        GL30.glBindVertexArray(0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    private void renderButtons() {
        // Render the buttons on the title screen
        for (Button button : buttons) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, button.getTextureID());
            button.render();
        }
    }

    private void handleButtonClicks() {
        // Handle button clicks
        double mouseX = 0.0;
        double mouseY = 0.0;

        // Get the current mouse position
        double[] xpos = new double[1];
        double[] ypos = new double[1];
        GLFW.glfwGetCursorPos(window, xpos, ypos);
        mouseX = xpos[0];
        mouseY = ypos[0];

        for (Button button : buttons) {
            if (button.isClicked(mouseX, mouseY)) {
                if (button.getText().equals("Join")) {
                    // Start the game
                    System.out.println("Starting the game...");
                } else if (button.getText().equals("Exit")) {
                    // Exit the game
                    GLFW.glfwSetWindowShouldClose(window, true);
                }
            }
        }
    }
}