import java.util.ArrayList;

public class Cos extends Trig {

	public Cos(double freq, double phase, double constant) {
		super(freq, phase, constant);
	}


	public Cos(double freq, double phase) {
		super(freq, phase);
	}


	public Cos(double freq) {
		super(freq);
	}


	public Cos() {
		super();
	}


	@Override
	public String toStringCustom(String base) {
		StringBuilder wawa = new StringBuilder();
		wawa.append(super.getC());
		wawa.append("cos(");
		wawa.append(super.getFreq());
		wawa.append(base);
		wawa.append(" + ");
		wawa.append(super.getPhase());
		wawa.append(")");
		return wawa.toString();
	}


	@Override
	public double evaluate(double x) {
		return super.getC() * Math.cos(super.getFreq()*x + super.getPhase());
	}


	@Override
	public ArrayList<Double> evaluate(ArrayList<Double> xs) {
		double c = super.getC();
		double f = super.getFreq();
		double ϕ = super.getPhase();
		ArrayList<Double> evaluated = xs.stream().mapToDouble((x) -> c * Math.cos(x * f + ϕ))
				   						.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
		return evaluated;
	}


	@Override
	public Trig differentiate() {
		return new Sin(super.getFreq(), super.getPhase(), -super.getC());
	}


	@Override
	public String getFuncType() {
		return "c";
	}

}
