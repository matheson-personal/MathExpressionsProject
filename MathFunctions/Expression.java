
import java.util.ArrayList;

public abstract class Expression extends Function {
	public abstract void add(Function f);

	public abstract void addAll(ArrayList<Function> fs);

	public abstract Function getFunc(int i);

	//public abstract Function simplify() 
	//this is far off in the future. Just for collecting terms especially for multiplying exponentials and adding polynomials, evaluating if Functionals have only constants as arguments

}
