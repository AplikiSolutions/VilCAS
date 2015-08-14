package vililaskin.scripter;


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
