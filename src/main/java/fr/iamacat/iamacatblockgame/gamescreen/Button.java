package fr.iamacat.iamacatblockgame.gamescreen;

import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL46;

public class Button {
    private String text;
    private int x;
    private int y;
    private int width;
    private int height;
    private int textureID;

    public Button(String text, int x, int y, int width, int height, int textureID) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.textureID = textureID;
    }

    public int getTextureID() {
        return textureID;
    }

    public String getText() {
        return text;
    }

    public void render() {
        GL46.glEnable(GL46.GL_TEXTURE_2D); // Enable 2D texturing
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, textureID); // Bind the texture

        // Draw the button quad with texture coordinates
        GL46.glBegin(GL46.GL_QUADS);
        GL46.glTexCoord2f(0, 0);
        GL46.glVertex2f(x, y);
        GL46.glTexCoord2f(1, 0);
        GL46.glVertex2f(x + width, y);
        GL46.glTexCoord2f(1, 1);
        GL46.glVertex2f(x + width, y + height);
        GL46.glTexCoord2f(0, 1);
        GL46.glVertex2f(x, y + height);
        GL46.glEnd();

        GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0); // Unbind the texture
        GL46.glDisable(GL46.GL_TEXTURE_2D); // Disable 2D texturing
    }

    public boolean isClicked(double mouseX, double mouseY) {
        // Check if the mouse coordinates are inside the button
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}