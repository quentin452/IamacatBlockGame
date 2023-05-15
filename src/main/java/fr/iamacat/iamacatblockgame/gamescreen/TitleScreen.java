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

    private boolean gameStarted;  // Ajout d'un indicateur pour suivre si le jeu a déjà été démarré ou non
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
        gameStarted = false;  // Initialiser l'indicateur à false
    }

    private void loadTitleScreenTexture() {
        String titleScreenTexturePath = "textures/gamescreen/titlescreen.png";

        try {
            titleScreenTextureID = loadTextureFromResource(titleScreenTexturePath);
        } catch (Exception e) {
            System.err.println("Failed to load title screen texture: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createButtons() {
        //Create buttons and add them to the button list
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
            System.out.println("Loading texture from resource: " + path);
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
            if (inputStream == null) {
                System.err.println("Failed to load texture: " + path);
                throw new RuntimeException("Failed to load texture: " + path);
            }
            BufferedImage image = ImageIO.read(inputStream);
            TextureLoader textureLoader = new TextureLoader();
            int textureID = textureLoader.loadTexture(image);
            System.out.println("Texture loaded successfully. Texture ID: " + textureID);
            return textureID;
        } catch (IOException e) {
            System.err.println("Failed to load texture: " + path);
            e.printStackTrace();
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
                0.0f, 1.0f,  // Bottom-left texcoord
                1.0f, 1.0f,  // Bottom-right texcoord
                1.0f, 0.0f,  // Top-right texcoord
                0.0f, 0.0f   // Top-left texcoord
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
        vertexVBOID = GL46.glGenBuffers();
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vertexVBOID);
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(vertices.length).put(vertices).flip(), GL46.GL_STATIC_DRAW);
        GL46.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

        // Create the texture coordinate VBO and bind it
        texCoordVBOID = GL46.glGenBuffers();
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, texCoordVBOID);
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(texCoords.length).put(texCoords).flip(), GL46.GL_STATIC_DRAW);
        GL46.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);

        // Create the index VBO and bind it
        indexVBOID = GL46.glGenBuffers();
        GL46.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexVBOID);
        GL46.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, BufferUtils.createIntBuffer(indices.length).put(indices).flip(), GL46.GL_STATIC_DRAW);

        // Unbind the VAO
        GL46.glBindVertexArray(0);
    }

    public void update() {
        // Perform game logic and rendering for the title screen
        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT);
        renderTitleScreen();
        renderButtons();
        handleButtonClicks();
        GLFW.glfwSwapBuffers(window);
        GLFW.glfwPollEvents();
    }

    private void renderTitleScreen() {
        // Enable alpha blending
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);

        // Set the clear color to a visible color (black with alpha = 1)
        GL46.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // Clear the color buffer
        GL46.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);  // Black with full alpha (opaque)
        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT);

        // Bind the texture and VAO
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, titleScreenTextureID);
        GL46.glBindVertexArray(vaoID);

        // Enable vertex attribute arrays
        GL46.glEnableVertexAttribArray(0);
        GL46.glEnableVertexAttribArray(1);

        // Draw the quad
        GL46.glDrawElements(GL46.GL_TRIANGLES, 6, GL46.GL_UNSIGNED_INT, 0);

        // Disable vertex attribute arrays
        GL46.glDisableVertexAttribArray(0);
        GL46.glDisableVertexAttribArray(1);

        // Unbind the texture and VAO
        GL46.glBindVertexArray(0);
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);

        // Disable alpha blending
        GL46.glDisable(GL46.GL_BLEND);
    }

    private void renderButtons() {
        // Render the buttons on the title screen
        for (Button button : buttons) {
            GL46.glBindTexture(GL46.GL_TEXTURE_2D, button.getTextureID());
            button.render();
        }
    }

    private void handleButtonClicks() {
        // Handle button clicks
        double mouseX;
        double mouseY;

        // Get the current mouse position
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
                        // Exit the game
                        GLFW.glfwSetWindowShouldClose(window, true);
                    }
                }
            }
        }
    }
}