package modelChecker;

import formula.stateFormula.StateFormula;
import model.Model;
import model.State;

import java.util.ArrayList;

public class Tree {

    Tree leftNode;
    Tree rightNode;
    Tree parent;
    boolean isRoot;
    boolean isLeaf;

    StateFormula stateFormula;

    public Tree (StateFormula formula, boolean isLeaf) {
        this.stateFormula = formula;
        this.isLeaf = isLeaf;
    }

    public Tree getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(Tree leftNode) {
        this.leftNode = leftNode;
    }

    public Tree getRightNode() {
        return rightNode;
    }

    public void setRightNode(Tree rightNode) {
        this.rightNode = rightNode;
    }

    public Tree getParent() {
        return parent;
    }

    public void setParent(Tree parent) {
        this.parent = parent;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean root) {
        isRoot = root;
    }
}
