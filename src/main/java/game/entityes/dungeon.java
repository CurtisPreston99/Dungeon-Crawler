package game.entityes;

import java.util.ArrayList;
import java.util.Random;

import dungeonGen.gen;
import engine.scene;
import engine.window;
import engine.entity.entity;
import processing.core.PGraphics;

public class dungeon extends entity {

    private int[][] map;

    public dungeon(int x, int y, int size, scene s, window w, String name) {
        super(x, y, size, size, s, w, name);
        map = gen.genRoom(100, 10);
    }

    @Override
    public void click() {
        // TODO Auto-generated method stub

    }

    @Override
    public void draw(PGraphics g) {
        int s = g.width / map.length;

        for (int i = 0; i < map.length; i++) {
            for (int e = 0; e < map[i].length; e++) {
                if (map[i][e] > 0) {
                    switch (map[i][e]) {
                        case 1:
                            g.fill(255);
                            break;
                        case 2:
                            g.fill(255, 0, 0);
                            break;
                        case 3:
                            g.fill(0, 255, 0);
                            break;

                        default:
                            break;
                    }
                    g.rect(s * i, s * e, s, s);
                }

            }
        }

    }

    @Override
    public void key() {
        // TODO Auto-generated method stub
        if (w.input.get('r')) {
            map = gen.genRoom(100, 10);

        }

    }

    @Override
    public void update(window arg0) {
        // TODO Auto-generated method stub

    }

}
