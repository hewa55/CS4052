package model;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;

/**
 * A model is consist of stateArrayList and transitionArrayList
 */
public class Model {
    State[] states;
    Transition[] transitions;
    ArrayList<State> stateArrayList = new ArrayList<>();
    ArrayList<Transition> transitionArrayList = new ArrayList<>();

    public static Model parseModel(String filePath) throws IOException {

        Gson gson = new Gson();
        Model model = gson.fromJson(new FileReader(filePath), Model.class);
        for (Transition t : model.transitionArrayList) {
            System.out.println(t);
        }
        return model;
    }

    /**
     * Returns the list of the stateArrayList
     *
     * @return list of state for the given model
     */
    public ArrayList<State> getStateArrayList() {
        ArrayList<State> states1 = new ArrayList(Arrays.asList(states));
        return states1;
    }
    public void setStates(){
        stateArrayList = new ArrayList(Arrays.asList(states));
    }
    public void setTransitions(){
        transitionArrayList = new ArrayList<>(Arrays.asList(transitions));
    }


    /**
     * Returns the list of transitionArrayList
     *
     * @return list of transition for the given model
     */
    public ArrayList<Transition> getTransitionArrayList() {
        ArrayList<Transition> trans1 = new ArrayList(Arrays.asList(transitions));
        return trans1;
    }

    /**
     * returns a list of initial stateArrayList of the model.
     * @param state
     * @return
     */
    public ArrayList<State> initialStates(){
        ArrayList<State> initials = new ArrayList<>();
        ArrayList<State> states12 = new ArrayList(Arrays.asList(states));
        for (int i = 0; i < states12.size(); i++) {
            if(states12.get(i).isInit()) initials.add(states12.get(i));
        }

        return initials;
    }

    /**
     * Return a state with a specified name
     * @param name
     * @return
     */
    public State statebyName(String name){
        for (int i = 0; i < getStateArrayList().size(); i++) {
            if(getStateArrayList().get(i).getName().equals(name))
                return getStateArrayList().get(i);
        }
        return null;
    }

    /**
     * Returns a list of stateArrayList that can be reached with one transition from specified state.
     * @param state
     * @return
     */
    public ArrayList<State> nextStates(State state){
        ArrayList<State> result = new ArrayList<>();
        ArrayList<Transition> trans = getTransitionArrayList();
        for (int i = 0; i < trans.size(); i++) {
            if(trans.get(i).getSource().equals(state.getName())){
                result.add(statebyName(trans.get(i).getTarget()));
            }
        }
        return result;
    }

    /**
     * Removing terminal stateArrayList from the model by adding transition to itself..
     */
    public void prepare(){
        for (int i = 0; i < stateArrayList.size(); i++) {
            State state = stateArrayList.get(i);
            if(nextStates(state).size() == 0){
                transitionArrayList.add(new Transition(state.getName(),state.getName(),new String[]{}));
            }
        }
    }

    /**
     * Returning transitionArrayList leading to state.
     * @param state
     * @return
     */
    public ArrayList<Transition> getToStateTrans(State state){
        ArrayList<Transition> trans = new ArrayList<>();
        for (int i = 0; i < transitionArrayList.size(); i++) {
            if(transitionArrayList.get(i).getTarget().equals(state.getName())){
                trans.add(transitionArrayList.get(i));
            }
        }
        return trans;
    }

    /**
     * Returning transitionArrayList going from state.
     * @param state
     * @return
     */
    public ArrayList<Transition> getFromStateTrans(State state){
        ArrayList<Transition> trans = new ArrayList<>();
        for (int i = 0; i < transitionArrayList.size(); i++) {
            if(transitionArrayList.get(i).getSource().equals(state.getName())){
                trans.add(transitionArrayList.get(i));
            }
        }
        return trans;
    }


}