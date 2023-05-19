package fr.iamacat.iamacatblockgame.gamescreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class OptionsScreen extends ScreenAdapter implements InputProcessor {
    private final SpriteBatch batch;
    private final Texture optionsScreenTexture;
    private final TextButton backButton;
    private boolean vsyncEnabled;
    private final OrthographicCamera camera;
    private Stage stage;
    private CheckBox vsyncCheckbox;
    private Skin skin;
    private Slider fpsSlider;
    private Label fpsLabel;
    private float fpsTimer;
    private float fpsLimit;

    public OptionsScreen(SpriteBatch batch) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        this.batch = batch;
        optionsScreenTexture = new Texture("textures/optionscreen/optionbackground.png");
        float windowWidth = Gdx.graphics.getWidth();
        float windowHeight = Gdx.graphics.getHeight();
        float aspectRatio = windowWidth / windowHeight;
        camera = new OrthographicCamera(windowWidth, windowHeight);
        skin = new Skin(Gdx.files.internal("assets/uis/skins/default/skin/uiskin.json"));
        backButton = new TextButton("Back", skin);
        backButton.setSize(100, 50);
        backButton.setPosition(100, 100);
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

        vsyncCheckbox = new CheckBox("VSync", skin);
        vsyncCheckbox.setPosition(100, 200);
        vsyncCheckbox.setSize(100, 50);
        vsyncCheckbox.setChecked(vsyncEnabled);
        vsyncCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                vsyncEnabled = vsyncCheckbox.isChecked();
                Gdx.graphics.setVSync(vsyncEnabled);
            }
        });

        fpsSlider = new Slider(30, 400, 1, false, skin);
        fpsSlider.setPosition(100, 300);

        config.foregroundFPS = (int) fpsSlider.getValue();

        fpsSlider.setSize(200, 50);
        fpsSlider.setValue(400); // Set the initial FPS limit here (e.g., 400)
        fpsSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int fpsValue = (int) fpsSlider.getValue();
                fpsLimit = 1f / fpsValue;
                Gdx.graphics.setVSync(vsyncEnabled);
                config.foregroundFPS = fpsValue; // Move this line here to update the config value
                updateFPSLabel();
            }
        });

        fpsLabel = new Label("", skin);
        fpsLabel.setPosition(310, 300);
        fpsLabel.setSize(50, 50);
        fpsTimer = 0f;
        fpsLimit = 1f / 400f;
        updateFPSLabel();

        stage.addActor(vsyncCheckbox);
        stage.addActor(fpsSlider);
        stage.addActor(fpsLabel);
        stage.addActor(backButton);
    }

    public void render(float delta) {
        // Render the frame
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(optionsScreenTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.act(delta);
        stage.draw();
    }


    private void updateFPSLabel() {
        int targetFPS = (int) (1f / fpsLimit);
        fpsLabel.setText(targetFPS + " FPS");
    }

    private void renderOptionsScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(optionsScreenTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
    }

    // Implement other methods and InputProcessor interface as needed...
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
}
