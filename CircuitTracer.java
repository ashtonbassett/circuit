
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

	/**
	 * Launch the program.
	 * 
	 * @param args three required arguments:
	 *             first arg: -s for stack or -q for queue
	 *             second arg: -c for console output or -g for GUI output
	 *             third arg: input file name
	 */
	public static void main(String[] args) {
		new CircuitTracer(args); // create this with args
	}

	/** Print instructions for running CircuitTracer from the command line. */
	private void printUsage() {
		// TODO: print out clear usage instructions when there are problems with
		// any command line args
		System.out.println("Usage: $ java CircuitTracer -s|-q -c|-g filename");
		System.out.println("Where -s chooses a stack state and -q chooses a queue state");
		System.out.println("and -c chooses for a console output and -g chooses a GUI output");
	}

	/**
	 * Set up the CircuitBoard and all other components based on command
	 * line arguments.
	 * 
	 * @param args command line arguments passed through from main()
	 */
	public CircuitTracer(String[] args) {
		ArrayList<TraceState> bestPaths = new ArrayList<TraceState>();
		CircuitBoard board = null;
		// TODO: parse and validate command line args - first validation provided
		if (args.length != 3) {
			printUsage();
			return; // exit the constructor immediately
		}

		if (!args[0].equals("-s") && !args[0].equals("-q")) {
			printUsage();
			return;
		}

		if (!args[1].equals("-c") && !args[1].equals("-g")) {
			printUsage();
			return;
		}
		// TODO: initialize the Storage to use either a stack or queue
		Storage<TraceState> stateStore = null;
		switch (args[0]) {
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
		// TODO: read in the CircuitBoard from the given file
		try {
			board = new CircuitBoard(args[2]);
		} catch (FileNotFoundException e) {
			System.out.println(e + " File not found");
			return;
		} catch (InvalidFileFormatException e) {
			System.out.println(e + " Invalid file format");
			return;
		}
		// TODO: run the search for best paths

		// finds the starting point and adds it to the stateStore
		int x = board.getStartingPoint().x;
		int y = board.getStartingPoint().y;

		if (board.isOpen(x + 1, y)) {
			stateStore.store(new TraceState(board, x + 1, y));
		}
		if (board.isOpen(x - 1, y)) {
			stateStore.store(new TraceState(board, x - 1, y));
		}
		if (board.isOpen(x, y + 1)) {
			stateStore.store(new TraceState(board, x, y + 1));
		}
		if (board.isOpen(x, y - 1)) {
			stateStore.store(new TraceState(board, x, y - 1));
		}

		while (!stateStore.isEmpty()) {
			TraceState currentState = stateStore.retrieve();

			// base case for the recursion
			if (currentState.isSolution()) {
				if (bestPaths.isEmpty() || currentState.pathLength() == bestPaths.get(0).pathLength()) {
					bestPaths.add(currentState);
				} else if (currentState.pathLength() < bestPaths.get(0).pathLength()) {
					bestPaths.clear();
					bestPaths.add(currentState);
				}

				// recursive case
			} else {

				x = currentState.getRow();
				y = currentState.getCol();

				if (currentState.isOpen(x - 1, y)) {
					stateStore.store(new TraceState(currentState, x - 1, y));
				}
				if (currentState.isOpen(x + 1, y)) {
					stateStore.store(new TraceState(currentState, x + 1, y));
				}
				if (currentState.isOpen(x, y - 1)) {
					stateStore.store(new TraceState(currentState, x, y - 1));
				}
				if (currentState.isOpen(x, y + 1)) {
					stateStore.store(new TraceState(currentState, x, y + 1));
				}
			}

		}

		// TODO: output results to console or GUI, according to specified choice
		switch (args[1]) {
			case "-c":
				for (TraceState path : bestPaths) {
					System.out.println(path.getBoard().toString());
				}
				break;

			case "-g":
				System.out.println("This system does not support GUI");
				break;

			default:
				printUsage();
				return;

		}
	}

} // class CircuitTracer