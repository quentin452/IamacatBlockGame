package fr.iamacat.iamacatblockgame.gamescreen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

class Button {
        private String label;
        private float x;
        private float y;
        private float width;
        private float height;
        private Texture texture;
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

        public Button(String label, float x, float y, float width, float height, Texture texture) {
            this.label = label;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.texture = texture;
        }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }

    public boolean isClicked(float mouseX, float mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}