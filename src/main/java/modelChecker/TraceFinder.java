package modelChecker;

import formula.stateFormula.Not;
import formula.stateFormula.StateFormula;
import model.Model;
import model.State;

import java.util.ArrayList;

public class TraceFinder {
    ArrayList<String> trace = new ArrayList<>();
    ENF enf;
    SATCheck SATCheck;


    public TraceFinder() {
        this.SATCheck = new SATCheck();
        this.enf = new ENF();
    }

    public void printTrace() {
        for (int i = 0; i < trace.size(); i++) {
            System.out.println(trace.get(i));
        }
    }

    public String[] getTrace(Model model, StateFormula formula){

        StateFormula negatedFormula = new Not(formula);
        SATCheck.setModel(model);
        ArrayList<State> satisfactoryState = SATCheck.sat(model.getStatesList(), negatedFormula);
        satisfactoryState.retainAll(model.initialStates());

        for (State state: satisfactoryState) {
            trace.add(state.getName());
        }

        return trace.toArray(new String[trace.size()]);
    }
}
