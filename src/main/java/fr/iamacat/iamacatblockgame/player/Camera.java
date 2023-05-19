package fr.iamacat.iamacatblockgame.player;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

public class Camera {
    private OrthographicCamera camera;
    private Vector2 position;
    private Vector2 size;

    public Camera(float screenWidth, float screenHeight) {
        position = new Vector2();
        size = new Vector2(screenWidth, screenHeight);
        camera = new OrthographicCamera(screenWidth, screenHeight);
        camera.position.set(screenWidth / 2, screenHeight / 2, 0);
        camera.update();
    }

    public void update(Player player) {
        position.set(player.getPosition().x - size.x / 2, player.getPosition().y - size.y / 2);
        camera.position.set(position.x + size.x / 2, position.y + size.y / 2, 0);
        camera.update();
    }

    public Vector2 getPosition() {
        return position;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}