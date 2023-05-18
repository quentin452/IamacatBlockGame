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
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import fr.iamacat.iamacatblockgame.worldgen.core.WorldGenerator;

import com.badlogic.gdx.utils.Array;
import java.util.ArrayList;
import java.util.List;

public class WorldGeneratorScene implements Screen {
    private PerspectiveCamera camera;
    private ModelBatch modelBatch;
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
        Block[][][] blocks = worldGenerator.generateBlocks();

        List<ModelInstance> instances = new ArrayList<>();

        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[x].length; y++) {
                for (int z = 0; z < blocks[x][y].length; z++) {
                    Block block = blocks[x][y][z];

                    if (block != null) {
                        ModelBuilder modelBuilder = new ModelBuilder();
                        modelBuilder.begin();
                        modelBuilder.node().id = "block_" + x + "_" + y + "_" + z;
                        modelBuilder.part("part1", GL20.GL_TRIANGLES,
                                        VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
                                        new Material(ColorAttribute.createDiffuse(1f, 1f, 1f, 1f)))
                                .box(1f, 1f, 1f);
                        Model blockModel = modelBuilder.end();
                        ModelInstance blockInstance = new ModelInstance(blockModel, new Vector3(x, y, z));
                        instances.add(blockInstance);
                    }
                }
            }
        }

        instance = new ModelInstance(new Model()); // Create an empty ModelInstance

        Node parentNode = new Node();
        for (ModelInstance modelInstance : instances) {
            Node node = new Node();
            node.translation.set(modelInstance.transform.getTranslation(Vector3.Zero));
            node.rotation.set(modelInstance.transform.getRotation(new Quaternion()));
            node.scale.set(modelInstance.transform.getScale(new Vector3(1f, 1f, 1f)));
            node.parts.addAll(modelInstance.model.nodes.get(0).parts);
            parentNode.addChild(node);
        }
        instance.nodes.add(parentNode);

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

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
    }

    public void render() {
    }
}