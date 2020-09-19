package game.entityes;

import engine.input;
import engine.scene;
import engine.window;
import engine.entity.entity;
import processing.core.PGraphics;

public class player extends entity {
    int xspd,yspd;
    int acc=1;
    int maxSpd=5;
    int[][] map;
    public player(int x, int y, int size, scene s, window w) {
        super(x, y, size, s, w);
        map = new int[size][size];
        
    }

    @Override
    public void click() {
    }

    @Override
    public void draw(PGraphics arg0) {
        arg0.rect(x,y,sizex,sizey);
    }

    @Override
    public void key() {
    }

    @Override
    public void update(window win) {
        input i = win.input;
        if(i.get('a')){
            xspd-=acc;
        }
        if(i.get('d')){
            xspd+=acc;
        }
        if(i.get('w')){
            yspd-=acc;
        }
        if(i.get('s')){
            yspd+=acc;
        }

        x+=xspd;
        y+=yspd;
    }

    


    
    
}


