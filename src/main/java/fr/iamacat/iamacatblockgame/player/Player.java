package fr.iamacat.iamacatblockgame.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class Player {
    private Vector3 position;
    private ModelInstance modelInstance;

    public Player(Vector3 position) {
        this.position = position;
        ModelBuilder modelBuilder = new ModelBuilder();
        Model model = modelBuilder.createSphere(0.5f, 0.5f, 0.5f, 16, 16,
                new Material(ColorAttribute.createDiffuse(Color.RED)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        modelInstance = new ModelInstance(model);
        modelInstance.transform.setToTranslation(position);
    }

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
        modelInstance.transform.setToTranslation(position);
    }

    public Vector3 getPosition() {
        return position;
    }

    public void move(Vector3 movement) {
        position.add(movement);
        modelInstance.transform.setToTranslation(position);
    }

    public void render(ModelBatch modelBatch, Environment environment) {
        modelBatch.render(modelInstance, environment);
    }

    public void dispose() {
        // Dispose of resources
        modelInstance.model.dispose();
    }
}
