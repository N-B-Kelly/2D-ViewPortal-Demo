package Map;

import org.newdawn.slick.Color;

public class Tile {
    Color col;
    int id;

    protected Tile(int id, Color col) {
	this.col = col;
	this.id = id;
    }

    public Color Col() { return col; }
    public int ID() {    return  id; }

    public static Tile Hidden() { return new Tile(-10, Color.black); }
    public static Tile Floor() { return new Tile(0, Color.orange); }
    public static Tile Wall() { return new Tile(10, Color.red); }
    public static Portal Portal(int x, int y, int rot, MapFeature target)
    { return new Portal(x, y, rot, target); }

    public static Tile Floor(Color a, Color b, int x, int y) {
	Tile t = Floor();
	if((x + y)%2 == 0)	    
	    t.col = a;
	else
	    t.col = b;
	return t;
    }

    public boolean hidden() { return id == -10; }
    public boolean floor() { return id == 0; }
    public boolean wall() { return id == 10; }
    public boolean portal() { return id == 20; }
}

class Portal extends Tile {
    protected int x;
    protected int y;
    protected int rot;
    protected MapFeature target;
    
    protected Portal(int x, int y, int rot, MapFeature target) {
	super(20, Color.magenta);
	this.x = x;
	this.y = y;
	this.rot = rot;
	this.target = target;
    }

    public int X() {  return x;  }
    public int Y() {  return y;  }
    public int R() { return rot; }
    
    public MapFeature T() { return target; }
}
