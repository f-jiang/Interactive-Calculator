/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interactivecalculator.calculator;

/**
 *
 * @author feilan
 */
public enum Operator {
    ADDITION            ("+", 1, 2),
    SUBTRACTION         ("-", 1, 2),
    MULTIPLICATION      ("*", 2, 2),
    DIVISION            ("/", 2, 2),    
    MODULUS             ("%", 2, 2),
    UNARY_MINUS         ("_", 3, 1),
    EXPONENTIATION      ("^", 4, 2),
    LEFT_PARENTHESIS    ("(", 5, -1),
    RIGHT_PARENTHESIS   (")", 5, -1);
    
    public final int precedence;
    public final int numOperands;
    public final String value;
    
    Operator(String val, int pr, int numOps) {
        this.precedence = pr;
        this.numOperands = numOps;
        this.value = val;
    }
    
    public static Operator withValue(String val) {
        for (Operator op : Operator.values()) {
            if (op.value.equals(val)) {
                return op;
            }
        }
        
        return null;
    }
}
