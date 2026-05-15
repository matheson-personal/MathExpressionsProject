//Sum

//The sum of 2 or more Functions

import java.util.stream.*;
import java.util.ArrayList;

public class Sum extends Expression {

	ArrayList<Function> functionsToSum;
	ArrayList<Double> coefficients;
	String functionType = "+[";

	public Sum() {
		ArrayList<Function> functionsToSum = new ArrayList<Function>(); //unsure if this is necessary. 80% sure it is
		ArrayList<Double> coefficients = new ArrayList<Double>();
	}


	public Sum(ArrayList<Function> fs) { //Doesn't check if function is a sum TODO (can just be subsumed into the main sum)
		functionsToSum = fs;
		coefficients = new ArrayList<Double>();

		for (int i=0; i<fs.size(); i++) {
			coefficients.add(1.0);
			this.functionType += fs.get(i).getFuncType();
		}
	}


	public Sum(ArrayList<Function> fs, ArrayList<Double> cs) {
		functionsToSum = fs;
		coefficients = cs;

		for (Function f : fs) {
			this.functionType += f.getFuncType();
		}
	}


	public ArrayList<Function> getFs() {
		return this.functionsToSum;
	}


	public Function getFunc(int i) {
		return this.getFs().get(i);
	}


	public ArrayList<Double> getCs() {
		return this.coefficients;
	}


	public double getCoeff(int i) {
		return this.getCs().get(i);
	}


	@Override
	public void add(Function f) {
		this.getFs().add(f);
		this.getCs().add(1.0);

		this.functionType += f.getFuncType();
	}


	public void add(Function f, double c) {
		this.getFs().add(f);
		this.getCs().add(c);

		this.functionType += f.getFuncType();
	}


	@Override
	public void addAll(ArrayList<Function> fs) {
		this.getFs().addAll(fs);

		for (int i=0; i<fs.size(); i++) {
			coefficients.add(1.0);
			this.functionType += fs.get(i).getFuncType();
		}
	}


	public void addAll(ArrayList<Function> fs, ArrayList<Double> cs) {
		this.getFs().addAll(fs);
		this.getCs().addAll(cs);

		for (Function f : fs) {
			this.functionType += f.getFuncType();
		}
	}


	public Function Simplify() {
		return this; //doesn't break the program if ever used before implementation
	}


	@Override
	public double evaluate(double x) {
		double total = 0;

		/*
		for (Function f : this.getFs()) {
			total += f.evaluate(x);
		}
		*/

		total = this.getFs().stream().mapToDouble(f -> f.evaluate(x)).reduce(0, Double::sum);

		return total;
	}


	public ArrayList<Double> evaluate(ArrayList<Double> toEval) {
		ArrayList<Double> results = new ArrayList<Double>();
		ArrayList<ArrayList<Double>> toSum = new ArrayList<ArrayList<Double>>();

		for (int i = 0; i < this.getFs().size(); i++) {
			toSum.add(this.getFunc(i).evaluate(toEval));
		}


		for (int j=0; j < toEval.size(); j++) {
			double p = 1;

			for (int k=0; k < this.getFs().size(); k++) {
				p += toSum.get(k).get(j);
			}

			results.add(p);
		}

		return results;
	}


	public Function differentiate() {
		Sum newsum = new Sum();
		
		for (Function f : this.getFs()) {
			newsum.add(f.differentiate());
		}

		return newsum;
	}


	@Override
	public String getFuncType() {
		return this.functionType + "]";
	}


	//just not a priority rn i can't be askkedddduhh
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public String toStringCustom(String base) {
		// TODO Auto-generated method stub
		return null;
	}
}
	