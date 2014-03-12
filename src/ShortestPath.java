import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


public class ShortestPath {
	
	int task = 0;
	String inputFileName = null;
	String outputFileName = null;
	String logFileName = null;
	
	//maze is accessed as (y,x)
	HashMap<Integer,HashMap<Integer,Position>> maze;
	int xmax = -1;
	int ymax = -1;
	
	TreeMap<Character,Position> nodes = null;
	TreeMap<Character,TreeMap<Character,Double>> graph = null;
	
	public ShortestPath() {
		maze = new HashMap<Integer,HashMap<Integer,Position>>();
		nodes = new TreeMap<Character,Position>();
		graph = new TreeMap<Character,TreeMap<Character,Double>>();
	}
	
	public void parseInputFile() throws IOException{
		BufferedReader reader = new BufferedReader( new FileReader(inputFileName));
		String line = null;
		
		int y = 0;
		while( (line = reader.readLine()) != null) {
			
			if(xmax == -1) {
				xmax = line.length();
			}
			HashMap<Integer,Position> ythRow = new HashMap<Integer,Position>();
			for(int x = 0; x<line.length(); x++) {
				
				Position n = new Position();
				n.x = x;
				n.y = y;
				n.data = line.charAt(x);
				ythRow.put(x,n);
				if(n.data != ' ' && n.data != '*') {
					nodes.put(n.data, n);
				}
			}
			maze.put(y,ythRow);
			y++;
		}
		ymax = y;
		reader.close();
		calculateNeighbours();
	}
	
	public void calculateNeighbours() {
		
		for(int x=0; x<xmax; x++) {
			
			for(int y=0; y<ymax; y++) {
				Position temp = maze.get(y).get(x);
				if(x==0) {					
					temp.east = maze.get(y).get(x+1);
				} else if(x==xmax-1) {
					temp.west = maze.get(y).get(x-1); 
				} else {
					temp.east = maze.get(y).get(x+1);
					temp.west = maze.get(y).get(x-1);
				}
				if(y==0) {
					temp.south = maze.get(y+1).get(x);
				} else if(y==ymax-1) {
					temp.north = maze.get(y-1).get(x);
				} else {
					temp.south = maze.get(y+1).get(x);
					temp.north = maze.get(y-1).get(x);
				}
			}
		}
		
		//System.out.println(nodes);
	}

	public void findShortestPath(Position start, Position end, PrintWriter out, PrintWriter log) {
		
		for(Map.Entry<Character,Position> temp : nodes.entrySet()) {
			temp.getValue().visited = false;
			temp.getValue().pathcost = 0;
		}
		
		for(Map.Entry<Integer,HashMap<Integer,Position>> s : maze.entrySet()) {
			for(Map.Entry<Integer,Position> e : s.getValue().entrySet()) {
				e.getValue().visited = false;
			}
		}
		PriorityQueue<Position> q = new PriorityQueue<Position>(nodes.size(), new Comparator<Position>() {

			@Override
			public int compare(Position o1, Position o2) {
				// TODO Auto-generated method stub
				if(o1.eval > o2.eval) {
					return 1;
				} else if(o1.eval < o2.eval){
					return -1;
				} 
				
				if(o1.y == o2.y) {
					if(o1.x > o2.x) {
						return 1;
					} else {
						return -1;
					}
				} else if(o1.y > o2.y) {
					return 1;
				} else {
					return -1;
				}
			}
			
		});
		
		log.println("x,y,g,h,f");
		Position curr = null;
		
		start.visited = true;
		start.pathcost = 0;
		start.calculateHeuristic(end);
		start.eval = start.pathcost+start.heuristic;
		q.add(start);
		while(!q.isEmpty()) {
			curr = q.remove();
			
			if(!curr.visited || curr.data == start.data) {
				log.println(curr.x+","+curr.y+","+curr.pathcost+","+curr.heuristic+","+curr.eval);
			}
			curr.visited = true;
			if(curr.data == end.data) {
				out.println(start.data+","+end.data+","+end.pathcost);
				if(graph.containsKey(start.data)) {
					graph.get(start.data).put(end.data, end.pathcost);
				} else {
					TreeMap<Character,Double> temp = new TreeMap<Character,Double>();
					temp.put(end.data,end.pathcost);
					graph.put(start.data,temp);
				}
				if(graph.containsKey(end.data)) {
					graph.get(end.data).put(start.data, end.pathcost);
				} else {
					TreeMap<Character,Double> temp = new TreeMap<Character,Double>();
					temp.put(start.data,end.pathcost);
					graph.put(end.data,temp);
				}
				return;
			}
			List<Position> neighbours = curr.getNeighbours();
			
			for(Position n: neighbours) {
				
				boolean inQueue = false;
				Position nInQ = null;
				for(Position trav: q) {
					if(trav.x == n.x && trav.y == n.y) {
						inQueue = true;
						nInQ = trav;
					}
				}
				
				if(!n.visited && !inQueue) {
					n.parent = curr;
					n.pathcost = 1+curr.pathcost;
					n.calculateHeuristic(end);
					n.eval = n.pathcost + n.heuristic;
					q.add(n);
				} else if(!n.visited && inQueue) {
					n.calculateHeuristic(end);
					if( (curr.pathcost+1+n.heuristic) < nInQ.eval) {
						if(q.remove(nInQ)) {
							n.pathcost = curr.pathcost+1;
							n.parent = curr;
							n.eval = n.pathcost + n.heuristic;
							//child.depth = curr.depth+1;
							q.add(n);
						}						
					}
				}
			}
		}
	}
	
	public void findAllPairsShortestPath() throws IOException {
		
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFileName)));
		PrintWriter log = new PrintWriter(new BufferedWriter(new FileWriter(logFileName)));
		
		for(Map.Entry<Character,Position> s : nodes.entrySet()) {
			for(Map.Entry<Character,Position> e : nodes.entrySet()) {
				if(s.getKey() < e.getKey()) {
					log.println("from '"+s.getKey()+"' to '"+e.getKey()+"'");
					log.println("-----------------------------------------------");
					findShortestPath(s.getValue(),e.getValue(),out,log);
					log.println("-----------------------------------------------");
				}
			}
		}
		//System.out.println(graph);
		out.close();
		log.close();
	}
}