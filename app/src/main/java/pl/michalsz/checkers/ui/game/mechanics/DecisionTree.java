package pl.michalsz.checkers.ui.game.mechanics;

import java.util.LinkedList;


class DecisionTree {
    private int score;
    private LinkedList<DecisionTree> children;
    private Move move;

    DecisionTree(int score, Move move) {
        this.score = score;
        this.children = new LinkedList<>();
        this.move = move;
    }

    Move getMove() {
        return move;
    }

    int getScore() {
        return score;
    }

    LinkedList<DecisionTree> getChildren() {
        return children;
    }

    void setScore(int score) {
        this.score = score;
    }

    void setChild(DecisionTree child) {
        children.add(child);
    }
}



