RoboTour
========

Name: 		Aakarsh Medleri Hire Math

----------------------------------------------------------------------

Structure of the program:

The application has 5 java files/classes:

tsp.java
 - Main class which will read the user input from command line arguments and perform the necessary operation based on the task.

Position.java
 - Position class represents a node in the map (2D matrix)
 
ShortestPath.java
 - Will calculate shortest path between all the nodes in the given map using A* algorithm and returns an adjacency list (graph)
   It employees Manhattan Distance as heuristics for A*.

 State.java
 - State class represents a state for TSP
   
OptimalTour.java
 - Will calculate optimal tour (tsp) for the input graph (from ShortestPath) using A* algorithm.
   It employees Minimum Spanning Tree as heuristics.
   
 ----------------------------------------------------------------------
 
 How to compile:
	
	-------------------------------------------
	Set Environment variable for Java 1.6: (or higher)
	
	In your .cshrc file, set JAVA_HOME and PATH as below:

	setenv JAVA_HOME /usr/usc/jdk/1.6.0_23
	setenv PATH /usr/usc/jdk/1.6.0_23/bin:${PATH}
	-------------------------------------------
	Then run below command to compile:
	
	javac tsp.java

------------------------------------------------------------------------

How to execute:

	java tsp -t 1 -i map1.txt -op output1_path_sgraph.txt -ol output1_log_sgraph.txt
	
	java tsp -t 1 -i map2.txt -op output2_path_sgraph.txt -ol output2_log_sgraph.txt
	
	java tsp -t 2 -i map1.txt -op output1_path_tour.txt -ol output1_log_tour.txt
	
	java tsp -t 2 -i map2.txt -op output2_path_tour.txt -ol output2_log_tour.txt
	
------------------------------------------------------------------------
