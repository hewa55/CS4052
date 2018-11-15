package modelChecker;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import formula.FormulaParser;
import formula.stateFormula.StateFormula;
import modelChecker.ModelChecker;
import modelChecker.SimpleModelChecker;
import model.Model;

public class MCRoma {

    /*
     * An example of how to set up and call the model building methods and make
     * a call to the model checker itself. The contents of model.json,
     * constraint1.json and ctl.json are just examples, you need to add new
     * models and formulas for the mutual exclusion task.
     */
   /* @Test
    public void buildAndCheckModel11() {
        try {
            Model model = Model.parseModel("src/test/resources/model1.json");

            StateFormula fairnessConstraint = new FormulaParser("src/test/resources/constraint1.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/model1queries/ctl1.json").parse();

            SimpleModelChecker mc = new SimpleModelChecker();

            assertFalse(mc.check(model, null, fairnessConstraint));
            System.out.println("test11: ");
            //mc.getTrace();
            System.out.println(mc.getTraceAsString());


        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }*/
    @Test
    public void buildAndCheckModel12(){
        try {
            Model model = Model.parseModel("src/test/resources/model1.json");

            StateFormula fairnessConstraint = new FormulaParser("src/test/resources/constraint1.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/model1queries/ctl3.json").parse();

            ModelChecker mc = new SimpleModelChecker();

            assertTrue(mc.check(model, null, query));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
    @Test
    public void buildAndCheckModel13(){
        try {
            Model model = Model.parseModel("src/test/resources/model1.json");

            StateFormula query = new FormulaParser("src/test/resources/model1queries/ctl3.json").parse();

            ModelChecker mc = new SimpleModelChecker();

            assertTrue(mc.check(model, null, query));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
    @Test
    public void buildAndCheckModel14(){
        try {
            Model model = Model.parseModel("src/test/resources/model1.json");

            StateFormula fairnessConstraint = new FormulaParser("src/test/resources/constraint1.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/model1queries/ctl4.json").parse();

            ModelChecker mc = new SimpleModelChecker();

            assertTrue(mc.check(model, null, query));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
    @Test
    public void buildAndCheckModel15(){
        try {
            Model model = Model.parseModel("src/test/resources/model1.json");

            StateFormula fairnessConstraint = new FormulaParser("src/test/resources/constraint1.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/model1queries/ctl5.json").parse();

            SimpleModelChecker mc = new SimpleModelChecker();

            assertFalse(mc.check(model, null, query));
            System.out.println("test15: ");
            System.out.println(mc.getTraceAsString());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
    @Test
    public void buildAndCheckModel16(){
        try {
            Model model = Model.parseModel("src/test/resources/model1.json");

            StateFormula fairnessConstraint = new FormulaParser("src/test/resources/constraint1.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/model1queries/ctl6.json").parse();

            ModelChecker mc = new SimpleModelChecker();

            assertTrue(mc.check(model, null, query));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    @Test
    public void buildAndCheckModel17(){
        try {
            Model model = Model.parseModel("src/test/resources/model1.json");

            StateFormula fairnessConstraint = new FormulaParser("src/test/resources/constraint1.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/model1queries/ctl7.json").parse();

            ModelChecker mc = new SimpleModelChecker();

            assertTrue(mc.check(model, null, query));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
    @Test
    public void buildAndCheckModel18(){
        try {
            Model model = Model.parseModel("src/test/resources/model1.json");

            StateFormula fairnessConstraint = new FormulaParser("src/test/resources/constraint1.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/model1queries/ctl8.json").parse();

            SimpleModelChecker mc = new SimpleModelChecker();

            assertFalse(mc.check(model, null, query));
            System.out.println("test18: ");
            System.out.println(mc.getTraceAsString());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

}