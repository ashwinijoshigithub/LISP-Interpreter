/**
 * Class for throwing custom exceptions in the tokenizer while parsing the input
 * @Author: Ashwini Joshi
*/

import java.util.*;

public class customException extends Exception{
	String error;

	public customException(String errorMessage, String errorType){
		// Invoke constructor with error message and error type (Parser or Evaluation)
		error = "** " + errorType + " Error in the input expression: ";
		error = error + errorMessage;
	}


	public void printErrorMessage(){
		System.out.println(error);
	}

}