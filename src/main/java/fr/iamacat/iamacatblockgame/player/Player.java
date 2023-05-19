package fr.iamacat.iamacatblockgame.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class Player {
    private Vector3 position;
    private Vector3 size;
    private Texture skin;

    public Player() {
        position = new Vector3();
        size = new Vector3();
        skin = new Texture("textures/skins/player_skin.png");
    }

    public void setPosition(float x, float y) {
        position.set(x, y, 0);  // Set z-coordinate to 0
    }

    public Vector3 getPosition() {
        return position;
    }

    public void render(SpriteBatch batch, Camera camera) {
        float x = position.x - camera.getPosition().x;
        float y = position.y - camera.getPosition().y;

        batch.begin();
        batch.draw(skin, x, y, size.x, size.y);
        batch.end();
    }
}