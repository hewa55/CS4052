package modelChecker;

import formula.FormulaParser;
import formula.stateFormula.StateFormula;
import model.Model;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ModelCheckerTest2 {

    /*
     * An example of how to set up and call the model building methods and make
     * a call to the model checker itself. The contents of model.json,
     * constraint1.json and ctl.json are just examples, you need to add new
     * models and formulas for the mutual exclusion task.
     */
    @Test
    public void CheckModel2Ctl1() {
        try {
            Model model = Model.parseModel("src/test/resources/2018/model2.json");

            //StateFormula fairnessConstraint = new FormulaParser("src/test/resources/2018/constraint1.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/2018/model2ctl/ctl1.json").parse();

            SimpleModelChecker mc = new SimpleModelChecker();

            assertTrue(mc.check(model, null, query));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
    @Test
    public void CheckModel2Ctl2() {
        try {
            Model model = Model.parseModel("src/test/resources/2018/model2.json");

            //StateFormula fairnessConstraint = new FormulaParser("src/test/resources/2018/constraint1.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/2018/model2ctl/ctl2.json").parse();

            SimpleModelChecker mc = new SimpleModelChecker();

            assertTrue(mc.check(model, null, query));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
    @Test
    public void CheckModel2Ctl3() {
        try {
            Model model = Model.parseModel("src/test/resources/2018/model2.json");

            StateFormula query = new FormulaParser("src/test/resources/2018/model2ctl/ctl3.json").parse();

            SimpleModelChecker mc = new SimpleModelChecker();

            assertTrue(mc.check(model, null, query));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
    // do you mind having a look @Carlos?
    // I think the formula means : There exists (a path) in which q is true until action act2 happens and then r is true
    // trace just gives --> s0 - but q is true in s0 from my understanding?
    @Test
    public void CheckModel2Ctl4() {
        try {
            Model model = Model.parseModel("src/test/resources/2018/model2.json");

            StateFormula query = new FormulaParser("src/test/resources/2018/model2ctl/ctl4.json").parse();

            SimpleModelChecker mc = new SimpleModelChecker();

            assertTrue(mc.check(model, null, query));
            //System.out.println(mc.getTraceAsString());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
    @Test
    public void CheckModel2Ctl5() {
        try {
            Model model = Model.parseModel("src/test/resources/2018/model2.json");

            StateFormula query = new FormulaParser("src/test/resources/2018/model2ctl/ctl5.json").parse();

            SimpleModelChecker mc = new SimpleModelChecker();

            assertTrue(mc.check(model, null, query));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
    @Test
    public void CheckModel2Ctl6() {
        try {
            Model model = Model.parseModel("src/test/resources/2018/model2.json");

            StateFormula query = new FormulaParser("src/test/resources/2018/model2ctl/ctl6.json").parse();

            SimpleModelChecker mc = new SimpleModelChecker();

            assertFalse(mc.check(model, null, query));
            System.out.println(mc.getTraceAsString());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    // should fail without the constraint
    @Test
    public void CheckModel2Ctl7() {
        try {
            Model model = Model.parseModel("src/test/resources/2018/model2.json");

            StateFormula query = new FormulaParser("src/test/resources/2018/model2ctl/ctl7.json").parse();

            SimpleModelChecker mc = new SimpleModelChecker();

            assertFalse(mc.check(model, null, query));
            System.out.println(mc.getTraceAsString());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
    @Test
    public void CheckModel2Ctl7Constraint2() {
        try {
            Model model = Model.parseModel("src/test/resources/2018/model2.json");

            StateFormula fairnessConstraint = new FormulaParser("src/test/resources/2018/Constraints/constraint2.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/2018/model2ctl/ctl7.json").parse();

            SimpleModelChecker mc = new SimpleModelChecker();

            assertTrue(mc.check(model, fairnessConstraint, query));
            //System.out.println(mc.getTraceAsString());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }


}