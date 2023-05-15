package fr.iamacat.iamacatblockgame.gamescreen;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class TitleScreen {

    private long window;

    public TitleScreen(long window) {
        this.window = window;
    }

    public void update() {
        // Perform game logic and rendering for the title screen
        GL11.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
        // Render title screen content
        // ...
        // Swap buffers and poll events
        GLFW.glfwSwapBuffers(window);
        GLFW.glfwPollEvents();
    }
}