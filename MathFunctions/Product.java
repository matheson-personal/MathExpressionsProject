//Product
//The product of two or more Function objects

import java.util.ArrayList;

public class Product extends Expression {

	ArrayList<Function> functionProducts = new ArrayList<Function>();
	String functionSignature = "x[";


	public Product() {
		//the lists are already predefined so I can be lazy and call addAll on the other constructors
		//so nothing for this guy to do :(((((((
	}


	public Product(ArrayList<Function> fs) {
		this.addAll(fs);
	}



	@Override
	public String getFuncSignature() {
		return this.functionSignature + "]";
	}


	public ArrayList<Function> getFs() {
		return this.functionProducts;
	}


	@Override
	public Expression differentiate() {
		/*uses product rule to differentiate the product of two or more Functions. Returns an Expression 
		containing Products with each element of this Product differentiated and multiplied by the rest.

		i.e.
		d(uv)/dx = uv' + vu'
		
		return type is expression just in case there is only one nonzero function to sum, but thats later TODO
		*/

		Sum sum = new Sum();

		for (int i=0; i < this.functionProducts.size(); i++) {
			Function ddx = this.getFunc(i).differentiate();
			Product yep = new Product(this.ddxHelper(i));
			yep.add(ddx);

			sum.add(yep);
		}

		return sum;
	}

	//before I used clone, then remove but the return type is Object. Looked much cleaner and was probably more performant
	//maybe there's a similar method/ a way to use clone. If it just clones the list reference that's stupid

	public ArrayList<Function> ddxHelper(int i) {
		/*returns a shallow copy of the Function list sans i, to clean up the differentiation method*/
		ArrayList<Function> copy = new ArrayList<Function>();
		for (int j=0; j < this.getFs().size(); j++) {

			if (j==i) { continue; }
			copy.add(this.getFunc(j));
		}
				
		return copy;
	}



	@Override
	//perhaps overengineered, may turn into a for loop as the performance difference is negligible/worse for short lists
	public double evaluate(double x) {

		double result = 1;
		for (Function f : this.functionProducts) {
			result *= f.evaluate(x);
		}

		return result;
	}



	//p sure this would work. Couldn't think of a parallelisable way of multiplying the function evals together in java
	//maybe there's a sequence of maptos that would work TODO?

	@Override
	public ArrayList<Double> evaluate(ArrayList<Double> toEval) {
		/*Accepts ArrayList<Double> of inputs returns the product of all Functions contained inside
		this object evaluated at those points as another ArrayList<Double>*/

		ArrayList<Double> results = new ArrayList<Double>();
		ArrayList<ArrayList<Double>> toMultiply = new ArrayList<ArrayList<Double>>();


		for (int i = 0; i < functionProducts.size(); i++) {
			toMultiply.add(this.getFunc(i).evaluate(toEval));
		}


		for (int j=0; j < toEval.size(); j++) {
			double p = 1;

			for (int k=0; k < this.functionProducts.size(); k++) {
				p *= toMultiply.get(k).get(j);
			}

			results.add(p);
		}

		return results;
	}


	/*
	@Override
	public void add(Function f) {
		this.getFs().add(f);
		this.functionSignature += f.getFuncSignature();
	}
	*/

	
	public void add(Function f) {

		if (f.getFuncType() == "x") {
			Product productDetected = (Product) f;
			this.addAll(productDetected.getFs());

		} else if (f.getFuncType() == "k") {
			System.out.println("Time to implement collecting constants");

		} else {
			this.getFs().add(f);
			this.functionSignature += f.getFuncSignature();
		}
	}



	@Override
	public void addAll(ArrayList<Function> fs) {
		for (Function f : fs) {
			this.add(f);
		}
	}


	@Override
	public Function getFunc(int i) {
		return this.functionProducts.get(i);
	}


	@Override
	public String getFuncType() {
		return "x";
	}


	@Override
	public String toStringCustom(String base) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public String saveString(int depth) {
		/* Looks like
		 * x {} [
		 * 		p {1.0, 2.0, 3.0, 4.0};
		 * 		e {1.0, 2.0};
		 * 		... rest of Functions similarly
		 * ]
		 */
		StringBuilder ss = new StringBuilder();
		
		ss.append(this.tabs(depth));
		ss.append("x {} [\n");

		for (Function f : this.getFs()) {
			ss.append(f.saveString(depth + 1));
			ss.append(";\n");
		}

		ss.append(this.tabs(depth));
		ss.append("]");

		return ss.toString();
	}
}