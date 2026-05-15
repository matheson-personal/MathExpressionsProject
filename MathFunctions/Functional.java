//Functional

import java.util.ArrayList;
import java.util.stream.*;


public class Functional extends Function {

	public Function f;
	public Function u;
	public String functionType;

	public Functional(Function outerFunction, Function subForX) {
		this.f = outerFunction;
		this.u = subForX;

		functionType = f.getFuncType() + "(" + u.getFuncType() + ")";
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


	public String toString(String basef, String baseu) {
		//Check if u is a functional otherwise it gets printed twice TODO
		//a proper toString for a Big function with many parts is a big ask

		String fu = this.getF().toStringCustom(basef);
		String ux = this.getU().toStringCustom(baseu);

		return fu + "\nWhere u(" + baseu+ ") = " + ux;
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
		return this.functionType;
	}
}
