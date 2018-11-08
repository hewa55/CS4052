package modelChecker;

import model.State;
import model.Transition;

import java.util.LinkedList;

public class Node {

    String name;
    State state;
    LinkedList<Transition> transitions;

    public Node (String name, State state) {
        this.name = name;
        this.state = state;
        transitions = new LinkedList<>();
    }

    public void addTransition(Transition transition) {
        this.transitions.add(transition);
    }




}
