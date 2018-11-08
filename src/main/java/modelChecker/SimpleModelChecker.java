package modelChecker;

import formula.stateFormula.StateFormula;
import model.Model;

public class SimpleModelChecker implements ModelChecker {

    @Override
    public boolean check(Model model, StateFormula constraint, StateFormula query) {
        // TODO Auto-generated method stub
        // System.out.println("Hello ");

        //For all states, query holds true given constraint
        // checkAllStates( model, constraint, formula);
        // 0.1 build a graph
        // 0.2 get all execution paths (-->cycles)

        // 1 get root
        // 2 check the possible transitions - depth first
        // 3 pick a state, transition, check formula do the same thing again
        //  a if correct, go up and select second possible transition
        //  b if fail, stop and return trace
        // do the same thing again


        return false;
    }

    @Override
    public String[] getTrace() {
        // TODO Auto-generated method stub
        return null;
    }

}
