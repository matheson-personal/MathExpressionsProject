//Functional

import java.util.ArrayList;


public class Functional extends Function {

	public Function f;
	public Function u;
	public String functionSignature;

	public Functional(Function outerFunction, Function subForX) {
		this.f = outerFunction;
		this.u = subForX;

		functionSignature = f.getFuncType() + "(" + u.getFuncType() + ")";
	}


	public String getFuncSignature() {
		return this.functionSignature;
	}
	
	public Function getU() {
		return this.u;
	}


	public Function getF() {
		return this.f;
	}


	@Override
	public String toString() {
		String fu = this.getF().toStringCustom("u");
		String ux = this.getU().toStringCustom("x");

		return fu + "\nWhere u(x) = " + ux;
	}


	public String toStringCustom(String basef, String baseu) {
		//Check if u is a functional otherwise it gets printed twice TODO
		//a proper toString for a Big function with many parts is a big ask

		String fu = this.getF().toStringCustom(basef);
		String ux = this.getU().toStringCustom(baseu);

		return "f(u) = " + fu + "\nWhere u(" + baseu+ ") = " + ux;
	}


	@Override
	public double evaluate(double x) {
		double u = this.getU().evaluate(x);
		return this.getF().evaluate(u);
	}


	public ArrayList<Double> evaluate(ArrayList<Double> xs) {
		ArrayList<Double> us = this.getU().evaluate(xs);
		return this.getF().evaluate(us);
	}


	public Function differentiate() {
		Function uPrime = this.getU().differentiate();				// du/dx
		Function fPrimex = this.getF().differentiate();				// df(x)/dx
		Functional fPrimeu = new Functional(fPrimex, this.getU());	// df(u)/du

		Product result = new Product();
		result.add(fPrimeu); result.add(uPrime);					// df/dx = du/dx * df/du
		return result;
	}


	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

        	if (o == null || getClass() != o.getClass()) {
            		return false;
        	}	

		Functional other = (Functional) o;

		return (this.getU().equals(other.getU()) && 
				this.getF().equals(other.getF()));

	}


	@Override
	public String toStringCustom(String base) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getFuncType() {
		return "F";
	}


	/*
	 * looks like
	 * p {1.0, 2.0, 3.0, 4.0} (
	 * 		e {1.0, 2.0}
	 * )
	 * this looks nicer imo but the method below can use methods used for parsing expression, and standardises the fact that
	 * Functions are always listed in square brackets and end in semicolons
	@Override
	public String saveString(int depth) {
		StringBuilder ss = new StringBuilder();

		ss.append(this.getF().saveString(depth));
		ss.append("(\n");
		ss.append(this.getU().saveString(depth + 1));
		ss.append("\n)");

		return ss.toString();
	}
	*/

	@Override
	public String saveString(int depth) {
		/* Looks like
		 * f {} [
		 * 		p {1.0, 2.0, 3.0, 4.0};
		 * 		e {1.0, 2.0};
		 * ]
		 */
		StringBuilder ss = new StringBuilder();

		ss.append(this.tabs(depth));
		ss.append("f {} [\n");

		ss.append(this.getU().saveString(depth + 1));
		ss.append(";\n");
		ss.append(this.getF().saveString(depth + 1));
		ss.append(";\n");


		ss.append(this.tabs(depth));
		ss.append("]");

		return ss.toString();
	}
}
