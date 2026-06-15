import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.nio.file.*;

/* TODO
 * 		think about comments section? Parser can read until special character <- after the function ends, the parser doesn't care
 * 			^->INSTEAD loadFunc will take responsibility and allow for a single file to hold multiple Functions?
 * 				^->just has to look for the start of each function. cut at the start of next to avoid too lazy coding
 *
 * 		think about custom written functions being used in formulae - u {funcName, filepath(?)} done
 * 			-> Loader object can store loaded functions? maybe then functions shouldn't be static, at least parseFunction?
 * 		unfortunate but they can't all be static if they wanna access already loaded u Functions
 * 		maybe there can be a static copy that has to reload it everytime if they can't be bothered with a Loader object??
 */
public class Loader {

	public static Character[] clist = {'u', 'p', 'l', 'e', 'k', 's', 'c', 't', 'x', '+', '^'};
	public static ArrayList<Character> funcTypes = new ArrayList<Character>(Arrays.asList(clist));
	
	public static Character[] exList = {'+', 'x'};
	public static ArrayList<Character> expressionTypes = new ArrayList<Character>(Arrays.asList(exList));

	public HashMap<String, Function> loadedFuncs = new HashMap<String, Function>();


	public static int countBrackets(String s, char openChar, char closeChar) {
		//counts opened and closed brackets in order to figure what is inside the current function being parsed
		//returns marker when the count has started, and every opened bracket has been closed
		//normally this will be used with '[' and ']' but equally useable for '(' and ')' and '{' and '}' depending on open and close char
		int marker=0;
		int count = 0;
		boolean countStart = false;

		while (marker < s.length()) {
			char nextChar = s.charAt(marker);

			if (nextChar == openChar) {
				count++;
				countStart = true;
			} else if (nextChar == closeChar) {
				count--;
			}

			if (count==0 && countStart) {
				break;
			}

			marker++;
		}

		return marker;
	}


	public static ArrayList<String> splitFuncList(String funcList) {
		//splits a list of functions within square brackets in order to parse them individually
		//SHOULD NOT COUNT INITIAL OPENED SQUARE BRACKET. Square brackets will be omitted when passing in the string funcList
		//The counts below is to account for Expression objects inside Expression objects
		//'(' brackets accounts for Functionals. The syntax for this may be changed TODO update this comment if+when this is done
		//^^right now functionals hold functions inside square brackets but emotionally i would like if to be p(q) rather than f {} [p;q;]
		int[] count = new int[2];
		int marker = 0;
		int funcStart = 0;
		ArrayList<String> split = new ArrayList<String>();

		while (marker < funcList.length()) {
			char nextChar = funcList.charAt(marker);

			switch (nextChar) {
				case '(':
					count[0]++;

				case ')':
					count[0]--;

				case '[':
					count[1]++;

				case ']':
					count[1]--;
			}

			if (nextChar == ';' && count[0]==0 && count[1]==0) { //
				String nextFunc = funcList.substring(funcStart, marker);
				split.add(nextFunc);
				funcStart = marker;
			}

			marker++;
		}

		return split;
	}


	public Function pointToProtocol (char funcType, String argList, String funcList) {
		//Cleans up the method parseFunction by pointing to the interpretation method for the correct funcType
		switch (funcType) {
			case 'u':
				return makeUser(argList);
			case 'p':
				return makePolynomial(argList);
			case 'l':
				return makeLogarithmic(argList);
			case 'e':
				return makeExponential(argList);
			case 'k':
				return makeConstant(argList);
			case 's':
				return makeSin(argList);
			case 'c':
				return makeCos(argList);
			case 't':
				return makeTan(argList);
			case '+':
				return makeSum(argList, funcList);
			case 'x':
				return makeProduct(argList, funcList);
			case 'f':
				return makeFunctional(argList, funcList);
			case '^':
				return makeSpindle(argList, funcList);
			default:
				throw new IlleagalArgumentException("Func type \"" + funcType + "\" not recognised.");
		}


	public String loadInOneLine(String filename) {
		//returns a stored Function in one line to be interpreted by the Loader class
		try {
			String data = new String(Files.readAllBytes(Paths.get(filename)));
			return data;
		} catch (Exception e) {
			System.out.println("didnt load :(");
			return null;
		}
	}

	public void loadFunc(String funcName, String filename) {
		//loads the text file under filename and turns it into a Function object
		String data = loadInOneLine(filename);

		Function loadedFunc = parseFunction(data);
		this.loadedFuncs.put(funcName, loadedFunc);

	}


	//Maybe shove these text parsers into the class they make, and make em static? This way if only one Function class is imported, the code
	//will still be able to interpret functions of that type written in text
	public Function makeUser(String argList) {
		String[] poo = argList.split(",");

		if (this.loadedFuncs.containsKey(poo[0])) {
			return this.loadedFuncs.get(poo[0]);
		} else if (poo.length == 2) {
			this.loadFunc(poo[0], poo[1]);
			return this.loadedFuncs.get(poo[0]);
		} else {
			System.out.print("Function named " + argList + " not found :(");
			return null;
		}
	}


	public void store(String funcName, Function func) {
		//store for reuse without having to first save in a separate txt file
		//now u{funcName} will return the same object rather than having to instantiate another Function (saving copy+pasting, and storage)
		//PLUS equations can be written in more englishy form, more understandable and easier to track
		this.loadedFuncs.put(funcName, func);
	} 


	public static Constant makeConstant(String argList) {
		return new Constant(Double.valueOf(argList));
	}

	public static Polynomial makePolynomial(String argList) {
		//Takes a String of the contents of curly brackets following the p. Curly brackets are Excluded!!!!!!
		//returns polyomial with coefficients given by argList. Index is equal to the power of x
		String[] splitList = argList.split(",");
		ArrayList<Double> coefficients = Arrays.stream(splitList).mapToDouble((x) -> Double.valueOf(x))
											   .collect(ArrayList::new, ArrayList::add,ArrayList::addAll);

		return new Polynomial(coefficients);
	}


	public static Exponential makeExponential(String argList) {
		String[] args = argList.split(",");

		double multiplier = Double.valueOf(args[0]);
		double factor = Double.valueOf(args[2]);

		if (args[1].matches("\s*e\s*")) {
			Exponential finagle = new Exponential(multiplier);
			finagle.linearFactor = factor; //i might let -1 for base just return an exponential with base e
			return finagle;
		}

		return new Exponential(multiplier, Double.valueOf(args[1]), factor);
	}


	//a, base
	public static Logarithmic makeLogarithmic(String argList) {
		String[] args = argList.split(",");

		double a = Double.valueOf(args[0]);

		if (args[1].matches("\s*e\s*")) {
			return new Logarithmic(a);
		}

		return new Logarithmic(a, Double.valueOf(args[1]));
	}


	public static Sin makeSin(String argList) {
		String[] poo = argList.split(",");
		double freq = Double.valueOf(poo[0]);
		double phase = Double.valueOf(poo[1]);
		double c = Double.valueOf(poo[2]);
		return new Sin(freq, phase, c);
	}


	public static Cos makeCos(String argList) {
		String[] poo = argList.split(",");
		double freq = Double.valueOf(poo[0]);
		double phase = Double.valueOf(poo[1]);
		double c = Double.valueOf(poo[2]);
		return new Cos(freq, phase, c);
	}


	public static Tan makeTan(String argList) {
		String[] poo = argList.split(",");
		double freq = Double.valueOf(poo[0]);
		double phase = Double.valueOf(poo[1]);
		double c = Double.valueOf(poo[2]);
		return new Tan(freq, phase, c);
	}

	//in future maybe some way to use wrappers since sum and product methods are all similar

	public Sum makeSum(String argList, String funcList) {
		//Takes a String of the contents of curly brackets following the +. Surrounding brackets are Excluded!!!!!!
		//Takes a String of the contents of square brackets following the curly brackets. Surrounding brackets are Excluded!!!!!!
		//returns sum of functions within funcList with coefficients given by argList.
		Sum poo = new Sum();
		String[] coeffList = argList.split(",");
		ArrayList<String> functionList = splitFuncList(funcList);
		
		for (int i=0; i<functionList.size(); i++) {
			Function nextFunc = parseFunction(functionList.get(i));
			double nextCoeff = Double.valueOf(coeffList[i]);
			poo.add(nextFunc, nextCoeff);
		}

		return poo;
	}


	public Product makeProduct(String argList, String funcList) {
		//Takes a String of the contents of curly brackets following the +. Surrounding brackets are Excluded!!!!!!
		//at present curly brackets are unused by Product, so the contents will be ignored
		//Takes a String of the contents of square brackets following the curly brackets. Surrounding brackets are Excluded!!!!!!
		//returns sum of functions within funcList with coefficients given by argList.
		Product poo = new Product();
		ArrayList<String> functionList = splitFuncList(funcList);
		
		for (int i=0; i<functionList.size(); i++) {
			Function nextFunc = parseFunction(functionList.get(i));
			poo.add(nextFunc);
		}

		return poo;
	}

	
	public Functional makeFunctional(String argList, String funcList) {
		ArrayList<String> functionList = splitFuncList(funcList);

		Function f = parseFunction(functionList.get(0));
		Function u = parseFunction(functionList.get(1));

		return new Functional(f, u);
	}


	public Function makeSpindle(String argList, String funcList) {
		ArrayList<String> functionList = splitFuncList(funcList);

		Function f = parseFunction(functionList.get(0));

		//if f is constant, a Functional of an Exponential is much better, as its differential is simpler
		//might be a tad more performant since a constant no longer needs to be evaluated a billion times
		if (f.getFuncType() == "k") {
			Constant k = (Constant) f;
			Exponential yep = new Exponential(1, k.getValue());
			Function g = parseFunction(functionList.get(1));

			return new Functional(yep, g);

		} else if (!(Boolean.valueOf(argList))) {
			return new Power(f, Double.valueOf(functionList.get(1)));

		}

		Function g = parseFunction(functionList.get(1));

		return new Spindle(f, g);
	}


	public Function parseFunction(String funcString) {

		int marker = 0;
		boolean funcNotFound = true;
		char funcType = ' ';

		while (marker < funcString.length()) {
			char nextChar = funcString.charAt(marker);
			
			if (funcNotFound && funcTypes.contains(nextChar)) {
				funcType = funcString.charAt(marker);	
				funcNotFound = false;
			}

			marker++;
		}
			
		int argStart = funcString.indexOf("{") + 1;
		int argEnd = funcString.indexOf("}");
		String argList = funcString.substring(argStart, argEnd);

		String funcList = "";

		if (expressionTypes.contains(funcType)) {
			int funcListStart = funcString.indexOf("[");
			int funcListEnd = countBrackets(funcString, '[', ']');
			funcList = funcString.substring(funcListStart, funcListEnd);
		}
		
		return pointToProtocol(funcType, argList, funcList);
	}
}
