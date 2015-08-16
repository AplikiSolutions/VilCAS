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

package vilcas.categories;

import vilcas.Constants;


public class CategoryFysiikka extends Category{
    
    public CategoryFysiikka(){
        super("Physics");
    }
    
    @Override
    public void addEquations(){
        
        String name = "x = vt + ½at²";
        String desc = "<html>Final position as<br>a function of initial velocity,<br>acceleration and time</html>";
        //Kiihtyvyys
        addEquation(new Equation(name, desc, "Position", "Velocity", "Acceleration", "Time"){
            private final int x = 0, v = 1, a = 2, t = 3;
            
            @Override
            public void calculate(int object) throws Exception{
                switch(object){
                    case x:
                        //calculate position
                        set(x, get(v) * get(t) + get(a) / 2 * square(get(t)));
                        break;
                    case v:
                        //calculate velocity
                        set(v, (get(x) - get(a) / 2 * square(get(t)))/get(t));
                        break;
                    case a:
                        //calculate acceleration
                        set(a, 2 * (get(x) - get(v) * get(t)) / square(get(t)));
                        break;
                    case t:
                        
                        //calculate time (quadratic, has two answers)
                        double d1 = (-get(v) + Math.sqrt(square(get(v)) + 2 * get(a) * get(x))) / get(a);
                        double d2 = (-get(v) - Math.sqrt(square(get(v)) + 2 * get(a) * get(x))) / get(a);
                        
                        setMultiple(t, d1, d2);
                        break;
                }
            }
        });
        
        
        name = "F = ma";
        desc = "Newton's second law of motion";
        //Voima
        addEquation(new Equation(name, desc, "Force", "Mass", "Acceleration"){
            private final int F = 0, m = 1, a = 2;
            
            @Override
            public void calculate(int object) throws Exception{
                switch(object){
                    case F:
                        //calculate force
                        set(F, get(m) * get(a));
                        break;
                    case m:
                        //calculate mass
                        set(m, get(F) / get(a));
                        break;
                    case a:
                        //calculate acceleration
                        set(a, get(F) / get(m));
                        break;
                }
                
            }
        });
        
        
        name = "Orbital period";
        desc = "<html>Orbital period of a light object<br>orbiting a massive body</html>";
        //Orbital period
        addEquation(new Equation(name, desc, "Time", "Semi-major axis", "Mass"){
            private final int t = 0, a = 1, M = 2;
            
            @Override
            public void calculate(int object) throws Exception{
                switch(object){
                    case t:
                        set(t, 2 * Math.PI * Math.sqrt(Math.pow(get(a), 3) / (get(M) * Constants.G)));
                        break;
                    case a:
                        set(a, Math.cbrt(square(get(t) / (2 * Math.PI)) * Constants.G * get(M)));
                        break;
                    case M:
                        set(M, Math.pow(get(a), 3) / square(get(t) / (2 * Math.PI)) / Constants.G);
                        break;
                }
                
            }
        });
        
    }
    
    private double square(double d){
        return d * d;
    }
}
