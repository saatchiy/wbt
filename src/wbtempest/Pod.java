package wbtempest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import wbtempest.Ex.State;

public class Pod extends Ex {

	public Pod(int col, int ncols, boolean canMove, boolean levelContinuous) {
		super(col, ncols, canMove, levelContinuous);
		visible = true;
	}
	
	/**
     * Spawns an additional ex, traveling in the opposite direction.
     */
    public ArrayList<Ex> spawn() {
    	ArrayList<Ex> spawns = new ArrayList<Ex>();
    	Ex spawn1 = new Ex(col, ncols, canMove, levContinuous);
    	spawn1.z = z;
    	spawn1.prefdir = State.JUMPRIGHT1;
    	spawns.add(spawn1);
    	
    	Ex spawn2 = new Ex(col, ncols, canMove, levContinuous);
    	spawn2.z = z;
    	spawn2.prefdir = State.JUMPRIGHT1;
    	spawns.add(spawn2);
    	
    	return spawns;
    }
	
    public List<int[]> getCoords(Level lev){
    	int[][] coords = new int[17][3];
    	Column c = lev.getColumns().get(col);
    	int[] p1 = c.getFrontPoint1();
    	int[] p2 = c.getFrontPoint2();
    	
    	if (z < Board.LEVEL_DEPTH) {
    		int cx = p1[0] + (p2[0]-p1[0])/2;
    		int cy = p1[1] + (p2[1]-p1[1])/2;

    		// define outer and inner diamonds
    		int[][] outer = new int[4][3];
    		int[][] inner = new int[4][3];
    		outer[0][0] = cx;
    		outer[0][1] = cy - PODSIZE;
    		outer[0][2] = (int)z;
    		outer[1][0] = cx + PODSIZE;
    		outer[1][1] = cy;
    		outer[1][2] = (int) z;
    		outer[2][0] = cx;
    		outer[2][1] = cy + PODSIZE;
    		outer[2][2] = (int) z;
    		outer[3][0] = cx - PODSIZE;
    		outer[3][1] = cy;
    		outer[3][2] = (int) z;
    		inner[0][0] = cx;
    		inner[0][1] = cy - PODSIZE/3;
    		inner[0][2] = (int) z;
    		inner[1][0] = cx + PODSIZE/3;
    		inner[1][1] = cy;
    		inner[1][2] = (int) z;
    		inner[2][0] = cx;
    		inner[2][1] = cy + PODSIZE/3;
    		inner[2][2] = (int) z;
    		inner[3][0] = cx - PODSIZE/3;
    		inner[3][1] = cy;
    		inner[3][2] = (int) z;

    		// define line path through those diamonds:
    		coords[0] = outer[0];
    		coords[1] = outer[1];
    		coords[2] = inner[1];
    		coords[3] = inner [0];
    		coords[4] = outer[1];
    		coords[5] = outer[2];
    		coords[6] = inner[2];
    		coords[7] = inner[1];
    		coords[8] = outer[2];
    		coords[9] = outer[3];
    		coords[10]= inner[3];
    		coords[11]= inner[2];
    		coords[12]= outer[3];
    		coords[13]= outer[0];
    		coords[14]= inner[0];
    		coords[15]= inner[3];
    		coords[16]= outer[0];
    	}
    	
    	return Arrays.asList(coords);
    }
    
    public void moveStraight(int crawlerCol) {
    	if (z <= 0 || z > Board.LEVEL_DEPTH){
    		// if exes are allowed to move column to column, or if we're already at 
    		// the front of the board, move every now and then.
    		jumptimer--;
    		if (jumptimer <= 0) {
    				if (z < Board.LEVEL_DEPTH && spawning)
    				{ // haven't chosen a direction yet
    					spawning = false;
    					prefdir = State.values()[rnd.nextInt(2)+1]; 
    					// right or left at random, note depends on order 
    					// of values in state enum, which is tacky
    				}
    				if (z <= 0 && (rnd.nextInt(4) == 1)) {
    					// at top, randomly reorient and make sure 
    					// we're evilly headed toward the player
    					if (levContinuous) {
    						int diff = crawlerCol - getColumn();
    						boolean wrap = Math.abs(diff) > ncols/2;
    						if ((diff > 0 && wrap) // ex is low and we need to wrap
    								|| (diff < 0 && !wrap)) // ex is high and we don't
    							prefdir = State.JUMPLEFT1;
    						else
    							prefdir = State.JUMPRIGHT1;
    					}
    					else{
    						if (crawlerCol > getColumn())
    							prefdir = State.JUMPRIGHT1;
    						else
    							prefdir = State.JUMPLEFT1;
    					}
    				}
					s = prefdir;
    		}
    	}
    }

}
