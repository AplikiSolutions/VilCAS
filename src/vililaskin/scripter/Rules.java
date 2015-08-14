package vililaskin.scripter;

import java.util.*;

public class Rules {
    
    private static final ArrayList<String[]> rules = new ArrayList();
    
    //types
    public static final int VARIABLE = 0, CONSTANT = 1, OPERATION = 2, SPECIFIC_NUM = 3;
    public static final int PLUS = 0, MINUS = 1, TIMES = 2, DIVIDED = 3, 
                            POW = 4, ROOT = 5, LOG = 6;
    
    public static ArrayList<String[]> getRules(){
        return rules;
    }
    
    public static void initRules(){
        //format:
        //letters for operations, numbers as variables
        //for functions: {type_child1, type_self, type_child2}
        
        //basic calculations
        make("0 * _a", "0");
        make("_a * 0", "0");
        
    }
    
    private static void make(String s, String s2){
        String[] a = {s, s2};
        rules.add(a);
    }
    
}
