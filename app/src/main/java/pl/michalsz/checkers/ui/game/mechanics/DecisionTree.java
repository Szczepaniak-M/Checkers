package pl.michalsz.checkers.ui.game.mechanics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DecisionTree {
    private Board board;
    private int score;
    private ArrayList<DecisionTree> children;
    private Move move;

    DecisionTree(Board board, int score, Move move, DecisionTree... children) {
        this.board = board;
        this.score = score;
        if (children != null){
            this.children = new ArrayList<>(Arrays.asList(children));
        }else
            this.children = new ArrayList<>();

        this.move = move;
    }

    public Board getBoard() {
        return board;
    }

    public Move getMove() {
        return move;
    }

    int getScore() {
        return score;
    }

    List<DecisionTree> getChildren() {
        return children;
    }

    void setScore(int newValue) {
        score = newValue;
    }

    public void setChildren(DecisionTree ... children) {
        for (DecisionTree i : children) {
            setChild(i);
        }
    }

    void setChild(DecisionTree child) {
        children.add(child);
    }

    public int getNumberChildren() {
        return children.size();
    }
}



