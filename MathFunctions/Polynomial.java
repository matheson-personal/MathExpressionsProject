//Polynomial
//all but add and subPolynomial have been teseted p sure
//oh and newton raphson but low priority and should be moved to superclass

import java.util.ArrayList;
import java.util.ArrayDeque;

public class Polynomial extends Function{

	public ArrayList<Double> coefficients; //coefficients with index equal to power


	public Polynomial() {
		this.coefficients = new ArrayList<Double>();
	}


	public Polynomial(ArrayList<Double> coeffs) {
		this.coefficients = coeffs;
	}

	
	/*
	public Polynomial(Collection<Double> coeffs) {
		this.coefficients = new ArrayList<Double>(coeffs);
	}
	*/


	public int getDegree() {
		return this.coefficients.size();
	}


	public ArrayList<Double> getCoefficients() {
		return this.coefficients;
	}


	@Override
	public String getFuncType() {
		return "p";
	}


	@Override
	public String getFuncSignature() {
		return this.getFuncType();
	}

	
	public String singleTerm(double coeff, int power, String base) {
		//helper function for toString that returns the term for a given power and coefficient

		switch (power) {
			case 0:
				return " + " + coeff;

			case 1:
				return " + " + coeff + base;

			default:
				return " + " + coeff + base + "^" + power;
		}
	}


	public String singleTerm(int power, String base) {
		//helper function for toString that returns the term for a given power (automatically grabs corresponding coefficient)

		double coeff = this.get(power);

		switch (power) {
			case 0:
				return " + " + coeff;

			case 1:
				return " + " + coeff + base;

			default:
				return " + " + coeff + base + "^" + power;
		}
	}


	public String toString() {
		int i = 0;
		ArrayDeque<String> terms = new ArrayDeque<>(10);

		String base = "x";

		for (int power=0; power < this.getDegree(); power++) {
			double coeff = this.get(power);

			if (i==10) {
				terms.add("+ ...");	//adding to the back rather than front
				break; 			//maybe it should be at the front idk
			}

			if (coeff == 0.0) {
				continue;		//this clause means that i only increments when a term is added
			}


			terms.addFirst(this.singleTerm(coeff, power, base));
			i++;
		}


		/* remove if the stream stuff works
		message.append(terms.pop().subString(3));

		while (!terms.empty()) { 
			message.append(terms.pop());
		}
		*/


		StringBuilder poo = terms.stream().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append);
		
		return poo.toString();
	}


	public String toStringCustom(String base) {
		int i = 0;
		ArrayDeque<String> terms = new ArrayDeque<>(10);

		for (int power=0; power < this.getDegree(); power++) {
			double coeff = this.get(power);

			if (i==10) {
				terms.add("+ ...");	//adding to the back rather than front
				break; 			//maybe it should be at the front idk
			}

			if (coeff == 0.0) {
				continue;		//this clause means that i only increments when a term is added
			}


			terms.addFirst(this.singleTerm(coeff, power, base));
			i++;
		}


		/* remove if the stream stuff works
		message.append(terms.pop().subString(3));

		while (!terms.empty()) { 
			message.append(terms.pop());
		}
		*/


		StringBuilder poo = terms.stream().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append);

		return poo.toString();
	}




	public String toString(int loPow, int hiPow) {
		StringBuilder poo = new StringBuilder();
		int li = this.getDegree();

		for(int i=loPow; i<=hiPow; i++) {
			if (i > li) {
				poo.append(this.singleTerm(i, "x"));
			}
		}

		return poo.toString();
	}


	//TODO there's probably a more efficient way to do the below, but not a big issue really
	private void removeTrailingZeroes() {
		int li = this.getDegree() - 1;		//last index

		while (this.get(li) == 0) {
			this.coefficients.remove(li);
			li--;							//decrement li since list has shortened

		}
	}

	
	public void add(double coefficient) {
		this.coefficients.add(coefficient);
	}

	
	public void addAll(ArrayList<Double> coefficients) {
		this.coefficients.addAll(coefficients);
	}
	
	
	public void add(int index, double coefficient) {
		this.coefficients.add(index, coefficient);
		this.removeTrailingZeroes(); //this will rarely do anything but whatever inexpensive
	}


	public void remove(int index) {
		this.coefficients.remove(index);
		this.removeTrailingZeroes();
	}


	public double get(int index) {
		return this.coefficients.get(index);
	}


	public void set(int index, double input) {
		this.coefficients.set(index, input);
	}


	@Override
	public double evaluate(double input) {
		double tot = this.get(0);
		double xn = input;

		for (int i=1; i<this.getDegree(); i++) {
			tot += xn*this.get(i);
			xn *= input;
		}

		return tot;
	}



	//could make it compatible with more types of list but i can't be bothered rn

	//below is not parallelisable TODO
	@Override
	public ArrayList<Double> evaluate(ArrayList<Double> toEval) {
		ArrayList<Double> evaluated = new ArrayList<>();

		for (double yep : toEval) {
			evaluated.add(this.evaluate(yep)); //this will use the single Number version above
		}

		//below is stream version to be tested
		//evaluated = toEval.stream.mapToDouble(x -> this.evaluate(x)).toList();

		return evaluated;
	}


	@Override
	public Function differentiate() {
		ArrayList<Double> differentiated = new ArrayList<>();

		if (this.getCoefficients().size() == 2) {
			return new Constant(2*this.get(1));
		}

		for (int i=1; i < this.getDegree(); i++) {
			Double currentTerm = this.get(i);
			differentiated.add(i*currentTerm);
		}

		/* cooler version with streams - don't know if it's faster or if it works
		differentiated = IntStream.range(1, this.getDegree())
		                          .mapToDouble( (i) -> i * this.get(i) )
		                          .toList();
		*/

		return new Polynomial(differentiated);
	}


	public Polynomial addPolynomial(Polynomial that) {
		ArrayList<Double> newCoeff;
		int terms2add;
		boolean thisBigger;

		if (this.getDegree() > that.getDegree()) {
			terms2add = that.getDegree();
			thisBigger = true;
			newCoeff = new ArrayList<Double>(this.getDegree());
		} else {
			terms2add = this.getDegree();
			thisBigger = false;
			newCoeff = new ArrayList<Double>(that.getDegree());
		}


		for (int i=0; i<terms2add; i++) {
			newCoeff.add(this.get(i) + that.get(i));
		}


		if (thisBigger) { //may as well not add 0 to every component and do the rest at once
			newCoeff.addAll(terms2add, this.coefficients.subList(terms2add, this.getDegree()));
		} else {
			newCoeff.addAll(terms2add, that.coefficients.subList(terms2add, that.getDegree()));
		}


		return new Polynomial(newCoeff);
	}


	//subPolynomial will NOT work yet. problem is with the sublist section

	public Polynomial subPolynomial(Polynomial that) {
		int terms2sub;
		boolean thisBigger;
		ArrayList<Double> newCoeff = new ArrayList<Double>();

		if (this.getDegree() > that.getDegree()) {
			terms2sub = that.getDegree();
			thisBigger = true;
		} else {
			terms2sub = this.getDegree();
			thisBigger = false;
		}


		for (int i=0; i<terms2sub; i++) {
			newCoeff.add(this.get(i) - that.get(i));
		}


		//not sure if below implementation works PLEASE TEST. Again maybe overengineered
		if (thisBigger) {
			ArrayList<Double> extraTerms = this.coefficients.subList(terms2sub, this.getDegree())
											   .stream().mapToDouble(x -> -x)
											   .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
			newCoeff.addAll(terms2sub, extraTerms);
		} else {
			ArrayList<Double> extraTerms = that.coefficients.subList(terms2sub, that.getDegree())
					   						   .stream().mapToDouble(x -> -x)
					   						   .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
			newCoeff.addAll(terms2sub, extraTerms);
		}


		return new Polynomial(newCoeff);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

        	if (o == null || getClass() != o.getClass()) {
            		return false;
        	}	

		Polynomial otherPoly = (Polynomial) o;

		return (this.getCoefficients().equals(otherPoly.getCoefficients()));
	}


	@Override
	public String saveString(int depth) {
		StringBuilder yep = new StringBuilder();

		yep.append(this.tabs(depth));
		yep.append("p {");

		for (int i=0; i<this.getDegree()-1; i++) {
			yep.append(this.get(i));
			yep.append(", ");
		}

		yep.append(this.get(this.getDegree()-1));
		yep.append("}");
		return yep.toString();
	}
}




