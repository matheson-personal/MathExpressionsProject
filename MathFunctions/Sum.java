//Sum

//The sum of 2 or more Functions

import java.util.ArrayList;

public class Sum extends Expression {

	ArrayList<Function> functionsToSum = new ArrayList<Function>();
	ArrayList<Double> coefficients = new ArrayList<Double>();
	String functionSignature = "+[";

	public Sum() {
		//the lists are already predefined so I can be lazy and call addAll on the other constructors
		//so nothing for this guy to do :(((((((
	}


	public Sum(ArrayList<Function> fs) { //Doesn't check if function is a sum TODO (can just be subsumed into the main sum)
		this.addAll(fs);
	}


	public Sum(ArrayList<Function> fs, ArrayList<Double> cs) {
		this.addAll(fs, cs);
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


	//maybe have this in the superclass?
	public void add(Function f, double c) {

		if (f.getFuncType() == "+") {
			Sum sumDetected = (Sum) f;
			this.addAll(sumDetected.getFs());

		} else if (f.getFuncType() == "k") {
			System.out.println("Time to implement collecting constants");

		} else {
			this.getFs().add(f);
			this.getCs().add(c);
			this.functionSignature += f.getFuncSignature();
		}
	}

	
	@Override
	public void add(Function f) {
		this.add(f, 1.0); //lazy and clean B)
	}


	@Override
	public void addAll(ArrayList<Function> fs) {

		for (Function f : fs) {
			this.add(f); //must be checked individually if they're sums, so may as well use the individual method
		}
	}


	public void addAll(ArrayList<Function> fs, ArrayList<Double> cs) {

		for (int i = 0; i < fs.size(); i++) {
			this.add(fs.get(i), cs.get(i));
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
	public String getFuncSignature() {
		return this.functionSignature + "]";
	}

	
	@Override
	public String getFuncType() {
		return "+";
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
	