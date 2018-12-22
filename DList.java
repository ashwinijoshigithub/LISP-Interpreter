/** 
 * This class is for maintaining DList. Whenever a new function is defined using DEFUN,
 * it is added into dList hashmap along with its name.
 * @Author: Ashwini Joshi
*/

import java.util.HashMap;
import java.util.ArrayList;

public class DList{
	// Map to store functions 
	public static HashMap <String, Defun> dList = new HashMap<>();

	// Add Defun object contaning function deifnition and formal paramaters as a value for function name as key
	public static void addFunction(String functionName, Defun function){
		dList.put(functionName, function);
	}

	// Return Defun object for corresponding function name
	public static Defun getFunction(String functionName) throws customException{
		Defun defun = null;
		if(dList.containsKey(functionName))
			defun = dList.get(functionName);

		return defun;
	}

}