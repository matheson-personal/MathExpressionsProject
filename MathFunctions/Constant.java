import java.util.ArrayList;
import java.util.Collections;

public class Constant extends Function {

	public double value;

	public Constant(double value) {
		this.value = value;
	}


	public Constant() {
		this.value = 0;
	}


	public double getValue() {
		return this.value;
	}


	@Override
	public String toStringCustom(String base) {
		return Double.valueOf(this.getValue()).toString();
	}


	@Override
	public double evaluate(double x) {
		return this.getValue();
	}

	@Override
	public ArrayList<Double> evaluate(ArrayList<Double> xs) {
		ArrayList<Double> evaluated = new ArrayList<Double>(xs.size());
		Collections.fill(evaluated, this.getValue());
		return evaluated;
	}

	@Override
	public Function differentiate() {
		return new Constant();
	}

	@Override
	public String getFuncType() {
		return "k";
	}

	@Override
	public String getFuncSignature() {
		return "k";
	}

	@Override
	public String saveString(int depth) {
		StringBuilder yep = new StringBuilder();

		yep.append(this.tabs(depth));
		yep.append("k {");

		yep.append(this.value);

		yep.append("}");

		return yep.toString();
	}

}
