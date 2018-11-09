package modelChecker;

import formula.pathFormula.*;
import formula.stateFormula.*;

import java.util.HashSet;

public class ENFTranslator {
    private static final String ATOMIC = "AtomicProp";
    private static final String AND = "And";
    private static final String BOOL = "BoolProp";
    private static final String FOR_ALL = "ForAll";
    private static final String NOT = "Not";
    private static final String OR = "Or";
    private static final String THERE_EXISTS = "ThereExists";
    private static final String ALWAYS = "Always";
    private static final String EVENTUALLY = "Eventually";
    private static final String NEXT = "Next";
    private static final String UNTIL = "Until";


    public StateFormula parseENF(StateFormula formula) {
        switch (formula.getFormulaType()) {
            case ATOMIC:
                return formula;
            case AND:
                return parseAnd((And) formula);
            case BOOL:
                return parseBoolProp((BoolProp) formula);
            case FOR_ALL:
                return parseForAll((ForAll) formula);
            case NOT:
                return parseNot((Not) formula);
            case OR:
                return parseOr((Or) formula);
            case THERE_EXISTS:
                return parseThereExists((ThereExists) formula);
            default:
                return null;
        }
    }

    /**
     * ENF parser AND CTL
     *
     * @param formula
     * @return StateFormula
     */
    private StateFormula parseAnd(And formula) {
        // p and q = enf(p) AND enf(q)
        return new And(
                parseENF(formula.left),
                parseENF(formula.right)
        );
    }

    /**
     * ENF parser BoolProp CTL
     *
     * @param formula
     * @return StateFormula
     */
    private StateFormula parseBoolProp(BoolProp formula) {
        // true = true, false = Not(true)
        if (formula.value) {
            return formula;
        } else {
            return new Not(new BoolProp(true));
        }
    }

    /**
     * Simplifiy Not(ThereExists) Structure
     * @param formula
     * @return
     */
    private Not NotThereExists(PathFormula formula) {
        return new Not(new ThereExists(formula));
    }

    /**
     * ENF parsper ForAll CTL
     *
     * @param formula
     * @return
     */
    private StateFormula parseForAll(ForAll formula) {
        switch (formula.pathFormula.getFormulaType()) {
            case NEXT: return forAllNext((Next) formula.pathFormula);
            case EVENTUALLY: return forAllEventually((Eventually) formula.pathFormula);
            case UNTIL: return forAllUntil((Until) formula.pathFormula);
            case ALWAYS: return forAllAlways((Always) formula.pathFormula);
            default:
                return null;
        }

    }

    /**
     * ENF parser ThereExists CTL
     * @param formula
     * @return
     */
    private StateFormula parseThereExists(ThereExists formula) {
        switch (formula.pathFormula.getFormulaType()) {
            case ALWAYS: return thereExistsAlways((Always) formula.pathFormula);
            case EVENTUALLY:return thereExistsEventually((Eventually) formula.pathFormula);
            case NEXT: return thereExistsNext((Next) formula.pathFormula);
            case UNTIL:return thereExistsUntil((Until) formula.pathFormula);
            default: return null;
        }
    }

    /**
     * ENF parser NOT CTL
     *
     * @param formula
     * @return
     */
    private StateFormula parseNot(Not formula) {
        // Not(q) = Not(enf(q))
        return new Not(
                parseENF(formula.stateFormula)
        );
    }

    /**
     * ENF parser OR CTL
     *
     * @param formula
     * @return
     */
    private StateFormula parseOr(Or formula) {
        // q or p = Not(Not(enf(q)) AND Not(enf(p)))
        return new Not(
                new And(
                        new Not(parseENF(formula.left)),
                        new Not(parseENF(formula.right))
                )
        );
    }

    //
    // FORALL HELPER FUNCTIONS
    //
    private Not forAllAlways(Always alwaysFormula){
        // ForAll(Always(q)) equals Not(ThereExists(TRUE U Not(enf(q))))
        return NotThereExists(
                new Until(
                        new BoolProp(true),
                        new Not(parseENF(alwaysFormula.stateFormula)),
                        new HashSet<String>(),
                        alwaysFormula.getActions()
                ));
    }

    private Not forAllNext(Next nextFormula){
        // ForAll(Next(q)) equals Not(ThereExists(Next(Not(enf(q)))))
        return NotThereExists(
                new Next(
                        new Not(parseENF(nextFormula.stateFormula)),
                        nextFormula.getActions()
                ));
    }

    private Not forAllEventually(Eventually eventuallyFormula){
        // ForAll(EVENTUALLY(q)) equals Not(ThereExists(Always(Not(enf(q)))))
        return NotThereExists(
                new Always(
                        new Not(parseENF(eventuallyFormula.stateFormula)),
                        eventuallyFormula.getRightActions()
                ));
    }

    private And forAllUntil(Until untilFormula){
        // ForAll(q U p) equals
        // Not(ThereExists(Not(enf(q)) U (Not(enf(p)) AND Not(enf(q)))))
        // AND Not(ThereExists(Always(Not(enf(q)))))
        StateFormula enfQ = parseENF(untilFormula.right);
        StateFormula enfP = parseENF(untilFormula.left);

        StateFormula leftAnd = NotThereExists(
                new Until(
                        new Not(enfQ),
                        new And(
                                new Not(enfP),
                                new Not(enfQ)
                        ),
                        untilFormula.getLeftActions(),
                        untilFormula.getRightActions()
                ));

        StateFormula rightAnd = NotThereExists(
                new Always(
                        new Not(enfQ),
                        untilFormula.getRightActions()
                ));


        return new And(leftAnd, rightAnd);
    }


    //
    // THERE EXISTS HELPER FUNCTIONS
    //
    private ThereExists thereExistsAlways(Always formula){
        // ThereExists(Always(q)) = ThereExists(Always(enf(q)))
        return new ThereExists(
                new Always(
                        parseENF(formula.stateFormula),
                        formula.getActions()
                )
        );
    }

    private ThereExists thereExistsEventually(Eventually formula){
        // ThereExists(EVENTUALLY(q)) = ThereExists( TRUE U enf(q))
        return new ThereExists(
                new Until(
                        new BoolProp(true),
                        parseENF(formula.stateFormula),
                        formula.getLeftActions(),
                        formula.getRightActions()
                )

        );
    }
    private ThereExists thereExistsNext(Next formula){
        // ThereExists(Next(q)) = ThereExists(Next(enf(q)))
        return new ThereExists(
                new Next(
                        parseENF(formula.stateFormula),
                        formula.getActions()
                )
        );
    }

    private ThereExists thereExistsUntil(Until formula){
        // ThereExists(q U p) = ThereExists(enf(q) U enf(p))
        return new ThereExists(
                new Until(
                        parseENF(formula.left),
                        parseENF(formula.right),
                        formula.getLeftActions(),
                        formula.getRightActions()
                )
        );
    }

}
