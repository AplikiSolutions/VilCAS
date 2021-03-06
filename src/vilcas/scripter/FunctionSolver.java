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

package vilcas.scripter;


public class FunctionSolver {
    
    public void solve(Function f){
        
        if(f.type == Function.OPERATION){
            solve(f.f1);
            solve(f.f2);
        }
        
        
        boolean changed = true;
        
        while(changed){
            changed = false;
            for(String[] s: Rules.getRules()){
                if(applyRule(f, s)){
                    changed = true;
                }
            }
        }
    }//solve
    
    public boolean applyRule(Function f, String[] rule){
        
        
        
        return true;
    }//applyRule
    
}
