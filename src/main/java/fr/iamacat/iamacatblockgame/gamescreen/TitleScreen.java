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
    private final SpriteBatch batch;
    private final Texture titleScreenTexture;
    private final OrthographicCamera camera;
    private final Button playButton;
    private final Button quitButton;
    private final Button optionsButton;

    private boolean gameStarted;

    public TitleScreen(SpriteBatch batch) {
        this.batch = batch;

        titleScreenTexture = new Texture("textures/gamescreen/titlescreen.png");

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(false);

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
        batch.draw(titleScreenTexture, 0, 0, camera.viewportWidth, camera.viewportHeight);
        batch.end();
    }

    public void renderButtons() {
        batch.setProjectionMatrix(camera.combined);
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
        Vector3 touchPos = new Vector3(screenX, screenY, 0);
        camera.unproject(touchPos);

        float mouseX = touchPos.x;
        float mouseY = touchPos.y;

        System.out.println("Clicked at: (" + mouseX + ", " + mouseY + ")");

        if (playButton.isClicked(mouseX, mouseY)) {
            System.out.println("Play button clicked");
            gameStarted = true;
            PlayButtonScreen playbuttonScreen = new PlayButtonScreen(batch);
            ((Game) Gdx.app.getApplicationListener()).setScreen(playbuttonScreen);
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

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        update();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.update();
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
        titleScreenTexture.dispose();
    }
}