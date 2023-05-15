package fr.iamacat.iamacatblockgame.gamescreen;

import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL46;

public class Button {
    private String text;
    private int x;
    private int y;
    private int width; // Added width variable
    private int height; // Added height variable
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
        // Bind the texture and set up the rendering position
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, textureID);
        GL46.glMatrixMode(GL46.GL_MODELVIEW);
        GL46.glPushMatrix();
        GL46.glLoadIdentity();
        GL46.glTranslatef(x, y, 0);

        // Draw the button quad
        GL46.glBegin(GL46.GL_QUADS);
        GL46.glTexCoord2f(0, 0);
        GL46.glVertex2f(0, 0);
        GL46.glTexCoord2f(1, 0);
        GL46.glVertex2f(width, 0);
        GL46.glTexCoord2f(1, 1);
        GL46.glVertex2f(width, height);
        GL46.glTexCoord2f(0, 1);
        GL46.glVertex2f(0, height);
        GL46.glEnd();

        // Restore the modelview matrix and unbind the texture
        GL46.glPopMatrix();
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);
    }

    public boolean isClicked(double mouseX, double mouseY) {
        // Check if the mouse coordinates are inside the button
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}