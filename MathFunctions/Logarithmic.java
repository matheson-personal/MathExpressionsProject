import java.util.ArrayList;

public class Logarithmic extends Function {

	public double c;
	public double logA;
	public double base = -1;
	public double a;


	public Logarithmic(double a, double base) {
		this.base = base;
		this.a = a;

		if (base == Math.E) {
			this.c = 1;
		} else {
			this.c = 1/Math.log(base); //maybe handle -ve and 0 base and a arguments
		}

		this.logA = this.c * Math.log(a);
	}


	public Logarithmic (double a) {
		this.a = a;
		this.c = 1;
		this.logA = Math.log(a);
	}


	public Logarithmic() {
		this.a = 0;
		this.c = 1;
		this.logA = 0;
	}


	@Override
	public String toStringCustom(String arg) {
		String poo;

		if (this.base < 0) {
			poo = "e";
		} else {
			poo = Double.valueOf(this.base).toString();
		}

		return "log" + poo + "(" + this.a + arg + ")";
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
		//d(ln(a) + c*ln(x))/dx = c/x
		return new OneTerm(-1, this.c);
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


		if (this.base < 0) {
			yep.append('e');

		} else {
			yep.append(this.base);
		}

		yep.append(", ");
		yep.append(this.a);

		yep.append("}");
		return yep.toString();
	}

}
