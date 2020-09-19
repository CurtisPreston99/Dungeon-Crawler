package game.scenes;

import engine.scene;
import engine.window;
import engine.ui.button;
import engine.ui.text;

public class welcomeScreen extends scene {

    public welcomeScreen(window Parrent, String id) {
        super(Parrent, id);
        addEntity(new text(Parrent.width / 2, Parrent.height / 3, 100, 100, this, Parrent, "Dungeon Explorer"),
                "title");
        addEntity(new button((Parrent.width / 2) - 50, Parrent.height / 2, 100, 50, "play", this, Parrent), "play");

    }

    @Override
    public void tick() {
        if (((button) getEntity("play")).getVal()) {
            Parrent.selectScene("Dungeon");
        }
    }
}