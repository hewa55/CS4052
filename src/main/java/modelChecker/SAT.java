package modelChecker;

import formula.pathFormula.Always;
import formula.pathFormula.Next;
import formula.pathFormula.PathFormula;
import formula.pathFormula.Until;
import formula.stateFormula.*;
import model.Model;
import model.State;
import model.Transition;
import static modelChecker.Keywords.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;


public class SAT {

    private Model model;
    private Tree tree;

    ArrayList<Tree> allLeafNodes;

    public void setModel(Model model){
        this.model = model;
    }

    public  ArrayList<State> satCheck(ArrayList<State> states, StateFormula formula) {
        //get formula type and split into two nodes
        System.out.println("Recursive Call");
        System.out.println(formula);

        ArrayList<State> satStates = new ArrayList<>();
        switch (formula.getFormulaType()) {
            case THERE_EXISTS:
                satStates = satThereExists((ThereExists) formula,states);
                break;
            case BOOL:
                satStates = satBool((BoolProp)formula,states);
                break;
            case ATOMIC:
                satStates = atoimc((AtomicProp) formula, states);
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

    private ArrayList<State> satNOT(Not form, ArrayList<State> states) {
        ArrayList<State> satisfactoryStates = satCheck(states,form.stateFormula);
        ArrayList<State> result = new ArrayList<>(states);
        result.removeAll(satisfactoryStates);
        return result;
    }

    private ArrayList<State> satThereExists(ThereExists formula,ArrayList<State> states){
        PathFormula pathFormula = formula.pathFormula;

        switch (pathFormula.getFormulaType()) {
            case NEXT:
                return satNext(formula, states);
            case UNTIL:
                return until(formula, states);
            case ALWAYS:
                return always(formula,  states);
            default: return null;
        }
    }

    private ArrayList<State> atoimc(AtomicProp form, ArrayList<State> states){
        ArrayList<State> result = new ArrayList<>();
        for (int i = 0; i < states.size(); i++) {
            String[] labels = states.get(i).getLabel();
            if(Arrays.asList(labels).contains(form.label.trim())){
                result.add(states.get(i));
            }
        }
        return result;
    }

    private ArrayList<State>satBool(BoolProp formula, ArrayList<State> states){
        return states;
    }

    private ArrayList<State> satAND(And form, ArrayList<State> states){
        ArrayList<State> trueForLeft = satCheck(states,form.left);
        ArrayList<State> trueForRight = satCheck(states,form.right);
        ArrayList<State> intersection = new ArrayList<>();
        for (State aTrueForLeft : trueForLeft)
            for (State aTrueForRight : trueForRight)
                if (aTrueForLeft.equals(aTrueForRight)) {
                    intersection.add(aTrueForLeft);
                }
        return intersection;
    }

    private ArrayList<State> satNext(ThereExists form, ArrayList<State> states ) {

        StateFormula formula = ((Next)form.pathFormula).stateFormula;
        ArrayList<State> tempList = satCheck(states,formula);
        ArrayList<State> result = new ArrayList<>();

        if(!((Next)form.pathFormula).getActions().isEmpty()){

            tempList = prevSat(tempList, ((Next)form.pathFormula).getActions());
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

    private ArrayList<State> until(ThereExists form,  ArrayList<State> states){

        PathFormula formula = form.pathFormula;

        //Left Branch
        StateFormula left = ((Until)formula).left;
        ArrayList<State> leftStates = satCheck(states, left);

        ArrayList<String> leftActions = new ArrayList<>();
        Set<String> leftActionsAsSet = ((Until)formula).getLeftActions();
        leftActions.addAll(leftActionsAsSet);

        //Right Branch
        StateFormula right = ((Until)formula).right;
        ArrayList<State> rightStates = satCheck(states, right);

        ArrayList<String> rightActions = new ArrayList<>();
        Set<String> rightActionsAsSet = ((Until)formula).getRightActions();
        rightActions.addAll(rightActionsAsSet);



        if(!((Until)formula).getRightActions().isEmpty()){
            rightStates = prevSat(rightStates, ((Until)formula).getRightActions());
        }

        if(!((Until)formula).getLeftActions().isEmpty()){
            leftStates = postSat(leftStates,rightStates, ((Until)formula).getLeftActions());
        }

        ArrayList<State> tempList = rightStates;

        boolean validUntil = true;

        while(validUntil){

            ArrayList<State> smth = new ArrayList<>(leftStates);
            smth.removeAll(rightStates);

            ArrayList<State> remove = new ArrayList<>();

            for (int i = 0; i < smth.size(); i++) {

                ArrayList<State> afterStates = model.nextStates(smth.get(i));
                ArrayList<State> removeAfters = new ArrayList<>();

                for (int j = 0; j < afterStates.size(); j++) {

                    //Right
                    if(rightActions.size()>0 && rightStates.contains(afterStates.get(j))){

                        expandTree(i, j, afterStates, smth, rightActions, removeAfters);

                    }

                    //Left
                    if(leftActions.size() > 0 && smth.contains(afterStates.get(j))){

                        expandTree(i, j, afterStates, smth, rightActions, removeAfters);

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

    private void expandTree(int i, int j, ArrayList<State> afterStates, ArrayList<State> smth,
                            ArrayList<String> rightActions, ArrayList<State> removeAfters ) {

        int count = 0 ;
        ArrayList<Transition> inTrans = model.getToStateTrans(afterStates.get(j));

        for (int k = 0; k < inTrans.size(); k++) {

            String target = inTrans.get(i).getTarget();
            String source = inTrans.get(i).getSource();

            if(!(source.equals(smth.get(i).getName()) && target.equals( afterStates.get(j).getName()) ) ) {
                inTrans.remove(k);
                k--;
            }

        }

        if (inTrans.isEmpty()) return;

        for (int k = 0; k < inTrans.size(); k++) {

            boolean exist = false;
            for (int l = 0; l < inTrans.get(k).getActions().length ; l++) {
                for (String rightAction : rightActions) {
                    if (inTrans.get(i).getActions()[l].equals(rightAction)) {
                        exist = true;
                    }
                }
                if(!exist){
                    count++;
                }
            }
            if(count == inTrans.size()){
                removeAfters.add(afterStates.get(j));
            }
        }

    }


    private ArrayList<State> always(ThereExists form, ArrayList<State> states){

        PathFormula formulaP = form.pathFormula;
        StateFormula formulaS = ((Always)formulaP).stateFormula;
        ArrayList<State> satisfactoryStates = satCheck(states,formulaS);

        if(!((Always)formulaP).getActions().isEmpty()){
            satisfactoryStates = prevSat(satisfactoryStates, ((Always)formulaP).getActions());
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


    private ArrayList<State> prevSat(ArrayList<State> states, Set<String> actions){

        ArrayList<State> remove = new ArrayList<>();
        for (int i = 0; i < states.size(); i++) {
            int count = 0;
            ArrayList<Transition> inTrans = model.getToStateTrans(states.get(i));
            if(inTrans.size()==0) continue;

            for (int j = 0; j < inTrans.size(); j++) {
                boolean empty = false;


                for (int k = 0; k < inTrans.get(j).getActions().length; k++) {

                    if(actions.contains(inTrans.get(j).getActions()[k])){
                        empty = true;
                    }
                }

                if(empty){
                    count ++;
                }
            }
            if(count == inTrans.size()) remove.add(states.get(i));
        }
        states.removeAll(remove);
        return states;
    }


    private ArrayList<State> postSat(ArrayList<State> leftStates, ArrayList<State> rightStates, Set<String> actions) {

        ArrayList<State> toRemove = new ArrayList<>();

        for (int i = 0; i < leftStates.size(); i++) {

            int count = 0;
            ArrayList<Transition> out = model.getFromStateTrans(leftStates.get(i));



            if(out.size()==0) continue;

            for (int j = 0; j < out.size(); j++) {

                Transition toConsider = out.get(j);

                boolean empty = false;
                boolean isFromRight = false;

                for (int k = 0; k < out.get(j).getActions().length; k++) {

                    for (int l = 0; l < rightStates.size(); l++) {
                        if(rightStates.get(l).getName().equals(out.get(j).getActions()[k])){
                            isFromRight = true;
                        }
                    }

                    if(actions.contains(toConsider.getActions()[k])){
                        empty = true;
                    }
                    if(empty&&isFromRight){
                        count++;
                    }
                }
                if(empty){
                    count ++;
                }
            }
            if(count == out.size()) toRemove.add(leftStates.get(i));
        }

        leftStates.removeAll(toRemove);
        return leftStates;
    }
}
