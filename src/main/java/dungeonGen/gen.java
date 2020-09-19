package dungeonGen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class gen {
    static boolean valueInRange(int value, int min, int max) {
        return (value >= min) && (value <= max);
    }

    static boolean rectOverlap(int[] A, int[] B) {
        boolean xOverlap = valueInRange(A[0], B[0], B[0] + B[2]) || valueInRange(B[0], A[0], A[0] + A[2]);

        boolean yOverlap = valueInRange(A[1], B[1], B[1] + B[3]) || valueInRange(B[1], A[1], A[1] + A[3]);

        return xOverlap && yOverlap;
    }

    public static boolean roomRoomsCol(int[] room, ArrayList<int[]> rooms) {
        // {x,y,w,h}
        for (int[] ro : rooms) {
            // check x;
            if (rectOverlap(ro, room)) {
                return true;
            }

        }
        return false;
    }

    static boolean lineLine(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {

        // calculate the direction of the lines
        float uA = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1));
        float uB = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1));

        // if uA and uB are between 0-1, lines are colliding
        if (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1) {

            // optionally, draw a circle where the lines meet
            // float intersectionX = x1 + (uA * (x2 - x1));
            // float intersectionY = y1 + (uA * (y2 - y1));
            // fill(255, 0, 0);
            // noStroke();
            // ellipse(intersectionX, intersectionY, 20, 20);

            return true;
        }
        return false;
    }

    static boolean lineRect(float x1, float y1, float x2, float y2, float rx, float ry, float rw, float rh) {

        // check if the line has hit any of the rectangle's sides
        // uses the Line/Line function below
        boolean left = lineLine(x1, y1, x2, y2, rx, ry, rx, ry + rh);
        boolean right = lineLine(x1, y1, x2, y2, rx + rw, ry, rx + rw, ry + rh);
        boolean top = lineLine(x1, y1, x2, y2, rx, ry, rx + rw, ry);
        boolean bottom = lineLine(x1, y1, x2, y2, rx, ry + rh, rx + rw, ry + rh);

        // if ANY of the above are true, the line
        // has hit the rectangle
        if (left || right || top || bottom) {
            return true;
        }
        return false;
    }

    public static int[][] genRoom(int size, int level) {
        ArrayList<int[]> rooms = new ArrayList<int[]>();
        int[][] out = new int[size][size];
        // int Nrooms=new Random().nextInt((20 - 5) + 1) + 5;
        int Nrooms = 20;
        for (int i = 0; i < Nrooms; i++) {
            int w = new Random().nextInt((size / 5 - size / 30) + 1) + size / 30;
            int h = new Random().nextInt((size / 5 - size / 30) + 1) + size / 30;
            int x = new Random().nextInt((size - w));
            int y = new Random().nextInt((size - h));
            int[] curRoom = { x, y, w, h };
            if (!roomRoomsCol(curRoom, rooms)) {
                rooms.add(curRoom);

                for (int p = x; p < w + x; p++) {

                    out[p][y] = 1;
                    out[p][y + h] = 1;
                }
                for (int p = y; p < h + y; p++) {
                    out[x][p] = 1;
                    out[x + w][p] = 1;
                }
            } else {
                i--;
            }
        }

        System.out.println(rooms);

        // coridors

        gencorridors(rooms, out);
        return out;
    }

    static void gencorridors(ArrayList<int[]> rooms, int[][] map) {

        // weigthed adjacency matrix weigth = distance
        int[][] dis = new int[rooms.size()][rooms.size()];

        for (int i = 0; i < rooms.size(); i++) {
            int[] room1 = rooms.get(i);
            int[] room1mid = { room1[0] + (room1[2] / 2), room1[1] + (room1[3] / 2) };
            for (int e = 0; e < rooms.size(); e++) {
                int[] room2 = rooms.get(e);
                int[] room2mid = { room2[0] + (room2[2] / 2), room2[1] + (room2[3] / 2) };

                int disR = (int) Math
                        .sqrt(Math.pow(room2mid[0] - room1mid[0], 2) + Math.pow(room2mid[1] - room1mid[1], 2));
                dis[i][e] = disR;

            }
        }
        // for (int[] i : dis) {
        // for (int e : i) {
        // System.out.print(e);
        // System.out.print('|');
        // }
        // System.out.println();
        // }
        // ordering by weights
        ArrayList<ArrayList<int[]>> orders = new ArrayList<ArrayList<int[]>>();

        for (int i = 0; i < dis.length; i++) {
            ArrayList<int[]> ord = new ArrayList<int[]>();

            for (int e = 0; e < dis[i].length; e++) {
                if (e != i) {
                    int[] o = { dis[i][e], e };
                    ord.add(o);
                }
            }

            for (int[] p : ord) {
                for (int e : p) {
                    System.out.print(e);
                    System.out.print('|');
                }
                System.out.println();
            }
            System.out.println("----------------");

            ord.sort(new java.util.Comparator<int[]>() {
                public int compare(int[] a, int[] b) {
                    return Integer.compare(a[0], b[0]);
                }
            });

            for (int[] p : ord) {
                for (int e : p) {
                    System.out.print(e);
                    System.out.print('|');
                }
                System.out.println();
            }
            System.out.println("----------------");

            System.out.println("----------------");
            orders.add(ord);

        }

        // todo: make conection graph
        int[][] conection = new int[rooms.size()][rooms.size()];
        int itter = 0;

        while (!isFullyConnected(conection) && itter < rooms.size() * 2) {
            for (int i = 0; i < orders.size(); i++) {
                ArrayList<int[]> cur = orders.get(i);
                int[] g;

                try {
                    g = cur.get(0);

                    cur.remove(0);
                    conection[i][g[1]] = 1;

                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            System.out.println(itter);
            itter++;
        }

        for (int[] p : conection) {
            for (int e : p) {
                System.out.print(e);
                System.out.print('|');
            }
            System.out.println();
        }
        int[][] mask = new int[map.length][map.length];

        for (int x = 0; x < conection.length; x++) {
            for (int y = 0; y < conection[x].length; y++) {
                if (conection[x][y] == 1) {
                    int[][] e=drawPath(rooms.get(x), rooms.get(y), map, rooms);
                    mask=join(e,mask);
                }
            }
        }

        for (int x = 0; x < mask.length; x++) {
            for (int y = 0; y < mask[x].length; y++) {
                if (mask[x][y] == 1) {
                    map[x][y]=0;
                }
            }
        }
        


    }


    static int[][] join(int[][] one,int[][] two){
        int[][] out=new int[one.length][one[0].length];
        for(int x=0;x<one.length;x++){
            for(int y=0;y<one[0].length;y++){
                if(one[x][y]==1||two[x][y]==1){
                    out[x][y]=1;
                }
            }
        }
        return out;
    }

    public static int[][] drawPath(int[] room1, int[] room2, int[][] out, ArrayList<int[]> rooms) {
        int[][] mask = new int[out.length][out.length];
        int[] room1Mid = { room1[0] + (room1[2] / 2), room1[1] + (room1[3] / 2) };
        int[] room2Mid = { room2[0] + (room2[2] / 2), room2[1] + (room2[3] / 2) };

        int angleDeg = (int) (Math.atan2(room2Mid[1] - room1Mid[1], room2Mid[0] - room1Mid[0]) * 180 / Math.PI);
        System.out.println(angleDeg);
        // vert first
        int wall=1;

        if ((angleDeg >= -45 && angleDeg <= 45) || (angleDeg <= -135 && angleDeg >= 135)) {

            for (int y = room1Mid[1]; y != room2Mid[1];) {
                if (!inRoom(room1Mid[0], y, rooms)) {
                    mask[room1Mid[0]][y]=1;
                    out[room1Mid[0]+1][y] = wall;
                    out[room1Mid[0]-1][y] = wall;
                }
                if (y > room2Mid[1])
                    y--;
                else
                    y++;
            }

            for (int x = room1Mid[0]; x != room2Mid[0];) {
                if (!inRoom(x, room2Mid[1], rooms)) {
                    mask[x][room2Mid[1]]=1;

                    out[x][room2Mid[1]-1] = wall;
                    out[x][room2Mid[1]+1] = wall;
                }
                if (x > room2Mid[0])
                    x--;
                else
                    x++;
            }

            // filling in cornners
            int[] mid={room1Mid[0],room2Mid[1]};

            for(int i=mid[0]-1;i<=mid[0]+1;i++){
                for(int e=mid[1]-1;e<=mid[1]+1;e++){
                    if (!inRoom(i, e, rooms)) {

                        out[i][e]=1;
                    }
                }
            }

        } else if ((angleDeg < -45 && angleDeg > -135) || (angleDeg > 45 && angleDeg < 135)) {
            int x,y;
            // hz
            for (x = room1Mid[0]; x != room2Mid[0];) {
                if (!inRoom(x, room1Mid[1], rooms)) {
                    mask[x][room1Mid[1]] = 1;
                    out[x][room1Mid[1]+1] = wall;
                    out[x][room1Mid[1]-1] = wall;
                }

                if (x > room2Mid[0])
                    x--;
                else
                    x++;

            }
            for (y = room1Mid[1]; y != room2Mid[1];) {
                if (!inRoom(room2Mid[0], y, rooms)) {
                    mask[room2Mid[0]][y] = 1;
                    out[room2Mid[0]-1][y] = wall;
                    out[room2Mid[0]+1][y] = wall;
                }
                if (y > room2Mid[1])
                    y--;
                else
                    y++;

            }

            // filling in cornners

            int[] mid={room2Mid[0],room1Mid[1]};
            for(int i=mid[0]-1;i<=mid[0]+1;i++){
                for(int e=mid[1]-1;e<=mid[1]+1;e++){
                    if (!inRoom(i, e, rooms)) {

                    out[i][e]=1;
                    }
                }
                
            }

        }

        return mask;
        
    }

    static boolean inRoom(int x, int y, ArrayList<int[]> rooms) {
        for (int[] i : rooms) {
            if ((x > i[0] && x < i[0] + i[2]) && (y > i[1] && y < i[1] + i[3]))
                return true;
        }
        return false;
    }
    // breath first seatch to see if all nodes are visitable from node[0]
    static boolean isFullyConnected(int[][] m) {
        int n = m.length;
        boolean[] conn1 = new boolean[n + 1];

        Set<Integer> visited = new HashSet<Integer>();

        Set<Integer> frontier = new HashSet<Integer>();
        frontier.add(0);
        int moves = 0;
        while (visited.size() < n && moves < n) {
            Set<Integer> Nfrontier = new HashSet<Integer>();

            for (int i : frontier) {
                int[] v = m[i];
                for (int c = 0; c < v.length; c++) {
                    if (v[c] == 1 && !visited.contains(c)) {
                        Nfrontier.add(c);
                    }
                }
            }
            visited.addAll(frontier);
            frontier = Nfrontier;
            moves++;
        }

        return visited.size() == n;
    }

}
