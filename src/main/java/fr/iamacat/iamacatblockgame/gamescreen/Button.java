package fr.iamacat.iamacatblockgame.gamescreen;

public class Button {
    private String text;
    private int x;
    private int y;
    private int textureID;

    public Button(String text, int x, int y, int textureID) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.textureID = textureID;
    }

    public int getTextureID() {
        return textureID;
    }

    public String getText() {
        return text;
    }

    public void render() {
        // Render the button on the screen
        // ...
    }

    public boolean isClicked(double mouseX, double mouseY) {
        // Check if the mouse coordinates are inside the button
        int buttonWidth = 0;
        int buttonHeight = 0;
        return mouseX >= x && mouseX <= x + buttonWidth && mouseY >= y && mouseY <= y + buttonHeight;
    }

}