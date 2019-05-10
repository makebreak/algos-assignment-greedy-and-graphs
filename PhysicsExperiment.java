/**
 * Physics Experiment
 * Author: Your Name and Carolyn Yao
 * Does this compile or finish running within 5 seconds? Y/N
 */

/**
 * This class implements a greedy scheduler to assign students
 * to steps in a physics experiment. There are three test cases in the main
 * method. Please read through the whole file before attempting to code the
 * solution.
 *
 * You will only be graded on code you add to the scheduleExperiments method.
 * Do not mess with the existing formatting and indentation.
 * You don't need to use the helper methods, but if they come in handy setting
 * up a custom test case, feel free to use them.
 */
public class PhysicsExperiment {

  /**
   * The actual greedy scheduler you will be implementing!
   * @param numStudents The number of students who can participate, m
   * @param numSteps The number of steps in the experiment, n
   * @param signUpTable An easy lookup tool, signUpTable[x][Y] = student X signed up or did not sign up for step Y.
   *      Example:
          signUpTable[1][3] = 1 if Student 1 signed up for Step 3
          signUpTable[1][3] = 0 if Student 1 didn't sign up for Step 3
   * @return scheduleTable: a table similar to the signUpTable where scheduleTable[X][Y] = 1 means
   *     student X is assigned to step Y in an optimal schedule
   */
  public int[][] scheduleExperiments(
    int numStudents,
    int numSteps,
    int[][] signUpTable
  ) {
    // Your scheduleTable is initialized as all 0's so far. Your code will put 1's
    // in the table in the right places based on the return description
    int[][] scheduleTable = new int[numStudents + 1][numSteps + 1];
    
    // starting point is currentStep = 1
    // check how far each student can go from a given point
    // keep student who can go farthest - greedy choice
    // start next at end point of chosen sequence
	int currentStep = 1;
	int lastConsecNum = currentStep;	
	int trackArray[] = new int[numStudents+1];
    while(currentStep < numSteps) {
    	// start at step 1, lookupTable[x][1] = 1 if student x can do step 1
	    // check how far every student can go from this and each subsequent step    
    	int endpt = currentStep-1; // making endpt before current starting point will force us to change endpt later
    	int student = 0;
    	boolean longestSoFar = false;
    	
	    for (int x=1; x<=numStudents; x++) { // for every student x
	    	int count = 0; // keep a counter to track for how many consecutive steps the student can volunteer
    		int next = currentStep; 
	    	// check if first student can do it and if they can do the next one
	    	if(signUpTable[x][currentStep] == 1) {
	    		longestSoFar = true;
	    		try {
		    		while (signUpTable[x][++next] == 1) { //stop when next is not consecutive 
		    			count++;
		    			if (next >= numSteps) {
		    				break;
		    			}
		    			//System.out.println("next step for student " + x + " is " + next);
		    		}	    
		    		// set last number in sequence
		    		if (count == 0)
		    			lastConsecNum = currentStep;	// no subsequent step after currentStep 
		    		else if(signUpTable[x][next] == 1)
		    			lastConsecNum = next; 
		    		else lastConsecNum = next - 1;
		    		//System.out.println("last step for student " + x + " from " + y + " is " + lastConseqNum);
		    		//System.out.println("count is " + count);
	    		} catch (ArrayIndexOutOfBoundsException e) {
	        		System.out.println(" out of bounds");
	        	}
	    		
	    		// store last point in sequence for student x
	    		trackArray[x] = lastConsecNum;
	    		
	    		// check if any other student already reached this point
	    		for(int i=0; i < x; i++) {
	    			try {
			    		if (trackArray[i] >= trackArray[x]) { // another student already reached this point
			    			longestSoFar = false;	// this is not the longest sequence we have seen
			    		}
		    		} catch (ArrayIndexOutOfBoundsException e) {
		    			System.out.println("error at index");
		    		}
	    		} // end for loop
	    		//setting scheduleTable
    			//System.out.println(longestSoFar);
    			if (longestSoFar == true) {
		    		//System.out.println("student " + x + " reached " + lastConseqNum + " from " + currentStep);
		    		// keep track of this, perhaps through count array or hashmap
		    		endpt=lastConsecNum;
		    		student = x;
    			}	    				
	    	} // end if with current currentStep as true 
	    	else continue;
	    }	// end for loop iterating through students for current currentStep step
	    
		// take longest count and 
		// schedule student for steps currentStep through last pt in longest sequence
	    if (endpt >= currentStep) {
			for (int j = currentStep; j<= endpt; j++) {
				scheduleTable[student][j] = 1;
			}	    	
	    }
		
		// set next currentStep now that we've reached longest sequence
		// set next currentStep 
		if (endpt < numSteps)
			currentStep = endpt+1; 
		else if (endpt == numSteps)
			currentStep = endpt; 

    } // end while 

    return scheduleTable;
  }

  /**
   * Makes the convenient lookup table based on the steps each student says they can do
   * @param numSteps the number of steps in the experiment
   * @param studentSignUps student sign ups ex: {{1, 2, 4}, {3, 5}, {6, 7}}
   * @return a lookup table so if we want to know if student x can do step y,
      lookupTable[x][y] = 1 if student x can do step y
      lookupTable[x][y] = 0 if student x cannot do step y
   */
  public int[][] makeSignUpLookup(int numSteps, int[][] studentSignUps) {
    int numStudents = studentSignUps.length;
    int[][] lookupTable = new int[numStudents+1][numSteps + 1];
    for (int student = 1; student <= numStudents; student++) {
      int[] signedUpSteps = studentSignUps[student-1];
      for (int i = 0; i < signedUpSteps.length; i++) {
        lookupTable[student][signedUpSteps[i]] = 1;
      }
    }
    return lookupTable;
  }

  /**
   * Prints the optimal schedule by listing which steps each student will do
   * Example output is Student 1: 1, 3, 4
   * @param schedule The table of 0's and 1's of the optimal schedule, where
   *   schedule[x][y] means whether in the optimal schedule student x is doing step y
   */
  public void printResults(int[][] schedule) {
    for (int student = 1; student < schedule.length; student++) {
      int[] curStudentSchedule = schedule[student];
      System.out.print("Student " + student + ": ");
      for (int step = 1; step < curStudentSchedule.length; step++) {
        if (curStudentSchedule[step] == 1) {
          System.out.print(step + " ");
        }
      }
      System.out.println("");
    }
  }

  /**
   * This validates the input data about the experiment step sign-ups.
   * @param numStudents the number of students
   * @param numSteps the number of steps
   * @param signUps the data given about which steps each student can do
   * @return true or false whether the input sign-ups match the given number of
   *    students and steps, and whether all the steps are guaranteed at least
   *    one student.
   */
  public boolean inputsValid(int numStudents, int numSteps, int signUps[][]) {
    int studentSignUps = signUps.length;

    // Check if there are any students or signups
    if (numStudents < 1 || studentSignUps < 1) {
      System.out.println("You either did not put in any student or any signups");
      return false;
    }

    // Check if the number of students and sign-up rows matches
    if (numStudents != studentSignUps) {
      System.out.println("You input " + numStudents + " students but your signup suggests " + signUps.length);
      return false;
    }

    // Check that all steps are guaranteed in the signups
    int[] stepsGuaranteed = new int[numSteps + 1];
    for (int i = 0; i < studentSignUps; i++) {
      for (int j = 0; j < signUps[i].length; j++) {
        stepsGuaranteed[signUps[i][j]] = 1;
      }
    }
    for (int step = 1; step <= numSteps; step++) {
      if (stepsGuaranteed[step] != 1) {
        System.out.println("Your signup is incomplete because not all steps are guaranteed.");
        return false;
      }
    }

    return true;
  }

  /**
   * This sets up the scheduling test case and calls the scheduling method.
   * @param numStudents the number of students
   * @param numSteps the number of steps
   * @param signUps which steps each student can do, in order of students and steps
   */
  public void makeExperimentAndSchedule(int experimentNum, int numStudents, int numSteps, int[][] signUps) {
    System.out.println("----Experiment " + experimentNum + "----");
    if (!inputsValid(numStudents, numSteps, signUps)) {
      System.out.println("Experiment signup info is invalid");
      return;
    }
    int[][] signUpsLookup = makeSignUpLookup(numSteps, signUps);
    int[][] schedule = scheduleExperiments(numStudents, numSteps, signUpsLookup);
    printResults(schedule);
    System.out.println("");
  }

  /**
   * You can make additional test cases using the same format. In fact the helper functions
   * I've provided will even check your test case is set up correctly. Do not touch any of
   * of the existing lines. Just make sure to comment out or delete any of your own test cases
   * when you submit. The three experiment test cases existing in this main method should be
   * the only output when running this file.
   */
  public static void main(String args[]){
    PhysicsExperiment pe = new PhysicsExperiment();

    // Experiment 1: Example 1 from README, 3 students, 6 steps:
    int[][] signUpsExperiment1 = {{1, 2, 3, 5}, {2, 3, 4}, {1, 4, 5, 6}};
    pe.makeExperimentAndSchedule(1, 3, 6, signUpsExperiment1);

    // Experiment 2: Example 2 from README, 4 students, 8 steps
    int[][] signUpsExperiment2 = {{5, 7, 8}, {2, 3, 4, 5, 6}, {1, 5, 7, 8}, {1, 3, 4, 8}};
    pe.makeExperimentAndSchedule(2, 4, 8, signUpsExperiment2);

    // Experiment 3: Another test case, 5 students, 11 steps
    int[][] signUpsExperiment3 = {{7, 10, 11}, {8, 9, 10}, {2, 3, 4, 5, 7}, {1, 5, 6, 7, 8}, {1, 3, 4, 8}};
    pe.makeExperimentAndSchedule(3, 5, 11, signUpsExperiment3);
  }
}
