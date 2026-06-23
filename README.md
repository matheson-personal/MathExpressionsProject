# MathExpressionsProject
Small-medium project for Java exercise.

This Package contains different classes to express most conventional single argument functions as a series of objects. Each Function object and each combination of them is infinitely differentiable. There is no simplification yet, so the product of many infinitely differentiable functions will balloon in size (though contributions equal to 0 are not included)

There is also functionality to return any object as a pseudocode string that can be interpreted by the Loader class, to again load it into memory. The parsing of saveStrings ignores whitespace and is somewhat styled after java. Adding elements to Sums and Polynomials and such is somewhat cumbersome, so this saveString method is also a convenient way to quickly write new Functions.

The pseudo-code is structured as follows:
"""
For individual Functions
v Function type
p {1, 3, 4, 6, 0, 0, 2.5}
   ^  ^  ^ everything within curly brackets are arguments
"""

For Combining Functions (and in general Functions that store other Functions)
"""
+ {1, -1, 4} [ <-some combining Functions still have non-Function arguments
    p {2,3,4}; <-Internal Functions are inside square brackets and are separated by semicolons
    e {3, 0.6};
    x {} [     <-some combining Functions do not have arguments, but still must have curly brackets
        s {1,5,10}; <-the saveString method automatically indents, but this is not necessary for the parser to understand
        c {1,5,1};
    ]; <-combining functions finish on the next line in saveStrings but again this is not necessary
]
"""
List of implemented Functions:
(p)olynomial
(e)xponential
(l)ogarithmic
(s)in
(c)os
(t)an
(x)product
(+)sum
(^)spindle
