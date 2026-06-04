import java.util.ArrayList;

public class Exponential extends Function {

	double multiplier;
	double linearFactor = 1;
	double base = -1;


	public Exponential() {
		this.multiplier = 1.0;
	}


	public Exponential(double multiplier) {
		this.multiplier = multiplier;
	}


	public Exponential(double multiplier, double base) {
		this.multiplier = multiplier * Math.log(base);
	}


	public double getFactor() {
		return this.multiplier;
	}


	public double getlinearFactor() {
		return this.linearFactor;
	}


	@Override
	public String toStringCustom(String base) {
		return this.getlinearFactor() + "e^(" + this.getFactor() + base + ")";
	}


	@Override
	public double evaluate(double x) {
		return this.getFactor() * Math.exp(x * this.getFactor());
	}


	@Override
	public ArrayList<Double> evaluate(ArrayList<Double> xs) {
		double g = this.getFactor();
		double a = this.getlinearFactor();
		ArrayList<Double> evaluated = xs.stream().mapToDouble((x) -> a * Math.exp(g * this.getFactor()))
				   						.collect(ArrayList::new, ArrayList::add,ArrayList::addAll);
		return evaluated;
	}


	@Override
	public Function differentiate() {
		Exponential newExp = new Exponential(this.multiplier);
		newExp.linearFactor = this.multiplier * this.linearFactor;
		return null;
	}


	@Override
	public String getFuncType() {
		return "e";
	}


	@Override
	public String getFuncSignature() {
		return "e";
	}


	@Override
	public String saveString(int depth) {
		StringBuilder yep = new StringBuilder();

		yep.append(this.tabs(depth));
		yep.append("e {");
		
		if (this.base == -1) {
			yep.append('e');
		} else {
			yep.append(this.base);
		}

		yep.append(',');
		yep.append(this.multiplier);

		yep.append(',');
		yep.append(this.linearFactor);

		yep.append('}');
		return yep.toString();
	}
}
