package fr.iamacat.iamacatblockgame.gamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TitleScreen implements InputProcessor {
    private SpriteBatch batch;
    private Texture titleScreenTexture;
    private float titleScreenX;
    private float titleScreenY;
    private float titleScreenWidth;
    private float titleScreenHeight;

    private Button playButton;
    private Button quitButton;
    private Button optionsButton; // Added options button

    private boolean gameStarted;

    public TitleScreen(SpriteBatch batch) {
        this.batch = batch;

        titleScreenTexture = new Texture("textures/gamescreen/titlescreen.png");

        // Calculate the position and size of the title screen based on the window's aspect ratio
        float windowWidth = Gdx.graphics.getWidth();
        float windowHeight = Gdx.graphics.getHeight();
        float aspectRatio = windowWidth / windowHeight;

        // Set the title screen size and position
        if (aspectRatio > 1) {
            // Landscape aspect ratio
            titleScreenWidth = windowWidth;
            titleScreenHeight = windowHeight;
            titleScreenX = 0;
            titleScreenY = (windowHeight - titleScreenHeight) / 2;
        } else {
            // Portrait aspect ratio
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

        Gdx.input.setInputProcessor(this); // Set this class as the input processor
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
        optionsButton.draw(batch); // Added rendering for options button
        batch.end();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        float mouseX = screenX;
        float mouseY = Gdx.graphics.getHeight() - screenY;

        if (playButton.isClicked(mouseX, mouseY)) {
            gameStarted = true;
            return true; // Consume the touch event
        }
        if (quitButton.isClicked(mouseX, mouseY)) {
            Gdx.app.exit();
            return true; // Consume the touch event
        }
        if (optionsButton.isClicked(mouseX, mouseY)) { // Handle options button click
            // Open OptionsScreen or perform related actions
            OptionsScreen optionsScreen = new OptionsScreen(batch);
            // Perform any additional actions, such as setting the current screen to optionsScreen
            return true; // Consume the touch event
        }

        return false; // Continue processing the touch event
    }

    // Other InputProcessor methods
    // ...

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
}
