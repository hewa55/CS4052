package modelChecker;

import formula.stateFormula.Not;
import formula.stateFormula.StateFormula;
import model.Model;
import model.State;

import java.util.ArrayList;

public class Tracer {
    ArrayList<String> trace = new ArrayList<>();
    ENF enf;
    SATCheck sat;


    public Tracer() {
        this.sat = new SATCheck();
        this.enf = new ENF();
    }

    public String[] getTrace(Model model, StateFormula formula){
        StateFormula negationOfOriginalFormula = new Not(formula);
        sat.setModel(model);
        ArrayList<State> satisfactoryState = sat.sat(model.getStateArrayList(), negationOfOriginalFormula);
        satisfactoryState.retainAll(model.initialStates());
        for (State state: satisfactoryState) {
            trace.add("" + state.getName());
        }

        return trace.toArray(new String[trace.size()]);
    }
}
