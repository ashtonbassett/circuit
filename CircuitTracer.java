import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Search for shortest paths between start and end points on a circuit board
 * as read from an input file using either a stack or queue as the underlying
 * search state storage structure and displaying output to the console or to
 * a GUI according to options specified via command-line arguments.
 * 
 * @author mvail, Ashton Bassett
 */
public class CircuitTracer {

	/** Launch the program. 
	 * 
	 * @param args three required arguments:
	 *  first arg: -s for stack or -q for queue
	 *  second arg: -c for console output or -g for GUI output
	 *  third arg: input file name 
	 */
	public static void main(String[] args) {
		new CircuitTracer(args); //create this with args
	}

	/** Print instructions for running CircuitTracer from the command line. */
	private void printUsage() {
		//TODO: print out clear usage instructions when there are problems with
		// any command line args
		System.out.println("Usage: $ java CircuitTracer <-s | -q> <-c | -g> <filename>");
		System.out.println("Where -s chooses a stack state and -q chooses a queue state\n" +
		 "And -c chooses for a console output and -g chooses a GUI output" );
	}
	
	/** 
	 * Set up the CircuitBoard and all other components based on command
	 * line arguments.
	 * 
	 * @param args command line arguments passed through from main()
	 */
	public CircuitTracer(String[] args) {
		//TODO: parse and validate command line args - first validation provided
		if (args.length != 3) {
			printUsage();
			return; //exit the constructor immediately
		}

		if(!args[0].equals("-s") || !args[0].equals("-q")){
			printUsage();
			return;
		}

		if(!args[1].equals("-c") || !args[1].equals("-g")) {
			printUsage();
			return;
		}
		//TODO: initialize the Storage to use either a stack or queue
		Storage<TraceState> stateStore = null;
		switch (args[0]){
			case "-s":
				stateStore = Storage.getStackInstance();
				break;

			case "-q":
				stateStore = Storage.getQueueInstance();
				break;

			default:
				printUsage();
				return;

		}
		//TODO: read in the CircuitBoard from the given file
		try{
			CircuitBoard board = new CircuitBoard(args[2]);
		}
		catch(FileNotFoundException e) {
			System.out.println("File not found"); 
			return;
		}
		catch(InvalidFileFormatException e) {
			System.out.println("Invalid file format");
			return;
		}
		//TODO: run the search for best paths
		ArrayList<TraceState> bestPaths = new ArrayList<TraceState>();
		
		//TODO: output results to console or GUI, according to specified choice
		switch(args[1]) {
			case "-c":
			System.out.println();
			break;

			case"-g":
			System.out.println("This system does not support GUI");
			break;

			default:
			printUsage();
			return;

		}
	}
	
} // class CircuitTracer