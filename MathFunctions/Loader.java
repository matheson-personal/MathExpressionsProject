import java.util.Arrays;
import java.util.ArrayList;
/* TODO
 * 		think about comments section? Parser can read until special character
 * 		think about custom written functions being used in formulae - u {funcName, filepath(?)}
 * 			-> make a new object to point to function, or add name variable to superclass
 * 			-> Loader object can store loaded functions? maybe then functions shouldn't be static, at least parseFunction?
 */
public class Loader {

	public static Character[] clist = {'p', 'l', 'e', 'k', 's', 'c', 't', 'x', '+', '^'};
	public static ArrayList<Character> funcTypes = new ArrayList<Character>(Arrays.asList(clist));
	
	public static Character[] exList = {'+', 'x'};
	public static ArrayList<Character> expressionTypes = new ArrayList<Character>(Arrays.asList(exList));


	public static int countBrackets(String s, char openChar, char closeChar) {
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


	public static Function pointToProtocol (char funcType, String argList, String funcList) {
		if (funcType == 'p') {
			return makePolynomial(argList);

		/* TODO - classes not implemented yet
		} else if (funcType == 'l') {
			return makeLogarithmic(argList);

		} else if (funcType == 'e') {
			return makeExponential(argList);

		} else if (funcType == 'k') {
			return makeConstant(argList);

		} else if (funcType == 's') {
			return makeSin(argList);

		} else if (funcType == 'c') {
			return makeCos(argList);

		} else if (funcType == 't') {
			return makeTan(argList);
		*/

		} else if (funcType == '+') {
			return makeSum(argList, funcList);

		} else if (funcType == 'x') {
			return makeProduct(argList, funcList);

		} else if (funcType == 'f') {
			return makeFunctional(argList, funcList);

		} else {
			System.out.println("Func type \"" + funcType + "\" not recognised.");
			return null;
		}
	}



	//Maybe shove these text parsers into the class they make since they're static? This way if only one is imported, the code
	//will still be able to interpret functions of that type written in text
	public static Polynomial makePolynomial(String argList) {
		String[] splitList = argList.split(",");
		ArrayList<Double> coefficients = Arrays.stream(splitList).mapToDouble((x) -> Double.valueOf(x))
											   .collect(ArrayList::new, ArrayList::add,ArrayList::addAll);

		return new Polynomial(coefficients);
	}

	//in future maybe some way to use wrappers since sum and product methods are all similar

	public static Sum makeSum(String argList, String funcList) {
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


	public static Product makeProduct(String argList, String funcList) {
		Product poo = new Product();
		ArrayList<String> functionList = splitFuncList(funcList);
		
		for (int i=0; i<functionList.size(); i++) {
			Function nextFunc = parseFunction(functionList.get(i));
			poo.add(nextFunc);
		}

		return poo;
	}

	
	public static Functional makeFunctional(String argList, String funcList) {
		ArrayList<String> functionList = splitFuncList(funcList);

		Function f = parseFunction(functionList.get(0));
		Function u = parseFunction(functionList.get(1));

		return new Functional(f, u);
	}



	public static Function parseFunction(String funcString) {

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
