package fr.iamacat.iamacatblockgame.scene.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import fr.iamacat.iamacatblockgame.algorythme.chunkalgo.Chunk;
import fr.iamacat.iamacatblockgame.gamescreen.TestWorldScreen;
import fr.iamacat.iamacatblockgame.player.Player;

public class TestWorldScene implements Screen {

    private float viewDistance = 5f; // Initial view distance in chunks , IMPORTANT VALUE
    private float viewDistanceIncrement = 1f; // Amount to increase view distance per second

    private SpriteBatch batch;
    private PerspectiveCamera camera;
    private ModelBatch modelBatch;
    private Environment environment;
    private CameraInputController cameraController;
    private Player player;
    private ModelInstance terrain;
    private ModelInstance otherObject;
    private Model terrainModel;
    private Model objectModel;
    private Chunk[][] chunks;
    private float elapsedTime = 0f;

    public TestWorldScene(SpriteBatch batch, Chunk[][] chunks) {
        this.batch = batch;
        this.chunks = chunks;

        // Initialize the player object
        player = new Player(new Vector3(0f, 0f, 0f));

        // Call the create() method after initializing the player object
        create();
    }

    private void create() {
        modelBatch = new ModelBatch();
        environment = createEnvironment();

        for (Chunk[] row : chunks) {
            for (Chunk chunk : row) {
                chunk.createModelInstance(player.getPosition(), (int) viewDistance);
            }
        }
    }

    @Override
    public void show() {

        // Set up the camera with appropriate settings
        float fieldOfView = 60f; // Set the desired field of view angle
        camera = new PerspectiveCamera(fieldOfView, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0f, 5f, 10f); // Adjusted camera position
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
        if (terrainModel == null) {
            ModelBuilder modelBuilder = new ModelBuilder();
            modelBuilder.begin();
            modelBuilder.node().id = "terrain";
            modelBuilder.part("terrain", GL30.GL_TRIANGLES, Usage.Position | Usage.Normal,
                            new Material(ColorAttribute.createDiffuse(Color.GREEN)))
                    .box(10f, 1f, 10f); // Adjusted terrain size
            terrainModel = modelBuilder.end();
        }
        terrain = new ModelInstance(terrainModel);

        // Create another 3D object model
        if (objectModel == null) {
            ModelBuilder modelBuilder = new ModelBuilder();
            modelBuilder.begin();
            modelBuilder.node().id = "object";
            modelBuilder.part("object", GL30.GL_TRIANGLES, Usage.Position | Usage.Normal,
                            new Material(ColorAttribute.createDiffuse(Color.YELLOW)))
                    .box(2f, 2f, 2f); // Added a more complex 3D object
            objectModel = modelBuilder.end();
        }
        otherObject = new ModelInstance(objectModel);
        otherObject.transform.setToTranslation(3f, 0.5f, 0f); // Adjusted position of the other object
    }

    private Environment createEnvironment() {
        Environment env = new Environment();
        env.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1f));
        env.add(new DirectionalLight().set(1f, 1f, 1f, -1f, -0.8f, -0.2f)); // Improved lighting
        return env;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Clear the screen
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);

        for (Chunk[] row : chunks) {
            for (Chunk chunk : row) {
                modelBatch.render(chunk.getModelInstance(), environment);
            }
        }

        // Update the view distance over time
        elapsedTime += delta;
        viewDistance = Math.min(viewDistance + viewDistanceIncrement * elapsedTime, 32f);

        // Update the view distance for each chunk
        for (Chunk[] row : chunks) {
            for (Chunk chunk : row) {
                chunk.createModelInstance(player.getPosition(), (int) viewDistance);
            }
        }

        // Handle input
        handleInput();

        // Update the player position based on input
        float movementSpeed = 5f;
        if (Gdx.input.isKeyPressed(Input.Keys.Z)) { // Use Z for moving forward
            player.setPosition(player.getPosition().x + camera.direction.x * movementSpeed * Gdx.graphics.getDeltaTime(),
                    player.getPosition().y + camera.direction.y * movementSpeed * Gdx.graphics.getDeltaTime(),
                    player.getPosition().z + camera.direction.z * movementSpeed * Gdx.graphics.getDeltaTime());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) { // Use S for moving backward
            player.setPosition(player.getPosition().x - camera.direction.x * movementSpeed * Gdx.graphics.getDeltaTime(),
                    player.getPosition().y - camera.direction.y * movementSpeed * Gdx.graphics.getDeltaTime(),
                    player.getPosition().z - camera.direction.z * movementSpeed * Gdx.graphics.getDeltaTime());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) { // Use Q for moving left
            Vector3 rightDirection = new Vector3(camera.direction).crs(camera.up).nor();
            player.setPosition(player.getPosition().x - rightDirection.x * movementSpeed * Gdx.graphics.getDeltaTime(),
                    player.getPosition().y - rightDirection.y * movementSpeed * Gdx.graphics.getDeltaTime(),
                    player.getPosition().z - rightDirection.z * movementSpeed * Gdx.graphics.getDeltaTime());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) { // Use D for moving right
            Vector3 rightDirection = new Vector3(camera.direction).crs(camera.up).nor();
            player.setPosition(player.getPosition().x + rightDirection.x * movementSpeed * Gdx.graphics.getDeltaTime(),
                    player.getPosition().y + rightDirection.y * movementSpeed * Gdx.graphics.getDeltaTime(),
                    player.getPosition().z + rightDirection.z * movementSpeed * Gdx.graphics.getDeltaTime());
        }

        // Handle mouse input
        float mouseSensitivity = 0.2f;
        float deltaX = Gdx.input.getDeltaX() * mouseSensitivity;
        float deltaY = Gdx.input.getDeltaY() * mouseSensitivity;

        // Calculate the rotation angles based on the mouse input
        float yaw = -deltaX;
        float pitch = -deltaY;

        // Rotate the camera around the player based on the rotation angles
        camera.rotateAround(player.getPosition(), Vector3.Y, yaw);

        // Calculate the horizontal rotation axis perpendicular to the camera's up vector
        Vector3 horizontalRotationAxis = new Vector3(camera.up).crs(camera.direction).nor();

        // Rotate the camera around the horizontal rotation axis based on the pitch angle
        camera.rotateAround(player.getPosition(), horizontalRotationAxis, pitch);

        // Update the camera's direction based on the rotated angles
        camera.direction.rotate(Vector3.Y, yaw);

        // Reset the camera's pitch to prevent tilting
        camera.direction.y = 0;
        camera.direction.nor();

        // Update the camera's position and lookAt target
        camera.position.set(player.getPosition());
        camera.lookAt(camera.position.cpy().add(camera.direction));
        camera.update();

        // Render 3D models using ModelBatch
        modelBatch.begin(camera);
        modelBatch.render(terrain, environment);
        player.render(modelBatch, environment);
        modelBatch.render(otherObject, environment);
        modelBatch.end();
    }

    @Override
    public void resize(int width, int height) {

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

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            System.out.println("ESCAPE key pressed");
            ((Game) Gdx.app.getApplicationListener()).setScreen(new TestWorldScreen(batch));
        }
        // Handle other input if needed
    }

    @Override
    public void dispose() {
        // Dispose of resources
        modelBatch.dispose();
        player.dispose();
        terrainModel.dispose();
        objectModel.dispose();
    }
}