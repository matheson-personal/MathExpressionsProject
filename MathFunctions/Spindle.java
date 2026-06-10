import java.util.ArrayList;

public class Spindle extends Function {

	public Function f;
	public Function g;


	public Spindle(Function f, Function g) {
		this.f = f;
		this.g = g;
	}


	@Override
	public String toStringCustom(String base) {
		String f = this.getF().toStringCustom(base);
		String g = this.getG().toStringCustom(base);

		return f + "^(" + g + ")";
	}


	public Function getF() {
		return this.f;
	}


	public Function getG() {
		return this.g;
	}


	@Override
	public double evaluate(double x) {
		double f = this.getF().evaluate(x);
		double g = this.getG().evaluate(x);

		return Math.pow(f, g);
	}


//	@Override
//	public ArrayList<Double> evaluate(ArrayList<Double> xs) {
//		ArrayList<Double> fs = this.getF().evaluate(xs);
//		ArrayList<Double> gs = this.getG().evaluate(xs);
//
//		ArrayList<Double> evaluated = new ArrayList<Double>();
//
//		for (int i=0; i<xs.size(); i++) {
//			evaluated.add(Math.pow(fs.get(i), gs.get(i)));
//		}
//
//		return evaluated;
//	}


	//i think this one is probably the best but I'll test each eventually TODO
	@Override
	public ArrayList<Double> evaluate(ArrayList<Double> xs) {
		ArrayList<Double> evaluated = new ArrayList<Double>();

		for (double x : xs) {
			evaluated.add(Math.pow(this.getF().evaluate(x), this.getG().evaluate(x)));
		}

		return evaluated;
	}


//	@Override
//	public ArrayList<Double> evaluate(ArrayList<Double> xs) {
//		Function f = this.getF();
//		Function g = this.getG();
//		ArrayList<Double> evaluated = xs.stream().mapToDouble((x) -> Math.pow(f.evaluate(x), g.evaluate(x)))
//				   						.collect(ArrayList::new, ArrayList::add,ArrayList::addAll);
//		return evaluated;
//	}


	@Override
	public Function differentiate() {
		// dy/dx = y(ln(f)*dg/dx + g*(df/dx)/f)

		Function dfdx = this.getF().differentiate();
		Function dgdx = this.getG().differentiate();

		Sum bracket = new Sum();

			Functional lnf = new Functional(new Logarithmic(), this.getF());
			Product dgdxlnf = new Product();
			dgdxlnf.add(dgdx); dgdxlnf.add(lnf);

		bracket.add(dgdxlnf);
	
			Product gdfdx_f = new Product();
			Power oneOverF = new Power(this.getF(), -1);
			gdfdx_f.add(this.getG()); gdfdx_f.add(dfdx); gdfdx_f.add(oneOverF);

		bracket.add(gdfdx_f);

		Product finalFunc = new Product();
		finalFunc.add(this); finalFunc.add(bracket);

		return finalFunc;
	}


	@Override
	public String getFuncType() {
		return "^";
	}


	@Override
	public String getFuncSignature() {
		String fType = this.getF().getFuncSignature();
		String gType = this.getG().getFuncSignature();
		return fType + "^(" + gType + ")";
	}


	@Override
	public String saveString(int depth) {
		/* Looks like
		 * ^ {true} [
		 * 		p {1.0, 2.0, 3.0, 4.0};
		 * 		e {1.0, 2.0};
		 * ]
		 */
		StringBuilder ss = new StringBuilder();

		ss.append(this.tabs(depth));
		ss.append("^ {true} [\n");

		ss.append(this.getF().saveString(depth + 1));
		ss.append(";\n");

		ss.append(this.getG().saveString(depth + 1));
		ss.append(";\n");


		ss.append(this.tabs(depth));
		ss.append("]");

		return ss.toString();
	}


	public Function simplify() {
		return null;//check if exponent is constant, return a Power if it is. if both are const return a const
		//TODO put a isConstant method in Function should be easy just work for later
		//OR simplify starts at the deepest functions and constants propagate upwards
	}

}
