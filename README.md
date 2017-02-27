# Interactive-Calculator
This is a simple, versatile scientific calculator that can be used in both front- and back-end environments. Dijkstra's shunting-yard algorithm
is used to convert expressions from infix into postfix notation, although expressions can also be entered in postfix if desired.
The calculator also allows custom variables to be defined and used in expressions.

## Usage
### Evaluating expressions
```
String a = Calculator.eval("3 + (4 * 5)^2", false);    // a == "403"
String b = Calculator.eval("2 9 *", true);             // b == "18"
```

###Conversions
```
String postfix = Calculator.toPostfix("3 + (4 * 5)^2");  // postfix == "3 4 5 * 2 ^ +"
```

### Using variables
```
ArrayList<String> undefined = Calculator.getUndefinedVariables("3 + value + q * value");    // undefined == {"value", "q"}

Calculator.storeVariable("value", 5);
Calculator.storeVariable("q", 6);
String ans = Calculator.eval("3 + value + q * value", false);    // ans == "38"

String expr = Calculator.plugIn("3 + value + q * value");    // expr == "3 + 5 + 6 * 5"
String expr2 = Calculator.plugIn("a + b", 3, 4);             // expr2 == "3 + 4"
```

## Possible improvements
- Constants such as Pi and Euler's number
- Trigonometric, logarithmic, and other functions
- Equation solver using Newton's method or equivalent
- Postfix-to-infix conversion (via method `Calculator.toInfix()`)
- Access stored variables
- Exception instead of silent failure on undefined variable
