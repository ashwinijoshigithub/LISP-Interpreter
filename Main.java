/**
 * This is the main class of the lisp interpreter.
 * Takes input lisp expression from user, 
 * creates the s-expression tree internally
 * and prints s-expression in the dot notation.
 * Then it evaluates the parsed s-expression and displays the output.
 * @author: Ashwini Joshi
*/

import java.util.Scanner;

public class Main{
	public static void main(String args[]){
		System.out.println("Enter your input expressions:");

		Scanner scanner = new Scanner(System.in);
		String input = "";

		while(scanner.hasNextLine()){
			// Read input until a '$'' occurs
			String line = scanner.nextLine();

			if(line.equals("$") || line.equals("$$")){
				// Parse the current input SExp
				Parser parser = new Parser();
				Evaluator evaluator = new Evaluator();
				try{
					SExpression SExp = parser.parse(input);
					System.out.println("Dot Notation:");
					SExp.printSExpression();
					System.out.println("Evaluation Result:");
					SExpression resultSExp = evaluator.eval(SExp);
					resultSExp.printSExpression();
				}
				catch (customException c) {
					c.printErrorMessage();
				}

				// If '$$' occurs, endOfInput the program
				if(line.equals("$$")){
					System.out.println();
					System.out.println("Done!Bye!");
					System.exit(0);
				}

				// Reset the input after a '$' occurs
				input = "";

				// Ask to enter a new input
				System.out.println();
				System.out.println("Enter a new expression:");	
			}
			else{
				// Keep adding newly scanned input to current input SExp
				input = input + line + "\n";
			}
		}
	}
}