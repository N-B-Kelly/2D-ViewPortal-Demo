package Map.Gen;

import org.newdawn.slick.Color;

import Map.*;

public class Room extends MapFeature {
    protected Room(int x, int y) {
	super(x, y);
    }

    public static Room testSet() {
	Room main = new Room(16, 16);

	Color a = new Color(0x6B, 0x0A, 0x07);
	Color b = new Color(0xAF, 0x3F, 0x3C);
	Color c = new Color(0xF6, 0x9B, 0x98);
	
	main.setFloor(a, c);
	
	main.walls(3);

	//x, y
	int[] walls_a = new int[] {6,6 ,7,6 ,8,6 ,9,6,
				   6,7 ,6,8 ,6,9 ,
				   9,7 ,9,8 ,9,10
	};

	int[] floor_a = new int[] {7 ,2  , 8 ,2,
				   13,4  , 13,5,
				   13,7  , 13,8,
				   13,10 , 13,11,
				   7 ,13 , 8 ,13
	};	
	
	main.setWalls(walls_a);
	main.setFloor(a,c,floor_a);

	Room hallway = new Room(9, 4);
	
	hallway.setFloor(a, b);
	hallway.walls(1);
		
	Room axle = new Room(17, 18);
	axle.setFloor(c,b);
	axle.walls(3);

	axle.setWall(6, 5, 15, 13);
	
	axle.setFloor(c,b, 1, 3, 14, 5);
	axle.setFloor(c,b, 1, 8, 7, 10);
	axle.setFloor(c,b, 1, 13, 14, 15);

	//hallway left to main right mid
	hallway.portal(1, 1, main, 13, 7, 0);
	hallway.portal(1, 2, main, 13, 8, 0);
	main.portal(14, 7, hallway, 2, 1, 0);
	main.portal(14, 8, hallway, 2, 2, 0);

	//hallway right to axle mid left
	hallway.portal(7,1,axle,2,8,0);
	hallway.portal(7,2,axle,2,9,0);
	axle.portal(1,8,hallway,6,1,0);
	axle.portal(1,9,hallway,6,2,0);

	//axle top to axle bottom
	axle.portal(13, 3, axle, 12, 14, 2);
	axle.portal(13, 4, axle, 12, 13, 2);
	axle.portal(13, 14, axle, 12, 3, 2);
	axle.portal(13, 13, axle, 12, 4, 2);

	//main right top to axle left top
	main.portal(14, 4, axle, 2, 3, 0);
	main.portal(14, 5, axle, 2, 4, 0);
	axle.portal(1, 3, main, 13, 4, 0);
	axle.portal(1, 4, main, 13, 5, 0);

	//main right bot to axle left bot
	main.portal(14, 10, axle, 2, 13, 0);
	main.portal(14, 11, axle, 2, 14, 0);
	axle.portal(1, 13, main, 13, 10, 0);
	axle.portal(1, 14, main, 13, 11, 0);

	//connect axle mid mid to main bot mid (90/270 rot)
	axle.portal(7, 8, main, 7, 13, 3);
	axle.portal(7, 9, main, 8, 13, 3);
	main.portal(7, 14, axle, 6, 8, 1);
	main.portal(8, 14, axle, 6, 9, 1);

	//connect mid mid to mid top
	main.portal(7, 1, main, 8, 8, 2);
	main.portal(8, 1, main, 7, 8, 2);
	main.portal(7, 7, main, 8, 2, 2);
	main.portal(8, 7, main, 7, 2, 2);
	
	hallway.Embed();
	main.Embed();
	axle.Embed();
	return main;
    }

    private void portal(int x, int y, MapFeature dest, int dx, int dy, int rot) {
	t[x][y] = Tile.Portal(dx, dy, rot, dest);
    }

    private void setFloor(Color a, Color b, int x1, int y1, int x2, int y2) {
	for(int x = x1; x < x2; x++)
	    for(int y = y1; y < y2; y++)
		t[x][y] = Tile.Floor(a, b, x, y);
    }

    private void setWall(int x1, int y1, int x2, int y2) {
	for(int x = x1; x < x2; x++)
	    for(int y = y1; y < y2; y++)
		t[x][y] = Tile.Wall();
    }
    
    private void setFloor(Color a, Color b) {
	for(int x = 0; x < width; x++)
	    for(int y = 0; y < height; y++)
		t[x][y] = Tile.Floor(a, b, x, y);
    }
    
    private void setFloor(Color a, Color b, int[] xy) {
	for(int i = 0; i < xy.length/2; i++)
	    t[xy[2*i]][xy[2*i + 1]] = Tile.Floor(a, b, xy[2*i], xy[2*i + 1]);
    }

    private void setWalls(int[] xy) {
	for(int i = 0; i < xy.length/2; i++)
	    t[xy[2*i]][xy[2*i + 1]] = Tile.Wall();
    }
    
    private void walls(int layers) {
	for(int i = 0; i < layers; i++) {
	    for(int x = 0; x < width; x++)
		t[x][i] = t[x][height - 1 - i] = Tile.Wall();
	    for(int y = 0; y < height; y++)
		t[i][y] = t[width  - 1 - i][y] = Tile.Wall();
	}
    }

    

}
