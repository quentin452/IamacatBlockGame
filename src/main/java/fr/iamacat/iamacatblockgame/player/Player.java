package fr.iamacat.iamacatblockgame.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Player {
    private Vector2 position;
    private Vector2 size;
    private Texture skin;

    public Player() {
        position = new Vector2();
        size = new Vector2();
        skin = new Texture("textures/skins/player_skin.png");
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setSize(float width, float height) {
        size.set(width, height);
    }

    public void render(SpriteBatch batch, Camera camera) {
        float x = position.x - camera.getPosition().x;
        float y = position.y - camera.getPosition().y;

        batch.begin();
        batch.draw(skin, x, y, size.x, size.y);
        batch.end();
    }
}