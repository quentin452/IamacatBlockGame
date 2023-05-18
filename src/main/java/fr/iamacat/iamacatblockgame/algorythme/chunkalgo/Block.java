package fr.iamacat.iamacatblockgame.algorythme.chunkalgo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Block {
    private float height;

    public Block(float height) {
        this.height = height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getHeight() {
        return height;
    }

    public void render(SpriteBatch batch) {
        // Render the block using the provided SpriteBatch
    }
}