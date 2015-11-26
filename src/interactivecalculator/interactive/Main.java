
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interactivecalculator.interactive;

import interactivecalculator.calculator.CalculatorPrototype;
import java.util.Scanner;

/**
 *
 * @author Feilan
 */
public class Main {
    
    public static void main(String[] args) {
        final String EVALUATE_INFIX = "i";
        final String EVALUATE_POSTFIX = "p";
        final String STORE_VARIABLE = "v";
        final String QUIT = "q";
        
        Scanner s = new Scanner(System.in);
        
        String input = "", name;
        double value;
        
        do {
            System.out.format("%-20s%-20s%-20s%-20s%n", 
                "'" + EVALUATE_INFIX + "': eval infix", 
                "'" + EVALUATE_POSTFIX + "': eval postfix", 
                "'" + STORE_VARIABLE + "': store var", 
                "'" + QUIT + "': quit");
            System.out.print("Pick option: ");
            
            try {
                input = s.nextLine().substring(0, 1);
                
                switch (input) {
                    case EVALUATE_INFIX:
                        System.out.print("Expression: ");
                        input = s.nextLine();
                        System.out.println(CalculatorPrototype.eval(input, false));

                        break;
                    case EVALUATE_POSTFIX:
                        System.out.print("Expression: ");
                        input = s.nextLine();
                        System.out.println(CalculatorPrototype.eval(input, true));

                        break;
                    case STORE_VARIABLE:
                        try {
                            System.out.print("Name: ");
                            name = s.nextLine();
                            System.out.print("Value: ");
                            value = Double.parseDouble(s.nextLine());
                            CalculatorPrototype.storeVariable(name, value);
                        } catch (NumberFormatException e) {
                            System.out.println(e.toString());
                        }

                        break;
                    default:
                        break;
                }                
            } catch (StringIndexOutOfBoundsException e) {
                // No action needs to be taken once caught
            } finally {
                System.out.print("\n");
            }
        } while (!input.equals(QUIT));
    }
    
}
