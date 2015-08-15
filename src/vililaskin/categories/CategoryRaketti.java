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
import vililaskin.Constants;


public class CategoryRaketti extends Category{
    
    public CategoryRaketti(){
        super("Rocketry");
    }
    
    @Override
    public void addEquations(){
        
        String name = "Delta v";
        String desc = "<html>The Tsiolkovsky rocket<br>equation for delta v</html>";
        //Delta v
        addEquation(new Equation(name, desc, "Delta v", "Isp", "Wet mass", "Dry mass"){
            private final int dv = 0, Isp = 1, m0 = 2, m1 = 3;
            
            @Override
            public void calculate(int object) throws Exception{
                switch(object){
                    case dv:
                        set(dv, get(Isp) * Constants.g * Math.log(get(m0) / get(m1)));
                        break;
                    case Isp:
                        set(Isp, get(dv) / (Constants.g * Math.log(get(m0) / get(m1))));
                        break;
                    case m0:
                        set(m0, get(m1) * Math.exp(get(dv) / (get(Isp) * Constants.g)));
                        break;
                    case m1:
                        set(m1, get(m0) / Math.exp(get(dv) / (get(Isp) * Constants.g)));
                        break;
                }
            }
        });
        
        
        name = "Re = Rt * sqrt(E)";
        desc = "<html>Equation for the expansion<br>ratio of a nozzle</html>";
        //Kuristimen expansion ratio
        addEquation(new Equation(name, desc, "Exit radius", "Throat radius", "Exp ratio"){
            private final int re = 0, rt = 1, E = 2;
            
            @Override
            public void calculate(int object) throws Exception{
                switch(object){
                    case re:
                        set(re, get(rt) * Math.sqrt(get(E)));
                        break;
                    case rt:
                        set(rt, get(re) / Math.sqrt(get(E)));
                        break;
                    case E:
                        double d = get(re) / get(rt);
                        d *= d;
                        set(E, d);
                        break;
                }
            }
        });
        
    }
}
