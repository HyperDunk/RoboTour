import java.io.IOException;


public class tsp {
	
	public static void main(String[] args) {
		if(args.length != 8) {
			System.out.println("Incorrect # of parameters");
			return;
		}
		
		ShortestPath app = new ShortestPath();
		OptimalTour tspApp = new OptimalTour();
		
		for(int i=0; i< args.length; i=i+2) {
			if( (args[i].equals("-t"))) {
				app.task = Integer.parseInt(args[i+1]);
			} else if(args[i].equals("-i")) {
				app.inputFileName = args[i+1];
			} else if(args[i].equals("-op")) {
				app.outputFileName = args[i+1];
			} else if(args[i].equals("-ol")) {
				app.logFileName = args[i+1];
			} else {
				System.out.println("Incorrect parameters");
				return;
			}
		}
		
		try {
			app.parseInputFile();
			
			if(app.task == 1) {
				app.findAllPairsShortestPath();
			} else if (app.task == 2) {
				app.findAllPairsShortestPath();
				if( app.nodes != null && app.graph != null) {
					tspApp.findOptimalTour(app.nodes, app.graph, app.outputFileName, app.logFileName);
				}
			}
			
		} catch(IOException e) {
			System.err.println("Screwed!!");
			return;
		}
	}

}
