import java.util.ArrayList;

public class Logarithmic extends Function {

	public double c;
	public double logA;
	public double base;
	public double a;


	public Logarithmic(double base, double a) {
		this.base = base;
		this.a = a;

		if (base == Math.E) {
			this.c = 1;
		} else {
			this.c = 1/Math.log(base); //maybe handle -ve and 0 base and a arguments
		}

		this.logA = this.c * Math.log(a);
	}


	public Logarithmic (double base) {
		this.base = base;
		this.a = 1;

		if (base == Math.E) {
			this.c = 1;
		} else {
			this.c = 1/Math.log(base);
		}

		this.logA = 0;
	}


	public Logarithmic() {
		this.base = Math.E;
		this.a = 1;
		this.c = 1;
		this.logA = 0;
	}


	@Override
	public String toStringCustom(String base) {
		return "log" + this.base + "(" + this.a + base + ")";
	}


	@Override
	public double evaluate(double x) {
		return this.logA + this.c*Math.log(x);
	}


	@Override
	public ArrayList<Double> evaluate(ArrayList<Double> xs) {
		ArrayList<Double> evaluated = xs.stream().mapToDouble((x) -> this.logA + this.c * Math.log(x))
										.collect(ArrayList::new, ArrayList::add,ArrayList::addAll);
		return evaluated;
	}


	@Override
	public Function differentiate() {
		// TODO need quotient first and or -ve powers of x in polynomial idkkk
		return null;
	}


	@Override
	public String getFuncType() {
		return "l";
	}


	@Override
	public String getFuncSignature() {
		return "l";
	}


	@Override
	public String saveString(int depth) {
		StringBuilder yep = new StringBuilder();

		yep.append(this.tabs(depth));
		yep.append("l {");
		
		yep.append(this.base);
		yep.append(",");
		yep.append(this.a);

		yep.append("}");
		return yep.toString();
	}

}
