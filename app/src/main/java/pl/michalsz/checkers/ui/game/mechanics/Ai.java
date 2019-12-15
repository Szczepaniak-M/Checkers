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

    private DecisionTree makeDecisionTree(Board startBoard) {
        startBoard.possibleAction();
        LinkedList<Move> firstMoves = allMoves(startBoard.getAttackOption(), startBoard.getRedPawns());
        DecisionTree mainTree = new DecisionTree(score(startBoard), null);
        for (Move firstMove : firstMoves) {
            Board firstMoveBoard = makeBoardAfterMove(startBoard, firstMove, true);
            DecisionTree firstLayer = new DecisionTree(score(firstMoveBoard), firstMove);
            LinkedList<Move> secondMoves = allMoves(firstMoveBoard.getAttackOption(), firstMoveBoard.getWhitePawns());
            for (Move secondMove : secondMoves) {
                Board secondMoveBoard = makeBoardAfterMove(firstMoveBoard, secondMove, true);
                DecisionTree secondLayer = new DecisionTree(score(secondMoveBoard), secondMove);
                LinkedList<Move> thirdMoves = allMoves(secondMoveBoard.getAttackOption(), secondMoveBoard.getRedPawns());
                for (Move thirdMove : thirdMoves) {
                    Board thirdMoveBoard = makeBoardAfterMove(secondMoveBoard, thirdMove, false);
                    secondLayer.setChild(new DecisionTree(score(thirdMoveBoard), thirdMove));
                }
                firstLayer.setChild(secondLayer);
            }
            mainTree.setChild(firstLayer);
        }
        return mainTree;
    }

    private Board makeBoardAfterMove(Board board, Move move, boolean updateAction) {
        Board newBoard = new Board(board, true);
        newBoard.possibleAction();
        newBoard.actionAI(move);
        newBoard.changeTurn();
        if (updateAction)
            newBoard.possibleAction();
        return newBoard;
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
            return redScore - whiteScore;
        else
            return whiteScore - redScore;

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
