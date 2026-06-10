import java.util.ArrayList;

public class Power extends Function {

	public double p;
	public double k;
	public Function f;
	public boolean isFunctionless = false;

	public Power(double p) {
		this.isFunctionless = true;
		this.p = p;
		this.k = 1;
	}


	public Power(double p, double k) {
		this.isFunctionless = true;
		this.p = p;
		this.k = k;
	}


	public Power(Function f, double p) {
		this.f = f;
		this.p = p;
		this.k = 1;
	}


	public Power(Function f, double p, double k) {
		this.f = f;
		this.p = p;
		this.k = k;
	}


	public Function getF() {
		return this.f;
	}


	public double getP() {
		return this.p;
	}


	public double getK() {
		return this.k;
	}

	@Override
	public String toStringCustom(String base) {
		return this.getK() + "(" + f.toStringCustom(base) + ")^p";
	}


	@Override
	public double evaluate(double x) {
		if (this.isFunctionless) {
			return k * Math.pow(x, this.p);
		}

		double f = this.getF().evaluate(x);
		return k*Math.pow(f, this.getP());
	}


	@Override
	public ArrayList<Double> evaluate(ArrayList<Double> xs) {
		/*
		 * ArrayList<Double> poo = this.getF().evaluate(xs);
		 * ArrayList<Double> evaluated = poo.stream().mapToDouble((x) -> Math.pow(x, this.p))
										    .collect(ArrayList::new, ArrayList::add,ArrayList::addAll);
			below is probably faster though doin it one step
		 */

		double p = this.getP();
		double k = this.getK();

		if (this.isFunctionless) {
			ArrayList<Double> evaluated = xs.stream().mapToDouble((x) -> k * Math.pow(x,p))
											.collect(ArrayList::new, ArrayList::add,ArrayList::addAll);
			return evaluated;
		}

		Function f = this.getF();
		ArrayList<Double> evaluated = xs.stream().mapToDouble((x) -> k * Math.pow(f.evaluate(x),p))
										.collect(ArrayList::new, ArrayList::add,ArrayList::addAll);
		return evaluated;
	}


	@Override
	public Power differentiate() {

		double newK = this.getK()*this.getP();
		double newP = this.getP()-1;

		if (this.isFunctionless) {
			return new Power(newP, newK);
		}

		Function ddx = this.getF().differentiate();
		return new Power(ddx, newP, newK);
	}


	@Override
	public String getFuncType() {
		return "^k";
	}


	@Override
	public String getFuncSignature() {
		return "(" + this.getF().getFuncSignature() + ")^k";
	}


	@Override
	public String saveString(int depth) {
		/* Looks like
		 * ^ {false} [
		 * 		p {1.0, 2.0, 3.0, 4.0};
		 * 		3.0;
		 * ]
		 * 
		 * false in the curly brackets indicates this is a Power, and not a Spindle
		 */
		StringBuilder ss = new StringBuilder();

		ss.append(this.tabs(depth));
		ss.append("^ {false} [\n");

		ss.append(this.getF().saveString(depth + 1));
		ss.append(";\n");

		ss.append(this.tabs(depth + 1));
		ss.append(this.getP());
		ss.append(";\n");

		ss.append(this.tabs(depth));
		ss.append("]");

		return ss.toString();
	}

}
