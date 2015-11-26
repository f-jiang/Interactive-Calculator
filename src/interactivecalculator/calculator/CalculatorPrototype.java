package interactivecalculator.calculator;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.ArrayDeque;

/**
 *
 * @author Feilan
 */
public class CalculatorPrototype {

    private static Pattern pattern;
    private static Matcher matcher;
    private static HashMap<String, Double> variables = new HashMap();
    
    private static final String OPERATORS = String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s",
        Operator.MULTIPLICATION.value, Operator.ADDITION.value, Operator.DIVISION.value,
        Operator.EXPONENTIATION.value, Operator.MODULUS.value, Operator.UNARY_MINUS.value,
        Operator.LEFT_PARENTHESIS.value, Operator.RIGHT_PARENTHESIS.value,
        Operator.SUBTRACTION.value);
    
    public static final String VARIABLE_PATTERN = "(?<!\\p{Alpha})\\p{Alpha}+(?!\\p{Alpha})";
    public static final String NUMBER_PATTERN = "(?<!\\d)-?(([\\d]+(\\.[\\d]+)?)|(\\.[\\d]+))";
    public static final String OPERATOR_PATTERN = String.format("[%s]", OPERATORS);
    public static final double UNDEFINED_VARIABLE_DEFAULT_VALUE = 0.0;
    
    public static String eval(String exp, boolean isPostfix) {
//        System.out.println("Expression before plugging in: " + exp);
        exp = plugIn(exp);
//        System.out.println("Expression after plugging in: " + exp);

        ArrayDeque<String> expQueue = queueify(exp);
        ArrayDeque<Double> operandStack = new ArrayDeque<>();
        Double a, b;
        Double result = 0.0;
        Operator op;
        String item;
        String ans = null;
        
        if (!isPostfix) {
            expQueue = getPostfixNotation(expQueue);
        }

        while (!expQueue.isEmpty()) {
            item = expQueue.removeFirst();
            
            if (item.matches(NUMBER_PATTERN)) {
                try {
                    operandStack.addFirst(Double.valueOf(item));
                } catch (NumberFormatException e) {
//                    System.out.format("Operand \"%s\": %s%n", item, e.getMessage());
                    return ans;
                }
            } else if (item.matches(OPERATOR_PATTERN)) {
                op = Operator.withValue(item);
                
                b = operandStack.pollFirst();
                a = operandStack.pollFirst();

                if (op.numOperands == 1 && a != null) {
                    operandStack.addFirst(a);
                }
                
                try {
                    switch (op) {
                        case ADDITION:
                            result = a + b;
                            break;
                        case SUBTRACTION:
                            result = a - b;
                            break;
                        case MULTIPLICATION:
                            result = a * b;
                            break;
                        case DIVISION:
                            result = a / b;
                            break;
                        case EXPONENTIATION:
                            result = Math.pow(a, b);
                            break;
                        case MODULUS:
                            result = a % b;
                            break;
                        case UNARY_MINUS:
                            result = -b;
                            break;
                    }

                    operandStack.addFirst(result);
                } catch (NullPointerException e) {
//                    System.out.format("Insufficient operands for operator \"%s\"%n", op.value);
                    return ans;
                }   
            }
        }

        if (operandStack.size() == 1) {
            ans = operandStack.removeFirst().toString();
        } else {
//            System.out.println("Expression contains excess operands");
        }
             
        return ans;
    }
    
    public static String getPostfixNotation(String exp) {
        ArrayDeque<String> expQueue = queueify(exp);
        return CalculatorPrototype.getPostfixNotation(expQueue).toString();
    }    
    
    public static void storeVariable(String name, double value) {
        if (name.matches(VARIABLE_PATTERN)) {
            variables.put(name, value);
        } else {
//            System.out.println("Invalid variable name");
        }
    }
    
    public static void deleteVariable(String name) {
        variables.remove(name);
    }
    
    public static void deleteAllVariables() {
        variables.clear();
    }    

    public static ArrayList<String> getUndefinedVariables(String exp) {
        ArrayList<String> undefinedVars = new ArrayList<>();
        String var;
        pattern = Pattern.compile(VARIABLE_PATTERN);
        matcher = pattern.matcher(exp);
        
        while(matcher.find()) {            
            var = exp.substring(matcher.start(), matcher.end());
            if (!variables.containsKey(var) && !undefinedVars.contains(var)) {
                undefinedVars.add(var);
            }
        }
        
        return undefinedVars;
    }

    public static String plugIn(String exp, double... vals) {
        String var;
        int i = 0;
        double val;

        pattern = Pattern.compile(VARIABLE_PATTERN);
        matcher = pattern.matcher(exp);

        while (matcher.find()) {
            var = exp.substring(matcher.start(), matcher.end());
            if (variables.containsKey(var)) {
                val = variables.get(var);
            } else if (i < vals.length) {
                val = vals[i++];
            } else {
//                System.out.format("Found undefined variable %s when plugging in; assigned default value of %f",
//                    var, UNDEFINED_VARIABLE_DEFAULT_VALUE);
                    val = UNDEFINED_VARIABLE_DEFAULT_VALUE;
            }

            exp = exp.replaceAll(var, String.valueOf(val));
            matcher.reset(exp);
        }

        return exp;
    }

    private static ArrayDeque<String> queueify(String exp) {
        ArrayDeque<String> expQueue = new ArrayDeque<>();
        String item;
        
        pattern = Pattern.compile(String.format("%s|%s|%s", VARIABLE_PATTERN, NUMBER_PATTERN, OPERATOR_PATTERN));
        matcher = pattern.matcher(exp);
        
        while (matcher.find()) {
            item = exp.substring(matcher.start(), matcher.end());
            expQueue.add(item);
        }

//        System.out.println("Queueified expression: " + expQueue.toString());
        
        return expQueue;
    }
    
    // TODO: make this recognize mismatched parentheses and excess/lack of operands and operators
    private static ArrayDeque<String> getPostfixNotation(ArrayDeque<String> exp) {
        // shunting yard algorithm
        ArrayDeque<String> postfixExp = new ArrayDeque<>();  // output queue
        ArrayDeque<Operator> ops = new ArrayDeque<>();  // operator stack
        Operator op, topOp;
        String item;
        boolean foundRightParenthesis = false;

        while (!exp.isEmpty()) {
            item = exp.peekFirst();

            if (item.matches(NUMBER_PATTERN) || item.matches(VARIABLE_PATTERN)) {
                postfixExp.addLast(item);
                exp.removeFirst();
            } else if (item.matches(OPERATOR_PATTERN)) {
                op = Operator.withValue(item);
                topOp = ops.peekFirst();

                if (ops.isEmpty()) {
                    exp.removeFirst();
                    ops.addFirst(op);
                } else if (foundRightParenthesis) {
                    topOp = ops.removeFirst();

                    if (topOp == Operator.LEFT_PARENTHESIS) {
                        foundRightParenthesis = false;
                    } else {
                        postfixExp.addLast(topOp.value);
                    }
                } else if (op == Operator.RIGHT_PARENTHESIS) {
                    exp.removeFirst();
                    foundRightParenthesis = true;
                } else if (topOp.precedence < op.precedence || topOp == Operator.LEFT_PARENTHESIS) {
                    exp.removeFirst();
                    ops.addFirst(op);
                } else if (topOp.precedence >= op.precedence) {
                    ops.removeFirst();
                    postfixExp.addLast(topOp.value);
                }
            }
        }

        while (!ops.isEmpty()) {
            topOp = ops.removeFirst();

            if (topOp != Operator.LEFT_PARENTHESIS) {
                postfixExp.addLast(topOp.value);
            }
        }

//        System.out.println("Postfix expression: " + postfixExp.toString());

        return postfixExp;
    }

}
