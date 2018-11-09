package modelChecker;

import formula.stateFormula.And;
import formula.stateFormula.Not;
import formula.stateFormula.StateFormula;
import model.Model;
import model.State;

import java.util.ArrayList;

public class SimpleModelChecker implements ModelChecker {

    ENF enfConverter;
    SATCheck sat;
    private String[] trace;

    public SimpleModelChecker() {
        this.sat = new SATCheck();
        this.enfConverter = new ENF();
    }

    @Override
    public boolean check(Model model, StateFormula constraint, StateFormula query) {
        boolean satisfy;
        StateFormula finalFormula = query;

        finalFormula = (constraint == null) ? finalFormula : new And(constraint, finalFormula);

        model.setStates();
        model.setTransitions();
        model.prepare();

        StateFormula enfVersion = enfConverter.translateENF(finalFormula);
        sat.setModel(model);

        ArrayList<State> satisfactory_states = sat.sat(model.getStateArrayList() , enfVersion);

        satisfy = satisfactory_states.containsAll(model.initialStates());
        if (!satisfy)
            trace = getTrace(model, enfVersion);

        return satisfy;
    }

    @Override
    public String[] getTrace() {
        System.out.println("Trace");
        for (int i = 0; i < trace.length; i++) {
            System.out.println("-> " + trace[i]);
        }
        return trace;
    }

    public String getTraceAsString() {
        StringBuilder sb = new StringBuilder();

        for (String traceString : trace) {
            sb.append(" -> ");
            sb.append(traceString);
        }

        return sb.toString();
    }

    private String[] getTrace(Model model, StateFormula formula){

        ArrayList<String> trace = new ArrayList<>();
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