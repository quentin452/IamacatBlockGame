package fr.iamacat.iamacatblockgame.algorythme.chunkalgo;

import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class Block {
    private float height;
    private Texture texture;
    private Model model;

    private static final ModelBuilder modelBuilder = new ModelBuilder();
    private static final Material blockMaterial = new Material();
    private static final Vector3 playerPosition = new Vector3();

    public Block(float height) {
        this.height = height;
        // Load the texture or any other resources needed
        texture = new Texture("textures/blocks/block_texture.png");

        if (model == null) {
            modelBuilder.begin();
            modelBuilder.part("block", GL30.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates, blockMaterial).box(1f, height, 1f);
            model = modelBuilder.end();
        }
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
        if (texture != null) {
            texture.dispose();
            texture = null;
        }
        if (model != null) {
            model.dispose();
            model = null;
        }
    }
}
