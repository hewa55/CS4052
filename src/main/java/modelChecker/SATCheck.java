package modelChecker;

import formula.pathFormula.*;
import formula.stateFormula.*;
import model.*;

import model.Transition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class SATCheck {
    private static final String THERE_EXISTS = "ThereExists";
    private static final String NEXT = "Next";
    private static final String UNTIL = "Until";
    private static final String ALWAYS = "Always";
    private static final String BOOL = "BoolProp";
    private static final String ATOMIC = "AtomicProp";
    private static final String AND = "And";
    private static final String NOT = "Not";

    private Model model;

    public void setModel(Model model){
        this.model = model;
    }
    public ArrayList<State> sat(ArrayList<State> states, StateFormula formula) {
        ArrayList<State> satStates;
        String type = formula.getClass().getName().substring(formula.getClass().getCanonicalName().lastIndexOf('.') + 1);
        switch (type) {
            case THERE_EXISTS:
                PathFormula pathFormula = ((ThereExists) formula).pathFormula;
                String typeP = pathFormula.getClass().getCanonicalName().substring(pathFormula.getClass().getCanonicalName().lastIndexOf('.') + 1);
                switch (typeP) {
                    case NEXT:
                        satStates = satExNext((ThereExists)formula, states);
                        break;
                    case UNTIL:
                        satStates = eUntil((ThereExists) formula, states);
                        break;
                    case ALWAYS:
                        satStates = exAlways((ThereExists) formula,  states);
                        break;
                    default:
                        satStates = null;
                }
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

    public ArrayList<State> atomProp(AtomicProp form, ArrayList<State> states){

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

    public ArrayList<State> andSat(And form, ArrayList<State> states){
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

    public ArrayList<State> notSat(Not form, ArrayList<State> states){
        ArrayList<State> satisfactoryStates = sat(states,form.stateFormula);
        ArrayList<State> result = new ArrayList<>(states);
        result.removeAll(satisfactoryStates);
        return result;
    }

    public ArrayList<State> satExNext( ThereExists form, ArrayList<State> states ){
        ArrayList<State> result = new ArrayList<>();
        //TODO: CHECK
        //if(!(form.pathFormula instanceof Next)) return null;
        StateFormula formula = ((Next)form.pathFormula).stateFormula;
        ArrayList<State> tempList = sat(states,formula);
        if(!((Next)form.pathFormula).getActions().isEmpty()){
            //TODO: Change
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
    //TODO: change names of variables of arraylists
    public ArrayList<State> eUntil(ThereExists form,  ArrayList<State> states){
        PathFormula formula = form.pathFormula;
        StateFormula left = ((Until)formula).left;
        StateFormula right = ((Until)formula).right;
        ArrayList<State> leftStates = sat(states, left);
        ArrayList<State> rightStates = sat(states, right);

        ArrayList<String> rightActions = new ArrayList<>();
        Set<String> rightActionsAsSet = ((Until)formula).getRightActions();
        rightActions.addAll(rightActionsAsSet);

        ArrayList<String> leftActions = new ArrayList<>();
        Set<String> leftActionsAsSet = ((Until)formula).getLeftActions();
        leftActions.addAll(leftActionsAsSet);

        if(!((Until)formula).getRightActions().isEmpty()){
            rightStates = getPrevSat(rightStates, ((Until)formula).getRightActions());
        }
        if(!((Until)formula).getLeftActions().isEmpty()){
            leftStates = getPostSat(leftStates,rightStates, ((Until)formula).getLeftActions());
        }
        ArrayList<State> tempList = rightStates;
        while(true){
            ArrayList<State> smth = new ArrayList<State>(leftStates);
            smth.removeAll(rightStates);
//            leftStates.removeAll(rightStates);
            ArrayList<State> remove = new ArrayList<>();
            for (int i = 0; i < smth.size(); i++) {
                ArrayList<State> afterStates = model.nextStates(smth.get(i));
                ArrayList<State> removeAfters = new ArrayList<>();
                for (int j = 0; j < afterStates.size(); j++) {
                    if(rightActions.size()>0&&rightStates.contains(afterStates.get(j))){
                        int count = 0 ;
                        ArrayList<Transition> inTrans = model.getToStateTrans(afterStates.get(j));
                        for (int k = 0; k < inTrans.size(); k++) {
                            if(!(inTrans.get(i).getSource().equals(smth.get(i).getName())&&inTrans.get(i).getTarget().equals(afterStates.get(j).getName()))){
                                inTrans.remove(k);
                                k--;
                            }
                        }
                        if(inTrans.size()==0) continue;
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

                    //TODO LEFT`
                    if(leftActions.size() > 0 && smth.contains(afterStates.get(j))){
                        int count = 0 ;

                        ArrayList<Transition> inTrans = model.getToStateTrans(afterStates.get(j));
                        for (int k = 0; k < inTrans.size(); k++) {
                            if(!(inTrans.get(i).getSource().equals(smth.get(i).getName())&&inTrans.get(i).getTarget().equals(afterStates.get(j).getName()))){
                                inTrans.remove(k);
                                k--;
                            }
                        }
                        if(inTrans.size()==0) continue;
                        for (int k = 0; k < inTrans.size(); k++) {
                            boolean exist = false;
                            for (int l = 0; l < inTrans.get(k).getActions().length ; l++) {
                                for (String leftAction : leftActions) {
                                    if (inTrans.get(i).getActions()[l].equals(leftAction)) {
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
                }
                afterStates.removeAll(removeAfters);
                afterStates.retainAll(tempList);
                if(afterStates.isEmpty()) remove.add(leftStates.get(i));
            }
            smth.removeAll(remove);
            if(smth.isEmpty()) break;
            tempList.addAll(smth);
        }
        return tempList;
    }

    public ArrayList<State> exAlways(ThereExists form, ArrayList<State> states){
        PathFormula formulaP = form.pathFormula;

        StateFormula formulaS = ((Always)formulaP).stateFormula;
        ArrayList<State> satisfactoryStates = sat(states,formulaS);
        if(!((Always)formulaP).getActions().isEmpty()){
            satisfactoryStates = getPrevSat(satisfactoryStates, ((Always)formulaP).getActions());
        }
        ArrayList<State> tempList = new ArrayList<>(satisfactoryStates);
        while(true){
            ArrayList<State> stateList = new ArrayList<>(tempList);
            ArrayList<State> remove = new ArrayList<>();
            for (State temp : stateList) {
                ArrayList<State> afterStates = model.nextStates(temp);
                afterStates.retainAll(tempList);
                if (afterStates.isEmpty()) remove.add(temp);
            }
            if(remove.size()==0) break;
            tempList.removeAll(remove);
        }
        return tempList;
    }
    public ArrayList<State> getPrevSat(ArrayList<State> states, Set<String> actions) {

        ArrayList<State> remove = new ArrayList<>();
        for (int i = 0; i < states.size(); i++) {
            int count = 0;
            ArrayList<Transition> inTrans = model.getToStateTrans(states.get(i));
            if(inTrans.size()==0) continue;

            for (int j = 0; j < inTrans.size(); j++) {
                boolean empty = false;

                for (int k = 0; k < inTrans.get(i).getActions().length; k++) {

                    if(actions.contains(inTrans.get(i).getActions()[k])){
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
    public ArrayList<State> getPostSat(ArrayList<State> Lstates, ArrayList<State> Rstates, Set<String> actions){
        ArrayList<State> remove = new ArrayList<>();
        for (int i = 0; i < Lstates.size(); i++) {
            int count = 0;
            ArrayList<Transition> outTrans = model.getFromStateTrans(Lstates.get(i));
            if(outTrans.size()==0) continue;

            for (int j = 0; j < outTrans.size(); j++) {
                boolean empty = false;
                boolean isFromRight = false;
                for (int k = 0; k < outTrans.get(j).getActions().length; k++) {
                    for (int l = 0; l < Rstates.size(); l++) {
                        if(Rstates.get(l).getName().equals(outTrans.get(j).getActions()[k])){
                            isFromRight = true;
                        }
                    }
                    if(actions.contains(outTrans.get(i).getActions()[k])){
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
            if(count == outTrans.size()) remove.add(Lstates.get(i));
        }
        Lstates.removeAll(remove);
        return Lstates;
    }


}
