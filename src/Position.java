import java.util.*;


public class Position {
	
	int x;
	int y;
	char data;
	
	Position north = null;
	Position south = null;
	Position west = null;
	Position east = null;
	Position parent = null;
	
	double heuristic;
	double pathcost;
	double eval;
	
	boolean visited = false;
	
	void calculateHeuristic(Position goal) {
		heuristic = (Math.abs(this.x-goal.x) + Math.abs(this.y-goal.y));
	}
	List<Position> getNeighbours() {
		
		List<Position> list = new ArrayList<Position>();
		if(this.north != null && this.north.data != '*') {
			list.add(this.north);
		}
		if(this.south != null && this.south.data != '*') {
			list.add(this.south);
		}
		if(this.east != null && this.east.data != '*') {
			list.add(this.east);
		}
		if(this.west != null && this.west.data != '*') {
			list.add(this.west);
		}
		
		return list;
	}
	

}
