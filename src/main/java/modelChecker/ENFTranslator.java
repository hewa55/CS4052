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
        String formula_type = formula.getClass().getName().substring(formula.getClass().getName().lastIndexOf('.') + 1);
        switch (formula_type) {
            case ATOMIC: 	return formula;
            case AND:			return parseAnd((And) formula);
            case BOOL:	return parseBoolProp((BoolProp) formula);
            case FOR_ALL:		return parseForAll((ForAll) formula);
            case NOT:			return parseNot((Not) formula);
            case OR: 			return parseOr((Or) formula);
            case THERE_EXISTS:	return parseThereExists((ThereExists) formula);
            default: 			return null;
        }
    }

    /**
     * ENF parser AND CTL
     * @param formula
     * @return StateFormula
     */
    private StateFormula parseAnd(And formula) {
        // P and Q = enf(P) AND enf(Q)
        return new And(
                parseENF(formula.left),
                parseENF(formula.right)
        );
    }

    /**
     * ENF parser BoolProp CTL
     * @param formula
     * @return StateFormula
     */
    private StateFormula parseBoolProp(BoolProp formula) {
        // true = true, false = Not(true)
        if(formula.value){
            return formula;
        }else{
            return new Not(new BoolProp(true));
        }
    }

    private String obtainPathFormulaName(ForAll formula){
        // extracts name of path formula
        return formula.pathFormula.getClass().getName().substring(formula.pathFormula.getClass().getName().lastIndexOf('.') + 1);
    }

    private Not NotThereExists(PathFormula formula){
        return new Not(new ThereExists(formula));
    }
    /**
     * ENF parsper ForAll CTL
     * @param formula
     * @return
     */
    private StateFormula parseForAll(ForAll formula) {
        String path_formula_name = obtainPathFormulaName(formula);
        switch(path_formula_name) {
            // ForAll(Always(P)) = Not(ThereExists(TRUE U Not(enf(P))))
            case ALWAYS:
                Always alwaysFormula = (Always) formula.pathFormula;
                return NotThereExists(
                               new Until(
                                       new BoolProp(true),
                                       new Not(parseENF(alwaysFormula.stateFormula)),
                                       new HashSet<String>(),
                                       alwaysFormula.getActions()
                               ));


            // ForAll(EVENTUALLY(P)) = Not(ThereExists(Always(Not(enf(P)))))
            case EVENTUALLY:
                Eventually eventuallyFormula = (Eventually) formula.pathFormula;
                return NotThereExists(
                                new Always(
                                        new Not(parseENF(eventuallyFormula.stateFormula)),
                                        eventuallyFormula.getRightActions()
                                ));

            // ForAll(Next(P)) = Not(ThereExists(Next(Not(enf(P)))))
            case NEXT:
                Next nextFormula = (Next) formula.pathFormula;
                return NotThereExists(
                                new Next(
                                        new Not(parseENF(nextFormula.stateFormula)),
                                        nextFormula.getActions()
                                ));

             // ForAll(P U Q) =
             // Not(ThereExists(Not(enf(Q)) U (Not(enf(P)) AND Not(enf(Q)))))
             // AND
             // Not(ThereExists(Always(Not(enf(Q)))))
             //
            case UNTIL:
                Until untilFormula = (Until) formula.pathFormula;
                StateFormula enfP = parseENF(untilFormula.left);
                StateFormula enfQ = parseENF(untilFormula.right);

                StateFormula rightFinalAnd = NotThereExists(
                                new Always(
                                        new Not(enfQ),
                                        untilFormula.getRightActions()
                                ));

                StateFormula leftFinalAnd = NotThereExists(
                                new Until(
                                        new Not(enfQ),
                                        new And(
                                                new Not(enfP),
                                                new Not(enfQ)
                                        ),
                                        untilFormula.getLeftActions(),
                                        untilFormula.getRightActions()
                                ));
                return new And(leftFinalAnd, rightFinalAnd);

            default: return null;
        }

    }

    /**
     * ENF parser NOT CTL
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
     * ENF parser OR CTL
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
     * ENF parser ThereExists CTL
     * @param formula
     * @return
     */
    private StateFormula parseThereExists(ThereExists formula) {
        String path_formula_name = formula.pathFormula.getClass().getName().substring(formula.pathFormula.getClass().getName().lastIndexOf('.') + 1);
        switch (path_formula_name) {

            // ThereExists(Always(P)) = ThereExists(Always(enf(P)))
            case ALWAYS:
                Always alwaysFormula = (Always) formula.pathFormula;
                return new ThereExists(
                        new Always(
                                parseENF(alwaysFormula.stateFormula),
                                alwaysFormula.getActions()
                        )
                );

            // ThereExists(EVENTUALLY(P)) = ThereExists( TRUE U enf(P))
            case EVENTUALLY:
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
            case NEXT:
                Next nextFormula = (Next) formula.pathFormula;
                return new ThereExists(
                        new Next(
                                parseENF(nextFormula.stateFormula),
                                nextFormula.getActions()
                        )
                );

            // ThereExists(P U Q) = ThereExists(enf(P) U enf(Q))
            case UNTIL:
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
