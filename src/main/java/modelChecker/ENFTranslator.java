package modelChecker;

import formula.pathFormula.Always;
import formula.pathFormula.Eventually;
import formula.pathFormula.Next;
import formula.pathFormula.Until;
import formula.stateFormula.*;
import model.State;

import java.util.HashSet;

public class ENFTranslator {

    public StateFormula parseENF(StateFormula formula) {
        String formula_type = formula.getClass().getName().substring(formula.getClass().getName().lastIndexOf('.') + 1);
        switch (formula_type) {
            case "AtomicProp": 	return formula;
            case "And":			return parseAnd((And) formula);
            case "BoolProp":	return parseBoolProp((BoolProp) formula);
            case "ForAll":		return parseForAll((ForAll) formula);
            case "Not":			return parseNot((Not) formula);
            case "Or": 			return parseOr((Or) formula);
            case "ThereExists":	return parseThereExists((ThereExists) formula);
            default: 			return null;
        }
    }

    /**
     * ENF formula for AND CTL
     * @param formula
     * @return
     */
    private StateFormula parseAnd(And formula) {
        // P and Q = enf(P) AND enf(Q)
        return new And(
                parseENF(formula.left),
                parseENF(formula.right)
        );
    }

    /**
     * ENF formula for BoolProp CTL
     * @param formula
     * @return
     */
    private StateFormula parseBoolProp(BoolProp formula) {
        // true = true, false = Not(true)
        return (formula.value) ? formula : new Not(new BoolProp(true));
    }

    /**
     * ENF formula for ForAll CTL
     * @param formula
     * @return
     */
    private StateFormula parseForAll(ForAll formula) {
        String path_formula_name = formula.pathFormula.getClass().getName().substring(formula.pathFormula.getClass().getName().lastIndexOf('.') + 1);
        switch(path_formula_name) {
            // ForAll(Always(P)) = Not(ThereExists(TRUE U Not(enf(P))))
            case "Always":
                Always alwaysFormula = (Always) formula.pathFormula;
                return new Not(
                        new ThereExists(
                               new Until(
                                       new BoolProp(true),
                                       new Not(parseENF(alwaysFormula.stateFormula)),
                                       new HashSet<String>(),
                                       alwaysFormula.getActions()
                               )
                        )
                );

            // ForAll(Eventually(P)) = Not(ThereExists(Always(Not(enf(P)))))
            case "Eventually":
                Eventually eventuallyFormula = (Eventually) formula.pathFormula;
                return new Not(
                        new ThereExists(
                                new Always(
                                        new Not(parseENF(eventuallyFormula.stateFormula)),
                                        eventuallyFormula.getRightActions()
                                )
                        )
                );

            // ForAll(Next(P)) = Not(ThereExists(Next(Not(enf(P)))))
            case "Next":
                Next nextFormula = (Next) formula.pathFormula;
                return new Not(
                        new ThereExists(
                                new Next(
                                        new Not(parseENF(nextFormula.stateFormula)),
                                        nextFormula.getActions()
                                )
                        )
                );

            /** ForAll(P U Q) =
             * Not(ThereExists(Not(enf(Q)) U (Not(enf(P)) AND Not(enf(Q)))))
             * AND
             * Not(ThereExists(Always(Not(enf(Q)))))
             */
            case "Until":
                Until untilFormula = (Until) formula.pathFormula;
                StateFormula enfP = parseENF(untilFormula.left);
                StateFormula enfQ = parseENF(untilFormula.right);

                StateFormula rightFinalAnd = new Not(
                        new ThereExists(
                                new Always(
                                        new Not(enfQ),
                                        untilFormula.getRightActions()
                                )
                        )
                );

                StateFormula leftFinalAnd = new Not(
                        new ThereExists(
                                new Until(
                                        new Not(enfQ),
                                        new And(
                                                new Not(enfP),
                                                new Not(enfQ)
                                        ),
                                        untilFormula.getLeftActions(),
                                        untilFormula.getRightActions()
                                )
                        )
                );
                return new And(leftFinalAnd, rightFinalAnd);

            default: return null;
        }

    }

    /**
     * ENF formula for NOT CTL
     * @param formula
     * @return
     */
    private StateFormula parseNot(Not formula) {
        // Not(P) = Not(enf(P))
        return new Not(
                parseENF(formula.stateFormula)
        );
    }

    /**
     * ENF formula for OR CTL
     * @param formula
     * @return
     */
    private StateFormula parseOr(Or formula) {
        // P or Q = Not(Not(enf(P)) AND Not(enf(Q)))
        return new Not(
                new And(
                        new Not(parseENF(formula.left)),
                        new Not(parseENF(formula.right))
                )
        );
    }


    /**
     * ENF formula for ThereExists CTL
     * @param formula
     * @return
     */
    private StateFormula parseThereExists(ThereExists formula) {
        String path_formula_name = formula.pathFormula.getClass().getName().substring(formula.pathFormula.getClass().getName().lastIndexOf('.') + 1);
        switch (path_formula_name) {

            // ThereExists(Always(P)) = ThereExists(Always(enf(P)))
            case "Always":
                Always alwaysFormula = (Always) formula.pathFormula;
                return new ThereExists(
                        new Always(
                                parseENF(alwaysFormula.stateFormula),
                                alwaysFormula.getActions()
                        )
                );

            // ThereExists(Eventually(P)) = ThereExists( TRUE U enf(P))
            case "Eventually":
                Eventually eventuallyFormula = (Eventually) formula.pathFormula;
                return new ThereExists(
                        new Until(
                                new BoolProp(true),
                                parseENF(eventuallyFormula.stateFormula),
                                eventuallyFormula.getLeftActions(),
                                eventuallyFormula.getRightActions()
                        )

                );

            // ThereExists(Next(P)) = ThereExists(Next(enf(P)))
            case "Next":
                Next nextFormula = (Next) formula.pathFormula;
                return new ThereExists(
                        new Next(
                                parseENF(nextFormula.stateFormula),
                                nextFormula.getActions()
                        )
                );

            // ThereExists(P U Q) = ThereExists(enf(P) U enf(Q))
            case "Until":
                Until untilFormula = (Until) formula.pathFormula;
                return new ThereExists(
                        new Until(
                                parseENF(untilFormula.left),
                                parseENF(untilFormula.right),
                                untilFormula.getLeftActions(),
                                untilFormula.getRightActions()
                        )
                );

            default: return null;
        }
    }

}
