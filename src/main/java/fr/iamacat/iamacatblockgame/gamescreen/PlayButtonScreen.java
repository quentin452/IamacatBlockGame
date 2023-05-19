package fr.iamacat.iamacatblockgame.gamescreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import fr.iamacat.iamacatblockgame.algorythme.chunkalgo.Block;
import fr.iamacat.iamacatblockgame.algorythme.chunkalgo.Chunk;
import fr.iamacat.iamacatblockgame.scene.game.WorldGeneratorScene;
import fr.iamacat.iamacatblockgame.worldgen.core.WorldGenerator;

public class PlayButtonScreen implements Screen, InputProcessor {
    private final SpriteBatch batch;
    private final Texture optionsScreenTexture;
    private final TextButton backButton;
    private TextButton createWorldButton;
    private boolean vsyncEnabled;
    private final OrthographicCamera camera;
    private Stage stage;
    private CheckBox vsyncCheckbox;
    private Skin skin;
    private Slider fpsSlider;
    private Label fpsLabel;
    private float accumulator;
    private float frameTime;

    public PlayButtonScreen(SpriteBatch batch) {
        this.batch = batch;
        optionsScreenTexture = new Texture("textures/playbuttonscreen/playbuttonbackground.png");

        float windowWidth = Gdx.graphics.getWidth();
        float windowHeight = Gdx.graphics.getHeight();
        float aspectRatio = windowWidth / windowHeight;
        camera = new OrthographicCamera(windowWidth, windowHeight);
        skin = new Skin(Gdx.files.internal("assets/uis/skins/default/skin/uiskin.json"));
        backButton = new TextButton("Back", skin);
        backButton.setSize(100, 50);
        backButton.setPosition(100, 100);
        createWorldButton = new TextButton("Create a new world", skin);
        createWorldButton.setSize(200, 50);
        createWorldButton.setPosition(100, 200);
        createWorldButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Create world button clicked");

                // Create an instance of WorldGenerator
                WorldGenerator worldGenerator = new WorldGenerator(1000, 1000, 16, 16, 64);

                // Generate the blocks
                System.out.println("Generating blocks...");
                Block[][][] blocks = worldGenerator.generateBlocks();
                System.out.println("Blocks generated");

                // Generate the chunks
                System.out.println("Generating chunks...");
                Chunk[][] chunks = worldGenerator.generateChunks(blocks);
                System.out.println("Chunks generated");

                // Create an instance of WorldGeneratorScene and pass the generated chunks
                WorldGeneratorScene scene = new WorldGeneratorScene(chunks);

                // Set the game's screen to WorldGeneratorScene
                ((Game) Gdx.app.getApplicationListener()).setScreen(scene);
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Back button clicked");
                ((Game) Gdx.app.getApplicationListener()).setScreen(new TitleScreen(batch));
            }
        });
        vsyncEnabled = Gdx.graphics.isContinuousRendering();
        Gdx.input.setInputProcessor(this);
        stage = new Stage();

        stage.addActor(backButton);
        stage.addActor(createWorldButton); // Add createWorldButton to the stage
    }

    public void render(float delta) {
        update(delta); // Call the update method to update game logic
        renderOptionsScreen();
        stage.act(delta);
        stage.draw();
        Gdx.graphics.requestRendering(); // Request a new frame to be rendered
    }

    private void update(float delta) {
    }

    private void renderOptionsScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(optionsScreenTexture, 0, 0);
        batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPos = new Vector3(screenX, screenY, 0);
        camera.unproject(touchPos);
        float mouseX = touchPos.x;
        float mouseY = touchPos.y;
        System.out.println("Clicked at: (" + mouseX + ", " + mouseY + ")");
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
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

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height); // Update the camera's viewport when the screen is resized
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        optionsScreenTexture.dispose();
        stage.dispose();
        skin.dispose();
    }

    // Implement other InputProcessor methods (keyUp, keyDown, etc.) as needed
}