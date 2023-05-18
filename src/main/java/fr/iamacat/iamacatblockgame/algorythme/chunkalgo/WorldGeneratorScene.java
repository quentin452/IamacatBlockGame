package fr.iamacat.iamacatblockgame.algorythme.chunkalgo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import fr.iamacat.iamacatblockgame.worldgen.core.WorldGenerator;

public class WorldGeneratorScene implements Screen {
    private PerspectiveCamera camera;
    private ModelBatch modelBatch;
    private Model model;
    private ModelInstance instance;
    private Environment environment;
    private CameraInputController cameraController;
    private WorldGenerator worldGenerator;
    private boolean shouldExit = false;

    public boolean shouldExit() {
        return shouldExit;
    }

    private void exit() {
        shouldExit = true;
    }

    public void create() {
        System.out.println("Creating WorldGeneratorScene...");

        modelBatch = new ModelBatch();
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(10f, 10f, 10f);
        camera.lookAt(0, 0, 0);
        camera.near = 1f;
        camera.far = 300f;
        camera.update();

        worldGenerator = new WorldGenerator(1000, 1000, 16, 16, 64);
        Chunk[][] chunks = worldGenerator.generateChunks();

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();

        for (int chunkX = 0; chunkX < chunks.length; chunkX++) {
            for (int chunkY = 0; chunkY < chunks[chunkX].length; chunkY++) {
                Chunk chunk = chunks[chunkX][chunkY];
                for (int x = 0; x < chunk.getWidth(); x++) {
                    for (int y = 0; y < chunk.getHeight(); y++) {
                        for (int z = 0; z < chunk.getLength(); z++) {
                            float blockHeight = chunk.getBlockHeight(x, y, z);
                            // Create a cube model using the modelBuilder
                            modelBuilder.part("box_" + x + "_" + y + "_" + z, GL20.GL_TRIANGLES,
                                            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
                                            new Material(ColorAttribute.createDiffuse(1f, 1f, 1f, 1f)))
                                    .box(1f, 1f, 1f);
                            modelBuilder.node().id = "box_" + x + "_" + y + "_" + z;
                            modelBuilder.node().translation.set(x, blockHeight, y);
                        }
                    }
                }
            }
        }

        Model blockModel = modelBuilder.end(); // End the modelBuilder and retrieve the model
        System.out.println("ModelBuilder ended.");

        instance = new ModelInstance(blockModel); // Create a new ModelInstance using the model
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));

        cameraController = new CameraInputController(camera);
        Gdx.input.setInputProcessor(cameraController);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);

        cameraController.update();
        modelBatch.begin(camera);
        modelBatch.render(instance, environment);
        modelBatch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            exit();
        }
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

    public void dispose() {
        modelBatch.dispose();
        model.dispose();
    }

    public void render() {
    }
}