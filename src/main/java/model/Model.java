package model;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;

/**
 * A model is consist of statesList and allTransitions
 */
public class Model {
    State[] states;
    Transition[] transitions;
    ArrayList<State> statesList = new ArrayList<>();
    ArrayList<Transition> allTransitions = new ArrayList<>();

    public static Model parseModel(String filePath) throws IOException {

        Gson gson = new Gson();
        Model model = gson.fromJson(new FileReader(filePath), Model.class);
        for (Transition t : model.allTransitions) {
            System.out.println(t);
        }
        return model;
    }

    /**
     * Returns the list of the statesList
     *
     * @return list of state for the given model
     */
    public ArrayList<State> getStatesList() {
        ArrayList<State> states1 = new ArrayList(Arrays.asList(states));
        return states1;
    }
    public void setStates(){
        statesList = new ArrayList(Arrays.asList(states));
    }
    public void setTransitions(){
        allTransitions = new ArrayList<>(Arrays.asList(transitions));
    }


    /**
     * Returns the list of allTransitions
     *
     * @return list of transition for the given model
     */
    public ArrayList<Transition> getAllTransitions() {
        ArrayList<Transition> trans1 = new ArrayList(Arrays.asList(transitions));
        return trans1;
    }

    /**
     * returns a list of initial statesList of the model.
     * @param
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
        for (int i = 0; i < getStatesList().size(); i++) {
            if(getStatesList().get(i).getName().equals(name))
                return getStatesList().get(i);
        }
        return null;
    }

    /**
     * Returns a list of statesList that can be reached with one transition from specified state.
     * @param state
     * @return
     */
    public ArrayList<State> nextStates(State state){
        ArrayList<State> result = new ArrayList<>();
        ArrayList<Transition> trans = getAllTransitions();
        for (int i = 0; i < trans.size(); i++) {
            if(trans.get(i).getSource().equals(state.getName())){
                result.add(statebyName(trans.get(i).getTarget()));
            }
        }
        return result;
    }

    /**
     * Removing terminal statesList from the model by adding transition to itself..
     */
    public void prepare(){
        for (int i = 0; i < statesList.size(); i++) {
            State state = statesList.get(i);
            if(nextStates(state).size() == 0){
                allTransitions.add(new Transition(state.getName(),state.getName(),new String[]{}));
            }
        }
    }

    /**
     * Returning allTransitions leading to state.
     * @param state
     * @return
     */
    public ArrayList<Transition> getToStateTrans(State state){
        ArrayList<Transition> trans = new ArrayList<>();
        for (int i = 0; i < allTransitions.size(); i++) {
            if(allTransitions.get(i).getTarget().equals(state.getName())){
                trans.add(allTransitions.get(i));
            }
        }
        return trans;
    }

    /**
     * Returning allTransitions going from state.
     * @param state
     * @return
     */
    public ArrayList<Transition> getFromStateTrans(State state){
        ArrayList<Transition> trans = new ArrayList<>();
        for (int i = 0; i < allTransitions.size(); i++) {
            if(allTransitions.get(i).getSource().equals(state.getName())){
                trans.add(allTransitions.get(i));
            }
        }
        return trans;
    }


}