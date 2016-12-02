import java.util.logging.*;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import Map.*;
import Map.AI.*;
import Map.Gen.*;

public class BasicRender extends BasicGame {

    static MapFeature mf;
    static MapFeature fov_simple;
    static MapFeature fov_dummy;
    static Agent a;
    static DummyAgent da;
    static boolean first = true;
    
    //static FeatureAgentRenderFrame farsight;
    public static void main (String[] args) {
	System.out.println("Hello, World!");
	
	try  {
	    AppGameContainer appgc;
	    appgc = new AppGameContainer(new BasicRender("Render Target"));
	    appgc.setDisplayMode(640, 640, false);
	    mf = Room.testSet();
	    mf.setFrame(0.03333f, 0.275f, 0.45f, 0.45f);

	    a = new Agent(4, 4, mf);
	    a.setLOS(8);
	    da = new DummyAgent(5, 5, mf, 8);
	    fov_simple = a.genMap(false);
	    fov_simple.setFrame(0.51777f, 0.025f, 0.45f, 0.45f);
	    fov_dummy = da.genMap(false);
	    fov_dummy.setFrame(0.51777f, 0.525f, 0.45f, 0.45f);
	    appgc.start();
	} catch (SlickException e) {
	    Logger.getLogger(BasicRender.class.getName()).log(Level.SEVERE, null, e);
	}
    }    
    
    public BasicRender(String str) {
	super(str);
    }    
    
    public void render(GameContainer gc, Graphics g) throws SlickException {
	try {
	    mf.Render(gc, g, a, true);
	    fov_simple.Render(gc, g, a, true);
	    fov_dummy.Render(gc, g, da, true);
	    
	} catch(Exception e) {
	    e.printStackTrace();
	}
    }
    
    
    public void init(GameContainer gc) throws SlickException {
	// TODO Auto-generated method stub
	gc.setShowFPS(false);
    }
    
    
    public void update(GameContainer gc, int arg1) throws SlickException {
	boolean moved = false;
	if(gc.getInput().isKeyPressed(Input.KEY_Z))
	    moved = a.Move(0, -1);
	else if(gc.getInput().isKeyPressed(Input.KEY_S))
	    moved = a.Move(0, 1);
	else if(gc.getInput().isKeyPressed(Input.KEY_Q))
	    moved = a.Move(-1, 0);
	else if(gc.getInput().isKeyPressed(Input.KEY_D))
	    moved = a.Move(1, 0);
	else if(gc.getInput().isKeyPressed(Input.KEY_SPACE))
	    moved= true;
	else if(gc.getInput().isKeyPressed(Input.KEY_E)) {
	    mf.rotRight();
	    int dx = mf.rotX(a.X(), a.Y(), 3);
	    int dy = mf.rotY(a.X(), a.Y(), 3);
	    a.MoveTo(dx, dy);
	}
       
	if(moved || first) {
	    da.ACT();
	    fov_dummy = da.genMap(true);
	    fov_dummy.setFrame(0.51777f, 0.525f, 0.45f, 0.45f);
	    fov_dummy.set_render_center();
	}
	
	mf = a.M();
	mf.setFrame(0.03333f, 0.275f, 0.45f, 0.45f);
	
	fov_simple = a.genMap(true);
	fov_simple.setFrame(0.51777f, 0.025f, 0.45f, 0.45f);
	fov_simple.set_render_center();

	first = false;
    }
}

