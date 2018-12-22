# LISP-interpreter
Implementation of LISP interpreter in Java


1. For details about the structure of Interpreter, please refer to design.txt file. To run the code, go inside the src directory of the project where all '.java' files are stored. It contains a Makefile. 

2. After going in the main directory, type 
> make <br /> 

on terminal or command line. This will automatically compile all java files and execute the Main class.

3. If you want to remove all class files and compile again, type 
> make clean

4. It asks user to input expressions on standard system, prints dot notation (if valid) and evaluation result (if any) for that and keeps asking for a new one until "$$" occurs on a newline. After entering one complete S-Expression, enter $ so that interpreter knows it is one single S-Expression. At the end, enter $$ to stop the interpreter completely.

5. It only works for input from standard system (input entered on terminal) and may not work if input given from a file or using grep.

If make doesn't work on stdlinux, type
> javac *.java <br />
> java Main

to run the code.


6. Conventions: <br />
a) It works only with expressions written in capital letters. For example, (plus 5 10) is not valid. Type (PLUS 5 10) instead. <br />
b) Symbolic Atom - Length should not exceed 10. A combination of capital letters and numbers strictly starting with a letter. <br />
c) Integer Atom - Any integer with length less than or equal to 6. <br />

7. Following primitives are implemented:
T, NIL, CAR, CDR, CONS, ATOM, EQ, NULL, INT, PLUS, MINUS, TIMES, QUOTIENT, REMAINDER, LESS, GREATER, COND, QUOTE, DEFUN.
	

