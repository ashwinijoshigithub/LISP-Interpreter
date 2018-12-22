
/**
 * Defines methods to read a lisp code and convert it to corresponding SExp obj.
 * Uses recursive descent method to parse the S-Expression
 * Error handling is done and appropriate error messages are returned in case
 * @author: Ashwini Joshi
*/

import java.util.*;

public class Parser {

	public SExpression parse(String inputBuffer) throws customException{
		// handle empty sexpression
		if(inputBuffer.isEmpty()){
			throw new customException("Empty expression entered. Please enter a valid input. **", "Parse");

		}
		else{
			// Get tokens from the input expression
			Tokenizer tokenizer = new Tokenizer(inputBuffer);

			// Start parsing Car of the expression, 
			// Since no previous left brace is seen, called with seenLeftBrace = false
			SExpression SExp = parseCar(tokenizer, false);

			// If some characters are left after complete parsing like extra braces or invalid identifiers, throw appropriate error
			if(tokenizer.hasMoreTokens()){
				throw new customException(tokenizer.getError(), "Parse");
			}

			return SExp;
		}
	}

	public SExpression parseCar(Tokenizer tokenizer, boolean seenLeftBrace) throws customException{

		if(tokenizer.hasMoreTokens()){
			if(tokenizer.isLeftBrace()){
				// Increment count of open brace
				tokenizer.countBrace++;
				tokenizer.skipToken();

				if(tokenizer.isRightBrace()){
					tokenizer.skipToken();
					return SExpression.getFromTable("NIL");
				}

				// Called with seenLeftBrace = true since a brace is seen
				SExpression car = parseCar(tokenizer, true);

				// Otherwise parse rest of the input as Cdr
				if(tokenizer.hasMoreTokens()){
					SExpression cdr = parseCdr(tokenizer, true);

					// Form new Sexpression with returned Car and Cdr parts
					return new SExpression(car, cdr);					
				}

				return car;

			}
			else if(tokenizer.isRightBrace()){
				// Decrement count of open brace
				tokenizer.countBrace--;
				if(seenLeftBrace){
					tokenizer.skipToken();
					return SExpression.getFromTable("NIL");
				}
				else{
					// If right brace occurs without a previously seen left brace, throw error
					throw new customException(tokenizer.getError(), "Parse");
				}
			}
			else if(tokenizer.isIdentifier()){
				// Return SExpression for corresponding identifier
				SExpression SExp = tokenizer.getIdentifier();
				return SExp;
			}
			else if(tokenizer.isInteger()){
				// Get SExpression for corresponding integer
				SExpression SExp = tokenizer.getInteger();
				return SExp;			
			}
			else{
				// If not symbol, integer, left brace or right brace with a previous seen left brace, input is invalid
				throw new customException(tokenizer.getError(), "Parse");
			}
		}
		else{
			// If no more tokens left and still parseCar is called, input is invalid
			throw new customException(tokenizer.getError(), "Parse");
		}
	}

	public SExpression parseCdr(Tokenizer tokenizer, boolean seenLeftBrace) throws customException{
		// For parsing cdr part of input expression
		if(tokenizer.hasMoreTokens()){
			if(tokenizer.isRightBrace()){
				tokenizer.skipToken();
				// Decrement count of open brace
				tokenizer.countBrace--;
				// Left part is parsed and right brace occurs which means it is NIL
				return SExpression.getFromTable("NIL");
			}
			else if(tokenizer.isDot()){
				if(seenLeftBrace){
					tokenizer.skipToken();

					// A dot means rest of the part is cdr part of the SExpression
					// It can be an integer, symbolic atom or a new non-atomic Sexpression so call parseCar to parse it
					SExpression cdr = parseCar(tokenizer, false);

					// Once the cdr brace occurs, return the above cdr expression
					if(tokenizer.hasMoreTokens() && tokenizer.isRightBrace()){
						tokenizer.skipToken();
						// Decrement count of open brace
						tokenizer.countBrace--;
						return cdr;
					}
					else{
						// If a right brace doesn't occur after parsing input after '.', input is invalid
						throw new customException(tokenizer.getError(), "Parse");
					}
				}
				else{
					// If a dot occurs without previously seen left brace, input is invalid
					throw new customException(tokenizer.getError(), "Parse");
				}			
			}
			else{
				// If no dot or right brace occurs, parse rest of the input as new input having both car and cdr
				SExpression car = parseCar(tokenizer, false);
				SExpression cdr = parseCdr(tokenizer, false);
				return new SExpression(car, cdr);
			}
		}
		else{
			// If no more tokens left and still parseCdr is called, input is invalid
			throw new customException(tokenizer.getError(), "Parse");
		}
	}
}