package graph;

import formula.FormulaParser;
import formula.stateFormula.StateFormula;
import model.Model;
import modelChecker.SimpleModelChecker;

import java.io.IOException;

public class ExampleGraph {
    public static void main(String args[]) throws IOException {
        Model model = Model.parseModel("src/test/resources/test_cases/model2.json");

        GraphParser graphParser = new GraphParser();

        StateFormula query = new FormulaParser("src/test/resources/test_cases/model2ctl/ctl7.json").parse();

        SimpleModelChecker mc = new SimpleModelChecker();

        mc.check(model, null, query);
        graphParser.displayGraph(model,mc.getNotSatStates());
    }

}
