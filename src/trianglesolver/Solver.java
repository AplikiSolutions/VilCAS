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

package trianglesolver;

import javax.swing.JOptionPane;


public class Solver {
    
    
    private static final int side1 = 0, side2 = 1, side3 = 2, angle1 = 3, angle2 = 4, angle3 = 5;
    private static double[] values;
    private static boolean[] wasGiven;
    
    
    public static double[] solve(double[] in){
        
        values = in;
        wasGiven = new boolean[in.length];
        //update wasGiven to tell if given
        for(int i = 0; i < in.length; i++){
            wasGiven[i] = !Double.isNaN(in[i]);
        }
        
        switch(given(side1, side2, side3)){
            case 1:
                solveTwoSides();
                break;
            case 2:
                solveOneSide();
                break;
        }
        
        if(given(side1, side2, side3) == 3)
            solveThreeAngles();
        else
            if(given(angle1, angle2, angle3) == 2)
                solveOneAngle();
            
        
        for(int i = 0; i < values.length; i++){
            if(!wasGiven[i])
                values[i] = round(values[i]);
        }
        
        return values;
    }//solve
    
    
    private static void solveOneSide(){
        int s1 = side1, s2 = side2, s3 = side3;
        if(!isKnown(side1)){
            s3 = side1;
            s1 = side2;
            s2 = side3;
        }else if(!isKnown(side2)){
            s3 = side2;
            s2 = side3;
        }
        
        if(given(angle1, angle2, angle3) == 2){
            solveOneAngle();
        }
        
        if(isKnown(opposite(s3))){
            //two sides and a corner between them are known
            //use the cosine law
            double ans = values[s1]*values[s1] + values[s2]*values[s2];
            ans -= 2 * values[s1] * values[s2] * cos(values[opposite(s3)]);
            values[s3] = Math.sqrt(ans);
        }else{
            //two sides and a corner are known, there may be 2 possibilities for s3
            
            if(isKnown(opposite(s2))){
                int temp = s1;
                s1 = s2;
                s2 = temp;
            }
            
            //corner opposite from s1 is known
            
            
            //a = 2bcos(C) +- sqrt(4b^2cos(C)^2 - 4*(b^2 - c^2)) / 2
            //a = s3, b = s2, c = s1
                
            double ans1 = 4 * values[s2] * values[s2] * cos(opposite(s1)) * cos(opposite(s1));
            ans1 -= 4 * (values[s2] * values[s2] - values[s1] * values[s1]);
            ans1 = Math.sqrt(ans1);
            ans1 += 2 * values[s2] * cos(values[opposite(s1)]);
            ans1 /= 2;
            
            double ans2 = 4 * values[s2] * values[s2] * cos(opposite(s1)) * cos(opposite(s1));
            ans2 -= 4 * (values[s2] * values[s2] - values[s1] * values[s1]);
            ans2 = - Math.sqrt(ans2);
            ans2 += 2 * values[s2] * cos(values[opposite(s1)]);
            ans2 /= 2;
            
            if(ans1 > 0 && ans2 > 0){
                
                int selection = JOptionPane.showConfirmDialog(null, 
                            "Does the triangle have an obtuse angle?",
                            "Multiple answers", JOptionPane.YES_NO_OPTION);
                
                if(selection == JOptionPane.YES_OPTION){
                    values[s3] = ans1;
                }else if(selection == JOptionPane.NO_OPTION){
                    values[s3] = ans2;
                }
            }else if(ans1 > 0){
                values[s3] = ans1;
            }else if(ans2 > 0){
                values[s3] = ans2;
            }else{
                values[s3] = Double.NaN;
            }
            
        }
    }//solveOneSide
    
    
    private static void solveTwoSides(){
        int knownSide, empty1, empty2;
        if(values[side1] != 0){
            knownSide = side1;
            empty1 = side2;
            empty2 = side3;
        }else if(values[side2] != 0){
            knownSide = side2;
            empty1 = side3;
            empty2 = 1;
        }else{
            knownSide = side3;
            empty1 = side1;
            empty2 = side2;
        }
        
        int c1 = opposite(knownSide), c2 = opposite(empty1), c3 = opposite(empty2);
        
        if(isKnown(c1)){
            //opposite corner known
            //calculate third corner
            if(isKnown(c2)){
                values[c3] = 180 - values[c1] - values[c2];
            }else{
                if(!isKnown(c3))
                    return;
                
                values[c2] = 180 - values[c1] - values[c3];
            }
            
        }else if(isKnown(c2) && isKnown(c3)){
            //adjacent corners are known
            //calculate third corner and use sine law
            values[c1] = 180 - values[c2] - values[c3];
        }
        
        //c1 and c2 are known
        //use sine law

        values[opposite(c2)] = sin(values[c2]) * values[knownSide] / sin(values[c1]);
        
        solveOneSide();
        
    }//solveTwoSides
    
    
    private static void solveOneAngle(){
        if(!isKnown(angle1))
            values[angle1] = 180 - values[angle2] - values[angle3];
        if(!isKnown(angle2))
            values[angle2] = 180 - values[angle1] - values[angle3];
        if(!isKnown(angle3))
            values[angle3] = 180 - values[angle1] - values[angle2];
    }//solveOneCorner
    
    
    private static void solveThreeAngles(){
        
        if(given(angle1, angle2, angle3) == 3)
            return;
        
        if(!wasGiven[angle1]){
            double ans = values[side2] * values[side2] + values[side3] * values[side3] - values[side1] * values[side1];
            ans /= 2 * values[side2] * values[side3];
            values[angle1] = acos(ans);
        }
        
        if(!wasGiven[angle2]){
            double ans = values[side3] * values[side3] + values[side1] * values[side1] - values[side2] * values[side2];
            ans /= 2 * values[side3] * values[side1];
            values[angle2] = acos(ans);
        }
        
        if(!wasGiven[angle3]){
            double ans = values[side1] * values[side1] + values[side2] * values[side2] - values[side3] * values[side3];
            ans /= 2 * values[side1] * values[side2];
            values[angle3] = acos(ans);
        }
    }//solveThreeCorners
    
    
    
    private static int opposite(int i){
        if(i < 3)
            return i + 3;
        return i - 3;
    }//oppositeCorner
    
    
    
    private static boolean isKnown(int i){
        return !Double.isNaN(values[i]);
    }//isKnown
    private static int given(int... i){
        int counter = 0;
        for(int j: i)
            if(isKnown(j))
                counter++;
        
        return counter;
    }//given
    
    
    private static double sin(double d){
        return Math.sin(d / 180 * Math.PI);
    }//sin
    private static double cos(double d){
        return Math.cos(d / 180 * Math.PI);
    }//cos
    private static double acos(double d){
        return Math.acos(d) / Math.PI * 180;
    }//acos
    
    
    private static double round(double d){
        final int accuracy = 4;
        
        double multiplier = accuracy - 1 - (int)Math.log10(d);
        return Math.rint(d * Math.pow(10, multiplier)) / Math.pow(10, multiplier);
        
    }//round
}
