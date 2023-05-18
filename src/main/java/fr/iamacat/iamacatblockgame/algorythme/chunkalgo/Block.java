package fr.iamacat.iamacatblockgame.algorythme.chunkalgo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Block {
    private float height;
    private Texture texture;

    public Block(float height) {
        this.height = height;
        // Load the texture or any other resources needed
        texture = new Texture("textures/blocks/block_texture.png");
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void render(SpriteBatch batch) {
        // Render the block using the texture and appropriate logic
        batch.draw(texture, 0, 0);
    }

    public void dispose() {
        // Dispose of any resources associated with the block
        texture.dispose();
    }
}