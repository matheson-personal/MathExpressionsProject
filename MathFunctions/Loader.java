import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.nio.file.*;

/* TODO
 * 		think about comments section? Parser can read until special character
 * 		think about custom written functions being used in formulae - u {funcName, filepath(?)}
 * 			-> make a new object to point to function, or add name variable to superclass
 * 			-> Loader object can store loaded functions? maybe then functions shouldn't be static, at least parseFunction?
 * 
 * 		funcName can be the key for a map containing loaded custom functions
 * 		u {funcName} tries to access it, but if it fails alerts the user that it hasn't been loaded in
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
		//this could be a switch maybe TODO to make it look nicer
		if (funcType == 'u') {
			return makeUser(argList);

		} else if (funcType == 'p') {
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


	public static Polynomial makePolynomial(String argList) {
		//Takes a String of the contents of curly brackets following the p. Curly brackets are Excluded!!!!!!
		//returns polyomial with coefficients given by argList. Index is equal to the power of x
		String[] splitList = argList.split(",");
		ArrayList<Double> coefficients = Arrays.stream(splitList).mapToDouble((x) -> Double.valueOf(x))
											   .collect(ArrayList::new, ArrayList::add,ArrayList::addAll);

		return new Polynomial(coefficients);
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
