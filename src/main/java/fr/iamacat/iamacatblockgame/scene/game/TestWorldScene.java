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
    private float viewDistance = 5f; // Initial view distance in chunks
    private float viewDistanceIncrement = 1f; // Amount to increase view distance per second

    private static final float TERRAIN_SIZE = 10f;
    private static final float MOVEMENT_SPEED = 5f;
    private static final float FIELD_OF_VIEW = 60f;

    private SpriteBatch batch;
    private PerspectiveCamera camera;
    private ModelBatch modelBatch;
    private Environment environment;
    private Player player;
    private ModelInstance terrain;
    private ModelInstance otherObject;
    private Chunk[][] chunks;
    private float elapsedTime = 0f;

    private static Model terrainModel;
    private static Model objectModel;

    public TestWorldScene(SpriteBatch batch, Chunk[][] chunks) {
        this.batch = batch;
        this.chunks = chunks;
        create();
    }

    private void create() {
        modelBatch = new ModelBatch();
        environment = createEnvironment();

        if (terrainModel == null) {
            ModelBuilder modelBuilder = new ModelBuilder();
            modelBuilder.begin();
            modelBuilder.node().id = "terrain";
            modelBuilder.part("terrain", GL30.GL_TRIANGLES, Usage.Position | Usage.Normal,
                            new Material(ColorAttribute.createDiffuse(Color.GREEN)))
                    .box(TERRAIN_SIZE, 1f, TERRAIN_SIZE);
            terrainModel = modelBuilder.end();
        }
        terrain = new ModelInstance(terrainModel);

        if (objectModel == null) {
            ModelBuilder modelBuilder = new ModelBuilder();
            modelBuilder.begin();
            modelBuilder.node().id = "object";
            modelBuilder.part("object", GL30.GL_TRIANGLES, Usage.Position | Usage.Normal,
                            new Material(ColorAttribute.createDiffuse(Color.YELLOW)))
                    .box(2f, 2f, 2f);
            objectModel = modelBuilder.end();
        }
        otherObject = new ModelInstance(objectModel);

        for (Chunk[] row : chunks) {
            for (Chunk chunk : row) {
                chunk.createModelInstance(player.getPosition(), (int) viewDistance);
            }
        }
    }

    @Override
    public void show() {
        float fieldOfView = FIELD_OF_VIEW;
        camera = new PerspectiveCamera(fieldOfView, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0f, 5f, 10f);
        camera.lookAt(0f, 0f, 0f);
        camera.near = 0.1f;
        camera.far = 100f;
        camera.update();

        player = new Player(new Vector3(0f, 0f, 0f));
        player.setPosition(0f, 0f, 0f);

        modelBatch = new ModelBatch();
        environment = createEnvironment();
    }

    private Environment createEnvironment() {
        Environment env = new Environment();
        env.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1f));
        env.add(new DirectionalLight().set(1f, 1f, 1f, -1f, -0.8f, -0.2f));
        return env;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);

        for (Chunk[] row : chunks) {
            for (Chunk chunk : row) {
                modelBatch.render(chunk.getModelInstance(), environment);
            }
        }

        viewDistance = Math.min(viewDistance + viewDistanceIncrement * delta, 32f);

        updateChunkLoading();
        handleInput();

        float movementSpeed = MOVEMENT_SPEED;
        Vector3 movement = new Vector3();

        if (Gdx.input.isKeyPressed(Input.Keys.Z)) { // Use Z for moving forward
            movement.set(camera.direction).nor().scl(movementSpeed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) { // Use S for moving backward
            movement.set(camera.direction).scl(-1).nor().scl(movementSpeed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) { // Use Q for moving left
            movement.set(camera.up).crs(camera.direction).nor().scl(-movementSpeed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) { // Use D for moving right
            movement.set(camera.up).crs(camera.direction).nor().scl(movementSpeed * delta);
        }

        player.move(movement);

        float mouseSensitivity = 0.2f;
        float deltaX = Gdx.input.getDeltaX() * mouseSensitivity;
        float deltaY = Gdx.input.getDeltaY() * mouseSensitivity;

        float yaw = -deltaX;
        float pitch = -deltaY;

        camera.direction.rotate(Vector3.Y, yaw);

        Vector3 horizontalRotationAxis = camera.up.cpy().crs(camera.direction).nor();
        camera.direction.rotate(horizontalRotationAxis, pitch);

        camera.direction.y = 0;
        camera.direction.nor();

        camera.position.set(player.getPosition());
        camera.lookAt(camera.position.cpy().add(camera.direction));
        camera.update();

        modelBatch.begin(camera);
        modelBatch.render(terrain, environment);
        player.render(modelBatch, environment);
        modelBatch.render(otherObject, environment);
        modelBatch.end();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    private void updateChunkLoading() {
        Vector3 playerPosition = player.getPosition();

        for (Chunk[] row : chunks) {
            for (Chunk chunk : row) {
                if (!chunk.isLoaded() && !chunkIsWithinViewDistance(chunk)) {
                    chunk.unloadChunk();
                } else if (chunk.isLoaded() && chunkIsWithinViewDistance(chunk)) {
                    chunk.createModelInstance(playerPosition, (int) viewDistance);
                }
            }
        }
    }

    private boolean chunkIsWithinViewDistance(Chunk chunk) {
        Vector3 center = chunk.getCenterPoint();
        float distance = player.getPosition().dst(center);
        return distance <= viewDistance;
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            System.out.println("ESCAPE key pressed");
            ((Game) Gdx.app.getApplicationListener()).setScreen(new TestWorldScreen(batch));
        }
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        player.dispose();
        terrainModel.dispose();
        objectModel.dispose();
    }
}
