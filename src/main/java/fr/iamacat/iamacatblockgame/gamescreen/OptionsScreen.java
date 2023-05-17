package fr.iamacat.iamacatblockgame.gamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
public class OptionsScreen implements InputProcessor {
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private float backgroundX;
    private float backgroundY;
    private float backgroundWidth;
    private float backgroundHeight;

    private Button backButton;
    private CheckBox vsyncCheckbox;
    private CheckBox fpsCheckbox;

    private boolean vsyncEnabled;
    private boolean unlimitedFPS;

    public OptionsScreen(SpriteBatch batch) {
        this.batch = batch;

        // Load the background texture
        backgroundTexture = new Texture("textures/optionscreen/optionbackground.png");

        // Calculate the position and size of the background based on the window's aspect ratio
        float windowWidth = Gdx.graphics.getWidth();
        float windowHeight = Gdx.graphics.getHeight();
        float aspectRatio = windowWidth / windowHeight;

        // Set the background size and position
        if (aspectRatio > 1) {
            // Landscape aspect ratio
            backgroundWidth = windowWidth;
            backgroundHeight = windowHeight;
            backgroundX = 0;
            backgroundY = (windowHeight - backgroundHeight) / 2;
        } else {
            // Portrait aspect ratio
            backgroundWidth = windowHeight;
            backgroundHeight = windowHeight;
            backgroundX = (windowWidth - backgroundWidth) / 2;
            backgroundY = 0;
        }

        Skin skin = new Skin();

        // Load the font using FreeTypeFontGenerator
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("assets/font/french/Rubik-Black.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 16;  // Set the font size
        fontParameter.borderWidth = 2;  // Set the border width
        // Set other desired parameters such as color, shadow, etc.

        BitmapFont font = fontGenerator.generateFont(fontParameter);

        // Dispose the generator after generating the font
        fontGenerator.dispose();

        // Register the font with the name "default" in the skin
        skin.add("default", font);

        // Register the CheckBoxStyle with the name "default"
        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle();
        checkBoxStyle.checkboxOn = new TextureRegionDrawable(new TextureRegion(new Texture("textures/optionscreen/checkbox_on.png")));
        checkBoxStyle.checkboxOff = new TextureRegionDrawable(new TextureRegion(new Texture("textures/optionscreen/checkbox_off.png")));
        checkBoxStyle.font = skin.getFont("default"); // Use the registered "default" font
        skin.add("default", checkBoxStyle);

        backButton = new Button("Back", 100, 100, 100, 50,
                new Texture("textures/optionscreen/button/backbutton.png"));
        vsyncCheckbox = new CheckBox("VSync", skin); // Use the created Skin instance
        fpsCheckbox = new CheckBox("Unlimited FPS", skin); // Use the created Skin instance

        // Set checkbox values based on initial settings
        vsyncCheckbox.setChecked(vsyncEnabled);
        fpsCheckbox.setChecked(unlimitedFPS);

        Gdx.input.setInputProcessor(this); // Set this class as the input processor
    }

    public void update() {
        renderBackground();
        renderOptions();
    }

    public void renderBackground() {
        batch.begin();
        batch.draw(backgroundTexture, backgroundX, backgroundY, backgroundWidth, backgroundHeight);
        batch.end();
    }

    public void renderOptions() {
        batch.begin();
        backButton.draw(batch);
        vsyncCheckbox.draw(batch, 1f); // Specify alpha value for transparency
        fpsCheckbox.draw(batch, 1f); // Specify alpha value for transparency
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
        float mouseX = screenX;
        float mouseY = Gdx.graphics.getHeight() - screenY;

        if (backButton.isClicked(mouseX, mouseY)) {
            // Handle back button click (return to previous screen or exit options)
            return true; // Consume the touch event
        }
        if (vsyncCheckbox.isChecked() && isMouseOverCheckBox(vsyncCheckbox, mouseX, mouseY)) {
            vsyncEnabled = !vsyncEnabled;
            Gdx.graphics.setVSync(vsyncEnabled);
            return true; // Consume the touch event
        }
        if (fpsCheckbox.isChecked() && isMouseOverCheckBox(fpsCheckbox, mouseX, mouseY)) {
            unlimitedFPS = !unlimitedFPS;
            if (unlimitedFPS) {
                Gdx.graphics.setContinuousRendering(true);
                Gdx.graphics.requestRendering();
            } else {
                Gdx.graphics.setContinuousRendering(false);
            }
            return true; // Consume the touch event
        }

        return false; // Continue processing the touch event
    }

    private boolean isMouseOverCheckBox(CheckBox checkBox, float mouseX, float mouseY) {
        float checkBoxX = checkBox.getX();
        float checkBoxY = checkBox.getY();
        float checkBoxWidth = checkBox.getWidth();
        float checkBoxHeight = checkBox.getHeight();

        return mouseX >= checkBoxX && mouseX <= checkBoxX + checkBoxWidth
                && mouseY >= checkBoxY && mouseY <= checkBoxY + checkBoxHeight;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // Implement touch up logic here if needed
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // Implement touch dragged logic here if needed
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

    // Other InputProcessor methods
    // ...
}