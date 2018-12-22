/**
 * This is class defines represents the parsed, in-memory lisp expression.
 * Each S-Expression has a type: 1 integer, 2 symbolic, 3 non-atomic
 * In case the type is an integer atom, the value should of the corresponding
 * integer is stored in int val. For symbolic atoms, the name variable contains
 * the variable name. If it's a non atomic S-Expression, then left and right
 * variables point to the left and the right children nodes respectively,
 * otherwise they are null.
 * @author: Ashwini Joshi
*/

import java.util.*;

public class SExpression{
	// 1 for integer atom, 2 for symbolic atom, 3 for non-atomic SExpression
	int type;
	// For integer atom
	int val;
	// For symbolic atom
	String name;
	// For non-atomic S-Expression
	SExpression car, cdr;
    // Symbol table for identifiers
    public static HashMap<String, SExpression> map = new HashMap<>();

	// Create S-Expression of integer type
	public SExpression(int number){
		type = 1;
		val = number;
	}

    // Create S-Expression of string type
	public SExpression(String str){
		type = 2;
		name = str;
	}

	// Non-atomic S-Expression
	public SExpression(SExpression car, SExpression cdr){
		type = 3;
		this.car = car;
		this.cdr = cdr;
	}

	// Get Symbolic atomic S-Expression from Symbol Table
	public static SExpression getFromTable(String identifier){
		if(map.containsKey(identifier)){
			return map.get(identifier);
		}
		else{
			SExpression SExp = new SExpression(identifier);
			map.put(identifier, SExp);
			return SExp;
		}
	}

	// list of built-in functions 
	public static SExpression plus(SExpression SExp1, SExpression SExp2){
		int addition = SExp1.val + SExp2.val;
		return new SExpression(addition);
	}

	public static SExpression minus(SExpression SExp1, SExpression SExp2){
		int subtraction = SExp1.val - SExp2.val;
		return new SExpression(subtraction);
	}

	public static SExpression times(SExpression SExp1, SExpression SExp2){
		int multiplication = SExp1.val * SExp2.val;
		return new SExpression(multiplication);
	}

	public static SExpression quotient(SExpression SExp1, SExpression SExp2){
		int quotient = SExp1.val / SExp2.val;
		return new SExpression(quotient);
	}

	public static SExpression remainder(SExpression SExp1, SExpression SExp2){
		int rem = SExp1.val % SExp2.val;
		return new SExpression(rem);
	}

	public static SExpression less(SExpression SExp1, SExpression SExp2){
		if(SExp1.val < SExp2.val)
			return getFromTable("T");
		else
			return getFromTable("NIL");
	}

	public static SExpression greater(SExpression SExp1, SExpression SExp2){
		if(SExp1.val > SExp2.val)
			return getFromTable("T");
		else
			return getFromTable("NIL");
	}

	public static SExpression eq(SExpression SExp1, SExpression SExp2){
		if(SExp1.type != SExp2.type)
			return getFromTable("NIL");
		else{
			if(SExp1.type == 1 && SExp1.val == SExp2.val)
				return getFromTable("T");
			else if(SExp1.type == 2 && SExp1.name == SExp2.name)
				return getFromTable("T");
			else
				return getFromTable("NIL");
		}

	}

	public SExpression car(){
		return this.car;
	}

	public SExpression cdr(){
		return this.cdr;
	}

	public static SExpression cons(SExpression car, SExpression cdr){
		return new SExpression(car, cdr);
	}

	public boolean isNil(){
		return type == 2 && name.equals("NIL");
	}

	public boolean isT(){
		return type == 2 && name.equals("T");
	}

	public boolean isAtom(){
		return type != 3;
	}

	public boolean isInteger(){
		return type == 1;
	}

	public boolean isSymbol(){
		return type == 2;
	}

	// Return value of atom as a string for error handling
	public String getName(){
		String name = type == 1 ? Integer.toString(this.val) : this.name;
		return name;
	}

	// Get dot notation for the SExpression using recursion
	public String displayTree(){
        String displayString = "";
        // Integer atom
        if(type == 1){
        	displayString = displayString + Integer.toString(val);
        }
        // String atom
        else if(type == 2){
        	displayString = displayString + name;
        }
        // Non-atomic S-Expression
        else{
        	displayString = displayString + "(" + car.displayTree() + " . " + cdr.displayTree() + ")";
        }

        return displayString;
	}

	// Get the dot notation and print it
	public void printSExpression(){
		String output = displayTree();
		System.out.println("> " + output);
	}
}