package vililaskin.scripter;

import java.awt.*;
import javax.swing.*;
import java.util.*;

public class Function {
    
    public int type, operationType;
    
    String name;
    float value;
    Function f1, f2;
    
    //types
    public static final int VARIABLE = 0, CONSTANT = 1, OPERATION = 2;
    public static final int O_PLUS = 0, O_TIMES = 2, 
                            O_POW = 4, O_ROOT = 5, O_LOG = 6;
    
    //variable init
    public Function(String name){
        type = VARIABLE;
        this.name = name;
    }//Function

    //constant init
    public Function(float value){
        type = CONSTANT;
        this.value = value;
    }//Function

    //operation init
    public Function(int type, Function f1, Function f2){
        this.type = OPERATION;
        this.operationType = type;
        this.f1 = f1;
        this.f2 = f2;
    }//Function
    
    //check if identical
    public boolean identicalTo(Function f){
        if(type != f.type)
            return false;
        if(type == VARIABLE)
            return name.equals(f.name);
        if(type == CONSTANT)
            return value == f.value;
        return f1.identicalTo(f.f1) && f2.identicalTo(f.f2);
    }//identicalTo
    
}

