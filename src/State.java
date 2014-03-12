import java.util.ArrayList;

public class State {
		
		char current;
		ArrayList<Character> visited;
		double pathcost;
		double heuristics;
		
		public State(char c) {
			this.current = c;
			this.visited = new ArrayList<Character>();
		}
		
	}