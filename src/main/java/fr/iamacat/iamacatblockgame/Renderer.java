package fr.iamacat.iamacatblockgame;

import fr.iamacat.iamacatblockgame.gamescreen.Button;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL46;

import java.util.List;

public class Renderer {
    private List<Button> buttons;
    public static void renderTitleScreen(int textureID, int vaoID) {
        GL46.glActiveTexture(GL46.GL_TEXTURE0);
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, textureID);

        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);

        GL46.glBindVertexArray(vaoID);
        GL46.glEnableVertexAttribArray(0);
        GL46.glEnableVertexAttribArray(1);

        GL46.glActiveTexture(GL46.GL_TEXTURE0);
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, textureID);
        GL46.glDrawElements(GL46.GL_TRIANGLES, 6, GL46.GL_UNSIGNED_INT, 0);

        GL46.glDisableVertexAttribArray(0);
        GL46.glDisableVertexAttribArray(1);
        GL46.glBindVertexArray(0);
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);

        GL46.glDisable(GL46.GL_BLEND);
    }
    private static void bindTextureId(int textureId) {
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, textureId);
    }
    public static void renderButtons(List<Button> buttons) {
        enableBlending();

        for (Button button : buttons) {
            float[] vertices = {
                    // Define the button's vertices here
                    // Example: x, y, z coordinates of each vertex
                    -0.5f, 0.5f, 0.0f,    // Top-left vertex
                    0.5f, 0.5f, 0.0f,     // Top-right vertex
                    0.5f, -0.5f, 0.0f,    // Bottom-right vertex
                    -0.5f, -0.5f, 0.0f    // Bottom-left vertex
            };

            float[] texCoords = {
                    // Define the button's texture coordinates here
                    // Example: u, v coordinates of each vertex
                    0.0f, 1.0f,    // Top-left vertex
                    1.0f, 1.0f,    // Top-right vertex
                    1.0f, 0.0f,    // Bottom-right vertex
                    0.0f, 0.0f     // Bottom-left vertex
            };

            int[] indices = {
                    // Define the indices for the triangles here
                    // Example: indices that specify the order of vertices to form triangles
                    0, 1, 2,    // First triangle (top-right, top-left, bottom-right)
                    2, 3, 0     // Second triangle (bottom-right, bottom-left, top-left)
            };
            // Create and bind the VAO
            int vaoID = GL30.glGenVertexArrays();
            GL30.glBindVertexArray(vaoID);

            // Create the vertex VBO and bind it
            int vertexVBOID = GL46.glGenBuffers();
            GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vertexVBOID);
            GL46.glBufferData(GL46.GL_ARRAY_BUFFER, vertices, GL46.GL_STATIC_DRAW);
            GL46.glVertexAttribPointer(0, 3, GL46.GL_FLOAT, false, 0, 0);

            // Create the texture coordinate VBO and bind it
            int texCoordVBOID = GL46.glGenBuffers();
            GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, texCoordVBOID);
            GL46.glBufferData(GL46.GL_ARRAY_BUFFER, texCoords, GL46.GL_STATIC_DRAW);
            GL46.glVertexAttribPointer(1, 2, GL46.GL_FLOAT, false, 0, 0);

            // Create the index VBO and bind it
            int indexVBOID = GL46.glGenBuffers();
            GL46.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexVBOID);
            GL46.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL46.GL_STATIC_DRAW);

            // Bind the texture
            bindTextureId(button.getTextureID());

            // Enable vertex attributes
            GL46.glEnableVertexAttribArray(0);
            GL46.glEnableVertexAttribArray(1); // Enable texture coordinates attribute

            // Draw the button
            GL46.glDrawElements(GL46.GL_TRIANGLES, 6, GL46.GL_UNSIGNED_INT, 0);

            // Disable vertex attributes
            GL46.glDisableVertexAttribArray(0);
            GL46.glDisableVertexAttribArray(1);

            // Unbind the VAO and texture
            GL46.glBindVertexArray(0);
            GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);

            // Delete the VAO and VBOs
            GL30.glDeleteVertexArrays(vaoID);
            GL46.glDeleteBuffers(vertexVBOID);
            GL46.glDeleteBuffers(texCoordVBOID);
            GL46.glDeleteBuffers(indexVBOID);
        }

        disableBlending();
    }

    private static void enableBlending() {
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);
    }

    private static void disableBlending() {
        GL46.glDisable(GL46.GL_BLEND);
    }
}