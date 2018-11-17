package SAT_Check;

import formula.FormulaParser;
import formula.pathFormula.Always;
import formula.pathFormula.Eventually;
import formula.pathFormula.Next;
import formula.stateFormula.ForAll;
import formula.stateFormula.StateFormula;
import formula.stateFormula.ThereExists;
import model.Model;
import modelChecker.ENF;
import modelChecker.SimpleModelChecker;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class SatChecks {

    /*
     * Tests that the ENF checks work correctly
     */
    @Test
    public void CheckForAllUntil() {
        try {
            Model model = Model.parseModel("src/test/resources/test_cases/Sat_checks/model3.json");

            //StateFormula fairnessConstraint = new FormulaParser("src/test/resources/test_cases/constraint1.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/test_cases/Sat_checks/ctl1.json").parse();

            SimpleModelChecker mc = new SimpleModelChecker();

            assertTrue(mc.check(model, null, query));
            //System.out.println(mc.getTraceAsString());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
    @Test
    public void CheckAlways() {
        try {
            Model model = Model.parseModel("src/test/resources/test_cases/Sat_checks/model3.json");

            //StateFormula fairnessConstraint = new FormulaParser("src/test/resources/test_cases/constraint1.json").parse();
            StateFormula query = new FormulaParser("src/test/resources/test_cases/Sat_checks/ctl2.json").parse();

            SimpleModelChecker mc = new SimpleModelChecker();

            assertTrue(mc.check(model, null, query));
            //System.out.println(mc.getTraceAsString());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

}