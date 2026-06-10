//Function

//Abstract Function class to ensure base functionality


import java.util.ArrayList;

public abstract class Function {

	public String toString() {
		return this.toStringCustom("x");
	}

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
	//short String showing the structure of Function.

	public abstract String saveString(int depth);
	//Returns a String to save so a function can be reloaded. The Function is readable in the text file as well
	//Depth is a helper variable showing how many tabs to indent by

	public String saveString() {
		return this.saveString(0);
	}
	
	public String tabs(int times) {
		if (times == 0) { return "";}

		StringBuilder tabs = new StringBuilder();

		for (int i=0; i<times; i++) {
			tabs.append("	");
		}

		return tabs.toString();
	}


	public double newtonRaphson(double startx, double minError) {
		return newtonRaphson(startx, minError, 1000);
	}


	public double newtonRaphson(double startx, double minError, int maxIterations) {
		Function fprime = this.differentiate();

		double x = startx;
		double f = this.evaluate(startx);

		int i = 0;
		while (f > minError) {
			x -= f/fprime.evaluate(x);
			f = this.evaluate(x);
			i++;

			if (i == maxIterations) {	//kind of temporary
				System.out.println("Newton-Raphson for "+this.toString()+"exceeded 1000 iterations.");
				break;
			}
		}

		return x;
	}


	public void saveFunction(String filePath, String comments) {
		//TODO
	}
}

//can't think of any other needed general functionality (haha) atm