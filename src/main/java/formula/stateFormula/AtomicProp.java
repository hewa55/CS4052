package formula.stateFormula;

public class AtomicProp extends StateFormula {
    public final String label;
    private String formulaType = "AtomicProp";


    public AtomicProp(String label) {
        this.label = label;
    }

    @Override
    public void writeToBuffer(StringBuilder buffer) {
        buffer.append(" " + label + " ");
    }

    @Override
    public String getFormulaType() {
        return formulaType;
    }
}