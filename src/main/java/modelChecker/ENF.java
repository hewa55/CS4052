package modelChecker;

import formula.pathFormula.*;
import formula.stateFormula.*;

import java.util.HashSet;

import static modelChecker.Keywords.*;

/**
 * Convert into existential normal form (ENF) via this class
 */
public class ENF {

    public StateFormula translateENF(StateFormula formula) {
        switch (formula.getFormulaType()) {
            case ATOMIC:
                return formula;
            case AND:
                return processAnd((And) formula);
            case BOOL:
                return processBoolProp((BoolProp) formula);
            case FOR_ALL:
                return processForAll((ForAll) formula);
            case NOT:
                return processNot((Not) formula);
            case OR:
                return processOr((Or) formula);
            case THERE_EXISTS:
                return processThereExists((ThereExists) formula);
            default:
                return null;
        }
    }

    /**
     * Process AND from CTL Formula
     *
     * @param formula of class AND
     * @return StateFormula
     */
    private StateFormula processAnd(And formula) {
        // p and q equates to enf(p) AND enf(q)
        return new And(
                translateENF(formula.left),
                translateENF(formula.right)
        );
    }

    /**
     * Process a not token from the CTL Formula
     *
     * @param formula of type NOT
     * @return a StateFormula
     */
    private StateFormula processNot(Not formula) {
        // Not(q) = Not(enf(q))
        return new Not(
                translateENF(formula.stateFormula)
        );
    }

    /**
     * Process a boolprop from the CTL Formula
     *
     * @param formula of class BoolProp
     * @return StateFormula
     */
    private StateFormula processBoolProp(BoolProp formula) {
        // true = true, false = Not(true)
        if (formula.value) {
            return formula;
        } else {
            return new Not(new BoolProp(true));
        }
    }

    /**
     * Simplify Not(ThereExists) Structure
     *
     * @param formula of class PathFormula
     * @return An object of type NOT
     */
    private Not NotThereExists(PathFormula formula) {
        return new Not(new ThereExists(formula));
    }

    /**
     * Process ForAll expression from the CTL Formula
     *
     * @param formula of type ForAll
     * @return a StateFormula
     */
    private StateFormula processForAll(ForAll formula) {
        switch (formula.pathFormula.getFormulaType()) {
            case NEXT:
                return forAllNext((Next) formula.pathFormula);
            case EVENTUALLY:
                return forAllEventually((Eventually) formula.pathFormula);
            case UNTIL:
                return forAllUntil((Until) formula.pathFormula);
            case ALWAYS:
                return forAllAlways((Always) formula.pathFormula);
            default:
                return null;
        }

    }

    /**
     * Process a ThereExists from CTL Formula
     *
     * @param formula of type ThereExists
     * @return a StateFormula
     */
    private StateFormula processThereExists(ThereExists formula) {
        switch (formula.pathFormula.getFormulaType()) {
            case ALWAYS:
                return thereExistsAlways((Always) formula.pathFormula);
            case EVENTUALLY:
                return thereExistsEventually((Eventually) formula.pathFormula);
            case NEXT:
                return thereExistsNext((Next) formula.pathFormula);
            case UNTIL:
                return thereExistsUntil((Until) formula.pathFormula);
            default:
                return null;
        }
    }

    /**
     * Process OR from CTL Formula
     *
     * @param formula of type OR
     * @return a StateFormula
     */
    private StateFormula processOr(Or formula) {
        // q or p = Not(Not(enf(q)) AND Not(enf(p)))
        return new Not(
                new And(
                        new Not(translateENF(formula.left)),
                        new Not(translateENF(formula.right))
                )
        );
    }

    //
    // FORALL HELPER FUNCTIONS
    //
    private Not forAllAlways(Always formula) {
        // ForAll(Always(q)) equals Not(ThereExists(TRUE U Not(enf(q))))
        return NotThereExists(
                new Until(
                        new BoolProp(true),
                        new Not(translateENF(formula.stateFormula)),
                        new HashSet<String>(),
                        formula.getActions()
                ));
    }

    private Not forAllNext(Next formula) {
        // ForAll(Next(q)) equals Not(ThereExists(Next(Not(enf(q)))))
        return NotThereExists(
                new Next(
                        new Not(translateENF(formula.stateFormula)),
                        formula.getActions()
                ));
    }

    private Not forAllEventually(Eventually formula) {
        // ForAll(EVENTUALLY(q)) equals Not(ThereExists(Always(Not(enf(q)))))
        return NotThereExists(
                new Always(
                        new Not(translateENF(formula.stateFormula)),
                        formula.getRightActions()
                ));
    }

    private And forAllUntil(Until formula) {
        // ForAll(q U p) equals
        // Not(ThereExists(Not(enf(q)) U (Not(enf(p)) AND Not(enf(q)))))
        // AND Not(ThereExists(Always(Not(enf(q)))))
        StateFormula enfQ = translateENF(formula.right);
        StateFormula enfP = translateENF(formula.left);

        StateFormula leftAnd = NotThereExists(
                new Until(
                        new Not(enfQ),
                        new And(
                                new Not(enfP),
                                new Not(enfQ)
                        ),
                        formula.getLeftActions(),
                        formula.getRightActions()
                ));

        StateFormula rightAnd = NotThereExists(
                new Always(
                        new Not(enfQ),
                        formula.getRightActions()
                ));


        return new And(leftAnd, rightAnd);
    }


    //
    // THERE EXISTS HELPER FUNCTIONS
    //
    private ThereExists thereExistsAlways(Always formula) {
        // ThereExists(Always(q)) = ThereExists(Always(enf(q)))
        return new ThereExists(
                new Always(
                        translateENF(formula.stateFormula),
                        formula.getActions()
                )
        );
    }

    private ThereExists thereExistsEventually(Eventually formula) {
        // ThereExists(EVENTUALLY(q)) = ThereExists( TRUE U enf(q))
        return new ThereExists(
                new Until(
                        new BoolProp(true),
                        translateENF(formula.stateFormula),
                        formula.getLeftActions(),
                        formula.getRightActions()
                )

        );
    }

    private ThereExists thereExistsNext(Next formula) {
        // ThereExists(Next(q)) = ThereExists(Next(enf(q)))
        return new ThereExists(
                new Next(
                        translateENF(formula.stateFormula),
                        formula.getActions()
                )
        );
    }

    private ThereExists thereExistsUntil(Until formula) {
        // ThereExists(q U p) = ThereExists(enf(q) U enf(p))
        return new ThereExists(
                new Until(
                        translateENF(formula.left),
                        translateENF(formula.right),
                        formula.getLeftActions(),
                        formula.getRightActions()
                )
        );
    }
}
