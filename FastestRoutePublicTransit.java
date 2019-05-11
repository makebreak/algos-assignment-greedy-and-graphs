/**
 * Public Transit
 * Author: Your Name and Carolyn Yao
 * Does this compile? Y/N
 */

/**
 * This class contains solutions to the Public, Public Transit problem in the
 * shortestTimeToTravelTo method. There is an existing implementation of a
 * shortest-paths algorithm. As it is, you can run this class and get the solutions
 * from the existing shortest-path algorithm.
 */
public class FastestRoutePublicTransit {

  /**
   * The algorithm that could solve for shortest travel time from a station S
   * to a station T given various tables of information about each edge (u,v)
   *
   * @param S the s th vertex/station in the transit map, start From
   * @param T the t th vertex/station in the transit map, end at
   * @param startTime the start time in terms of number of minutes from 5:30am
   * @param lengths lengths[u][v] The time it takes for a train to get between two adjacent stations u and v
   * @param first first[u][v] The time of the first train that stops at u on its way to v, int in minutes from 5:30am
   * @param freq freq[u][v] How frequently is the train that stops at u on its way to v
   * @return shortest travel time between S and T
   */
  public int myShortestTravelTime(
    int S,
    int T,
    int startTime,
    int[][] lengths,
    int[][] first,
    int[][] freq
  ) {
	  int numVertices = lengths[0].length;
	  
	  Boolean visited [] = new Boolean[numVertices]; // boolean array the size of # of vertices
	  
	  // Initialize all visited[] as false
	  for (int v = 0; v < numVertices; v++) {
	    visited[v] = false;
	  }
	  
	  int currentTime = startTime; // time from 5:30AM until reaching the destination station
	  
	  int u = S;  // u is currentStation, initially S, starting point
	  
	  // initialize variables to use in for loop below 
	  int minTime = Integer.MAX_VALUE; // shortest time to next station
	  int nextStation = u;	// station corresponding to minTime
	  int thisTripEndsAt = startTime; // trip time from current point
		  
	  // take shortest edge to another unvisited vertex until you get to T
	  while (u != T && visited[u] != true) {
	      // Mark u as visited.
		  visited[u] = true;
		  minTime = Integer.MAX_VALUE; // reset minTime
		  
		  // to search for shortest trip to next station
		  // we take the best answer so far (we need the one that remains after the for loop)
		  for (int i=0; i<numVertices; i++) {
			  // Where do you go from u?
			  // First figure out where you CAN go
			  if (lengths[u][i] != 0 && visited[i] != true) { // for all stations i that are reachable from u
				  
				  // find next train from u to i and calculate time it would take to get there
				  int nextTrain = first[u][i]; // take the first train from u to i
				  
				  while (nextTrain < currentTime) // find a train time to leave at or after current time
					  nextTrain += freq[u][i]; // nextTrain = time of the next train from u to i
				  
				  // add length of trip
				  thisTripEndsAt = nextTrain + lengths[u][i];
				  
				  // update the min for the for loop, 
				  // if this trip is shorter than previous ones for this u and i
				  if (thisTripEndsAt < minTime) {
					  minTime = thisTripEndsAt;
					  nextStation = i;
					  //System.out.println("current i is: " + i);
				  }
			  }
		  }
		  // Take the shortest trip to the next station and update the train variables
		  System.out.println("Current Station is: " + u);
		  u = nextStation;
		  currentTime = minTime;
		  System.out.println("Next station is: " + u + " and current time is " + currentTime);				  
	  }
	  return currentTime;
  }

  /**
   * Finds the vertex with the minimum time from the source that has not been
   * processed yet.
   * @param times The shortest times from the source
   * @param processed boolean array tells you which vertices have been fully processed
   * @return the index of the vertex that is next vertex to process
   */
  public int findNextToProcess(int[] times, Boolean[] processed) {
    int min = Integer.MAX_VALUE;
    int minIndex = -1;

    for (int i = 0; i < times.length; i++) {
      if (processed[i] == false && times[i] <= min) {
        min = times[i];
        minIndex = i;
      }
    }
    return minIndex;
  }

  public void printShortestTimes(int times[]) {
    System.out.println("Vertex Distances (time) from Source");
    for (int i = 0; i < times.length; i++)
        System.out.println(i + ": " + times[i] + " minutes");
  }

  /**
   * Given an adjacency matrix of a graph, implements
   * @param graph The connected, directed graph in an adjacency matrix where
   *              if graph[i][j] != 0 there is an edge with the weight graph[i][j]
   * @param source The starting vertex
   */
  public void shortestTime(int graph[][], int source) {
    int numVertices = graph[0].length;

    // This is the array where we'll store all the final shortest times
    int[] times = new int[numVertices];

    // processed[i] will true if vertex i's shortest time is already finalized
    Boolean[] processed = new Boolean[numVertices];

    // Initialize all distances as INFINITE and processed[] as false
    for (int v = 0; v < numVertices; v++) {
      times[v] = Integer.MAX_VALUE;
      processed[v] = false;
    }

    // Distance of source vertex from itself is always 0
    times[source] = 0;

    // Find shortest path to all the vertices
    for (int count = 0; count < numVertices - 1 ; count++) {
      // Pick the minimum distance vertex from the set of vertices not yet processed.
      // u is always equal to source in first iteration.
      // Mark u as processed.
      int u = findNextToProcess(times, processed);
      processed[u] = true;

      // Update time value of all the adjacent vertices of the picked vertex.
      for (int v = 0; v < numVertices; v++) {
        // Update time[v] only if is not processed yet, there is an edge from u to v,
        // and total weight of path from source to v through u is smaller than current value of time[v]
        if (!processed[v] && graph[u][v]!=0 && times[u] != Integer.MAX_VALUE && times[u]+graph[u][v] < times[v]) {
          times[v] = times[u] + graph[u][v];
        }
      }
    }

    printShortestTimes(times);
  }

  public static void main (String[] args) {
    /* length(e) */
    int lengthTimeGraph[][] = new int[][]{
      {0, 4, 0, 0, 0, 0, 0, 8, 0},	// [0][1] and [0][7]
      {4, 0, 8, 0, 0, 0, 0, 11, 0},
      {0, 8, 0, 7, 0, 4, 0, 0, 2},
      {0, 0, 7, 0, 9, 14, 0, 0, 0},
      {0, 0, 0, 9, 0, 10, 0, 0, 0},
      {0, 0, 4, 14, 10, 0, 2, 0, 0},
      {0, 0, 0, 0, 0, 2, 0, 1, 6},
      {8, 11, 0, 0, 0, 0, 1, 0, 7}, 
      {0, 0, 2, 0, 0, 0, 6, 7, 0} // [8][2]
    };
    FastestRoutePublicTransit t = new FastestRoutePublicTransit();
    t.shortestTime(lengthTimeGraph, 0);
    
    System.out.println();
    // set myShortestTravelTime parameters 
    int s = 0;
    int d = 8;
    int startTime = 0;	// number of minutes from 5:30 AM
    
    System.out.println("myShortestTravelTime from " + s + " to " + d);
    
    // first [u][v] - The time of the first train from u to v, int in minutes from 5:30am
	// change 0s to -1 or null 
    int first[][] = new int[][] {
      {-1, 4, -1, -1, -1, -1, -1, 8, -1},
      {4, -1, 8, -1, -1, -1, -1, 11, -1},
      {-1, 8, -1, 7, -1, 4, -1, -1, 2},
      {-1, -1, 7, -1, 9, 14, -1, -1, -1},
      {-1, -1, -1, 9, -1, 10, -1, -1, -1},
      {-1, -1, 4, 14, 10, -1, 2, -1, -1},
      {-1, -1, -1, -1, -1, 2, -1, 1, 6},
      {8, 11, -1, -1, -1, -1, 1, -1, 7},
      {-1, -1, 2, -1, -1, -1, 6, 7, -1}
    };
    
    // freq freq[u][v] How frequently does train from u go to v 
    // change 0s to -1 or null 
    int freq[][] = new int[][] {
        {-1, 4, -1, -1, -1, -1, -1, 8, -1},
        {4, -1, 8, -1, -1, -1, -1, 11, -1},
        {-1, 8, -1, 7, -1, 4, -1, -1, 2},
        {-1, -1, 7, -1, 9, 14, -1, -1, -1},
        {-1, -1, -1, 9, -1, 10, -1, -1, -1},
        {-1, -1, 4, 14, 10, -1, 2, -1, -1},
        {-1, -1, -1, -1, -1, 2, -1, 1, 6},
        {8, 11, -1, -1, -1, -1, 1, -1, 7},
        {-1, -1, 2, -1, -1, -1, 6, 7, -1}
      };
      // You can create a test case for your implemented method for extra credit below
      int myTime = t.myShortestTravelTime(s, d, startTime, lengthTimeGraph, first, freq);
      System.out.println("Time: " + myTime);
  } // end main 
}
