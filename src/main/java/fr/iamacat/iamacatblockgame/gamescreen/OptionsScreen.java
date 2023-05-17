package fr.iamacat.iamacatblockgame.gamescreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class OptionsScreen implements Screen, InputProcessor {
    private final SpriteBatch batch;
    private final Texture optionsScreenTexture;
    private final TextButton backButton;
    private boolean vsyncEnabled;
    private final OrthographicCamera camera;
    private Stage stage;
    private CheckBox vsyncCheckbox;
    private Skin skin;

    public OptionsScreen(SpriteBatch batch) {
        this.batch = batch;

        optionsScreenTexture = new Texture("textures/optionscreen/optionbackground.png");

        float windowWidth = Gdx.graphics.getWidth();
        float windowHeight = Gdx.graphics.getHeight();
        float aspectRatio = windowWidth / windowHeight;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, windowWidth, windowHeight);

        backButton = new TextButton("Back", skin);
        backButton.setPosition(100, 100);
        backButton.setSize(100, 50);

        vsyncEnabled = Gdx.graphics.isContinuousRendering();

        Gdx.input.setInputProcessor(this);
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("assets/uis/skins/default/skin/uiskin.json"));

        vsyncCheckbox = new CheckBox("VSync", skin);
        vsyncCheckbox.setPosition(100, 200);
        vsyncCheckbox.setSize(100, 50);
        vsyncCheckbox.setChecked(vsyncEnabled);
        stage.addActor(vsyncCheckbox);
    }

    public void renderOptionsScreen() {
        batch.begin();
        batch.draw(optionsScreenTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
    }

    public void renderButtons() {
        batch.begin();
        backButton.draw(batch, 1f); // Pass 1f as the second argument to specify full opacity
        batch.end();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPos = new Vector3(screenX, screenY, 0);
        camera.unproject(touchPos);

        float mouseX = touchPos.x;
        float mouseY = touchPos.y;

        System.out.println("Clicked at: (" + mouseX + ", " + mouseY + ")");

        if (backButton.isPressed()) {
            System.out.println("Back button clicked");
            ((Game) Gdx.app.getApplicationListener()).setScreen(new TitleScreen(batch));
            return true;
        }

        if (vsyncCheckbox.isOver()) {
            System.out.println("VSync checkbox clicked");
            vsyncEnabled = vsyncCheckbox.isChecked();
            Gdx.graphics.setContinuousRendering(vsyncEnabled);
            return true;
        }

        return false;
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
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        renderOptionsScreen();
        renderButtons();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
    public void dispose() {
        optionsScreenTexture.dispose();
        stage.dispose();
    }
}