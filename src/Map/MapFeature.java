package Map;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Color;

import Map.Tile;

public class MapFeature {
    protected Tile[][] t;

    //current rotation of feature
    protected int rot;
    
    public int Rot() { return rot; }
    
    //size of map
    protected int width;
    protected int height;

    //render dimensions
    protected float r_x;
    protected float r_y;
    protected float r_w;
    protected float r_h;

    //ensure render dimensions are set AT LEAST once for rendering
    protected boolean set = false;
    protected boolean rendermode_center = false;

    public void set_render_center() { rendermode_center = true; }
    
    public MapFeature(int width, int height) {
	this.width = width;
	this.height = height;
	t = new Tile[width][height];
	this.rot = 0;
    }

    public static MapFeature ptest2() {
	MapFeature mf = new MapFeature(6, 6);
	mf.Fill(Tile.Floor());
	for(int i = 0; i < 6; i++)
	    mf.t[i][0] = mf.t[0][i] = mf.t[i][5] = mf.t[5][i] = Tile.Wall();

	mf.t[1][1] = mf.t[1][3] = mf.t[3][1] = Tile.Wall();
	mf.t[0][2] = Tile.Portal(4, 1, 3, mf);
	mf.t[4][0] = Tile.Portal(1, 2, 1, mf);

	mf.Embed();

	return mf;
    }

    public MapFeature GetRotation(int rot) {
	Tile[][] output = this.t;

	MapFeature mf = new MapFeature(width, width);
	mf.t = output;
	mf.rot = rot;

	mf.setFrame(r_x, r_y, r_w, r_h);
	return mf;	       
    }
    
    public static MapFeature ptest() {
	MapFeature mf = new MapFeature(14, 16);
	mf.Fill(Tile.Floor());
	for(int i = 0; i < 14; i++) {
	    mf.t[i][0] = mf.t[i][15] = Tile.Wall();
	    mf.t[0][1 + i] = mf.t[13][1+i] = Tile.Wall();
	}

	mf.t[6][2] = Tile.Wall();
	mf.t[7][2] = Tile.Wall();

	mf.t[12][2] = Tile.Wall();
	mf.t[12][4] = Tile.Wall();

	mf.t[5][2] = Tile.Wall();
	mf.t[5][4] = Tile.Wall();
	//mf.t[11][4] = Tile.Wall();
	
	mf.t[7][3] = Tile.Wall();	
	mf.t[6][4] = Tile.Wall();
	mf.t[7][4] = Tile.Wall();
	mf.t[6][5] = Tile.Wall();
	mf.t[7][6] = Tile.Wall();

	for(int x = 5; x < 9; x++)
	    mf.t[x][8] = Tile.Wall();

	mf.t[5][9] = mf.t[8][9] = Tile.Wall();
	mf.t[2][9] = mf.t[3][9] = mf.t[10][9] = mf.t[11][9] = Tile.Wall();

	mf.t[5][11] = mf.t[8][11] = Tile.Wall();
	mf.t[4][13] = mf.t[5][13] = mf.t[8][13] = mf.t[9][13] = Tile.Wall();

	mf.t[7][1] = mf.t[10][1] = mf.t[5][10] = mf.t[8][10] = Tile.Wall();
	mf.t[8][0] = Tile.Portal(7, 10, 2, mf);
	mf.t[9][0] = Tile.Portal(6, 10, 2, mf);

	mf.t[6][9] = Tile.Portal(9, 1, 2, mf);
	mf.t[7][9] = Tile.Portal(8, 1, 2, mf);

	mf.t[1][1] = mf.t[2][1] = mf.t[3][1] = mf.t[6][1] = mf.t[12][5] = mf.t[12][8] = Tile.Wall();
	
	mf.t[13][6] = Tile.Portal(5, 1, 1, mf);
	mf.t[13][7] = Tile.Portal(4, 1, 1, mf);
	mf.t[5][0]  = Tile.Portal(12, 6, 3, mf);
	mf.t[4][0]  = Tile.Portal(12, 7, 3, mf);
	
	mf.t[6][3] = Tile.Portal(12, 3, 2, mf); //portal A1
	mf.t[13][3] = Tile.Portal(5, 3, 2, mf); //portal 1A	

	mf.Embed();

	return mf;
    }

    //embed the current map in a square array (this makes it rotation work better: trust me)
    protected void Embed() {
	//get size of largest dimension
	int max = Math.max(width, height);

	Tile[][] newGrid = new Tile[max][max];
	Fill(Tile.Wall(), newGrid, max);
	for(int i = 0; i < width; i++)
	    for(int j = 0; j < height; j++)
		newGrid[i][j] = t[i][j];
	t = newGrid;
	width = height = max;
    }

    public void Fill(Tile tile) {
	for(int x = 0; x < width; x++)
	    for(int y = 0; y < height; y++) {
		t[x][y] = tile;
	    }
    }
    
    protected  void Fill(Tile tile, Tile[][] t2, int size) {
	for(int x = 0; x < size; x++)
	    for(int y = 0; y < size; y++) {
		t2[x][y] = tile;
	    }
    }

    public void setRot(int r) {
	this.rot = r%4;
    }

    public void rotRight() {
	setRot(rot + 1);
    }

    public void rotLeft() {
	setRot(rot + 3);
    }

    public void setFrame(float x, float y, float render_width, float render_height) {
	r_x = x;
	r_y = y;
	r_w = render_width;
	r_h = render_height;

	set = true;
    }

    public void Set(int x, int y, Tile tile) {
	t[x][y] = tile;
    }
    
    public int rotX(int x, int y, int rt) {
	rt = rt % 4;

	if(rt == 1)
	    return width - 1 - y;
	else if(rt == 2)
	    return width - 1 - x;
	else if(rt == 3)
	    return y;
	else return x;	
    }

    public int rotY(int x, int y, int rt) {
	rt = rt%4;

	if(rt == 1)
	    return x;
	else if(rt == 2)
	    return width - 1 - y;
	else if (rt == 3)
	    return width - 1 - x;
	else return y;	
    }

    public Tile getR(int x, int y, int rt) {
	return t[rotX(x, y, rt)][rotY(x, y, rt)];
    }

    public void Render(GameContainer gc, Graphics g, Agent a, boolean userot) throws Exception {
	if(!set) throw new Exception("Cannot render feature: use setFrame to set render dimensions");

	float x = gc.getWidth() * r_x;
	float y = gc.getHeight() * r_y;
	float w = (gc.getWidth() * r_w)/width;
	float h = (gc.getHeight() * r_h)/height;

	int nrx = rotX(a.X(), a.Y(), (4 - rot) % 4);
	int nry = rotY(a.X(), a.Y(), (4 - rot) %4);
	
	for(int i = 0; i < width; i++)
	    for(int j = 0; j < height; j++) {
		if(rendermode_center) {
		    if(i == width/2 && height/2 == j)
			g.setColor(Color.blue);
		    else if(getR(i,j, rot) == null)
			g.setColor(Color.white);
		    else
			g.setColor(getR(i, j, rot).Col());
		}
		else if(userot) {
		    if(a.X() == i && a.Y() == j)
			g.setColor(Color.blue);
		    else if(getR(i,j, rot) == null)
			g.setColor(Color.white);
		    else
			g.setColor(getR(i, j, rot).Col());
		}
		else {
		    if(nrx == i && nry == j)
			g.setColor(Color.blue);
		    else if(getR(i,j, 0) == null)
			g.setColor(Color.white);
		    else
			g.setColor(getR(i, j, 0).Col());
		}
		g.fillRect(x + w*i, y + h*j, w, h);
	    }
    }
}
