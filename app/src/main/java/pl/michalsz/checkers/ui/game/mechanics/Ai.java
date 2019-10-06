package pl.michalsz.checkers.ui.game.mechanics;

import java.util.LinkedList;
import java.util.PriorityQueue;

class Ai {

    private Move move;

    Ai(Board board){
        Board copyBoard = new Board(board, true);
        DecisionTree decisionTree = makeDecisionTree(copyBoard);
        move = selectMove(decisionTree);
    }

     Move getMove() {
        return move;
    }

    private DecisionTree makeDecisionTree(Board board) {
        board.possibleAction();
        LinkedList<Move> allMoves = allMoves(board.getAttackOption(),board.getRedPawns());
        DecisionTree mainTree = new DecisionTree(score(board), null);
        for (Move move : allMoves) {
            Board tmpBoard =  new Board(board, true);
            tmpBoard.possibleAction();
            Pawn pawn = move.getPawn();
            LinkedList<Pair> destination = move.getDestination();
            if(destination.size() > 1) {
                tmpBoard.attackAI(pawn, destination);
            } else {
                tmpBoard.movePawn(pawn, destination.get(0));
            }
            DecisionTree firstLayer = new DecisionTree(score(tmpBoard), move);
            tmpBoard.changeTurn();
            tmpBoard.possibleAction();
            LinkedList<Move> firstMoves = allMoves(tmpBoard.getAttackOption(), tmpBoard.getWhitePawns());
            for (Move move1 : firstMoves) {
                Board tmpBoard1 = new Board(tmpBoard, true);
                tmpBoard1.possibleAction();
                Pawn pawn1 = move1.getPawn();
                LinkedList<Pair> destination1 = move1.getDestination();
                if(destination1.size() > 1) {
                    tmpBoard1.attackAI(pawn1, destination1);
                } else {
                    tmpBoard1.movePawn(pawn1, destination1.get(0));
                }
                tmpBoard1.changeTurn();
                tmpBoard1.possibleAction();
                DecisionTree secondLayer = new DecisionTree(score(tmpBoard1), move1);
                LinkedList<Move> secondMoves = allMoves(tmpBoard1.getAttackOption(), tmpBoard1.getRedPawns());
                for (Move move2 : secondMoves) {
                    Board tmpBoard2 = new Board(tmpBoard1, true);
                    tmpBoard2.possibleAction();
                    Pawn pawn2 = move2.getPawn();
                    LinkedList<Pair> destination2 = move2.getDestination();
                    if(destination2.size() > 1) {
                        tmpBoard2.attackAI(pawn2, destination2);
                    } else {
                        tmpBoard2.movePawn(pawn2, destination2.get(0));
                    }

                    secondLayer.setChild(new DecisionTree(score(tmpBoard2), move2));
                }
                firstLayer.setChild(secondLayer);
            }
            mainTree.setChild(firstLayer);
        }
        return mainTree;
    }

    private LinkedList<Move> allMoves(final PriorityQueue<Pawn> attackOption, final LinkedList<Pawn> pawns) {
        LinkedList<Move> allMoves = new LinkedList<>();
        if (attackOption.size() > 0) {
            for (Pawn p : attackOption) {
                for (LinkedList<Pair> move : p.getPossibleAction()) {
                    allMoves.add(new Move(p, move));
                }
            }
        } else {
            for (Pawn pawn : pawns) {
                for (int j = 0; j < pawn.getAmountOfActions(); j++) {
                    allMoves.add(new Move(pawn, pawn.getPossibleAction().get(j)));
                }
            }
        }
        return allMoves;
    }

    private int score(Board board) {
        int count_own = 0;
        int count_opponent = 0;
        LinkedList<Pawn> own = board.getRedPawns();
        LinkedList<Pawn> opponents = board.getWhitePawns();
        for (Pawn pawn : own) {
            count_own++;
            if (pawn.isKing()) {
                    count_own+=2;
                }
            }
        for (Pawn pawn : opponents) {
            count_opponent++;
            if (pawn.isKing()) {
                count_opponent+=2;
            }
        }
        return count_own - count_opponent;
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
