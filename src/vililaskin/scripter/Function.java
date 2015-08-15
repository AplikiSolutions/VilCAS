/*
* Copyright (C) 2015 Apliki Solutions Nyman & Yli-Opas
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
* 
* 
* Full GNU GPL can be found in LICENSE.txt
* 
* If this code is reused, this header must tell if the code
* is modified or not.
* 
* All changes to the code must be distinguishable from
* the original code.
*/

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

