package fr.iamacat.iamacatblockgame.algorythme.chunkalgo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
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
                            // Create a cube model instance at the corresponding position and height
                            Model blockModel = modelBuilder.createBox(1f, 1f, 1f,
                                    new Material(ColorAttribute.createDiffuse(1f, 1f, 1f, 1f)),
                                    VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
                            ModelInstance blockInstance = new ModelInstance(blockModel);
                            blockInstance.transform.setToTranslation(x, blockHeight, y);
                            modelBatch.begin(camera); // Begin modelBatch before rendering the block instance
                            modelBatch.render(blockInstance, environment);
                        }
                    }
                }
            }
        }
        modelBuilder.end(); // End the modelBuilder after all blocks have been rendered

        instance = new ModelInstance(model);
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));

        cameraController = new CameraInputController(camera);
        Gdx.input.setInputProcessor(cameraController);
    }

    public void render() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);

        cameraController.update();
        modelBatch.begin(camera);
        modelBatch.render(instance, environment);
        modelBatch.end();
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) { // #todo
            exit();
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

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
}
