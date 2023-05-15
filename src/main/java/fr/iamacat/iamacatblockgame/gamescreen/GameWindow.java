package fr.iamacat.iamacatblockgame.gamescreen;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class GameWindow {
    private long window;
    private int width;
    private int height;

    public GameWindow(int width, int height, String title) {
        this.width = width;
        this.height = height;

        init(title);
    }

    private void init(String title) {
        GLFW.glfwInit();

        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 6);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);

        window = GLFW.glfwCreateWindow(width, height, title, 0, 0);
        if (window == 0) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwShowWindow(window);

        GL.createCapabilities();

        GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void update() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        // Perform game rendering here

        GLFW.glfwSwapBuffers(window);
        GLFW.glfwPollEvents();
    }

    public void setWindowIcon(String iconPath) {
        try {
            BufferedImage iconImage = ImageIO.read(new File(iconPath));

            int width = iconImage.getWidth();
            int height = iconImage.getHeight();

            ByteBuffer iconBuffer = ByteBuffer.allocateDirect(width * height * 4);
            int[] pixels = iconImage.getRGB(0, 0, width, height, null, 0, width);
            for (int pixel : pixels) {
                iconBuffer.put((byte) ((pixel >> 16) & 0xFF)); // Red
                iconBuffer.put((byte) ((pixel >> 8) & 0xFF));  // Green
                iconBuffer.put((byte) (pixel & 0xFF));         // Blue
                iconBuffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha
            }
            iconBuffer.flip();

            GLFWImage.Buffer imageBuffer = GLFWImage.malloc(1);
            GLFWImage image = GLFWImage.create();
            image.set(width, height, iconBuffer);
            imageBuffer.put(image);
            imageBuffer.flip();

            GLFW.glfwSetWindowIcon(window, imageBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        GLFW.glfwTerminate();
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(window);
    }
}