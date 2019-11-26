package pl.michalsz.checkers.ui.game.mechanics;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

class Ai {

    private Move move;

    Ai(Board board) {
        Board copyBoard = new Board(board, true); //zobaczc czy dizala poprawnie po refactorze
        DecisionTree decisionTree = makeDecisionTree(copyBoard);
        move = selectMove(decisionTree);
    }

    Move getMove() {
        return move;
    }

    private DecisionTree makeDecisionTree(Board board) {
        board.possibleAction();
        LinkedList<Move> allMoves = allMoves(board.getAttackOption(), board.getRedPawns());
        DecisionTree mainTree = new DecisionTree(score(board), null);
        for (Move move : allMoves) {
            Board tmpBoard = new Board(board, true);
            tmpBoard.possibleAction();
            Pawn pawn = move.getPawn();
            tmpBoard.actionAI(move);
            DecisionTree firstLayer = new DecisionTree(score(tmpBoard), move);
            tmpBoard.changeTurn();
            tmpBoard.possibleAction();
            LinkedList<Move> firstMoves = allMoves(tmpBoard.getAttackOption(), tmpBoard.getWhitePawns());
            for (Move move1 : firstMoves) {
                Board tmpBoard1 = new Board(tmpBoard, true);
                tmpBoard1.possibleAction();
                Pawn pawn1 = move1.getPawn();
                tmpBoard1.actionAI(move1);
                tmpBoard1.changeTurn();
                tmpBoard1.possibleAction();
                DecisionTree secondLayer = new DecisionTree(score(tmpBoard1), move1);
                LinkedList<Move> secondMoves = allMoves(tmpBoard1.getAttackOption(), tmpBoard1.getRedPawns());
                for (Move move2 : secondMoves) {
                    Board tmpBoard2 = new Board(tmpBoard1, true);
                    tmpBoard2.possibleAction();
                    Pawn pawn2 = move2.getPawn();
                    tmpBoard2.actionAI(move2);
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
        List<Pawn> pawnList;
        if (attackOption.size() > 0)
            pawnList = new LinkedList<>(attackOption);
        else
            pawnList = pawns;

        for (Pawn pawn : pawnList) {
            for (LinkedList<Pair> move : pawn.getPossibleAction()) {
                allMoves.add(new Move(pawn, move));
            }
        }

        return allMoves;
    }

    private int score(Board board) {
        int redScore = 0;
        int whiteScore = 0;
        for (Pawn pawn : board.getRedPawns()) {
            redScore++;
            if (pawn.isKing()) {
                redScore += 2;
            }
        }
        for (Pawn pawn : board.getWhitePawns()) {
            whiteScore++;
            if (pawn.isKing()) {
                whiteScore += 2;
            }
        }
        if (board.isWhiteTurn())
            return whiteScore - redScore;
        else
            return redScore - whiteScore;
    }

    private Move selectMove(DecisionTree decisionTree) {
        int maxMainScore = -20;
        Move selectedMove = null;
        for (DecisionTree firstLayer : decisionTree.getChildren()) {

            int maxFirstLayerScore = -20;
            for (DecisionTree secondLayer : firstLayer.getChildren()) {

                int maxSecondLayerScore = -20;
                for (DecisionTree thirdLayer : secondLayer.getChildren()) {
                    if (thirdLayer.getScore() >= maxSecondLayerScore) {
                        maxSecondLayerScore = thirdLayer.getScore();
                    }
                }

                secondLayer.setScore(maxSecondLayerScore);
                if (secondLayer.getScore() >= maxFirstLayerScore) {
                    maxFirstLayerScore = secondLayer.getScore();
                }
            }

            firstLayer.setScore(maxFirstLayerScore);
            if (firstLayer.getScore() >= maxMainScore) {
                maxMainScore = firstLayer.getScore();
                selectedMove = firstLayer.getMove();
            }
        }
        return selectedMove;
    }


}
