package fr.iamacat.iamacatblockgame.gamescreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class OptionsScreen implements Screen, InputProcessor {
    private SpriteBatch batch;
    private Texture optionsScreenTexture;
    private Button vsyncButton;
    private Button backButton;
    private boolean vsyncEnabled;
    private OrthographicCamera camera; // Added camera variable

    public OptionsScreen(SpriteBatch batch) {
        this.batch = batch;

        optionsScreenTexture = new Texture("textures/optionscreen/optionbackground.png");

        float windowWidth = Gdx.graphics.getWidth();
        float windowHeight = Gdx.graphics.getHeight();
        float aspectRatio = windowWidth / windowHeight;

        camera = new OrthographicCamera(); // Initialize the camera
        camera.setToOrtho(false, windowWidth, windowHeight); // Set the camera's viewport

        backButton = new Button("Back", 100, 100, 100, 50,
                new Texture("textures/optionscreen/button/backbutton.png"));

        vsyncButton = new Button("VSync", 100, 200, 100, 50,
                new Texture("textures/optionscreen/button/vsyncbutton.png"));

        vsyncEnabled = Gdx.graphics.isContinuousRendering();

        Gdx.input.setInputProcessor(this);
    }

    public void update() {
    }

    public void renderOptionsScreen() {
        batch.begin();
        batch.draw(optionsScreenTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
    }

    public void renderButtons() {
        batch.begin();
        backButton.draw(batch);
        vsyncButton.draw(batch);
        batch.end();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Convert screen coordinates to game coordinates
        Vector3 touchPos = new Vector3(screenX, screenY, 0);
        camera.unproject(touchPos); // Use the camera to unproject the coordinates

        float mouseX = touchPos.x;
        float mouseY = touchPos.y;

        System.out.println("Clicked at: (" + mouseX + ", " + mouseY + ")");

        if (backButton.isClicked(mouseX, mouseY)) {
            System.out.println("Back button clicked");
            ((Game) Gdx.app.getApplicationListener()).setScreen(new TitleScreen(batch));
            return true;
        }

        if (vsyncButton.isClicked(mouseX, mouseY)) {
            System.out.println("VSync button clicked");
            vsyncEnabled = !vsyncEnabled;
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

    }
    @Override
    public void render(float delta) {
        // Render the screen.
        renderOptionsScreen();
        renderButtons();
    }

    @Override
    public void resize(int width, int height) {
        // Update the option screen dimensions based on the new window size
        float aspectRatio = (float) width / height;

        camera.setToOrtho(false, width, height); // Update the camera's viewport

        if (aspectRatio > 1) {
            backButton.setPosition(100, 100);
            vsyncButton.setPosition(100, 200);
        } else {
            backButton.setPosition(100, 100);
            vsyncButton.setPosition(100, 200);
        }
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
        // Dispose of any disposable resources.
        optionsScreenTexture.dispose();
    }
}