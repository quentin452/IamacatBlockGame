package fr.iamacat.iamacatblockgame.gamescreen;

import org.lwjgl.opengl.GL11;

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
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glTranslatef(x, y, 0);

        // Draw the button quad
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2f(0, 0);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2f(width, 0);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2f(width, height);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2f(0, height);
        GL11.glEnd();

        // Restore the modelview matrix and unbind the texture
        GL11.glPopMatrix();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    public boolean isClicked(double mouseX, double mouseY) {
        // Check if the mouse coordinates are inside the button
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}
