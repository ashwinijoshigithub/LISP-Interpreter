/**
 * This class stores user defined function as objects. It has name, 
 * formal parameters as a list anf function body as S-Expression.
 * @Author: Ashwini Joshi
*/
import java.util.HashMap;
import java.util.ArrayList;

public class Defun{

	String functionName;
	ArrayList <String> parameters;
	SExpression funBody;


	// Constructor for creating Defun object
	public Defun(String name, SExpression params, SExpression body) throws customException{
		functionName = name;
		parameters = getParamsAsList(params);
		funBody = body;
	}


	public static ArrayList<String> getParamsAsList(SExpression params) throws customException{
		// This function converts S-Expression list to an arraylist of parameters

		ArrayList <String> parameters = new ArrayList<>();

		while(!params.isNil()){
			SExpression car = params.car();
			// Integers cannot be given as formal parameters to the list
			if(car.isInteger())
				throw new customException("Parameters to function must be symbolic atoms. **", "Evaluation");
			//if(!car.isNil())
			parameters.add(car.name);
			params = params.cdr();
		}

		return parameters;
	}
}