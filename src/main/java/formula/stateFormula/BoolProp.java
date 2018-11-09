package formula.stateFormula;

public class BoolProp extends StateFormula {
    public final boolean value;
    private String formulaType = "BoolProp";


    public BoolProp(boolean value) {
        this.value = value;
    }

    @Override
    public void writeToBuffer(StringBuilder buffer) {
        String stringValue = (value) ? "True" : "False";
        buffer.append(" " + stringValue + " ");
    }

    @Override
    public String getFormulaType() {
        return formulaType;
    }

}