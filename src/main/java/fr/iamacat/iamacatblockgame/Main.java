package fr.iamacat.iamacatblockgame;

import fr.iamacat.iamacatblockgame.gamescreen.TitleScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Main {

    public static void main(String[] args) {
        Log4jConfiguration.configure();
        logger.info("Starting the game...");
        new Main().run();
    }
    private static final Logger logger = LogManager.getLogger(Main.class);
    private long window;
    private boolean showTitleScreen;
    private TitleScreen titleScreen;

    public void run() {
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
                if (showTitleScreen && action == GLFW.GLFW_PRESS) {
                    // Launch the game here
                    System.out.println("Launching the game...");
                    showTitleScreen = false;
                }
            }
        });
       GL.createCapabilities();
    }
    private void setWindowIcon() {
        // Load the icon image
        ByteBuffer iconImageBuffer = loadIconImage("textures/gamescreen/icon.png");

        // Create GLFWImage object for the icon
        GLFWImage.Buffer icons = GLFWImage.create(1);
        icons.position(0).width(16).height(16).pixels(iconImageBuffer);

        // Set the window icon
        GLFW.glfwSetWindowIcon(window, icons);
    }


    private ByteBuffer loadIconImage(String path) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            // Load the icon image using STBImage
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
            if (inputStream == null) {
                throw new RuntimeException("Failed to load icon image: " + path);
            }
            BufferedImage image = ImageIO.read(inputStream);
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();
            int imageChannels = image.getRaster().getNumBands();
            ByteBuffer imageBuffer = ByteBuffer.allocateDirect(imageWidth * imageHeight * imageChannels);
            int[] imagePixels = image.getRGB(0, 0, imageWidth, imageHeight, null, 0, imageWidth);
            // Réorganiser les pixels dans l'ordre RGBA attendu par OpenGL
            for (int i = 0; i < imageWidth * imageHeight; i++) {
                int pixel = imagePixels[i];
                byte r = (byte) ((pixel >> 16) & 0xFF);
                byte g = (byte) ((pixel >> 8) & 0xFF);
                byte b = (byte) (pixel & 0xFF);
                byte a = (byte) ((pixel >> 24) & 0xFF);
                imageBuffer.put(r).put(g).put(b).put(a);
            }
            imageBuffer.flip();
            imageBuffer.flip();


            width.put(0, imageWidth);
            height.put(0, imageHeight);
            channels.put(0, imageChannels);

            return imageBuffer;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load icon image: " + path, e);
        }
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
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        titleScreen.update();
        GLFW.glfwPollEvents();
    }
    private void cleanup() {
        // Clean up GLFW resources
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    }
}