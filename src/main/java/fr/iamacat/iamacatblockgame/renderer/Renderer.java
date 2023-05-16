package fr.iamacat.iamacatblockgame.renderer;

import fr.iamacat.iamacatblockgame.gamescreen.Button;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL46;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

public class Renderer {

    private static int vaoID;
    private static int vertexVBOID;
    private static int texCoordVBOID;
    private static int indexVBOID;
    private static boolean isTitleScreenDisplayed = false;

    public static void renderTitleScreen(int textureID, int vaoIDParam) {
        GL46.glActiveTexture(GL46.GL_TEXTURE0);
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, textureID);

        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);

        GL46.glBindVertexArray(vaoIDParam);
        GL46.glEnableVertexAttribArray(0);
        GL46.glEnableVertexAttribArray(1);

        GL46.glDrawElements(GL46.GL_TRIANGLES, 6, GL46.GL_UNSIGNED_INT, 0);

        GL46.glDisableVertexAttribArray(0);
        GL46.glDisableVertexAttribArray(1);
        GL46.glBindVertexArray(0);
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);

        GL46.glDisable(GL46.GL_BLEND);

        isTitleScreenDisplayed = true;

        deleteResources();
    }

    private static void bindTextureId(int textureId) {
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, textureId);
    }

    public static void renderButtons(List<Button> buttons) {
        enableBlending();

        if (vaoID == 0) {
            createVAO();
            GL46.glBindVertexArray(vaoID);
        } else {
            GL46.glBindVertexArray(vaoID);
        }

        // Create and bind the vertex and texture coordinate buffers outside the loop
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vertexVBOID);
        GL46.glVertexAttribPointer(0, 3, GL46.GL_FLOAT, false, 0, 0);

        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, texCoordVBOID);
        GL46.glVertexAttribPointer(1, 2, GL46.GL_FLOAT, false, 0, 0);

        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(6); // Create an IntBuffer for indices

        for (Button button : buttons) {
            int[] indices = {
                    0, 1, 2,    // First triangle (top-right, top-left, bottom-right)
                    2, 3, 0     // Second triangle (bottom-right, bottom-left, top-left)
            };

            indicesBuffer.clear(); // Clear the buffer before adding new data
            indicesBuffer.put(indices).flip(); // Put the indices data into the buffer and flip it

            bindTextureId(button.getTextureID());

            GL46.glEnableVertexAttribArray(0);
            GL46.glEnableVertexAttribArray(1);

            GL46.glDrawElements(GL46.GL_TRIANGLES, indicesBuffer);

            GL46.glDisableVertexAttribArray(0);
            GL46.glDisableVertexAttribArray(1);
        }

        // Unbind the buffers after the loop
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
        GL46.glBindVertexArray(0);
        disableBlending();
        isTitleScreenDisplayed = false;

        deleteResources();
    }

    private static void createVAO() {
        vaoID = GL46.glGenVertexArrays();

        GL46.glBindVertexArray(vaoID);

        vertexVBOID = GL46.glGenBuffers();
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vertexVBOID);

        float[] vertices = {
                -0.5f, 0.5f, 0.0f,    // Top-left vertex
                0.5f, 0.5f, 0.0f,     // Top-right vertex
                0.5f, -0.5f, 0.0f,    // Bottom-right vertex
                -0.5f, -0.5f, 0.0f    // Bottom-left vertex
        };

        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuffer.put(vertices).flip();

        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, verticesBuffer, GL46.GL_STATIC_DRAW);
        GL46.glVertexAttribPointer(0, 3, GL46.GL_FLOAT, false, 0, 0);

        texCoordVBOID = GL46.glGenBuffers();
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, texCoordVBOID);

        float[] texCoords = {
                0.0f, 1.0f,    // Top-left vertex
                1.0f, 1.0f,    // Top-right vertex
                1.0f, 0.0f,    // Bottom-right vertex
                0.0f, 0.0f     // Bottom-left vertex
        };

        FloatBuffer texCoordsBuffer = BufferUtils.createFloatBuffer(texCoords.length);
        texCoordsBuffer.put(texCoords).flip();

        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, texCoordsBuffer, GL46.GL_STATIC_DRAW);
        GL46.glVertexAttribPointer(1, 2, GL46.GL_FLOAT, false, 0, 0);

        indexVBOID = GL46.glGenBuffers();
        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, indexVBOID);

        int[] indices = {
                0, 1, 2,    // First triangle (top-right, top-left, bottom-right)
                2, 3, 0     // Second triangle (bottom-right, bottom-left, top-left)
        };

        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
        indicesBuffer.put(indices).flip();

        GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, indicesBuffer.capacity() * 4);

        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
        GL46.glBindVertexArray(0);
    }

    private static void enableBlending() {
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);
    }

    private static void disableBlending() {
        GL46.glDisable(GL46.GL_BLEND);
    }

    public static void deleteResources() {
        if (!isTitleScreenDisplayed) {
            deleteVAOAndVBOs();
        }
    }

    private static void deleteVAOAndVBOs() {
        if (vaoID != 0) {
            GL46.glDeleteVertexArrays(vaoID);
            vaoID = 0;
        }
        if (vertexVBOID != 0) {
            GL46.glDeleteBuffers(vertexVBOID);
            vertexVBOID = 0;
        }

        if (texCoordVBOID != 0) {
            GL46.glDeleteBuffers(texCoordVBOID);
            texCoordVBOID = 0;
        }

        if (indexVBOID != 0) {
            GL46.glDeleteBuffers(indexVBOID);
            indexVBOID = 0;
        }
    }
}