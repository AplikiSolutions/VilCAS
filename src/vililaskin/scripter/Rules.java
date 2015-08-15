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
