package formula.stateFormula;

import formula.*;
import formula.pathFormula.PathFormula;

public class ForAll extends StateFormula {
    public final PathFormula pathFormula;
    private String formulaType = "ForAll";

    public ForAll(PathFormula pathFormula) {
        this.pathFormula = pathFormula;
    }

    @Override
    public void writeToBuffer(StringBuilder buffer) {
        buffer.append("(");
        buffer.append(FormulaParser.FORALL_TOKEN);
        pathFormula.writeToBuffer(buffer);
        buffer.append(")");
    }

    @Override
    public String getFormulaType() {
        return formulaType;
    }
}