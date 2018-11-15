package modelChecker;

import formula.pathFormula.Always;
import formula.pathFormula.Next;
import formula.pathFormula.PathFormula;
import formula.pathFormula.Until;
import formula.stateFormula.*;
import model.Model;
import model.State;
import model.Transition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static modelChecker.Keywords.*;
import static modelChecker.Keywords.NOT;

public class SAT_rewritten {
    private Model model;
    private ArrayList<String> trace;



    public void setModel(Model model){
        this.model = model;
    }

    public  ArrayList<State> satCheck(ArrayList<State> states, StateFormula formula) {
        //System.out.println("Recursive Call");
        //System.out.println(formula);

        ArrayList<State> satStates = new ArrayList<>();
        switch (formula.getFormulaType()) {
            case THERE_EXISTS:
                satStates = satThereExists((ThereExists) formula,states);
                break;
            case BOOL:
                // no need to do this. just remains the same
                satStates = states;
                break;
            case ATOMIC:
                satStates = atomic((AtomicProp) formula, states);
                break;
            case AND:
                satStates = satAND((And) formula, states);
                break;
            case NOT:
                satStates = satNOT((Not) formula, states);
                break;

            default:
                break;

        }
        //Run sat based on formula type at level

        //return sat on root node condition
        return satStates;
    }

    private ArrayList<State> satNOT(Not formula, ArrayList<State> states){
        // take formula and all which satisfy this formula get removed
        ArrayList<State> satStates = satCheck(states,formula.stateFormula);
        ArrayList<State> result = new ArrayList<>(states);
        result.removeAll(satStates);
        return result;
    }
    private ArrayList<State> atomic(AtomicProp form, ArrayList<State> states){
        ArrayList<State> satStates = new ArrayList<>();
        for (int i = 0; i < states.size(); i++) {
            State currentState = states.get(i);
            String[] labels = currentState.getLabel();
            // if state contains label, they satisfy the conditions
            if(Arrays.asList(labels).contains(form.label.trim())){
                satStates.add(currentState);
            }
        }
        return satStates;
    }

    private ArrayList<State> satAND(And form, ArrayList<State> states){
        ArrayList<State> trueLeft = satCheck(states,form.left);
        ArrayList<State> trueRight = satCheck(states,form.right);
        ArrayList<State> satStates = new ArrayList<>();
        // get the intersection between right and left
        for (State aTrueForLeft : trueLeft){
            if (trueRight.contains(aTrueForLeft)) {
                satStates.add(aTrueForLeft);
            }
        }
        return satStates;
    }

    private ArrayList<State> satThereExists(ThereExists formula,ArrayList<State> states){
        PathFormula pathFormula = formula.pathFormula;

        switch (pathFormula.getFormulaType()) {
            case NEXT:
                return satNext((Next) formula.pathFormula, states);
            case UNTIL:
                return until((Until)formula.pathFormula, states);
            case ALWAYS:
                return always((Always) formula.pathFormula,  states);
            default: return null;
        }
    }

    private ArrayList<State> satNext(Next form, ArrayList<State> states ) {
        // get temporary sat states for the formula
        ArrayList<State> tempSatStates = satCheck(states,form.stateFormula);
        //ArrayList<State> result = new ArrayList<>();
        // if there are action requirements check that the temp sat states fulfil those
        if(!form.getActions().isEmpty()){
            tempSatStates = prevSat(tempSatStates, form.getActions());
        }

        // get all successors in a set
        ArrayList<State> all_successors = new ArrayList<>();
        for (State state : states) {
            ArrayList<State> successors = model.nextStates(state);
            // and keep the direct successors
            all_successors.addAll(successors);
        }

        tempSatStates.retainAll(all_successors);
        return tempSatStates;
    }

    private ArrayList<State> always(Always form, ArrayList<State> states){
        StateFormula formulaS = form.stateFormula;
        ArrayList<State> satisfactoryStates = satCheck(states,formulaS);

        // enforce condition
        if(!form.getActions().isEmpty()){
            satisfactoryStates = prevSat(satisfactoryStates, form.getActions());
        }

        ArrayList<State> satList = new ArrayList<>(satisfactoryStates);

        // go through the next states and continue to do so. every time one of the states fails, we remove it
        boolean sat = true;
        while(sat){

            ArrayList<State> stateList = new ArrayList<>(satList);
            ArrayList<State> remove = new ArrayList<>();

            for (State state : stateList) {

                ArrayList<State> afterStates = model.nextStates(state);
                afterStates.retainAll(satList);
                if (afterStates.isEmpty()) remove.add(state);

            }

            if(remove.size()==0) sat = false;
            satList.removeAll(remove);

        }

        return satList;
    }

    private ArrayList<State> prevSat(ArrayList<State> states, Set<String> actions){
        // go through all the states and check if they have an incoming action equal to an element in actions,
        // if they do, keep them
        ArrayList<State> satStates = new ArrayList<>(states);
        for (State curr_state : states){
            ArrayList<Transition> transitions = model.getToStateTrans(curr_state);
            transitions = extractRelevantTransition(transitions,curr_state);
            transitions = removeWrongAction(transitions,actions);
            // if there are no possible transitions into curr_state, remove the state from the possible states
            if(transitions.size()==0 && !curr_state.isInit()){
                satStates.remove(curr_state);
            }
        }
        return satStates;
    }

    private ArrayList<Transition> removeWrongAction(ArrayList<Transition> transitions, Set<String> actions){
        if(actions.size()==0){
            return transitions;
        }
        for (int i = 0; i< transitions.size();i++){
            Transition transition = transitions.get(i);
            // size before and after the actions from this transition were removed
            // if size didnt shrink, the actions dont overlap and remove the transition
            Set<String> tempActions = new HashSet<>(actions);
            int size_action = tempActions.size();
            tempActions.removeAll(new ArrayList<>(Arrays.asList(transition.getActions())));
            if(size_action==tempActions.size()){
                transitions.remove(transition);
                i--;
            }
        }
        return transitions;
    }

    private ArrayList<Transition> extractRelevantTransition(ArrayList<Transition> transitions, State target){
        ArrayList<Transition> relevantTransitions = new ArrayList<>();
        for (Transition transition : transitions){
            if(transition.getTarget().equals(target.getName())) {
                relevantTransitions.add(transition);
            }
        }
        return relevantTransitions;
    }
    private ArrayList<State> until(Until formula,  ArrayList<State> states){

        //Left Branch
        StateFormula left = formula.left;
        ArrayList<State> leftStates = satCheck(states, left);

        Set<String> leftActionsAsSet = formula.getLeftActions();

        //Right Branch
        StateFormula right = formula.right;
        ArrayList<State> rightStates = satCheck(states, right);

        Set<String> rightActionsAsSet = formula.getRightActions();


        if(!formula.getRightActions().isEmpty()){
            rightStates = prevSat(rightStates, rightActionsAsSet);
        }

        // all the states can be transitioned into using leftActions
        if(!formula.getLeftActions().isEmpty()){
            leftStates = prevSat(leftStates,leftActionsAsSet);
        }
        // now, they need be either transition to another state in left states with one of left actions or they need to transition into one of the right states
        // for these states we satisfy the formula
        // repeat this until size doesn't change anymore
        boolean change = true;
        ArrayList<State> satStates = new ArrayList<>(leftStates);

        while (change){

            int size = satStates.size();
            // the problem is that at least one of the states needs to connect to a state in the right set. the other need to connect to that state
            // find which left states connect to my right state(s)
            // each other state needs to connect to a right state or to sufficient left states that it is somehow connect to the last left state
            // HOWTO:
            // Either
            // connected to right state - leave it in
            // OR:
            // check what left states are reachable, if connected to right state, good, else look what other left states are reachable (recursive)
            // if back at my own left state, there was a loop, try next left state
            // if no connection found - remove the state otherwise leave it in.
            Set<State> nonSatStates = new HashSet<>();
            for(State leftState : leftStates){
                // either connect to another in left states or one to right states with the respective actions, if not, remove
                if(!(connectToRight(leftState,rightStates,rightActionsAsSet)||connectedToRightViaLeft(new HashSet<State>(),leftState,satStates,rightStates,leftActionsAsSet,rightActionsAsSet))){
                    nonSatStates.add(leftState);
                }
            }
            satStates.removeAll(nonSatStates);
            if(size==satStates.size()){
                change=false;
            }
        }
        return satStates;
    }

    private boolean connectToRight(State state, ArrayList<State> rightStates,Set<String> actions){
        ArrayList<Transition> transitions = model.getFromStateTrans(state);
        transitions = removeWrongAction(transitions,actions);
        ArrayList<String> targets = getTargetsAsString(transitions);
        for (State desiredConnection : rightStates) {
            if(targets.contains(desiredConnection.getName())){
                return true;
            }
        }
        return false;
    }
    private ArrayList<String> getTargetsAsString(ArrayList<Transition> transitions){
        ArrayList<String> targetsAsString =  new ArrayList<>();
        for (int i = 0; i < transitions.size(); i++) {
            targetsAsString.add(transitions.get(i).getTarget());
        }
        return targetsAsString;
    }
    private boolean connectedToRightViaLeft(Set<State> visited,
                                            State state,
                                            ArrayList<State> desiredConnections,
                                            ArrayList<State> rightStates,
                                            Set<String> leftActions,
                                            Set<String> rightActions){
        // get transitions from the current State
        ArrayList<Transition> transitions = model.getFromStateTrans(state);
        // remove the transitions not complying with actions
        transitions = removeWrongAction(transitions,leftActions);
        // get all the targets from those transitions (source is always state)
        ArrayList<String> targets = getTargetsAsString(transitions);
        // go through the States we want to connect to
        boolean valid= false;
        // if the state is connected to right, we can immediately return true
        if(connectToRight(state,rightStates,rightActions)){
            return true;
        }
        visited.add(state);
        for (State desiredConnection : desiredConnections) {
            // if it is among the possible targets
            // A) it connects to a rightState - happy days and we return true
            // B) it connects to other LeftStates - happy days, iterate through those and do the same again - skip the root State
            // C) it doesn't connect to anything possible or only root state - give up and return false

            // we don't want to transition back to a state already visited
            if(visited.contains(desiredConnection)){
                continue;
            }

            // we can transition into another valid state which is is not one we visited before
            if(targets.contains(desiredConnection.getName())){
                //System.out.println(desiredConnection.getName());
                valid = valid || connectedToRightViaLeft(visited,desiredConnection,desiredConnections,rightStates,leftActions,rightActions);
            }


        }
        return valid;
    }
}
