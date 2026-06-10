import java.util.ArrayList;

public class Tan extends Trig {

	public Tan(double freq, double phase, double constant) {
		super(freq, phase, constant);
	}


	public Tan(double freq, double phase) {
		super(freq, phase);
	}


	public Tan(double freq) {
		super(freq);
	}


	public Tan() {
		super();
	}


	@Override
	//probably more elegant way to do this idk. it's copied across all 3 of sin cos and tan with just the function name before the bracket open
	//i figured it'd be more performant if it didn't have to check getFuncType and do a switch-case or something
	public String toStringCustom(String base) {
		StringBuilder wawa = new StringBuilder();
		wawa.append(super.getC());
		wawa.append("tan(");
		wawa.append(super.getFreq());
		wawa.append(base);
		wawa.append(" + ");
		wawa.append(super.getPhase());
		wawa.append(")");
		return wawa.toString();
	}


	@Override
	public double evaluate(double x) {
		return super.getC() * Math.tan(super.getFreq()*x + super.getPhase());
	}


	@Override
	public ArrayList<Double> evaluate(ArrayList<Double> xs) {
		double c = super.getC();
		double f = super.getFreq();
		double ϕ = super.getPhase();
		ArrayList<Double> evaluated = xs.stream().mapToDouble((x) -> c * Math.tan(x * f + ϕ))
				   						.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
		return evaluated;
	}


	@Override
	public Functional differentiate() {
		Functional newfunc = new Functional(new OneTerm(-2), 
											new Cos(this.getFreq(), this.getPhase(), this.getC()));
		return newfunc;
	}


	@Override
	public String getFuncType() {
		return "t";
	}
}