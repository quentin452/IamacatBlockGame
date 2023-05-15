package fr.iamacat.iamacatblockgame;

import fr.iamacat.iamacatblockgame.gamescreen.GameWindow;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class Main {

    private long window;
    private boolean showTitleScreen;

    public void run() {
        init();
        showTitleScreen = true;
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
    }

    private void loop() {
        while (!GLFW.glfwWindowShouldClose(window) && showTitleScreen) {
            // Perform game logic and rendering for the title screen
            GLFW.glfwPollEvents();
        }

        while (!GLFW.glfwWindowShouldClose(window)) {
            // Perform game logic and rendering for the main game
            GLFW.glfwPollEvents();
        }
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