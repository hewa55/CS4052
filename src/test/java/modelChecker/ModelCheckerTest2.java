package modelChecker;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import formula.FormulaParser;
import formula.stateFormula.StateFormula;
import modelChecker.ModelChecker;
import modelChecker.SimpleModelChecker;
import model.Model;
public class ModelCheckerTest2 {

    @Test
    public void buildAndCheckModel21() {
        try {
            Model model = Model.parseModel("src/test/resources/model2.json");

            StateFormula fairnessConstraint = new FormulaParser("src/test/resources/constraint2.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/model2queries/ctl1.json").parse();

            ModelChecker mc = new SimpleModelChecker();

            assertTrue(mc.check(model, null, query));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    @Test
    public void buildAndCheckModel22() {
        try {
            Model model = Model.parseModel("src/test/resources/model2.json");

            StateFormula fairnessConstraint = new FormulaParser("src/test/resources/constraint2.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/model2queries/ctl2.json").parse();

            ModelChecker mc = new SimpleModelChecker();

            assertTrue(mc.check(model, null, query));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
    @Test
    public void buildAndCheckModel23() {
        try {
            Model model = Model.parseModel("src/test/resources/model2.json");

            StateFormula fairnessConstraint = new FormulaParser("src/test/resources/constraint2.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/model2queries/ctl3.json").parse();

            ModelChecker mc = new SimpleModelChecker();

            assertTrue(mc.check(model, null, query));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
    @Test
    public void buildAndCheckModel24() {
        try {
            Model model = Model.parseModel("src/test/resources/model2.json");

            StateFormula fairnessConstraint = new FormulaParser("src/test/resources/constraint2.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/model2queries/ctl4.json").parse();

            ModelChecker mc = new SimpleModelChecker();

            assertTrue(mc.check(model, null, query));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    @Test
    public void buildAndCheckModel25() {
        try {
            Model model = Model.parseModel("src/test/resources/model2.json");

            StateFormula fairnessConstraint = new FormulaParser("src/test/resources/constraint2.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/model2queries/ctl5.json").parse();

            ModelChecker mc = new SimpleModelChecker();

            assertTrue(mc.check(model, null, query));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    @Test
    public void buildAndCheckModel26() {
        try {
            Model model = Model.parseModel("src/test/resources/model2.json");

            StateFormula fairnessConstraint = new FormulaParser("src/test/resources/constraint2.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/model2queries/ctl6.json").parse();

            SimpleModelChecker mc = new SimpleModelChecker();

            assertFalse(mc.check(model, null, query));
            System.out.println("test 26: ");
            System.out.println(mc.getTraceAsString());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
    @Test
    public void buildAndCheckModel27() {
        try {
            Model model = Model.parseModel("src/test/resources/model2.json");

            StateFormula fairnessConstraint = new FormulaParser("src/test/resources/constraint2.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/model2queries/ctl7.json").parse();

            ModelChecker mc = new SimpleModelChecker();

            assertTrue(mc.check(model, null, query));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
    @Test
    public void buildAndCheckModel28() {
        try {
            Model model = Model.parseModel("src/test/resources/model2.json");

            StateFormula fairnessConstraint = new FormulaParser("src/test/resources/constraint2.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/model2queries/ctl8.json").parse();

            ModelChecker mc = new SimpleModelChecker();

            assertTrue(mc.check(model, null, query));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    @Test
    public void buildAndCheckModel29() {
        try {
            Model model = Model.parseModel("src/test/resources/model2.json");

            StateFormula fairnessConstraint = new FormulaParser("src/test/resources/constraint2.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/model2queries/ctl9.json").parse();

            ModelChecker mc = new SimpleModelChecker();

            assertTrue(mc.check(model, null, query));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
    @Test
    public void buildAndCheckModel210() {
        try {
            Model model = Model.parseModel("src/test/resources/model2.json");

            StateFormula fairnessConstraint = new FormulaParser("src/test/resources/constraint2.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/model2queries/ctl10.json").parse();

            ModelChecker mc = new SimpleModelChecker();

            assertTrue(mc.check(model, null, query));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
    @Test
    public void buildAndCheckModel211() {
        try {
            Model model = Model.parseModel("src/test/resources/model2.json");

            StateFormula fairnessConstraint = new FormulaParser("src/test/resources/constraint2.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/model2queries/ctl11.json").parse();

            ModelChecker mc = new SimpleModelChecker();

            assertTrue(mc.check(model, null, query));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

}
