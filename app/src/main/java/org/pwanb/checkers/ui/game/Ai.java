package org.pwanb.checkers.application;

import android.util.Log;

import java.util.LinkedList;

class Ai {
    private DecisionTree decisionTree;
    private Move move;

    Ai(Board board){
        Board copyBoard = new Board(board, true);
        decisionTree = makeDecisionTree(copyBoard);
        move = selectMove(decisionTree);
    }

     Move getMove() {
        return move;
    }

    private DecisionTree makeDecisionTree(Board board) {
        board.possibleAction();
        LinkedList<Move> allMoves = board.allMoves(board.getBlackPawns());
        DecisionTree mainTree = new DecisionTree(board, score(board), null, null);
        for (Move move : allMoves) {
            Board tmpBoard =  new Board(board, true);
            tmpBoard.possibleAction();
            Pawn pawn = move.getPawn();
            LinkedList<Pair> destination = move.getDestination();
            if(destination.size() > 1) {
                tmpBoard.attackAI(pawn, destination);
            } else {
                tmpBoard.move(pawn, destination.get(0));
            }
            DecisionTree firstLayer = new DecisionTree(tmpBoard, score(tmpBoard), move, null);
            tmpBoard.changeTurn();
            tmpBoard.possibleAction();
            LinkedList<Move> firstMoves = tmpBoard.allMoves(tmpBoard.getWhitePawns());
            for (Move move1 : firstMoves) {
                Board tmpBoard1 = new Board(tmpBoard, true);
                tmpBoard1.possibleAction();
                Pawn pawn1 = move1.getPawn();
                LinkedList<Pair> destination1 = move1.getDestination();
                if(destination1.size() > 1) {
                    tmpBoard1.attackAI(pawn1, destination1);
                } else {
                    tmpBoard1.move(pawn1, destination1.get(0));
                }
                tmpBoard1.changeTurn();
                tmpBoard1.possibleAction();
                DecisionTree secondLayer = new DecisionTree(tmpBoard1, score(tmpBoard1), move1, null);
                LinkedList<Move> secondMoves = tmpBoard1.allMoves(tmpBoard1.getBlackPawns());
                for (Move move2 : secondMoves) {
                    Board tmpBoard2 = new Board(tmpBoard1, true);
                    tmpBoard2.possibleAction();
                    Pawn pawn2 = move2.getPawn();
                    LinkedList<Pair> destination2 = move2.getDestination();
                    if(destination2.size() > 1) {
                        tmpBoard2.attackAI(pawn2, destination2);
                    } else {
                        tmpBoard2.move(pawn2, destination2.get(0));
                    }

                    secondLayer.setChild(new DecisionTree(tmpBoard2, score(tmpBoard2), move2, null));
                }
                firstLayer.setChild(secondLayer);
            }
            mainTree.setChild(firstLayer);
        }
        return mainTree;
    }

    private int score(Board board) {
        int count_color = 0;
        int count_opponent = 0;
        Pawn[] color = board.getBlackPawns();
        Pawn[] opponent = board.getWhitePawns();
        for (int i = 0 ; i < 12 ; i++) {
            if (color[i]!=null) {
                count_color++;
                if (color[i].isKing()) {
                    count_color+=2;
                }
            }

            if (opponent[i]!=null) {
                count_opponent++;
                if (opponent[i].isKing()) {
                    count_opponent += 2;
                }
            }

        }
        return count_color - count_opponent;
    }

    private Move selectMove(DecisionTree decisionTree) {
        int max = -20;
        Move selectedMove = null;
        for (DecisionTree child : decisionTree.getChildren()) {
            int min1 = 20;
            for (DecisionTree child1 : child.getChildren()) {
                int max1 = -20;
                for (DecisionTree child2 : child1.getChildren()) {
                    if (child2.getScore() >= max) {
                        max1 = child2.getScore();
                    }
                }
                child1.setScore(max1);
                if (child1.getScore() <= min1) {
                    min1 = child1.getScore();
                }
            }
            child.setScore(min1);
            if (child.getScore() >= max) {
                max = child.getScore();
                selectedMove = child.getMove();
            }
        }
        return selectedMove;
    }
}
