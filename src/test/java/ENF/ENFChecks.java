package ENF;

import static org.junit.Assert.*;

import java.io.IOException;

import formula.pathFormula.Always;
import formula.pathFormula.Eventually;
import formula.pathFormula.Next;
import formula.pathFormula.Until;
import formula.stateFormula.ForAll;
import formula.stateFormula.ThereExists;
import modelChecker.ENF;
import org.junit.Test;

import formula.FormulaParser;
import formula.stateFormula.StateFormula;
import modelChecker.ModelChecker;
import modelChecker.SimpleModelChecker;
import model.Model;

public class ENFChecks {

    /*
     * Tests that the ENF checks work correctly
     */
    @Test
    public void CheckForAllUntil() {
        try {
            ENF enf = new ENF();
            StateFormula query = new FormulaParser("src/test/resources/asCTLFormula/ctl1.json").parse();

            //System.out.println(query.toString());
            //System.out.println( ((Until)((ForAll)query).pathFormula).getLeftActions().toArray()[0] );
            //System.out.println(enf.translateENF(query).toString().replaceAll(" ",""));
            assertEquals("(!((E(!(p)U(!(q)&&!(p)))))&&!((EG!(p))))",enf.translateENF(query).toString().replaceAll(" ",""));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
    @Test
    public void CheckForAllNext() {
        try {
            ENF enf = new ENF();
            StateFormula query = new FormulaParser("src/test/resources/asCTLFormula/ctl2.json").parse();

            //System.out.println(query.toString());
            //System.out.println( ((Next)((ForAll)query).pathFormula).getActions().toArray()[0] );
            //System.out.println(enf.translateENF(query).toString().replaceAll(" ",""));
            assertEquals("!((EX!(p)))",enf.translateENF(query).toString().replaceAll(" ",""));
            assertEquals("act1",((Next)((ForAll)query).pathFormula).getActions().toArray()[0]);
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
    @Test
    public void CheckThereExistsEventually() {
        try {
            ENF enf = new ENF();
            StateFormula query = new FormulaParser("src/test/resources/asCTLFormula/ctl3.json").parse();

            //System.out.println(query.toString());
            //System.out.println(query.getFormulaType());
            //System.out.println( ((Eventually)((ThereExists)query).pathFormula).getRightActions() );
            //System.out.println( ((Eventually)((ThereExists)query).pathFormula).getLeftActions() );
            //System.out.println(enf.translateENF(query).toString().replaceAll(" ",""));
            assertEquals("(E(TrueUp))",enf.translateENF(query).toString().replaceAll(" ",""));
            assertEquals("[act2]",((Eventually)((ThereExists)query).pathFormula).getRightActions().toString());
            assertEquals("[act1]",((Eventually)((ThereExists)query).pathFormula).getLeftActions().toString());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
    @Test
    public void ForAllEventually() {
        try {
            ENF enf = new ENF();
            StateFormula query = new FormulaParser("src/test/resources/asCTLFormula/ctl4.json").parse();

            //System.out.println(query.toString());
            //System.out.println(query.getFormulaType());
            //System.out.println( ((Eventually)((ForAll)query).pathFormula).getRightActions() );
            //System.out.println( ((Eventually)((ForAll)query).pathFormula).getLeftActions() );
            //System.out.println(enf.translateENF(query).toString().replaceAll(" ",""));
            assertEquals("!((EG!(p)))",enf.translateENF(query).toString().replaceAll(" ",""));
            assertEquals("[act2]",((Eventually)((ForAll)query).pathFormula).getRightActions().toString());
            assertEquals("[act1]",((Eventually)((ForAll)query).pathFormula).getLeftActions().toString());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
    @Test
    public void ForAllAlways() {
        try {
            ENF enf = new ENF();
            StateFormula query = new FormulaParser("src/test/resources/asCTLFormula/ctl5.json").parse();

            //System.out.println(query.toString());
            //System.out.println(query.getFormulaType());
            //System.out.println( ((Always)((ForAll)query).pathFormula).getActions() );
            //System.out.println( ((Eventually)((ForAll)query).pathFormula).getLeftActions() );
            //System.out.println(enf.translateENF(query).toString().replaceAll(" ",""));
            assertEquals("!((E(TrueU!(p))))",enf.translateENF(query).toString().replaceAll(" ",""));
            assertEquals("[act1]",((Always)((ForAll)query).pathFormula).getActions().toString());
            //assertEquals("[act1]",((Eventually)((ForAll)query).pathFormula).getLeftActions().toString());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
}