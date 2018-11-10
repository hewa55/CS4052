package modelChecker;

import formula.pathFormula.Always;
import formula.pathFormula.Next;
import formula.pathFormula.PathFormula;
import formula.pathFormula.Until;
import formula.stateFormula.*;
import model.Model;
import model.State;
import model.Transition;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import static modelChecker.Keywords.*;

public class SATCheck {
    private Model model;

    public void setModel(Model model){
        this.model = model;
    }

    public ArrayList<State> sat(ArrayList<State> states, StateFormula formula) {
        ArrayList<State> satStates;

        switch (formula.getFormulaType()) {
            case THERE_EXISTS:
                satStates = getSatThereExists((ThereExists) formula,states);
                break;
            case BOOL:
                satStates = getSatBool((BoolProp)formula,states);
                break;
            case ATOMIC:
                satStates = atomProp((AtomicProp) formula, states);
                break;
            case AND:
                satStates = andSat((And) formula, states);
                break;
            case NOT:
                satStates = notSat((Not) formula,states);
                break;
            default:
                satStates = null;
                break;
        }
        return satStates;
    }

    private ArrayList<State> getSatThereExists(ThereExists formula,ArrayList<State> states){
        PathFormula pathFormula = formula.pathFormula;
        switch (pathFormula.getFormulaType()) {
            case NEXT:
                return satExNext(formula, states);
            case UNTIL:
                return until(formula, states);
            case ALWAYS:
                return always(formula,  states);
            default: return null;
        }
    }

    private ArrayList<State> atomProp(AtomicProp form, ArrayList<State> states){

        ArrayList<State> result = new ArrayList<>();
        for (int i = 0; i < states.size(); i++) {
            String[] labels = states.get(i).getLabel();
            if(Arrays.asList(labels).contains(form.label.trim())){
                result.add(states.get(i));
            }
        }
        return result;
    }

    private ArrayList<State>getSatBool(BoolProp formula, ArrayList<State> states){
        return states;
    }

    private ArrayList<State> andSat(And form, ArrayList<State> states){
        ArrayList<State> trueForLeft = sat(states,form.left);
        ArrayList<State> trueForRight = sat(states,form.right);
        ArrayList<State> intersection = new ArrayList<>();
        for (State aTrueForLeft : trueForLeft)
            for (State aTrueForRight : trueForRight)
                if (aTrueForLeft.equals(aTrueForRight)) {
                    intersection.add(aTrueForLeft);
                }
        return intersection;
    }

    private ArrayList<State> notSat(Not form, ArrayList<State> states){
        ArrayList<State> satisfactoryStates = sat(states,form.stateFormula);
        ArrayList<State> result = new ArrayList<>(states);
        result.removeAll(satisfactoryStates);
        return result;
    }

    private ArrayList<State> satExNext( ThereExists form, ArrayList<State> states ) {

        StateFormula formula = ((Next)form.pathFormula).stateFormula;
        ArrayList<State> tempList = sat(states,formula);
        ArrayList<State> result = new ArrayList<>();


        if(!((Next)form.pathFormula).getActions().isEmpty()){

            tempList = getPrevSat(tempList, ((Next)form.pathFormula).getActions());
        }
        for (State state : states) {
            ArrayList<State> successors = model.nextStates(state);
            for (State aTempList : tempList) {
                for (State successor : successors) {
                    if (aTempList.equals(successor)) result.add(aTempList);
                }
            }
        }
        return result;
    }



    private void expandTree(int i, int j, ArrayList<State> afterStates, ArrayList<State> smth,
                                 ArrayList<String> rightActions, ArrayList<State> removeAfters ) {

        int count = 0 ;
        // transitions to get into the target state
        ArrayList<Transition> inTrans = model.getToStateTrans(afterStates.get(j));
        // go through the transitions
        for (int k = 0; k < inTrans.size(); k++) {

            String target = inTrans.get(i).getTarget();
            String source = inTrans.get(i).getSource();
            // if source of transition doesnt equal the current source state we investigate AND
            // the target equals the after state we investigate, go in here
            if(!(source.equals(smth.get(i).getName()) && target.equals( afterStates.get(j).getName()) ) ) {
                // remove the transition as we are not interested (i guess?)
                inTrans.remove(k);
                k--;
            }

        }
        // if we now removed all the transitions we can return
        if (inTrans.isEmpty()) return;
        // go through remaining transitions -- we know they have our source and target
        // run through transitions
        for (int k = 0; k < inTrans.size(); k++) {

            boolean exist = false;
            // go through each action of the transition
            for (int l = 0; l < inTrans.get(k).getActions().length ; l++) {
                for (String rightAction : rightActions) {
                    if (inTrans.get(i).getActions()[l].equals(rightAction)) {
                        // if the transition exists, we set boolean to true
                        exist = true;
                    }
                }
                // if our transition fulfils at least on of the possible actions, we increase the counter
                if(!exist){
                    count++;
                }
            }
            // if each of the possible
            if(count == inTrans.size()){
                removeAfters.add(afterStates.get(j));
            }
        }

    }

    private ArrayList<State> until2(ThereExists form, ArrayList<State> states){
        PathFormula formula = form.pathFormula;

        //Left Branch
        StateFormula left = ((Until)formula).left;
        ArrayList<State> leftStates = sat(states, left);

        ArrayList<String> leftActions = new ArrayList<>();
        Set<String> leftActionsAsSet = ((Until)formula).getLeftActions();
        leftActions.addAll(leftActionsAsSet);

        //Right Branch
        StateFormula right = ((Until)formula).right;
        ArrayList<State> rightStates = sat(states, right);

        ArrayList<String> rightActions = new ArrayList<>();
        Set<String> rightActionsAsSet = ((Until)formula).getRightActions();
        rightActions.addAll(rightActionsAsSet);
        // in rightStates - all states which fulfil right bit of formula
        // right actions all transitions which fulfil right part of formula
        if(!rightActionsAsSet.isEmpty()){
            //rightStates = getPostSat();
        }
        // check that the states on the right side can be transitioned into by rightActions
        // check that the states on the left can be transitioned into by left actions

        // check that there is a connection between leftStates and RightStates with b
        System.exit(0);
        return new ArrayList<State>();

    }
    private ArrayList<State> until(ThereExists form,  ArrayList<State> states){

        PathFormula formula = form.pathFormula;

        //Left Branch
        StateFormula left = ((Until)formula).left;
        ArrayList<State> leftStates = sat(states, left);

        ArrayList<String> leftActions = new ArrayList<>();
        Set<String> leftActionsAsSet = ((Until)formula).getLeftActions();
        leftActions.addAll(leftActionsAsSet);

        //Right Branch
        StateFormula right = ((Until)formula).right;
        ArrayList<State> rightStates = sat(states, right);

        ArrayList<String> rightActions = new ArrayList<>();
        Set<String> rightActionsAsSet = ((Until)formula).getRightActions();
        rightActions.addAll(rightActionsAsSet);



        if(!((Until)formula).getRightActions().isEmpty()){
            rightStates = getPrevSat(rightStates, ((Until)formula).getRightActions());
        }

        if(!((Until)formula).getLeftActions().isEmpty()){
            leftStates = getPostSat(leftStates,rightStates, ((Until)formula).getLeftActions());
        }

        ArrayList<State> tempList = rightStates;

        boolean validUntil = true;
        // check if valid is true
        while(validUntil){
            // all states which fulfil left side of until statement
            ArrayList<State> smth = new ArrayList<>(leftStates);
            // remove right states from the list, there might be loops we want to get rid of
            smth.removeAll(rightStates);

            // arraylist for what to remove?
            ArrayList<State> remove = new ArrayList<>();

            // go through each source states
            for (int i = 0; i < smth.size(); i++) {
                // for state smth.get(i) get the states it can transition to
                ArrayList<State> afterStates = model.nextStates(smth.get(i));
                ArrayList<State> removeAfters = new ArrayList<>();
                // go through every state the left side state can transition into
                for (int j = 0; j < afterStates.size(); j++) {

                    //Right
                    // of the possible states check that there are right actions AND
                    // the possible target states contain the possible states from the source state
                    if(rightActions.size()>0 && rightStates.contains(afterStates.get(j))){
                        // if they do expand the tree
                        expandTree(i, j, afterStates, smth, rightActions, removeAfters);

                    }

                    //Left
                    // of the possible states check that there are right actions AND
                    // the possible target states contain the possible states from the source state
                    if(leftActions.size() > 0 && smth.contains(afterStates.get(j))){

                        expandTree(i, j, afterStates, smth, leftActions, removeAfters);

                    }

                }

                afterStates.removeAll(removeAfters);
                afterStates.retainAll(tempList);
                if(afterStates.isEmpty()) remove.add(leftStates.get(i));
            }

            smth.removeAll(remove);

            if(smth.isEmpty()) validUntil = false;

            tempList.addAll(smth);

        }

        return tempList;
    }


    private ArrayList<State> always(ThereExists form, ArrayList<State> states){

        PathFormula formulaP = form.pathFormula;
        StateFormula formulaS = ((Always)formulaP).stateFormula;
        ArrayList<State> satisfactoryStates = sat(states,formulaS);

        if(!((Always)formulaP).getActions().isEmpty()){
            satisfactoryStates = getPrevSat(satisfactoryStates, ((Always)formulaP).getActions());
        }

        ArrayList<State> satList = new ArrayList<>(satisfactoryStates);

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


    private ArrayList<State> getPrevSat(ArrayList<State> states, Set<String> actions){

        ArrayList<State> keep = new ArrayList<>();
        // iterate through states
        for (int i = 0; i < states.size(); i++) {
            int count = 0;
            // get all transitions which get us to the state
            ArrayList<Transition> inwardsTrans = model.getToStateTrans(states.get(i));
            // if there are none, skip this state
            if(inwardsTrans.size()==0) continue;

            // go through each transition
            for (int j = 0; j < inwardsTrans.size(); j++) {
                boolean empty = false;
                Transition curr_transition = inwardsTrans.get(j);
                // go through each action of the current transition
                for (int k = 0; k < curr_transition.getActions().length; k++) {
                    // if the possible actions contain one of the actions of this transition, we found a valid transition
                    // into the state
                    if(actions.contains(curr_transition.getActions()[k])){
                        empty = true;
                    }
                }
                // increases by one for every single additional correct transition
                if(empty){
                    count ++;
                }
            }
            // if all transitions into this state contain the action, state is added to the "to be kept states"
            if(count == inwardsTrans.size()) keep.add(states.get(i));
        }
        // remove the states without valid transitions
        states.retainAll(keep);
        return states;
    }

    // why is the called postSat - shouldnt this be the prev, because it refers to the left states?
    private ArrayList<State> getPostSat(ArrayList<State> leftStates, ArrayList<State> rightStates, Set<String> actions) {

        ArrayList<State> toRemove = new ArrayList<>();
        // go through all states from the left size
        for (int i = 0; i < leftStates.size(); i++) {

            int count = 0;
            // get the transitions out of the left side of the model
            ArrayList<Transition> out = model.getFromStateTrans(leftStates.get(i));
            // if there are no transitions - skip this state
            if(out.size()==0) continue;
            // iterate through the outwards transitions
            for (int j = 0; j < out.size(); j++) {
                Transition curr_transition = out.get(j);
                boolean empty = false;
                boolean isFromRight = false;
                // go through actions of the current transition
                for (int k = 0; k < curr_transition.getActions().length; k++) {
                    // iterate through each rightState
                    for (int l = 0; l < rightStates.size(); l++) {
                        // we compare a statement with a transition name here - will never be true, what is this for?
                        // should read: ? if(rightStates.get(l).getName().equals(curr_transition.getTarget()))
                        // if one of the right states names equal the target of the current transition - good. set boolean flag to true, found a valid transition
                        if(rightStates.get(l).getName().equals(curr_transition.getActions()[k])){
                            isFromRight = true;
                        }
                    }
                    // if the set of allowed actions contain the current action, set to true
                    if(actions.contains(curr_transition.getActions()[k])){
                        empty = true;
                    }
                    // if this is from the right bit of the formula and the curr_transaction allows this action, increase counter
                    //if(empty&&isFromRight){
                    //    count++;
                    //}
                }
                // why do we increase this as well?
                // took away the empty&&isFromRight
                if(empty&&isFromRight){
                    count ++;
                }
            }
            // if all outwards bound transitions of this state are supported, add the current state
            if(count == out.size()) toRemove.add(leftStates.get(i));
        }
        // remove or retain?
        leftStates.retainAll(toRemove);
        return leftStates;
    }


}
