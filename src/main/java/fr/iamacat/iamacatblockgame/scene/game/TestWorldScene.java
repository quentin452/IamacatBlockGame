package fr.iamacat.iamacatblockgame.scene.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import fr.iamacat.iamacatblockgame.player.Player;

public class TestWorldScene implements Screen {
    private PerspectiveCamera camera;
    private ModelBatch modelBatch;
    private Environment environment;
    private ModelInstance playerModelInstance;
    private Player player;
    private ModelInstance terrain;
    private ModelInstance otherObject;

    @Override
    public void show() {
        // Set up the camera with appropriate settings
        float fieldOfView = 60f; // Set the desired field of view angle
        camera = new PerspectiveCamera(fieldOfView, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0f, 0f, 10f); // Adjusted camera position
        camera.lookAt(0f, 0f, 0f);
        camera.near = 0.1f; // Set the near clipping plane distance
        camera.far = 100f; // Set the far clipping plane distance
        camera.update();

        // Initialize the player and other 3D objects
        player = new Player(new Vector3(0f, 0f, 0f));
        player.setPosition(0f, 0f, 0f);

        // Initialize other required variables
        modelBatch = new ModelBatch();
        environment = createEnvironment();

        // Create a terrain model
        Model terrainModel = createTerrainModel();
        terrain = new ModelInstance(terrainModel);

        // Create another 3D object model
        Model objectModel = createObjectModel();
        otherObject = new ModelInstance(objectModel);
    }

    private Environment createEnvironment() {
        Environment env = new Environment();
        env.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1f));
        env.add(new DirectionalLight().set(1f, 1f, 1f, -1f, -0.8f, -0.2f)); // Improved lighting
        return env;
    }

    private Model createTerrainModel() {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        modelBuilder.node().id = "terrain";
        modelBuilder.part("terrain", GL30.GL_TRIANGLES, Usage.Position | Usage.Normal,
                        new Material(ColorAttribute.createDiffuse(Color.GREEN)))
                .box(10f, 1f, 10f); // Adjusted terrain size
        return modelBuilder.end();
    }

    private Model createObjectModel() {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        modelBuilder.node().id = "object";
        modelBuilder.part("object", GL30.GL_TRIANGLES, Usage.Position | Usage.Normal,
                        new Material(ColorAttribute.createDiffuse(Color.RED)))
                .box(2f, 2f, 2f); // Added a more complex 3D object
        return modelBuilder.end();
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);

        // Handle input
        handleInput();

        // Update the player position based on input
        float movementSpeed = 5f;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            player.setPosition(player.getPosition().x, player.getPosition().y + movementSpeed * Gdx.graphics.getDeltaTime(), player.getPosition().z);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            player.setPosition(player.getPosition().x, player.getPosition().y - movementSpeed * Gdx.graphics.getDeltaTime(), player.getPosition().z);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.setPosition(player.getPosition().x - movementSpeed * Gdx.graphics.getDeltaTime(), player.getPosition().y, player.getPosition().z);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.setPosition(player.getPosition().x + movementSpeed * Gdx.graphics.getDeltaTime(), player.getPosition().y, player.getPosition().z);
        }

        // Update the camera's position based on player position
        camera.position.set(player.getPosition());
        camera.update();

        // Render 3D models using ModelBatch
        modelBatch.begin(camera);
        modelBatch.render(terrain, environment);
        player.render(modelBatch, environment);
        modelBatch.render(otherObject, environment);
        modelBatch.end();
    }

    private void handleInput() {
        // Handle other input if needed
    }

    @Override
    public void resize(int width, int height) {
        // Update the camera's viewport when the screen is resized
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    // Implement other Screen interface methods as needed

    @Override
    public void dispose() {
        // Dispose of resources
        modelBatch.dispose();
        player.dispose();
        terrain.model.dispose();
        otherObject.model.dispose();
    }
}
