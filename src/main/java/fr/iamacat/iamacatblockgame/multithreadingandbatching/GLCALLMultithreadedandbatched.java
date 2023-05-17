package fr.iamacat.iamacatblockgame.multithreadingandbatching;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GLCALLMultithreadedandbatched {

    private BlockingQueue<Runnable> glCallQueue;
    private Thread renderThread;
    private SpriteBatch batch;
    private boolean disposed;

    public GLCALLMultithreadedandbatched() {

        glCallQueue = new LinkedBlockingQueue<>();

        // Create render thread
        renderThread = new Thread(this::renderLoop);
        renderThread.start();

        disposed = false;
    }

    // Queue GL call
    public void executeGLCall(Runnable glCall) {
        glCallQueue.add(glCall);
    }

    // Dispose
    public void dispose() throws InterruptedException {
        glCallQueue.add(this::shutdown);
        renderThread.join();
        batch.dispose();
        disposed = true;
    }

    private void renderLoop() {
        while(true) {

            try {
                Runnable glCall = glCallQueue.take();
                glCall.run();
            } catch (InterruptedException e) {
                // Render thread was interrupted, exit loop
                break;
            }
        }
    }

    private void shutdown() {
        // ...
    }

}