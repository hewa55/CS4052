package modelChecker;

import formula.FormulaParser;
import formula.stateFormula.StateFormula;
import model.Model;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ModelCheckerTest4 {

    /*
     * An example of how to set up and call the model building methods and make
     * a call to the model checker itself. The contents of model.json,
     * constraint1.json and ctl.json are just examples, you need to add new
     * models and formulas for the mutual exclusion task.
     */
    @Test
    public void CheckModel4Ctl1() {
        try {
            Model model = Model.parseModel("src/test/resources/test_cases/model4.json");

            //StateFormula fairnessConstraint = new FormulaParser("src/test/resources/test_cases/constraint1.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/test_cases/model4ctl/ctl1.json").parse();

            SimpleModelChecker mc = new SimpleModelChecker();

            assertTrue(mc.check(model, null, query));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
}