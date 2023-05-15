package fr.iamacat.iamacatblockgame;

import fr.iamacat.iamacatblockgame.gamescreen.TitleScreen;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class Main {

    private long window;
    private boolean showTitleScreen;
    private TitleScreen titleScreen;

    public void run() {
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

        // Set the clear color to green
        GL.createCapabilities();
        GL11.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
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

    public static void main(String[] args) {
        new Main().run();
    }
}