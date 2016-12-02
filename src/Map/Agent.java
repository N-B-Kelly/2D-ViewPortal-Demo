package Map;

import Map.Tile;

public class Agent {
    protected int ID;
    protected static int NEXT_ID = 0;
    
    protected MapFeature ct;
    protected int x;
    protected int y;
    protected int los = 5;
    
    public int X() { return x; }
    public int Y() { return y; }
    public int LOS() { return los; }
    public MapFeature M() { return ct; }
    
    public Agent(int x, int y, MapFeature ct) {
	this.ct = ct;
	this.x = x;
	this.y = y;
	ID = NEXT_ID;
	NEXT_ID++;
    }

    public Agent(int x, int y, MapFeature ct, int los) {
	this.ct = ct;
	this.x = x;
	this.y = y;
	this.los = los;
	ID = NEXT_ID;
	NEXT_ID++;
    }

    public void setLOS(int los) {
	this.los = los;
    }

    public void MoveTo(int x, int y) {
	this.x = x;
	this.y = y;
    }
    
    public boolean Move(int dx, int dy) {
	dx += x;
	dy += y;

	Tile t = ct.getR(dx, dy, ct.Rot());
	if(t.floor()) {
	    x = dx;
	    y = dy;
	    return true;
	}
	else if (t.portal()) {
	    Portal p = (Portal)t;
	    dx = p.X();
	    dy = p.Y();
	    System.err.println(p.R() + ", " + ct.Rot());
	    MapFeature mf = p.T().GetRotation(((p.R() + ct.Rot()))%4);	    
	    ct = mf;
	    x = mf.rotX(dx, dy, (4 - ct.Rot()%4));
	    y = mf.rotY(dx, dy, (4 - ct.Rot()%4));
	    
	    System.err.println(ct.Rot());
	    return true;
	}
	return false;
    }

    protected float line_offset = 1.00f;
    protected float diag_offset = 1.35f;
    
    public MapFeature genMap(boolean portal) {
	MapFeature mf = new MapFeature(2*los + 1, 2*los + 1);

	int ren_x = los;
	int ren_y = los;

	mf.Fill(Tile.Hidden());

	//horizontal hourglass
	walkscan(portal, x, y, ren_x, ren_y, los*1.15f, 1, false, 1, -1, ct, mf);
	walkscan(portal, x, y, ren_x, ren_y, los*1.15f, 1, false, -1, -1, ct, mf);
	walkscan(portal, x, y, ren_x, ren_y, los*1.15f, 1, false, 1, 1, ct, mf);
	walkscan(portal, x, y, ren_x, ren_y, los*1.15f, 1, false, -1, 1, ct, mf);
	
	walkscan(portal, x, y, ren_x, ren_y, los*1.15f, 0, false, 1, -1, ct, mf);
	walkscan(portal, x, y, ren_x, ren_y, los*1.15f, 0, false, -1, -1, ct, mf);
	walkscan(portal, x, y, ren_x, ren_y, los*1.15f, 0, false, 1, 1, ct, mf);
	walkscan(portal, x, y, ren_x, ren_y, los*1.15f, 0, false, -1, 1, ct, mf);
	
	return mf;
    }

    private void walkscan(boolean portal, int x, int y, int rx, int ry, float rad, int vert,
			  boolean force, int cx, int cy, MapFeature read, MapFeature write) {
	if(rx < 0 || ry < 0 || rx > los*2 || ry > los*2)
	    return;

	Tile t = read.getR(x, y, read.Rot()%4);

	if(t.portal() && portal) {
	    Portal p = (Portal)t;
	    int dx = p.X();
	    int dy = p.Y();	    
	    MapFeature mf = p.T().GetRotation(((p.R() + read.Rot()))%4);	    
	    read = mf;
	    x = mf.rotX(dx, dy, (4 - read.Rot()%4));
	    y = mf.rotY(dx, dy, (4 - read.Rot()%4));

	    t = read.getR(x, y, read.Rot()%4);
	}

	write.Set(rx, ry, t);

	if(rad <= 0 || !t.floor())
	    return;

	
	x += cx;
	rx += cx;
	y += cy;
	ry += cy;
	if(!force)
	    walkscan(portal, x, y, rx, ry, rad - diag_offset, vert, false, cx, cy, read, write);

	if(vert == 0) {
	    x-= cx;
	    rx -= cx;
	}
	else {
	    y -= cy;
	    ry -= cy;
	}

	walkscan(portal, x, y, rx, ry, rad - line_offset, vert, false, cx, cy, read, write);
    }    
}
