package modelChecker;

import formula.stateFormula.StateFormula;
import model.Model;

/**
 * Interface for model checkers.
 */
public interface ModelChecker {
    /**
     * Verification of Query given a Model and Constraint
     *
     * @param model model used
     * @param constraint constraint posed on model
     * @param query a state formula to validate
     * @return boolean, true if query was satisfied
     */
    boolean check(Model model, StateFormula constraint, StateFormula query);

    // Returns a trace of the previous check attempt if it failed.
    String[] getTrace();
}