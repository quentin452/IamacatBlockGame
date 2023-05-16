package fr.iamacat.iamacatblockgame.gamescreen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Button {
    private int x;
    private int y;
    private Texture texture;
    private Rectangle bounds;
    private String text;
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public Texture getTexture() {
        return texture;
    }

    public Button(String text, int x, int y, int width, int height, Texture texture) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.text = text;

        bounds = new Rectangle(x, y, width, height);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, bounds.x, bounds.y);
    }

    public boolean isClicked(float mouseX, float mouseY) {
        return bounds.contains(mouseX, mouseY);
    }

    // Getters and setters
}