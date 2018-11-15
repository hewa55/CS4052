package modelChecker;

import formula.stateFormula.And;
import formula.stateFormula.Not;
import formula.stateFormula.StateFormula;
import model.Model;
import model.State;

import java.util.ArrayList;

public class SimpleModelChecker implements ModelChecker {

    private ENF enfProcessor;
    private SATCheck satChecker;
    private String[] trace;

    private SAT sat;

    private SAT_rewritten sat_new;

    public SimpleModelChecker() {
        this.satChecker = new SATCheck();
        this.enfProcessor = new ENF();
        this.sat = new SAT();
        this.sat_new = new SAT_rewritten();

    }

    @Override
    public boolean check(Model model, StateFormula constraint, StateFormula query) {
        boolean satisfy;
        StateFormula finalFormula = query;


        model.setStates();
        model.setTransitions();
        model.prepare();
        ArrayList<State> modelStates = model.getStateArrayList();
        sat_new.setModel(model);
        // instead of this, remove everything which obliges to constraint from model
        if(constraint != null) {
            //finalFormula = new And(constraint, finalFormula);
            // use the enf as formula
            StateFormula enfConstraint = enfProcessor.translateENF(constraint);
            sat_new.setModel(model);
            // get all the states satisfying it and update the possible model states
            modelStates = sat_new.satCheck(modelStates, enfConstraint);
        }

        StateFormula enfVersion = enfProcessor.translateENF(finalFormula);

        ArrayList<State> satisfactory_states = sat_new.satCheck(modelStates , enfVersion);

        satisfy = satisfactory_states.containsAll(model.initialStates());
        if (!satisfy)
            trace = getTrace(model, enfVersion);

        return satisfy;
    }

    @Override
    public String[] getTrace() {
        System.out.println("Trace");

        for (String traceString :
                trace) {
            System.out.println("-> " + traceString);
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

        satChecker.setModel(model);

        ArrayList<State> satisfactoryState = sat_new.satCheck(model.getStateArrayList(), negationOfOriginalFormula);
        satisfactoryState.retainAll(model.initialStates());
        for (State state: satisfactoryState) {
            trace.add("" + state.getName());
        }

        return trace.toArray(new String[trace.size()]);
    }

}