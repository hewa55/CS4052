package modelChecker;

import formula.FormulaParser;
import formula.stateFormula.StateFormula;
import model.Model;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ModelCheckerTest3 {

    /*
     * An example of how to set up and call the model building methods and make
     * a call to the model checker itself. The contents of model.json,
     * constraint1.json and ctl.json are just examples, you need to add new
     * models and formulas for the mutual exclusion task.
     */
    // @Carlos - this should also pass I feel. It fails again in s0
    @Test
    public void CheckModel3Ctl1() {
        try {
            Model model = Model.parseModel("src/test/resources/2018/model2.json");

            //StateFormula fairnessConstraint = new FormulaParser("src/test/resources/2018/constraint1.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/2018/model3ctl/ctl1.json").parse();

            SimpleModelChecker mc = new SimpleModelChecker();

            assertFalse(mc.check(model, null, query));
            System.out.println(mc.getTraceAsString());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
    @Test
    public void CheckModel3Ctl2() {
        try {
            Model model = Model.parseModel("src/test/resources/2018/model2.json");

            //StateFormula fairnessConstraint = new FormulaParser("src/test/resources/2018/constraint1.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/2018/model3ctl/ctl2.json").parse();

            SimpleModelChecker mc = new SimpleModelChecker();

            assertTrue(mc.check(model, null, query));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
    @Test
    public void CheckModel3Ctl3() {
        try {
            Model model = Model.parseModel("src/test/resources/2018/model2.json");

            //StateFormula fairnessConstraint = new FormulaParser("src/test/resources/2018/constraint1.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/2018/model3ctl/ctl3.json").parse();

            SimpleModelChecker mc = new SimpleModelChecker();

            assertFalse(mc.check(model, null, query));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }




}