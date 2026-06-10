import java.util.ArrayList;

public class OneTerm extends Function {
	public double p;
	public double k;


	public OneTerm(double p) {
		this.p = p;
		this.k = 1;
	}


	public OneTerm(double p, double k) {
		this.p = p;
		this.k = k;
	}


	public double getP() {
		return this.p;
	}


	public double getK() {
		return this.k;
	}

	@Override
	public String toStringCustom(String base) {
		return this.getK() + "x^" + this.getP();
	}


	@Override
	public double evaluate(double x) {
		return k * Math.pow(x, this.p);
	}


	@Override
	public ArrayList<Double> evaluate(ArrayList<Double> xs) {
		double p = this.getP();
		double k = this.getK();

		ArrayList<Double> evaluated = xs.stream().mapToDouble((x) -> k * Math.pow(x,p))
										.collect(ArrayList::new, ArrayList::add,ArrayList::addAll);
		return evaluated;
	}


	@Override
	public Function differentiate() {

		double newK = this.getK()*this.getP();
		double newP = this.getP()-1;

		if (newP == 0) {
			return new Constant(newK);
		}

		return new Power(newP, newK);
	}


	@Override
	public String getFuncType() {
		return "o";
	}


	@Override
	public String getFuncSignature() {
		return this.getFuncType();
	}


	@Override
	public String saveString(int depth) {
		/* 5.2x^2 Looks like
		 * o {2.0, 5.2}
		 */
		StringBuilder ss = new StringBuilder();
		ss.append(this.tabs(depth));

		ss.append("o {");

		ss.append(this.getP());
		ss.append(",");		
		ss.append(this.getK());

		ss.append("}");

		return ss.toString();
	}

}
