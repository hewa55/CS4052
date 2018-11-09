package modelChecker;

import formula.stateFormula.And;
import formula.stateFormula.StateFormula;
import model.Model;
import model.State;

import java.util.ArrayList;

public class SimpleModelChecker implements ModelChecker {

    ENFTranslator enfTranslator;
    TraceFinder traceFinder;
    Sat sat;
    private String[] trace;

    public SimpleModelChecker() {
        this.sat = new Sat();
        this.enfTranslator = new ENFTranslator();
        this.traceFinder = new TraceFinder();
    }

    @Override
    public boolean check(Model model, StateFormula constraint, StateFormula query) {
        boolean satisfy;
        StateFormula finalFormula = query;

        finalFormula = (constraint == null) ? finalFormula : new And(constraint, finalFormula);


        model.setStates();
        model.setTransitions();
        model.prepare();

        StateFormula enfVersion = enfTranslator.translateENF(finalFormula);
        sat.setModel(model);

        ArrayList<State> satisfactory_states = sat.sat(model.getStateArrayList() , enfVersion);

        satisfy = satisfactory_states.containsAll(model.initialStates());
        if (!satisfy)
            trace = traceFinder.getTrace(model, enfVersion);

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
        for (int i = 0; i < trace.length; i++) {
            sb.append(" -> ");
            sb.append(trace[i]);
        }
        return sb.toString();
    }

}