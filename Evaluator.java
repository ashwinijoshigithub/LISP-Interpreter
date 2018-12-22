/**
 * This class contains functions for evaluation of the s-expression tree.
 * LISP function in design notation used for evaluation are as follows:

 	eval[ exp, aList] =
		[ atom[exp] -->	[ int[exp] --> exp;
						| eq[exp,T] -->	T;									
						| eq[exp,NIL] --> NIL;											
						| in[exp,aList]	-->	getVal[exp,aList];
						| T	--> ***unbound!***]		
		| atom[car[exp]] --> [ eq[car[exp],QUOTE] --> cadr[exp];
						     | eq[car[exp],COND] --> evcon[cdr[exp], aList];
						     | T  --> apply[car[exp], evlis[cdr[exp],aList], aList]]
		| T	-->  ***not a Lisp expression!*** ]

	evlis[list, aList] = 
		[ null[list] --> NIL;
		| T --> cons[ eval[car[list], aList], evlis[cdr[list], aList]]]

	evcon[be, aList]	=   // be is of form ((b1 e1) ... (bn en))
		[ null[be] --> ***exception!***
		| eval[caar[be], aList] --> eval[cadar[be], aList];
		| T --> evcon[cdr[be], aList]]

	apply[ f, x, aList] =
		[ atom[f] --> [	eq[f, CAR] -->	caar[x];
					  |	eq[f, CDR] -->	cdar[x];
					  |	eq[f, CONS]	-->	cons[car[x], cdr[x]];
					  |	eq[f, ATOM]	-->	atom[car[x]];
					  |	eq[f, NULL]	-->	null[car[x]];
					  |	eq[f, EQ] --> eq[car[x], cadr[x]]; … …
					  |	T --> eval[ cdr[getval[f , dList]],
					  addpairs[car[getval[f, dList]], x, aList] ]; ];
	    | T	-->	***Not a Lisp expression!***;  ]

 * @author: Ashwini Joshi
*/

import java.util.HashMap;
import java.util.ArrayList;

public class Evaluator{
	AList aList;

	// Contructor to invoke new AList for each
	public Evaluator(){
		aList = new AList();
	}

	public SExpression eval(SExpression SExp) throws customException{
		// This function implements eval function. 

		// If S-Expression is an atom, 
		// it gets the value from either symbol table or AList
		if(SExp.isAtom()){
			if(SExp.isT() || SExp.isNil() || SExp.isInteger()){
				return SExp;
			}
			else
				return aList.getVal(SExp);
		}

		// If not atom, take car and check what kind of function it is.
		else if(SExp.car().isSymbol()){
			SExpression car = SExp.car();
			SExpression cdr = SExp.cdr();
			String name = car.name;

			if(name.equals("QUOTE")){
				// If valid arguments are given, just return cdr of the S-Expression
				String error = checkIfValidArgs("QUOTE", cdr);
				if(error != null)
					throw new customException(error, "Evaluation");
				else
					return cdr.car();
			}
			else if(name.equals("COND")){
				// For COND, call evcon
				if(cdr.isNil())
					throw new customException("No arguments given to COND. **", "Evaluation");
				return evcon(cdr);
			}
			else if(name.equals("DEFUN")){
				// If Defun, check if it defined properly with function name, parameters and body

				String error = checkIfValidDefun(cdr);
				if(error != null)
					throw new customException(error, "Evaluation");

				// If all arguments are valid, create a Defun object with name, params and body
				SExpression functionName = cdr.car().car();
				SExpression parameters = cdr.car().cdr().car();
				SExpression funcBody = cdr.cdr().car();
				Defun defun = new Defun(functionName.name, parameters, funcBody);
				// Add the function to DList
				DList.addFunction(functionName.name, defun);

				return SExpression.getFromTable(functionName.name);
			}
			// else if(cdr.isSymbol())
			// 	throw new customException("Could not evaluate" + SExp.displayTree() + " .**", "Evaluation");

			else{
				SExpression evlisResult;
				try{
					evlisResult = evlis(cdr);
				}
				catch(NullPointerException e){
					throw new customException("Could not evaluate " + SExp.displayTree() + " **", "Evaluation");
				}
				return apply(car, evlisResult);
			}
		}
		else{
			throw new customException("Not a LISP Expression! **", "Evaluation");
		}
	}


	public SExpression evcon(SExpression be) throws customException{
		// This function implemets evcon function.

		if(be.isNil()){
			throw new customException("Error in the boolean expression.** ", "Evaluation");
		}

		String error = checkIfValidArgs("EVCON", be.car());
		if(error != null)
			throw new customException(error, "Evaluation");

		SExpression bool = be.car().car();
		SExpression expression = be.car().cdr().car();
		SExpression boolResult = eval(bool);

		if(!boolResult.isNil()){
			return eval(expression);
		}
		else{
			return evcon(be.cdr());
		}
	}

	public SExpression evlis(SExpression list) throws customException{
		// This function implements evlis
		if(list.isNil())
			return SExpression.getFromTable("NIL");

		SExpression car = eval(list.car());
		SExpression cdr = evlis(list.cdr());
		return SExpression.cons(car, cdr);		
	}

	public SExpression apply(SExpression function, SExpression args) throws customException{
		// This function applies appropriate function from arguments

		// Check if function has valid arguments
		String functionName = function.name;
		String error = checkIfValidArgs(functionName, args);
		if(error != null)
			throw new customException(error, "Evaluation");

		// Check if function is in a list of built-in functions
		if(functionName.equals("CAR"))
			return args.car().car();

		else if(functionName.equals("CDR"))
			return args.car().cdr();

		else if(functionName.equals("CONS"))
			return SExpression.cons(args.car(), args.cdr().car());

		else if(functionName.equals("PLUS"))
			return SExpression.plus(args.car(), args.cdr().car());

		else if(functionName.equals("MINUS"))
			return SExpression.minus(args.car(), args.cdr().car());

		else if(functionName.equals("TIMES"))
			return SExpression.times(args.car(), args.cdr().car());

		else if(functionName.equals("QUOTIENT"))
			return SExpression.quotient(args.car(), args.cdr().car());

		else if(functionName.equals("REMAINDER"))
			return SExpression.remainder(args.car(), args.cdr().car());

		else if(functionName.equals("LESS"))
			return SExpression.less(args.car(), args.cdr().car());

		else if(functionName.equals("GREATER"))
			return SExpression.greater(args.car(), args.cdr().car());	

		else if(functionName.equals("ATOM")){
			if(args.car().isAtom())
				return SExpression.getFromTable("T");
			else
				return SExpression.getFromTable("NIL");		
		}

		else if(functionName.equals("EQ"))
			return SExpression.eq(args.car(), args.cdr().car());			

		else if(functionName.equals("NULL")){
			if(args.car().isNil())
				return SExpression.getFromTable("T");
			else
				return SExpression.getFromTable("NIL");				
		}

		else if(functionName.equals("INT")){
			if(args.car().isInteger())
				return SExpression.getFromTable("T");
			else 
				return SExpression.getFromTable("NIL");
		}

		// If not built-in, check if in the DList
		else{
			Defun defun = DList.getFunction(functionName);

			// If not in the DList, undefined function
			if(defun == null)
				throw new customException("Undefined function " + functionName + " . **", "Evaluation");

			// Add arguments to AList
			aList.addPairs(functionName, defun.parameters, AList.getArgumentsAsList(args));
			// Execute the function
			SExpression SExp = eval(defun.funBody);
			// Remove the pairs from AList
			aList.destroyPairs(defun.parameters);
			return SExp;
		}
	}

	// Helper function to check for valid arguments

	public String checkIfValidDefun(SExpression defun){
		String error = null;

		// Function name
		if(defun.isNil())
			error = "Function name and body cannot be null. **";
		else if(defun.car().isNil())
			error = "Empty function name and parameters. **";
		else if(!defun.car().car().isSymbol())
			error = "Function name should be a symbolic atom. **";

		// Function parameters
		else if(defun.car().cdr().isNil() || defun.car().cdr().car().isNil())
			error = "Parameters of the function cannot be empty. **";

		else if(!defun.car().cdr().cdr().isNil())
			error = "Parameters should be defined as a separate list from function name. **";

		else if(defun.car().cdr().isAtom())
			error = "Parameters to the function cannot be an atom. **";

		// Function body
		else if(defun.cdr().isNil() || defun.cdr().car().isNil())
			error = "Function body cannot be empty. **";
		else if(defun.cdr().isAtom())
			error = "Function body cannot be an atom. **";

		// If the part after function body is not NIL, it cannot be evaluated.
		else if(!defun.cdr().cdr().isNil())
			error = "Unexpected expression " + defun.cdr().cdr().displayTree() + " found after function body. **";

		return error;
	}

	public String checkIfValidArgs(String function, SExpression args){
		// Function to check if given functions had valid arguments

		String error = null;
		int numArgs = countArgs(args);
		String functionName = function;

		if(function.equals("PLUS") || function.equals("MINUS") || 
			function.equals("TIMES") || function.equals("QUOTIENT") || 
			function.equals("GREATER") || function.equals("LESS") || function.equals("REMAINDER"))
			functionName = "BINARY";

		switch(functionName){
			case "QUOTE":
				if(numArgs != 1)
					error = "QUOTE expects exactly one argument. " + Integer.toString(numArgs) + " given. **";
				return error;

			case "EVCON":
				if(numArgs != 2)
					error = "EVCON expects two arguments. " + Integer.toString(numArgs) + " given. **";
				return error;

			case "CAR":
				if(numArgs != 1)
					error = "CAR expects exactly one argument. " + Integer.toString(numArgs) + " given. **";
				else if(args.car().isAtom())
					error =  "Invalid argument to CAR. " + args.car().getName() + " is an atom. **";
				return error;

			case "CDR":
				if(numArgs != 1)
					error = "CDR expects exactly one argument. " + Integer.toString(numArgs) + " given. **";
				else if(args.car().isAtom())
					error =  "Invalid argument to CDR. " + args.car().getName() + " is an atom. **";
				return error;

			case "CONS":
				if(numArgs != 2)
					error = "CONS expects two arguments. " + Integer.toString(numArgs) + " given. **";
				return error;

			case "ATOM":
				if(numArgs != 1)
					error = "ATOM expects exactly one argument. " + Integer.toString(numArgs) + " given. **";
				return error;

			case "EQ":
				if(numArgs != 2)
					error = "EQ expects two arguments. " + Integer.toString(numArgs) + " given. **";
				return error;

			case "NULL":
				if(numArgs != 1)
					error = "NULL expects exactly one argument. " + Integer.toString(numArgs) + " given. **";
				return error;

			case "INT":
				if(numArgs != 1)
					error = "INT expects exactly one argument. " + Integer.toString(numArgs) + " given. **";
				return error;

			case "BINARY":
				if(numArgs != 2)
					error = function + " expects exactly one argument. " + Integer.toString(numArgs) + " given. **";
				else if(!args.car().isInteger() || !args.cdr().car().isInteger())
					error = "Arguments to " + function + " must be integer atoms. **";
				else if((function.equals("QUOTIENT") || function.equals("REMAINDER")) && args.cdr().car().val == 0)
					error = "Cannot perform division with divisor value as 0";
				return error;

			default:
				return error;
		}
	}

	public int countArgs(SExpression SExp){
		// This function just counts number of arguments using car till NIL occurs
		int count = 0;
		while(!SExp.isNil()){
			SExp = SExp.cdr();
			count += 1;
		}
		return count;
	}

}