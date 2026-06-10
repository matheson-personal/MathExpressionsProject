
import java.util.ArrayList;
import java.util.Arrays;

public class testFile {

	public static void main(String[] args) {
		Loader L = new Loader();

		ArrayList<Double> polyCoeff = new ArrayList<Double>();
		polyCoeff.add(7.0); polyCoeff.add(3.0); polyCoeff.add(1.0); polyCoeff.add(2.0);
		// 7 + 3x + x^2 + 2x^3
		
		Polynomial oneByOne = new Polynomial();
		Polynomial allAtOnce = new Polynomial();
		Polynomial atInit = new Polynomial(polyCoeff);

		for (int i = 0; i < 4; i++) {
			oneByOne.add(i, polyCoeff.get(i));
		}

		allAtOnce.addAll(polyCoeff);
		
		System.out.println(oneByOne.toString());
		System.out.println(allAtOnce.toString());
		System.out.println(atInit.toString());
		//all 3 should print 2.0x^3 + 1.0x^2 + 3.0x + 7.0

		System.out.println();
		
		ArrayList<Double> calcAtOnce = new ArrayList<Double>();
		for (double i = 0; i < 4; i++) {
			System.out.print(atInit.evaluate(i) + ", ");
			calcAtOnce.add(i);
			//should print 7, 13, 33, 79, 
		}

		System.out.println();

		System.out.println(atInit.evaluate(calcAtOnce));

		System.out.println();



		Polynomial ddx = (Polynomial) atInit.differentiate();

		System.out.println(ddx.toString());
		//should print 3.0 + 2.0x + 6.0x^2

		System.out.println();
		
		for (double i = 0; i < 4; i++) {
			System.out.print(ddx.evaluate(i) + ", ");

			//should print 3, 11, 31, 63, 
		}
		
		System.out.println();
		
		
		
		System.out.println(oneByOne.equals(5));
		System.out.println(oneByOne.equals(oneByOne));
		System.out.println(oneByOne.equals(allAtOnce));
		System.out.println(oneByOne.equals(ddx));
		//should print false, true, true, false

		System.out.println();


		System.out.println(oneByOne.saveString());
		System.out.println(L.parseFunction(oneByOne.saveString()));
		System.out.println();



		Polynomial extraPoly = new Polynomial(calcAtOnce);
		// 3x^3 + 2x^2 + x + 0 from the calcAtOnce defined earlier (lazy)
		
		Polynomial fu = ddx;		// f(u) = 6u^2 + 2u + 3
		Polynomial ux = extraPoly;	// u(x) = 3x^3 + 2x^2 + x + 0

		Functional FalTest = new Functional(fu, ux);

		for (double i = 0; i < 4; i++) {
			System.out.print(FalTest.evaluate(i) + ", ");
			// u : 0, 6, 34, 102
			//so FalTest should print 3, 231, 7007, 62631, 
		}

		System.out.println();
		System.out.println(FalTest.evaluate(calcAtOnce));

		System.out.println();
		System.out.println(FalTest.toString());
		/* should print
		   f(u) = 6u^2 + 2u + 3
		   where u(x) = 3x^3 + 2x^2 + x + 0
		*/


		System.out.println();
		Product FalDdx = (Product) FalTest.differentiate(); //in this case I know it's a product. Maybe I should set it to the return type since things can be simplified later todo????
		
		for (Function f : FalDdx.getFs()) {
			System.out.println(f.toString());
		}
		/* should print
		   f(u) = 12u + 2
		   where u(x) = 3x^3 + 2x^2 + x + 0
		   9x^2 + 4x + 1

		   and since they're in a Product they're multiplied as according to the chain rule
		   no toString for Product yet. requires a lot more thinking
		*/

		System.out.println();


		ArrayList<Function> toSum = new ArrayList<Function>();
		toSum.add(extraPoly); toSum.add(atInit); toSum.add(ddx);
		//3x^3 + 2x^2 + x + 0,   2.0x^3 + 1.0x^2 + 3.0x + 7.0,   3.0 + 2.0x + 6.0x^2
		//in total i believe 5x^3 + 9x^2 + 6x + 10


		Sum internalSum = new Sum();
		
		Polynomial Whatever = new Polynomial();
		Whatever.add(-4.0); Whatever.add(-1.0); Whatever.add(2.0); Whatever.add(1.0);
		// x^3 + 2x^2 - x - 4 haven't tested -ves yet so this is worthwhile as well
		// TODO make -ves look nicer with the toString
		// TODO let initialisation take Arrays just so it takes less characters to type when testing
		
		Polynomial AugustusGloop = new Polynomial();
		AugustusGloop.add(9.0); AugustusGloop.add(-8.0); AugustusGloop.add(5.0);
		// 5x^2 - 8x + 9
		// for some reason naming a function Augustus Gloop is hilarious to me
		
		internalSum.add(Whatever);
		internalSum.add(AugustusGloop);
		//in total x^3 + 3x^2 - 9x - 4
		
		
		toSum.add(internalSum);
		
		
		Sum allAtOnceSum = new Sum();
		Sum oneByOneSum = new Sum();
		Sum atInitSum = new Sum(toSum);

		for (Function f : toSum) {
			oneByOneSum.add(f);
		}
		
		allAtOnceSum.addAll(toSum);
		
		for (double i = 0.0; i < 4; i++) {
			System.out.print(allAtOnceSum.evaluate(i)+" ");
			System.out.print(oneByOneSum.evaluate(i)+" ");
			System.out.print(atInitSum.evaluate(i)+" ");
			//should print 15 15 15 34 34 34 121 121 121 312 312 312
		}

		Sum ddxSum = (Sum) atInitSum.differentiate();

		System.out.println(atInitSum.evaluate(calcAtOnce));
		System.out.println();

		System.out.println(atInitSum.saveString());
		System.out.println(atInitSum.equals(L.parseFunction(atInitSum.saveString())));
		System.out.println(ddxSum.saveString());
		System.out.println();

		ArrayList<Function> toMultiply = new ArrayList<Function>();
		toMultiply.add(atInitSum); toMultiply.add(AugustusGloop);
		Product xOneByOne = new Product();
		Product xAllAtOnce = new Product();
		Product xAtInit = new Product(toMultiply);

		for (Function f : toMultiply) {
			xOneByOne.add(f);
		}
		
		xAllAtOnce.addAll(toMultiply);


		for (double i = 0.0; i < 4; i++) {
			System.out.print(xAllAtOnce.evaluate(i)+" ");
			System.out.print(xOneByOne.evaluate(i)+" ");
			System.out.print(xAtInit.evaluate(i)+" ");
			//should print 135 135 135 204 204 204 1573 1573 1573 9360 9360 9360
		}

		Sum ddxProd = (Sum) xAtInit.differentiate();

		System.out.println(xAllAtOnce.evaluate(calcAtOnce));
		
		System.out.println();
		System.out.println(xAtInit.saveString());
		System.out.println(xAtInit.equals(L.parseFunction(xAtInit.saveString()))); //still doesn't hava an overwritten equals method TODO
		System.out.println(ddxProd.saveString());
		System.out.println();


		Exponential noInput = new Exponential();		 //should be e^x
		Exponential multOnly = new Exponential(2.0);	 //should be e^2x
		Exponential baseToo = new Exponential(1.0, 2.0); //should be 2^x

		for (double i = 0.0; i < 4; i++) {
			System.out.print(noInput.evaluate(i) + " ");
			System.out.print(multOnly.evaluate(i) + " ");
			System.out.print(baseToo.evaluate(i) + " ");
			//1 1 1 e e^2 2 e^2 e^4 4 e^3 e^6 8
		}

		System.out.println(noInput.evaluate(calcAtOnce));

		System.out.println(noInput.toString());
		System.out.println(noInput.differentiate().toString());
		System.out.println(multOnly.saveString());
		System.out.println(multOnly.differentiate().saveString());


		System.out.println();

		Logarithmic log2_4x = new Logarithmic(4, 2);
		Logarithmic ln_ex = new Logarithmic(Math.E);
		Logarithmic ln = new Logarithmic();

		double two = 2;
		double e = Math.E;

		for (double i=0; i<4; i++) {
			System.out.print(log2_4x.evaluate(two)+" ");
			System.out.print(ln_ex.evaluate(e)+" ");
			System.out.print(ln.evaluate(e)+", ");
			two *=2; e*=Math.E;
			//3.0 2.0 1.0, 4.0 3.0 2.0, 5.0 4.0 3.0, 6.0 5.0 4.0, 
		}

		System.out.println(ln_ex.evaluate(calcAtOnce));
		// -Infinity, 1.0, 1.6931471805599454, 2.09861228866811

		//ddx not implemented yet!!!!!!!!!! TODO

		System.out.println("\n\n"+log2_4x.saveString());
		System.out.println(ln_ex.saveString());

		String polyWithCommentsTest = "p {1,7.7,2.0,50.0,0,9.0,0,10.0} in theory i can just type anything after the parser considers the function finished";
		Polynomial nowShouldBeAWorkingPolynomial = (Polynomial) L.parseFunction(polyWithCommentsTest);
		System.out.println(nowShouldBeAWorkingPolynomial.saveString());
		//yep that works
	}
}
