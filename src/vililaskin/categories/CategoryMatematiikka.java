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

package vililaskin.categories;

import vililaskin.Equation;


public class CategoryMatematiikka extends Category{
    
    public CategoryMatematiikka(){
        super("Mathematics");
    }
    
    @Override
    public void addEquations(){
        
        String name = "a² + b² + c² = d²";
        String desc = "<html>Pythagoras' theorem in 3d<br>(leave a, b or c as 0 for 2d)</html>";
        //Pythagoras
        addEquation(new Equation(name, desc, "a", "b", "c", "d"){
            private final int a = 0, b = 1, c = 2, d = 3;
            
            @Override
            public void calculate(int object) throws Exception{
                switch(object){
                    case a:
                        set(a, Math.sqrt(square(get(d)) - square(get(b)) - square(get(c))));
                        break;
                    case b:
                        set(b, Math.sqrt(square(get(d)) - square(get(a)) - square(get(c))));
                        break;
                    case c:
                        set(c, Math.sqrt(square(get(d)) - square(get(a)) - square(get(b))));
                        break;
                    case d:
                        set(d, Math.sqrt(square(get(a)) + square(get(b)) + square(get(c))));
                        break;
                }
            }
        });
        
        name = "ax² + bx + c = 0";
        desc = "The quadratic equation";
        //Quadratic
        addEquation(new Equation(name, desc, "x", "a", "b", "c"){
            private final int x = 0, a = 1, b = 2, c = 3;
            
            @Override
            public void calculate(int object) throws Exception{
                switch(object){
                    case x:
                        double d1 = -get(b) + Math.sqrt(square(get(b)) - 4 * get(a) * get(c));
                        d1 /= 2 * get(a);
                        double d2 = -get(b) - Math.sqrt(square(get(b)) - 4 * get(a) * get(c));
                        d1 /= 2 * get(a);
                        setMultiple(x, d1, d2);
                        break;
                }
                
            }
            
            @Override
            protected boolean selectable(int object){
                //can only calculate x
                return object == x;
            }
        });
        
        
        name = "Normal distribution";
        desc = "<html>The probability density<br>function for a non-normalized<br>"
                + "standard deviation<br>p = probability density<br>"
                + "x = value<br>μ = expected value<br>σ = deviation</html>";
        //Normal distribution
        addEquation(new Equation(name, desc, "p", "x", "μ", "σ"){
            private final int p = 0, x = 1, μ = 2, σ = 3;
            
            @Override
            public void calculate(int object) throws Exception{
                switch(object){
                    case p:
                        double d = - square((get(x) - get(μ)) / get(μ)) / 2;
                        d = Math.exp(d);
                        d /= get(σ) * Math.sqrt(2 * Math.PI);
                        set(p, d);
                        break;
                }
                
            }
            
            @Override
            protected boolean selectable(int object){
                //can only calculate x
                return object == p;
            }
            
        });
        
    }
    
    private double square(double d){
        return d * d;
    }
}
