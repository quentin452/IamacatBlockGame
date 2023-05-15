package fr.iamacat.iamacatblockgame;

import fr.iamacat.iamacatblockgame.gamescreen.TitleScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryStack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Main {

    public static void main(String[] args) throws IOException {
        Log4jConfiguration.configure();
        new Main().run();
    }
    private static final Logger logger = LogManager.getLogger(Main.class);
    private long window;
    private boolean showTitleScreen;
    private TitleScreen titleScreen;

    public void run() throws IOException {
        logger.debug("Running the game...");
        init();
        showTitleScreen = true;
        titleScreen = new TitleScreen(window);
        loop();
        cleanup();
    }

    private void init() {
        // Initialize GLFW and create the window
        GLFW.glfwInit();
        window = GLFW.glfwCreateWindow(1280, 720, "IamACat Block Game", 0, 0);

        // Set window icon
        setWindowIcon();

        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwSwapInterval(1);

        // Set up mouse button callback for handling clicks
        GLFW.glfwSetMouseButtonCallback(window, new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if (showTitleScreen && action == GLFW.GLFW_PRESS && button == GLFW.GLFW_MOUSE_BUTTON_1) {
                    // Launch the game here
                    System.out.println("Launching the game...");
                    showTitleScreen = false;
                }
            }
        });

        GL.createCapabilities(); // Create the OpenGL context after making it current
    }


    private void setWindowIcon() {
        // Load the icon image
        BufferedImage iconImage;
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("textures/gamescreen/icon.png");
            if (inputStream == null) {
                throw new RuntimeException("Failed to load icon image: textures/gamescreen/icon.png");
            }
            iconImage = ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load icon image: textures/gamescreen/icon.png", e);
        }

        // Get the image dimensions
        int iconWidth = iconImage.getWidth();
        int iconHeight = iconImage.getHeight();

        // Get the pixel data from the image
        int[] pixels = new int[iconWidth * iconHeight];
        iconImage.getRGB(0, 0, iconWidth, iconHeight, pixels, 0, iconWidth);

        // Create the GLFWImage object for the icon
        GLFWImage.Buffer icons = GLFWImage.malloc(1);
        icons.position(0).width(iconWidth).height(iconHeight);

        // Set the pixel data for the icon
        ByteBuffer buffer = BufferUtils.createByteBuffer(iconWidth * iconHeight * 4);
        for (int y = 0; y < iconHeight; y++) {
            for (int x = 0; x < iconWidth; x++) {
                int pixel = pixels[y * iconWidth + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));  // Green component
                buffer.put((byte) (pixel & 0xFF));         // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha component
            }
        }
        buffer.flip();
        icons.pixels(buffer);

        // Set the window icon
        GLFW.glfwSetWindowIcon(window, icons);

        // Free the GLFWImage buffer
        icons.free();
    }

    private void loop() {
        while (!GLFW.glfwWindowShouldClose(window)) {
            if (showTitleScreen) {
                updateTitleScreen();
            } else {
                // Perform game logic and rendering for the main game
                GLFW.glfwPollEvents();
            }
        }
    }

    private void updateTitleScreen() {
        GL46.glClear(GL11.GL_COLOR_BUFFER_BIT);
        titleScreen.update();
        GLFW.glfwPollEvents();
    }

    private void cleanup() {
        // Clean up GLFW resources
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    }
}