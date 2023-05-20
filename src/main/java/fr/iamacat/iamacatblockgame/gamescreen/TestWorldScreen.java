package fr.iamacat.iamacatblockgame.gamescreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.ResourceData;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import fr.iamacat.iamacatblockgame.algorythme.chunkalgo.Chunk;
import fr.iamacat.iamacatblockgame.savesystem.SaveData;
import fr.iamacat.iamacatblockgame.savesystem.SaveSystem;
import fr.iamacat.iamacatblockgame.player.Player;
import fr.iamacat.iamacatblockgame.scene.game.TestWorldScene;
import fr.iamacat.iamacatblockgame.settings.WorldSettings;

public class TestWorldScreen implements Screen, InputProcessor {
    private SpriteBatch batch;
    private Texture TestWorldTexture;
    private TextButton ExitButton;
    private TextButton ResumeWorld;
    private OrthographicCamera camera;
    private Stage stage;
    private Skin skin;
    private Player player;
    private Chunk[][] chunks = new Chunk[WorldSettings.GENERATED_WORLD_WIDTH][WorldSettings.GENERATED_WORLD_HEIGHT];
    private TestWorldScene testWorldScene;

    public TestWorldScreen(SpriteBatch batch) {
        this.batch = batch;

        TestWorldTexture = new Texture("textures/testworldscreen/playbuttonbackground.png");

        float windowWidth = Gdx.graphics.getWidth();
        float windowHeight = Gdx.graphics.getHeight();
        float aspectRatio = windowWidth / windowHeight;
        camera = new OrthographicCamera(windowWidth, windowHeight);
        skin = new Skin(Gdx.files.internal("assets/uis/skins/default/skin/uiskin.json"));
        ExitButton = new TextButton("Exit World", skin);
        ExitButton.setSize(100, 50);
        ExitButton.setPosition(100, 100);
        ResumeWorld = new TextButton("Resume World", skin);
        ResumeWorld.setSize(200, 50);
        ResumeWorld.setPosition(100, 200);
        ResumeWorld.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Resume world button clicked");
                // Load the saved world data #todo
            //    SaveData saveData = SaveSystem.loadData();
           //     player = saveData.getPlayer();
          //      testWorldScene = saveData.getTestWorldScene();

                // Switch to the TestWorldScene
                ((Game) Gdx.app.getApplicationListener()).setScreen(new TestWorldScene(batch,chunks));
            }
        });

        ExitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Back button clicked");
                ((Game) Gdx.app.getApplicationListener()).setScreen(new TitleScreen(batch));
            }
        });
        Gdx.input.setInputProcessor(this);
        stage = new Stage();

        stage.addActor(ExitButton);
        stage.addActor(ResumeWorld);
    }

    public void render(float delta) {
        update(delta); // Call the update method to update game logic
        renderTestWorldScreen();

        // Render 2D UI
        stage.getViewport().apply();
        stage.getCamera().update();
        stage.act(delta);
        stage.draw();

        // Request next frame render
        Gdx.graphics.requestRendering();
    }

    private void update(float delta) {
    }

    private void renderTestWorldScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(TestWorldTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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

        // Bounds checks to restrict the player's movement within the generated world bounds
        if (mouseX < 0 || mouseX >= WorldSettings.GENERATED_WORLD_WIDTH || mouseY < 0 || mouseY >= WorldSettings.GENERATED_WORLD_HEIGHT) {
            return false;
        }

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
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height); // Update the camera's viewport when the screen is resized
        stage.getViewport().update(width, height, true);
    }

    @Override
    // #todo
    public void hide() {
        // Save data when the world is exited
   //     SaveSystem.saveData(player, testWorldScene);
    }

    @Override
    // #todo
    public void show() {
        // Load the save data
   /*     SaveData saveData = SaveSystem.loadData();
        if (saveData != null) {
            player = saveData.getPlayer();
            testWorldScene = saveData.getTestWorldScene();
        } else {
            // If no save data is found, create new instances of player and testWorldScene
            Vector3 playerPosition = new Vector3(0, 0, 0); // Provide the appropriate position values
            player = new Player(playerPosition);
            testWorldScene = new TestWorldScene(batch);
        }

        // Save data when opening the TestWorldScreen
        SaveSystem.saveData(player, testWorldScene);

    */
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void dispose() {
        TestWorldTexture.dispose();
        stage.dispose();
        skin.dispose();
    }

    // Implement other InputProcessor methods (keyUp, keyDown, etc.) as needed
}
