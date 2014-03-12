import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;
import java.io.IOException;;

public class OptimalTour {
	
	ArrayList<Position> unvisitedNodes;
	
	TreeMap<Character,Position> hNodes; 
	
	private double calculateHeuristics(char start, State s, TreeMap<Character,TreeMap<Character,Double>> graph) {
		
		for(Map.Entry<Character,Position> e: hNodes.entrySet()) {
			e.getValue().heuristic = 0;
			e.getValue().pathcost = 0;
			e.getValue().eval = 0;
			e.getValue().parent = null;
			e.getValue().visited = false;
		}
		
		
		
		PriorityQueue<Position> q = new PriorityQueue<Position>(hNodes.size(), new Comparator<Position>() {

			@Override
			public int compare(Position o1, Position o2) {
				// TODO Auto-generated method stub
				if(o1.pathcost > o2.pathcost) {
					return 1;
				} else if(o1.pathcost < o2.pathcost){
					return -1;
				}
				return 0;
			}
			
		});
		q.add(hNodes.get(start));
		//ArrayList<Position> visited = new ArrayList<Position>();
		Position curr = null;
		Position parent = null;
		//System.out.println(hNodes);
		double h = 0;
		
		while(!q.isEmpty()) {
			parent = curr;
			curr = q.remove();
			if(parent != null && curr != null) {
				h = h+curr.pathcost;
			}
			hNodes.get(curr.data).visited = true;
			for(Map.Entry<Character, Position> child: hNodes.entrySet()) {
				if(child.getValue().data != curr.data && !s.visited.contains(child.getKey())) {
					boolean inQueue = false;
					Position childInQ = null;
					for(Position t: q) {
						if(t.data == child.getKey()) {
							inQueue = true;
							childInQ = t;
						}
					}
					if(!child.getValue().visited && !inQueue) {
						child.getValue().parent = curr;
						child.getValue().pathcost = graph.get(curr.data).get(child.getKey());
						q.add(child.getValue());
					} else if(inQueue) {
						if( graph.get(curr.data).get(child.getKey()) < childInQ.pathcost ) {
							if(q.remove(childInQ)) {
								child.getValue().parent = curr;
								child.getValue().pathcost = graph.get(curr.data).get(child.getKey());
								q.add(child.getValue());
							}
						}
					}
				}
			}
		}
		//unvisitedNodes.remove(start);
		//return curr.pathcost;
		return h;
	}
	
	
	public void findOptimalTour(TreeMap<Character,Position> nodes, TreeMap<Character,TreeMap<Character,Double>> graph, String outputFileName, String logFileName) throws IOException{
		
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFileName)));
		PrintWriter log = new PrintWriter(new BufferedWriter(new FileWriter(logFileName)));
		
		unvisitedNodes = new ArrayList<Position>();
		
		//clear the node attributes
		for(Map.Entry<Character,Position> e: nodes.entrySet()) {
			e.getValue().heuristic = 0;
			e.getValue().pathcost = 0;
			e.getValue().eval = 0;
			e.getValue().parent = null;
			e.getValue().visited = false;
			unvisitedNodes.add(e.getValue());
		}
		
		PriorityQueue<State> q = new PriorityQueue<State>(nodes.size(), new Comparator<State>() {

			@Override
			public int compare(State o1, State o2) {
				// TODO Auto-generated method stub
				if(o1.pathcost+o1.heuristics > o2.pathcost+o2.heuristics) {
					return 1;
				} else if(o1.pathcost+o1.heuristics < o2.pathcost+o2.heuristics){
					return -1;
				} else if(o1.visited.size() < o2.visited.size()) {
					return 1;
				} else {
					return -1;
				}
			}
			
		});
		
		hNodes = new TreeMap<Character,Position>();
		for(Map.Entry<Character, Position> n: nodes.entrySet()) {
			Position temp = new Position();
			temp.data = n.getValue().data;
			temp.x = n.getValue().x;
			temp.y = n.getValue().y;
			hNodes.put(temp.data, temp);
		}
		
		State s = new State('A');
		//s.visited.add('A');
		s.pathcost = 0;
		s.heuristics = calculateHeuristics(s.current,s, graph);
		q.add(s);
		
		State curr = null;
		String path = "";
		
		while(!q.isEmpty()) {
			curr = q.remove();
			log.print('A');
			for(char c: curr.visited) {
				log.print(c);
			}
			double f = curr.pathcost+curr.heuristics;
			log.println(","+curr.pathcost+","+curr.heuristics+","+f);
			if(nodes.size() == curr.visited.size()+1) {
				break;
			}			
			for(Map.Entry<Character, Position> trav: nodes.entrySet()) {
				if(!curr.visited.contains(trav.getValue().data) && curr.current != trav.getKey() && trav.getKey() != 'A') {
					
					
					State ts = new State(trav.getKey());
					ts.visited.addAll(curr.visited);
					ts.visited.add(trav.getKey());
					ts.pathcost = curr.pathcost + graph.get(curr.current).get(trav.getKey());
					ts.heuristics = calculateHeuristics(ts.current,ts, graph);
					q.add(ts);
				}	
			}
		}
		
		State last = new State('A');
		last.visited.addAll(curr.visited);
		last.visited.add('A');
		last.pathcost = curr.pathcost + graph.get(curr.current).get('A');
		last.heuristics = calculateHeuristics(last.current,last, graph);
		log.print('A');
		for(char c: last.visited) {
			log.print(c);
		}
		double f = last.pathcost+last.heuristics;
		log.println(","+last.pathcost+","+last.heuristics+","+f);
		
		out.println('A');
		for(char c: last.visited) {
			out.println(c);
		}
		out.println("Total Tour Cost: "+f);
		
		out.close();
		log.close();
	}

}
