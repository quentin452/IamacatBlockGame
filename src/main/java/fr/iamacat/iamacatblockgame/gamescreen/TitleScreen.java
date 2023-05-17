package fr.iamacat.iamacatblockgame.gamescreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class TitleScreen implements Screen, InputProcessor {
    private SpriteBatch batch;
    private Texture titleScreenTexture;
    private float titleScreenX;
    private float titleScreenY;
    private float titleScreenWidth;
    private float titleScreenHeight;
    private OrthographicCamera camera;
    private Button playButton;
    private Button quitButton;
    private Button optionsButton;

    private boolean gameStarted;

    public TitleScreen(SpriteBatch batch) {
        this.batch = batch;

        titleScreenTexture = new Texture("textures/gamescreen/titlescreen.png");

        float windowWidth = Gdx.graphics.getWidth();
        float windowHeight = Gdx.graphics.getHeight();
        float aspectRatio = windowWidth / windowHeight;

        if (aspectRatio > 1) {
            titleScreenWidth = windowWidth;
            titleScreenHeight = windowHeight;
            titleScreenX = 0;
            titleScreenY = (windowHeight - titleScreenHeight) / 2;
        } else {
            titleScreenWidth = windowHeight;
            titleScreenHeight = windowHeight;
            titleScreenX = (windowWidth - titleScreenWidth) / 2;
            titleScreenY = 0;
        }

        playButton = new Button("Play", 100, 100, 100, 50,
                new Texture("textures/gamescreen/button/playbutton.png"));
        quitButton = new Button("Quit", 100, 200, 100, 50,
                new Texture("textures/gamescreen/button/quitbutton.png"));
        optionsButton = new Button("Options", 100, 300, 100, 50,
                new Texture("textures/gamescreen/button/optionsbutton.png"));

        Gdx.input.setInputProcessor(this);
    }

    public void update() {
        renderTitleScreen();
        renderButtons();
    }

    public void renderTitleScreen() {
        batch.begin();
        batch.draw(titleScreenTexture, titleScreenX, titleScreenY, titleScreenWidth, titleScreenHeight);
        batch.end();
    }

    public void renderButtons() {
        batch.begin();
        playButton.draw(batch);
        quitButton.draw(batch);
        optionsButton.draw(batch);
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
        // Convert screen coordinates to game coordinates
        Vector3 touchPos = new Vector3(screenX, screenY, 0);
        touchPos = camera.unproject(touchPos);

        float mouseX = touchPos.x;
        float mouseY = touchPos.y;

        System.out.println("Clicked at: (" + mouseX + ", " + mouseY + ")");

        if (playButton.isClicked(mouseX, mouseY)) {
            System.out.println("Play button clicked");
            gameStarted = true;
            return true;
        }
        if (quitButton.isClicked(mouseX, mouseY)) {
            System.out.println("Quit button clicked");
            Gdx.app.exit();
            return true;
        }
        if (optionsButton.isClicked(mouseX, mouseY)) {
            System.out.println("Options button clicked");
            OptionsScreen optionsScreen = new OptionsScreen(batch);
            ((Game) Gdx.app.getApplicationListener()).setScreen(optionsScreen);
            return true;
        }

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

    // Other InputProcessor methods...

    @Override
    public void show() {
        // Called when this screen becomes the current screen.
    }

    @Override
    public void render(float delta) {
        // Render the screen.
        update();
    }

    @Override
    public void resize(int width, int height) {
        // Update the title screen dimensions based on the new window size
        float aspectRatio = (float) width / height;

        if (aspectRatio > 1) {
            titleScreenWidth = width;
            titleScreenHeight = height;
            titleScreenX = 0;
            titleScreenY = (height - titleScreenHeight) / 2;
        } else {
            titleScreenWidth = height;
            titleScreenHeight = height;
            titleScreenX = (width - titleScreenWidth) / 2;
            titleScreenY = 0;
        }
    }

    @Override
    public void pause() {
        // Called when the game is paused.
    }

    @Override
    public void resume() {
        // Called when the game is resumed from a paused state.
    }

    @Override
    public void hide() {
        // Called when this screen is no longer the current screen.
    }

    @Override
    public void dispose() {
        // Dispose of any disposable resources.
        titleScreenTexture.dispose();
    }
}