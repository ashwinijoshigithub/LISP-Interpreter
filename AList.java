/**
 * This class maintains AList structure. It stores list of actual arguments while the
 * the function is being executed. Once execution is done, it removes those parameters
 * from the hashmap. Hashmap stores a list for each parameter since there can be multiple scopes.
 * @author: Ashwini Joshi
*/

import java.util.HashMap;
import java.util.ArrayList;

public class AList{
	// HashMap as AList
	HashMap<String, ArrayList<SExpression>> aListMap = new HashMap<>();

	// Empty constructor
	public AList(){

	}


	public SExpression getVal(SExpression SExp) throws customException{
		// Return the most current value of the corresponding S-Expression atom
		String name = SExp.name;
		if(aListMap.containsKey(name)){
			ArrayList <SExpression> list = aListMap.get(name);
			return list.get(0);
		}
		else{
			throw new customException("Unbound atom " + name, "Evaluation");
		}
	}


	public static ArrayList<SExpression> getArgumentsAsList(SExpression args) throws customException{
		// This function converts S-Expression of arguments to a list for adding into AList structure
		ArrayList <SExpression> arguments = new ArrayList<>();
		while(!args.isNil()){
			SExpression car = args.car();
			if(!car.isNil())
				arguments.add(car);
			args = args.cdr();
		}

		return arguments;
	}


	public void addPairs(String functionName, ArrayList<String> params, ArrayList<SExpression> args) throws customException{
		// This function takes formal parameters and actual parameters as list and adds into AList structure

		int paramsSize = params.size();
		int argsSize = args.size();

		// If sizes differ, raise appropriate error with function name
		if(paramsSize != argsSize)
			throw new customException("Function " + functionName + " expects " + paramsSize + " arguments. " + argsSize + " given. **", "Evaluation");

		// Keep adding arguments one by one for each parameter at the front of the list 
		for(int i = 0; i < paramsSize; i++){
			String parameter = params.get(i);
			SExpression argument = args.get(i);
			ArrayList <SExpression> list = new ArrayList<>();
			if(aListMap.containsKey(parameter))
				list = aListMap.get(parameter);
			list.add(0, argument);
			aListMap.put(parameter, list);	
		}		
	}


	public void destroyPairs(ArrayList<String> params){
		// After function execution, remove latest arguments from the AList structure

		int paramsSize = params.size();
		for(int i = 0; i < paramsSize; i++){
			String parameter = params.get(i);
			ArrayList <SExpression> list = aListMap.get(parameter);
			list.remove(0);
			aListMap.put(parameter, list);
		}		
	}
}