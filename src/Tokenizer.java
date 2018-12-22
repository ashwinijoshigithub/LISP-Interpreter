/**
 * This is a helper class to extract tokens from the the input. 
 * First, it replaces all kinds of spaces (multiple, whitespace etc.)
 * Later, it inserts spaces vefore and after - '(', ')', '.'
 * Now, after splitting at a single space (' '), tokens are obtained.
 * @author: Ashwini Joshi 
*/

import java.util.regex.*;
import java.util.HashMap;

public class Tokenizer{

	String inputLine;
	int numberOfTokens;
	int currentToken;
	String[] tokens;
    boolean intLimitExceeded;
    boolean symbolLimitExceeded;
    int countBrace;

	public Tokenizer(String input){
		inputLine = input;

		// Insert spaces before and after ')', '(', '.' to get tokens
		inputLine = inputLine.replaceAll("\\(", " ( ");
		inputLine = inputLine.replaceAll("\\)", " ) ");
		inputLine = inputLine.replaceAll("\\.", " . ");

		// Replace all types of spaces with a single space " "
		inputLine = inputLine.replaceAll("\\s+", " ");
		// Trim leading and trailing spaces
		inputLine = inputLine.trim();

		// Since all types are spaces are replaced with a single space, we can get token by splitting at 
		tokens = inputLine.split(" ");

		// Number of tokens and current token
		numberOfTokens = tokens.length;
		currentToken = 0;

        // No limit exceeded yet
        intLimitExceeded = false;
        symbolLimitExceeded = false;

        // No braces seen
        countBrace = 0;
	}

	// Check if an identifier
	public boolean isIdentifier(){
		// Pattern to match symbols starting with upper case followed by 0 or more upper case letters and digits
		Matcher matchIdentifier = Pattern.compile("[A-Z][A-Z0-9]*").matcher(tokens[currentToken]);
		return matchIdentifier.matches();
	}

	// Get the current token as identifier
	public SExpression getIdentifier() throws customException{
		String identifier = tokens[currentToken];
		// Check for length limit
		if(identifier.length() > 10){
			symbolLimitExceeded = true;
			throw new customException(getError(), "Parsing");
		}
		currentToken++;
		return SExpression.getFromTable(identifier);
	}

	// Check if an integer
	public boolean isInteger(){
		// Pattern to match integers with optional '+' or '-' sign
		Matcher matchInteger= Pattern.compile("[+-]?\\d+").matcher(tokens[currentToken]);
		return matchInteger.matches();
	}

	// Get integer at current index
	public SExpression getInteger() throws customException {
		String num = tokens[currentToken];
		// Check for length limit
		if(num.length() > 6){
			intLimitExceeded = true;
			throw new customException(getError(), "Parsing");
		}
		currentToken++;
		return new SExpression(Integer.parseInt(num));		
	}

	// Check if current token is a dot
	public boolean isDot(){
		return tokens[currentToken].equals(".");
	}

	// heck if current token is a left brace
	public boolean isLeftBrace(){
		return tokens[currentToken].equals("(");
	} 

	// Check if current token is a right brace
	public boolean isRightBrace(){
		return tokens[currentToken].equals(")");
	} 

	// Check if input has more token left to be parsed
	public boolean hasMoreTokens(){
		return currentToken < numberOfTokens;
	}

	// Skip current token
	public void skipToken(){
		currentToken++;
	}

	// Check if current index has any invalid character
	public String checkIfInvalid(){
		String found = null;
		// Check if symbol or integer has invalid characters
		Matcher matchInvalidCharacters = Pattern.compile("[~#@$*%{}<>\\[\\]|\"\\_^]").matcher(tokens[currentToken]);
		if(matchInvalidCharacters.find()){
			int start_index = matchInvalidCharacters.start();
			found = Character.toString(tokens[currentToken].charAt(start_index));
		};

		return found;
	}

	// Check if input has lower case character 
	public String checkIfLowerCase(){
		String found = null;
		// Check if lower case character is entered
		Matcher matchLowerCaseCharacter = Pattern.compile("[a-z]").matcher(tokens[currentToken]);
		if(matchLowerCaseCharacter.find()){
			int start_index = matchLowerCaseCharacter.start();
			found = Character.toString(tokens[currentToken].charAt(start_index));
		}

		return found;
	}

	// Throw error for invalid input depending upon what is found at the index
	public String getError(){
		String error;
		if(!hasMoreTokens()){
			if(countBrace > 0)
				error = "Missing right brace.**";
			else
				error = "Unexpected end of input expression.**";
		}
		else if(isDot()){
			error = "Unexpected Dot.**";
		}
		else if(isLeftBrace()){
			error = "Unexpected Left Brace.**";
		}
		else if(isRightBrace()){
			error = "Unexpected Extra Right Brace.**";
		}
		else if(isLeftBrace()){
			error = "Unexpected Left Brace.**";
		}
		else if(symbolLimitExceeded){
			error = "Symbol Length Limit Exceeded for Symbol '" + tokens[currentToken] + "' **";
			symbolLimitExceeded = false;
		}
		else if(intLimitExceeded){
			error = "Integer Length Limit Exceeded for Integer '" + tokens[currentToken] + "' **";
			intLimitExceeded = false;
		}
		else if(checkIfInvalid() != null){
			error = "Invalid character '" + checkIfInvalid() + "' found while parsing input expression. **";
		}
		else if(checkIfLowerCase() != null){
			error = "Lower Case character '" + checkIfLowerCase() + "' found while parsing parsing input expression. **";
		}
		else if(tokens[currentToken].equals("+")){
			error = "Invalid Character '+' found while parsing. If you wish to enter an integer, please remove spaces.";
		}
		else if(tokens[currentToken].equals("-")){
			error = "Invalid Character '-' found while parsing. If you wish to enter an integer, please remove spaces.";
		}
		else
			error = "Unexpected token '" + tokens[currentToken] + "' found while parsing input. Please check if you have entered a valid input expression.**";

		return error;
	}
}