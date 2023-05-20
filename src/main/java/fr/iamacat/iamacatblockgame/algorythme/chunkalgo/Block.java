package fr.iamacat.iamacatblockgame.algorythme.chunkalgo;

import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class Block {
    private float height;
    private Texture texture;
    private Model model;

    public Block(float height) {
        this.height = height;
        // Load the texture or any other resources needed
        texture = new Texture("textures/blocks/block_texture.png");

        // Create the 3D model for the block
        ModelBuilder modelBuilder = new ModelBuilder();

        modelBuilder.begin();
        modelBuilder.part("block", GL30.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates, new Material()).box(1f, height, 1f);
        model = modelBuilder.end();
    }

    public Model getModel() {
        return model;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void dispose() {
        texture.dispose();
    }
}