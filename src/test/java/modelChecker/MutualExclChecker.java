package modelChecker;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import formula.FormulaParser;
import formula.stateFormula.StateFormula;
import modelChecker.ModelChecker;
import modelChecker.SimpleModelChecker;
import model.Model;
public class MutualExclChecker {
    @Test
    public void buildAndCheckModelExcl() {
        try {
            Model model = Model.parseModel("src/test/resources/mutual_mc.json");

            StateFormula query = new FormulaParser("src/test/resources/ctl_mc.json").parse();

            ModelChecker mc = new SimpleModelChecker();

            assertFalse(mc.check(model, null, query));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
}
