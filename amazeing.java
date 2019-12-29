import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.*;

public class amazeing{

    public static int[][] map = new int[25][39];
    public static Pair[][] weights = new Pair[25][39];
    public static int Sx,Sy,Ex,Ey;
    public static Queue<Struct> queue = new ArrayDeque<Struct>();

    public static void PutAll(Struct cur){
        int x = cur.getX();
        int y = cur.getY();
        if(map[x][y-1] != 1){
            go(x,y-1,cur.getKeys(),cur.getMoves()+"l");
        }
        if(map[x][y+1] != 1){
            go(x,y+1,cur.getKeys(),cur.getMoves()+"r");
        }
        if(map[x-1][y] != 1){
            go(x-1,y,cur.getKeys(),cur.getMoves()+"u");
        }
        if(map[x+1][y] != 1){
            go(x+1,y,cur.getKeys(),cur.getMoves()+"d");
        }
    }

    public static void go(int x, int y, int keys, String mv){
        if(map[x][y] == 3 && keys <= 0)
            return;
        if(weights[x][y] != null && weights[x][y].getKeys() >= keys && (weights[x][y].getMoves() != 0 && weights[x][y].getMoves() <= mv.length()))
            return;
        if(map[x][y] == 2){
            map[x][y] = 0;
            queue.add(new Struct(x,y,mv,keys+1));
            weights[x][y] = new Pair(mv.length(), keys+1);
        } else if(map[x][y] == 3 && keys > 0){
            queue.add(new Struct(x,y,mv,keys-1));
            weights[x][y] = new Pair(mv.length(), keys);
        } else {
            queue.add(new Struct(x,y,mv,keys));
            weights[x][y] = new Pair(mv.length(), keys);
        }
    }

    public static void main(String[] args){

        try {
            Process proc = Runtime.getRuntime().exec("nc tasks.open.kksctf.ru 31397");
            InputStream is = proc.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            OutputStreamWriter osw = new OutputStreamWriter(proc.getOutputStream());
            bis.read();
            bis.read();
            while(true){
                weights = new Pair[25][39];
                map = new int[25][39];
                queue = new ArrayDeque<Struct>();
                StringBuffer sb = new StringBuffer("");

                for (int i = 0; i < 1974; i++) {

                    sb.append((char) bis.read());
                    System.out.println(sb.toString());

                }
                sb.append('\n');
                System.out.println(sb.toString());

                int j = -1;
                Scanner sc = new Scanner(sb.toString());
                while (sc.hasNextLine()) {
                    j++;
                    String line = sc.nextLine();
                    for (int i = 0; i < line.length(); i++) {
                        if (i % 2 == 1)
                            continue;
                        if (line.charAt(i) == '#')
                            map[j][i / 2] = 1;
                        else if (line.charAt(i) == 'O')
                            map[j][i / 2] = 2;
                        else if (line.charAt(i) == ':') {
                            Sx = j;
                            Sy = i / 2;
                        } else if (line.charAt(i) == '<') {
                            Ex = j;
                            Ey = i / 2;
                        } else if (line.charAt(i) == '{')
                            map[j][i / 2] = 3;
                        else map[j][i / 2] = 0;
                    }
                }

                queue.add(new Struct(Sx, Sy, "", 0));
                boolean isFinish = false;
                while (!isFinish && queue.peek() != null) {
                    Struct cur = queue.poll();
                    if (cur.getX() == Ex && cur.getY() == Ey) {
                        System.out.println(cur.getMoves());
                        osw.write(cur.getMoves());
                        osw.flush();
                        isFinish = true;
                        System.out.println("Finished");
                        continue;
                    }
                    PutAll(cur);
                }
                System.out.println("Ended");
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }


}

class Struct{

    private int x;
    private int y;
    private String moves;
    private int keys;

    public Struct(int x, int y, String moves, int keys){
        this.x = x;
        this.y = y;
        this.moves = moves;
        this.keys = keys;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public String getMoves(){
        return this.moves;
    }

    public int getKeys(){
        return this.keys;
    }
}

class Pair{

    private int moves;
    private int keys;

    public Pair(int moves, int keys){
        this.moves = moves;
        this.keys = keys;
    }

    public int getMoves(){
        return this.moves;
    }

    public int getKeys(){
        return this.keys;
    }
}
