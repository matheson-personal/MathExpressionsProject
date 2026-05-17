//Function

//Abstract Function class to ensure base functionality


import java.util.ArrayList;

public abstract class Function {

	public abstract String toStringCustom(String base);
	//toString method that gives the function with a variable other than x

	public abstract double evaluate( double x );
	//evaluates the function at x and returns the result as a double

	public abstract ArrayList<Double> evaluate( ArrayList<Double> x );
	//evaluates the function at x and returns the result as a list of doubles

	public abstract Function differentiate();
	//performs differentiation on the expression and returns the result as a Function

	public abstract String getFuncType();
	//returns the functype to see if terms can be collected 
	//(further checks may be necessary but this method is necessary for the initial step)

	public abstract String getFuncSignature();
}

//can't think of any other needed general functionality (haha) atm