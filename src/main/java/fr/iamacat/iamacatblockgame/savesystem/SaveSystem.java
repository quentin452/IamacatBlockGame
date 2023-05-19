package fr.iamacat.iamacatblockgame.savesystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g3d.particles.ResourceData;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import fr.iamacat.iamacatblockgame.player.Player;
import fr.iamacat.iamacatblockgame.scene.game.TestWorldScene;

public class SaveSystem {
  /*  private static final String SAVE_FOLDER_PATH = "saves";
    private static final String SAVE_FILE_PATH = "saves/save.json";
    public static void saveData(Player player, TestWorldScene testWorldScene) {
        // Create the saves folder if it doesn't exist
        FileHandle saveFolder = Gdx.files.local(SAVE_FOLDER_PATH);
        if (!saveFolder.exists()) {
            saveFolder.mkdirs();
        }

        SaveData saveData = new SaveData(player, testWorldScene);
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        // #todo Caused by: java.lang.reflect.InaccessibleObjectException: Unable to make field private int java.nio.Buffer.mark accessible: module java.base does not "opens java.nio" to unnamed module @294425a7 when saving data
        String jsonData = json.prettyPrint(saveData);

        FileHandle file = Gdx.files.local(SAVE_FILE_PATH);
        file.writeString(jsonData, false);

        System.out.println("Data saved successfully.");
    }

    public static SaveData loadData() {
        FileHandle file = Gdx.files.local(SAVE_FILE_PATH);
        if (file.exists()) {
            String jsonData = file.readString();
            Json json = new Json();
            return json.fromJson(SaveData.class, jsonData);
        } else {
            System.out.println("No save data found.");
            return null;
        }
    }

   */
}
