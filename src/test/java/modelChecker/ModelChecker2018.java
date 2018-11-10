package modelChecker;

import static org.junit.Assert.*;

import java.io.IOException;

import formula.stateFormula.ThereExists;
import org.junit.Test;

import formula.FormulaParser;
import formula.stateFormula.StateFormula;
import modelChecker.ModelChecker;
import modelChecker.SimpleModelChecker;
import model.Model;

public class ModelChecker2018 {

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
    @Test
    public void CheckModel1Ctl5() {
        try {
            Model model = Model.parseModel("src/test/resources/2018/model1.json");

            //StateFormula fairnessConstraint = new FormulaParser("src/test/resources/2018/constraint1.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/2018/ctl2.json").parse();

            SimpleModelChecker mc = new SimpleModelChecker();

            assertTrue(mc.check(model, null, query));
            //System.out.println(mc.getTraceAsString());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }


}