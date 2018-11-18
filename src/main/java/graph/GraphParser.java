package graph;
import model.Model;
import model.State;
import model.Transition;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.ArrayList;
import java.util.List;

public class GraphParser {

    public void displayGraph(Model model, List<State> notSatStates){
        // displays the invalid states in red and the transitions in between, labels initial states as orange
        Graph graph = new SingleGraph("Graph");
        for (State state : model.getStateArrayList() ){
            graph.addNode(state.getName());
            graph.getNode(state.getName()).addAttribute("ui.label",state.getName()+" " + labelsAsString(state.getLabel()));
            if(state.isInit()){
                graph.getNode(state.getName()).addAttribute("ui.style", "fill-color: rgb(0,100,255);");
            }
        }

        for (State state : notSatStates){
            if(state.isInit()){
                graph.getNode(state.getName()).addAttribute("ui.style", "fill-color: rgb(255,140,0);");
            } else {
                graph.getNode(state.getName()).addAttribute("ui.style", "fill-color: rgb(255,0,0);");
            }
        }
        List<String> notStateNames = notSatStatesAsString(notSatStates);
        for (Transition transition : model.getTransitionArrayList()){
            if(graph.getEdge(transition.getSource()+transition.getTarget())==null){
                graph.addEdge(transition.getSource()+transition.getTarget(),transition.getSource(),transition.getTarget(),true);
                if(notStateNames.contains(transition.getSource())&&notStateNames.contains(transition.getTarget())){
                    graph.getEdge(transition.getSource()+transition.getTarget()).addAttribute("ui.style", "fill-color: rgb(255,0,0);");
                }
            }
        }
        graph.display();
    }

    private String labelsAsString(String[] label){
        StringBuilder builder = new StringBuilder("Labels ");
        for (int i = 0; i < label.length ; i++) {
            builder.append(label[i]);
            builder.append(" ");
        }
        return builder.toString();
    }

    private List<String> notSatStatesAsString(List<State> notSatStates){
        List<String> list = new ArrayList<>();
        for (State state : notSatStates){
            list.add(state.getName());
        }
        return list;
    }

}
