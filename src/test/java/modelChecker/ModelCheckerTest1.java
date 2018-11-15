package modelChecker;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import formula.FormulaParser;
import formula.stateFormula.StateFormula;
import model.Model;

public class ModelCheckerTest1 {

    /*
     * An example of how to set up and call the model building methods and make
     * a call to the model checker itself. The contents of model.json,
     * constraint1.json and ctl.json are just examples, you need to add new
     * models and formulas for the mutual exclusion task.
     */
    @Test
    public void CheckModel1Ctl1() {
        try {
            Model model = Model.parseModel("src/test/resources/2018/model1.json");

            //StateFormula fairnessConstraint = new FormulaParser("src/test/resources/2018/constraint1.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/2018/model1ctl/ctl1.json").parse();

            SimpleModelChecker mc = new SimpleModelChecker();

            assertTrue(mc.check(model, null, query));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
    @Test
    public void CheckModel1Ctl2() {
        try {
            Model model = Model.parseModel("src/test/resources/2018/model1.json");

            //StateFormula fairnessConstraint = new FormulaParser("src/test/resources/2018/constraint1.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/2018/model1ctl/ctl2.json").parse();

            SimpleModelChecker mc = new SimpleModelChecker();

            assertTrue(mc.check(model, null, query));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    @Test
    public void CheckModel1Ctl3() {
        try {
            Model model = Model.parseModel("src/test/resources/2018/model1.json");

            //StateFormula fairnessConstraint = new FormulaParser("src/test/resources/2018/constraint1.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/2018/model1ctl/ctl3.json").parse();

            SimpleModelChecker mc = new SimpleModelChecker();

            assertFalse(mc.check(model, null, query));
            System.out.println("Trace");
            System.out.println(mc.getTraceAsString());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
    @Test
    public void CheckModel1Ctl4() {
        try {
            Model model = Model.parseModel("src/test/resources/2018/model1.json");

            //StateFormula fairnessConstraint = new FormulaParser("src/test/resources/2018/constraint1.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/2018/model1ctl/ctl4.json").parse();

            SimpleModelChecker mc = new SimpleModelChecker();

            assertTrue(mc.check(model, null, query));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    // See this test and the one below - Constraints works as Constraints removes s2 possibility
    // at least it should from my understanding, but still fails @Carlos
    @Test
    public void CheckModel1Ctl5() {
        try {
            Model model = Model.parseModel("src/test/resources/2018/model1.json");

            //StateFormula fairnessConstraint = new FormulaParser("src/test/resources/2018/constraint1.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/2018/model1ctl/ctl5.json").parse();

            SimpleModelChecker mc = new SimpleModelChecker();

            assertFalse(mc.check(model, null, query));
            System.out.println(mc.getTraceAsString());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
    // should pass with this constraint? maybe I am using the constraint wrong
    @Test
    public void CheckModel1Ctl5Constraint() {
        try {
           Model model = Model.parseModel("src/test/resources/2018/model1.json");
            StateFormula fairnessConstraint = new FormulaParser("src/test/resources/2018/Constraints/constraint1.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/2018/model1ctl/ctl5.json").parse();
            SimpleModelChecker mc = new SimpleModelChecker();
            System.out.println("Failing test start:");

            try {
                assertTrue(mc.check(model, fairnessConstraint, query));
            } catch (AssertionError e) {
                System.out.println(mc.getTraceAsString());
            }


        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    @Test
    public void CheckModel1GivenCtl1() {
        try {
            Model model = Model.parseModel("src/test/resources/2018/model1.json");
            //StateFormula fairnessConstraint = new FormulaParser("src/test/resources/2018/constraint1.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/2018/ctl1.json").parse();

            SimpleModelChecker mc = new SimpleModelChecker();

            assertTrue(mc.check(model, null, query));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
    @Test
    public void CheckModel1GivenCtl2() {
        try {
            Model model = Model.parseModel("src/test/resources/2018/model1.json");
            //StateFormula fairnessConstraint = new FormulaParser("src/test/resources/2018/constraint1.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/2018/ctl2.json").parse();

            SimpleModelChecker mc = new SimpleModelChecker();

            assertFalse(mc.check(model, null, query));
            System.out.println(mc.getTraceAsString());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }


}