import java.util.ArrayList;

public class Sin extends Trig {

	public Sin(double freq, double phase, double constant) {
		super(freq, phase, constant);
	}


	public Sin(double freq, double phase) {
		super(freq, phase);
	}


	public Sin(double freq) {
		super(freq);
	}


	public Sin() {
		super();
	}


	@Override
	public String toStringCustom(String base) {
		StringBuilder wawa = new StringBuilder();
		wawa.append(super.getC());
		wawa.append("sin(");
		wawa.append(super.getFreq());
		wawa.append(base);
		wawa.append(" + ");
		wawa.append(super.getPhase());
		wawa.append(")");
		return wawa.toString();
	}


	@Override
	public double evaluate(double x) {
		return super.getC() * Math.sin(super.getFreq()*x + super.getPhase());
	}


	@Override
	public ArrayList<Double> evaluate(ArrayList<Double> xs) {
		double c = super.getC();
		double f = super.getFreq();
		double ϕ = super.getPhase();
		ArrayList<Double> evaluated = xs.stream().mapToDouble((x) -> c * Math.sin(x * f + ϕ))
				   						.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
		return evaluated;
	}


	@Override
	public Trig differentiate() {
		return new Cos(super.getFreq(), super.getPhase(), super.getC());
	}


	@Override
	public String getFuncType() {
		return "s";
	}

}
