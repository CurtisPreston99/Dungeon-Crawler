package game.scenes;

import engine.scene;
import engine.window;
import game.entityes.dungeon;
import game.entityes.player;

public class DungeonScene extends scene {

    public DungeonScene(window Parrent, String id) {
        super(Parrent, id);
        addEntity(new player(400,400,10,this,Parrent),"player");
        addEntity(new dungeon(400,400,800,this,Parrent,"dung"),"dungeon");
    }


    @Override
    public void tick() {
        
    }
    
}
