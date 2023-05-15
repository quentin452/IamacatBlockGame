package fr.iamacat.iamacatblockgame;

import fr.iamacat.iamacatblockgame.gamescreen.GameWindow;

public class Main {

    private GameWindow window;

    public void run() {
        init();
        loop();
        cleanup();
    }

    private void init() {
        window = new GameWindow(1280, 720, "IamACat Block Game");
        window.setWindowIcon("resources/textures/gamescreen/icon.png");
    }

    private void loop() {
        while (!window.shouldClose()) {
            // Perform game logic and rendering
            window.update();
        }
    }

    private void cleanup() {
        window.close();
    }

    public static void main(String[] args) {
        new Main().run();
    }
}