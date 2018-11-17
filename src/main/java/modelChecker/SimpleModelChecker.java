package modelChecker;

import formula.stateFormula.Not;
import formula.stateFormula.StateFormula;
import model.Model;
import model.State;
import model.Transition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SimpleModelChecker implements ModelChecker {

    private ENF enfProcessor;
    private String[] trace;


    private SAT_Solver sat_new;

    public SimpleModelChecker() {
        this.enfProcessor = new ENF();
        this.sat_new = new SAT_Solver();

    }

    @Override
    public boolean check(Model model, StateFormula constraint, StateFormula query) {
        boolean satisfy;
        StateFormula finalFormula = query;


        model.setStates();
        model.setTransitions();
        model.prepare();
        List<State> modelStates = model.getStateArrayList();
        sat_new.setModel(model);
        // remove everything which doesn't follow constraint
        if(constraint != null) {
            //finalFormula = new And(constraint, finalFormula);
            // use the enf as formula
            StateFormula enfConstraint = enfProcessor.translateENF(constraint);
            sat_new.setModel(model);
            // get all the states satisfying it and update the possible model states
            modelStates = sat_new.satCheck(modelStates, enfConstraint);
        }

        StateFormula enfVersion = enfProcessor.translateENF(finalFormula);

        List<State> satisfactory_states = sat_new.satCheck(modelStates , enfVersion);


        satisfy = satisfactory_states.containsAll(model.initialStates());

        if (!satisfy){
            getTrace(satisfactory_states,modelStates,model);
        }


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
            sb.append(" --> ");
            sb.append(traceString);
        }

        return sb.toString();
    }


    private void getTrace(List<State> satisfactory_states,List<State> modelStates,Model model){
        List<State> initials = new ArrayList<>(model.initialStates());
        // get a possible starting state
        initials.removeAll(satisfactory_states);
        // get the goal states
        modelStates.removeAll(satisfactory_states);
        modelStates.removeAll(initials);
        // find a connection between the initials and the model states above
        // picking any initial state is enough
        State initial = initials.get(0);
        // now find transition into one of the model states and continue until not possible anymore
        List<String> traceList = new ArrayList<>();
        traceList.add(initial.getName());
        State current_State = initial;
        boolean incomplete = true;

        while (incomplete){
            ArrayList<Transition> transitions = model.getFromStateTrans(current_State);
            List<String> targets = getTargetsAsString(transitions);
            targets.remove(current_State);
            if(modelStates.size()==0){
                break;
            }
            for (int i = 0; i < modelStates.size() ; i++) {
                State target = modelStates.get(i);
                if(targets.contains(target.getName())){
                    traceList.add(target.getName());
                    current_State=target;
                    modelStates.remove(target);
                    break;
                }
                if(i == modelStates.size()-1){
                    incomplete=false;
                }
            }
        }
        trace = new String[traceList.size()];
        trace = traceList.toArray(trace);
    }

    private List<String> getTargetsAsString(List<Transition> transitions) {
        List<String> targetsAsString = new ArrayList<>();
        for (int i = 0; i < transitions.size(); i++) {
            targetsAsString.add(transitions.get(i).getTarget());
        }
        return targetsAsString;
    }


}