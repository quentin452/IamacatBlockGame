package fr.iamacat.iamacatblockgame.gamescreen;

import org.lwjgl.opengl.GL11;
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

    public boolean isClicked(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}