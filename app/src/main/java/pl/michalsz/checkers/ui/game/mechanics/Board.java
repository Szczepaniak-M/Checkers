package pl.michalsz.checkers.ui.game.mechanics;


import android.app.Activity;
import android.widget.ImageView;


import java.util.ArrayList;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import pl.michalsz.checkers.R;


public class Board {
    private boolean whiteTurn;
    private Pair chosenField;
    private Field[][] board = new Field[8][8];
    private Activity activity;
    private LinkedList<Pawn> whitePawns;
    private LinkedList<Pawn> redPawns;
    private PriorityQueue<Pawn> attackOption;
    private Queue<Pair> highlightsFields;
    private int drawWhite;
    private int drawRed;
    private boolean isCopy;
    private boolean whitePlayer;
    private boolean redPlayer;


    public Board(ImageView[][] boardMain, Activity activity) {
        isCopy = false;
        chosenField = new Pair();
        this.activity = activity;
        redPawns = new LinkedList<>();
        whitePawns = new LinkedList<>();
        attackOption = new PriorityQueue<>();
        highlightsFields = new LinkedList<>();
        drawWhite = 0;
        drawRed = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i % 2 == j % 2) {
                    board[i][j] = new Field(this, i, j, boardMain[i][j]);
                }
            }
        }
    }

    Board(Board oldBoard, boolean isCopy) {
        whiteTurn = oldBoard.whiteTurn;
        chosenField = new Pair();
        drawWhite = 0;
        drawRed = 0;
        this.isCopy = isCopy;
        this.activity = oldBoard.activity;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i % 2 == j % 2)
                    board[i][j] = new Field(oldBoard.board[i][j], this);
            }
        }
        for (Pawn pawn : oldBoard.whitePawns) {
            whitePawns.add(new Pawn(pawn));
        }
        for (Pawn pawn : oldBoard.redPawns) {
            redPawns.add(new Pawn(pawn));
        }
        attackOption = new PriorityQueue<>();
        highlightsFields = new LinkedList<>();
    }

    Field getField(Pair position) {
        return board[position.getX()][position.getY()];
    }

    Pawn getPawn(Pair position) {
        return board[position.getX()][position.getY()].getPawn();
    }

    Pair getChosenField() {
        return chosenField;
    }

    LinkedList<Pawn> getWhitePawns() {
        return whitePawns;
    }

    LinkedList<Pawn> getRedPawns() {
        return redPawns;
    }

    PriorityQueue<Pawn> getAttackOption() {
        return attackOption;
    }

    int getDrawRed() {
        return drawRed;
    }

    int getDrawWhite() {
        return drawWhite;
    }

    void addDrawWhite() {
        drawWhite++;
    }

    void setDrawWhite() {
        this.drawWhite = 0;
    }

    void addDrawRed() {
        drawRed++;
    }

    void setDrawRed() {
        this.drawRed = 0;
    }

    void addAttackOption(Pawn pawn) {
        attackOption.add(pawn);
    }

    void deleteAttackOption() {
        attackOption.clear();
    }

    boolean isWhiteTurn() {
        return whiteTurn;
    }

    void setHighlight(Field field, int id) {
        field.setHighlight(activity, id);
        highlightsFields.add(field.getPosition());
    }

    void deleteHighlightBoard() {
        System.out.println(highlightsFields);
        Pair highlight;
        int x, y;
        while (highlightsFields.peek() != null) {
            highlight = highlightsFields.poll();
            x = highlight.getX();
            y = highlight.getY();
            board[x][y].deleteHighlightField();
        }
    }

    public void start() {
        whiteTurn = true;
        for (int i = 0; i < 4; i++) {
            board[2 * i][0].setPawn(2 * i, 0, true, PawnGraphics.WHITE_PAWN.get());
            board[2 * i + 1][1].setPawn(2 * i + 1, 1, true, PawnGraphics.WHITE_PAWN.get());
            board[2 * i][2].setPawn(2 * i, 2, true, PawnGraphics.WHITE_PAWN.get());
            board[2 * i + 1][5].setPawn(2 * i + 1, 5, false, PawnGraphics.BLACK_PAWN.get());
            board[2 * i][6].setPawn(2 * i, 6, false, PawnGraphics.BLACK_PAWN.get());
            board[2 * i + 1][7].setPawn(2 * i + 1, 7, false, PawnGraphics.BLACK_PAWN.get());
            whitePawns.add(board[2 * i][0].getPawn());
            whitePawns.add(board[2 * i + 1][1].getPawn());
            whitePawns.add(board[2 * i][2].getPawn());
            redPawns.add(board[2 * i + 1][5].getPawn());
            redPawns.add(board[2 * i][6].getPawn());
            redPawns.add(board[2 * i + 1][7].getPawn());
        }
        possibleAction();
    }

    void possibleAction() {
        if (whiteTurn) {
            searchAttack(whitePawns);
            if (attackOption.size() > 0) {
                showAttackOption();

            } else {
                searchMove(whitePawns);
            }
        } else {
            searchAttack(redPawns);
            if (attackOption.size() > 0)
                showAttackOption();
            else
                searchMove(redPawns);
        }

    }

    private void searchMove(LinkedList<Pawn> pawns) {
        boolean isMove = false;
        int x, y;
        for (Pawn pawn : pawns) {
            pawn.setPossibleActionEmpty();
            x = pawn.getCurrentPosition().getX();
            y = pawn.getCurrentPosition().getY();
            if (pawn.isKing()) {
                int j = 1;
                while (x + j < 8 && y + j < 8 && board[x + j][y + j].getPawn() == null) {
                    pawn.setPossibleMove(new Pair(x + j, y + j));
                    j++;
                }

                j = 1;
                while (x - j > -1 && y + j < 8 && board[x - j][y + j].getPawn() == null) {
                    pawn.setPossibleMove(new Pair(x - j, y + j));
                    j++;
                }

                j = 1;
                while (x - j > -1 && y - j > -1 && board[x - j][y - j].getPawn() == null) {
                    pawn.setPossibleMove(new Pair(x - j, y - j));
                    j++;
                }

                j = 1;
                while (x + j < 8 && y - j > -1 && board[x + j][y - j].getPawn() == null) {
                    pawn.setPossibleMove(new Pair(x + j, y - j));
                    j++;
                }
            } else {
                if (whiteTurn) {
                    if (y < 7) {
                        if (x < 7 && board[x + 1][y + 1].getPawn() == null) {
                            pawn.setPossibleMove(new Pair(x + 1, y + 1));
                        }
                        if (x > 0 && board[x - 1][y + 1].getPawn() == null) {
                            pawn.setPossibleMove(new Pair(x - 1, y + 1));
                        }
                    }
                } else {
                    if (y > 0) {
                        if (x < 7 && board[x + 1][y - 1].getPawn() == null) {
                            pawn.setPossibleMove(new Pair(x + 1, y - 1));
                        }
                        if (x > 0 && board[x - 1][y - 1].getPawn() == null) {
                            pawn.setPossibleMove(new Pair(x - 1, y - 1));
                        }
                    }

                }
                if (pawn.getAmountOfActions() > 0)
                    isMove = true;
            }
        }
        if (!isMove) {
            if (whiteTurn) {
                end(-1);
            } else {
                end(1);
            }
        }

    }


    private void searchAttack(LinkedList<Pawn> pawns) {
        boolean empty = true;
        int priority;
        for (Pawn pawn : pawns) {
            empty = false;
            pawn.setPossibleActionEmpty();
            if (pawn.isKing())
                priority = pawn.setPossibleAttack(possibleAttackKing(pawn.getCurrentPosition(), new LinkedList<Pair>()));
            else
                priority = pawn.setPossibleAttack(possibleAttack(pawn.getCurrentPosition(), new LinkedList<Pair>()));

            if (priority > 1) {
                attackOption.add(pawn);
            } else {
                pawn.setPossibleActionEmpty();
            }
        }
        if (empty) {
            if (whiteTurn) {
                end(-1);
            } else {
                end(1);
            }
        }
    }

    private LinkedList<LinkedList<Pair>> possibleAttack(Pair pawn, LinkedList<Pair> deleted) {
        int x = pawn.getX();
        int y = pawn.getY();
        LinkedList<LinkedList<Pair>> help, outPossibleAttack = null;
        if (y < 6) {
            if (x < 6 && board[x + 1][y + 1].getPawn() != null && board[x + 1][y + 1].getPawn().isWhite() != whiteTurn
                    && board[x + 2][y + 2].getPawn() == null && !deleted.contains(new Pair(x + 1, y + 1))) {
                deleted.addLast(new Pair(x + 1, y + 1));
                outPossibleAttack = possibleAttack(new Pair(x + 2, y + 2), deleted);
                deleted.removeLast();
            }

            if (x > 1 && board[x - 1][y + 1].getPawn() != null && board[x - 1][y + 1].getPawn().isWhite() != whiteTurn
                    && board[x - 2][y + 2].getPawn() == null && !deleted.contains(new Pair(x - 1, y + 1))) {
                deleted.addLast(new Pair(x - 1, y + 1));
                help = possibleAttack(new Pair(x - 2, y + 2), deleted);
                deleted.removeLast();
                if (outPossibleAttack != null) {
                    if (outPossibleAttack.get(0).size() == help.get(0).size()) {
                        outPossibleAttack.addAll(help);
                    } else if (outPossibleAttack.get(0).size() < help.get(0).size()) {
                        outPossibleAttack = help;
                    }
                } else {
                    outPossibleAttack = help;
                }
            }
        }

        if (y > 1) {
            if (x > 1 && board[x - 1][y - 1].getPawn() != null && board[x - 1][y - 1].getPawn().isWhite() != whiteTurn
                    && board[x - 2][y - 2].getPawn() == null && !deleted.contains(new Pair(x - 1, y - 1))) {
                deleted.addLast(new Pair(x - 1, y - 1));
                help = possibleAttack(new Pair(x - 2, y - 2), deleted);
                deleted.removeLast();
                if (outPossibleAttack != null) {
                    if (outPossibleAttack.get(0).size() == help.get(0).size()) {
                        outPossibleAttack.addAll(help);
                    } else if (outPossibleAttack.get(0).size() < help.get(0).size()) {
                        outPossibleAttack = help;
                    }
                } else {
                    outPossibleAttack = help;
                }
            }

            if (x < 6 && board[x + 1][y - 1].getPawn() != null && board[x + 1][y - 1].getPawn().isWhite() != whiteTurn
                    && board[x + 2][y - 2].getPawn() == null && !deleted.contains(new Pair(x + 1, y - 1))) {
                deleted.addLast(new Pair(x + 1, y - 1));
                help = possibleAttack(new Pair(x + 2, y - 2), deleted);
                deleted.removeLast();
                if (outPossibleAttack != null) {
                    if (outPossibleAttack.get(0).size() == help.get(0).size()) {
                        outPossibleAttack.addAll(help);
                    } else if (outPossibleAttack.get(0).size() < help.get(0).size()) {
                        outPossibleAttack = help;
                    }
                } else {
                    outPossibleAttack = help;
                }
            }

        }
        if (outPossibleAttack == null) {
            outPossibleAttack = new LinkedList<>();
            outPossibleAttack.add(new LinkedList<Pair>());
        }
        for (int i = 0; i < outPossibleAttack.size(); i++)
            outPossibleAttack.get(i).addFirst(pawn);
        return outPossibleAttack;
    }

    private LinkedList<LinkedList<Pair>> possibleAttackKing(Pair pawn, LinkedList<Pair> deleted) {
        int x = pawn.getX();
        int y = pawn.getY();
        int i = 1, j;
        LinkedList<LinkedList<Pair>> help, outPossibleAttack = null;

        while (x + i < 7 && y + i < 7) {
            if (board[x + i][y + i].getPawn() != null) {
                if (board[x + i][y + i].getPawn().isWhite() != whiteTurn
                        && board[x + i + 1][y + i + 1].getPawn() == null
                        && !deleted.contains(new Pair(x + i, y + i))) {
                    deleted.addLast(new Pair(x + i, y + i));
                    j = 1;
                    while (x + i + j < 8 && y + i + j < 8 && board[x + i + j][y + i + j].getPawn() == null) {
                        help = possibleAttackKing(new Pair(x + i + j, y + i + j), deleted);
                        if (outPossibleAttack != null) {
                            if (outPossibleAttack.get(0).size() == help.get(0).size())
                                outPossibleAttack.addAll(help);
                            else if (outPossibleAttack.get(0).size() < help.get(0).size())
                                outPossibleAttack = help;
                        } else
                            outPossibleAttack = help;
                        j++;
                    }
                    deleted.removeLast();
                }
                break;
            }
            i++;
        }

        i = 1;
        while (x - i > 0 && y + i < 7) {
            if (board[x - i][y + i].getPawn() != null) {
                if (board[x - i][y + i].getPawn().isWhite() != whiteTurn
                        && board[x - i - 1][y + i + 1].getPawn() == null
                        && !deleted.contains(new Pair(x - i, y + i))) {
                    deleted.addLast(new Pair(x - i, y + i));
                    j = 1;
                    while (x - i - j > -1 && y + i + j < 8 && board[x - i - j][y + i + j].getPawn() == null) {
                        help = possibleAttackKing(new Pair(x - i - j, y + i + j), deleted);
                        if (outPossibleAttack != null) {
                            if (outPossibleAttack.get(0).size() == help.get(0).size())
                                outPossibleAttack.addAll(help);
                            else if (outPossibleAttack.get(0).size() < help.get(0).size())
                                outPossibleAttack = help;
                        } else
                            outPossibleAttack = help;
                        j++;
                    }
                    deleted.removeLast();
                }
                break;
            }
            i++;
        }

        i = 1;
        while (x - i > 0 && y - i > 0) {
            if (board[x - i][y - i].getPawn() != null) {
                if (board[x - i][y - i].getPawn().isWhite() != whiteTurn
                        && board[x - i - 1][y - i - 1].getPawn() == null
                        && !deleted.contains(new Pair(x - i, y - i))) {
                    deleted.addLast(new Pair(x - i, y - i));
                    j = 1;
                    while (x - i - j > -1 && y - i - j > -1 && board[x - i - j][y - i - j].getPawn() == null) {
                        help = possibleAttackKing(new Pair(x - i - j, y - i - j), deleted);
                        if (outPossibleAttack != null) {
                            if (outPossibleAttack.get(0).size() == help.get(0).size())
                                outPossibleAttack.addAll(help);
                            else if (outPossibleAttack.get(0).size() < help.get(0).size())
                                outPossibleAttack = help;
                        } else
                            outPossibleAttack = help;
                        j++;
                    }
                    deleted.removeLast();

                }
                break;
            }
            i++;
        }

        i = 1;
        while (x + i < 7 && y - i > 0) {
            if (board[x + i][y - i].getPawn() != null) {
                if (board[x + i][y - i].getPawn().isWhite() != whiteTurn
                        && board[x + i + 1][y - i - 1].getPawn() == null
                        && !deleted.contains(new Pair(x + i, y - i))) {
                    deleted.addLast(new Pair(x + i, y - i));
                    j = 1;
                    while (x + i + j < 8 && y - i - j > -1 && board[x + i + j][y - i - j].getPawn() == null) {
                        help = possibleAttackKing(new Pair(x + i + j, y - i - j), deleted);
                        if (outPossibleAttack != null) {
                            if (outPossibleAttack.get(0).size() == help.get(0).size())
                                outPossibleAttack.addAll(help);
                            else if (outPossibleAttack.get(0).size() < help.get(0).size())
                                outPossibleAttack = help;
                        } else
                            outPossibleAttack = help;
                        j++;
                    }
                    deleted.removeLast();
                }
                break;
            }
            i++;
        }

        if (outPossibleAttack == null) {
            outPossibleAttack = new LinkedList<>();
            outPossibleAttack.add(new LinkedList<Pair>());
        }
        for (i = 0; i < outPossibleAttack.size(); i++)
            outPossibleAttack.get(i).addFirst(pawn);
        return outPossibleAttack;
    }

    private void choseAttackOption() {
        Pawn option = null;
        PriorityQueue<Pawn> newAttack = new PriorityQueue<>();
        while (attackOption.peek() != null) {
            if (option != null && option.getAmountOfActions() != attackOption.peek().getAmountOfActions())
                break;
            option = attackOption.poll();
            newAttack.add(option);
        }
        attackOption = newAttack;
    }

    void showAttackOption() {
        int x, y;
        choseAttackOption();
        for (Pawn pawn : attackOption) {
            x = pawn.getCurrentPosition().getX();
            y = pawn.getCurrentPosition().getY();
            if (board[x][y].getImage() != null) {
                setHighlight(board[x][y], R.drawable.highlight_red);
            }
        }
    }

    void movePawn(Pawn pawn, Pair destination) {
        int x = pawn.getCurrentPosition().getX();
        int y = pawn.getCurrentPosition().getY();
        int dstX = destination.getX();
        int dstY = destination.getY();
        board[dstX][dstY].setPawn(board[x][y].getPawn());
        board[dstX][dstY].getPawn().setCurrentPosition(destination);
        if (board[dstX][dstY].getImage() != null) {
            board[dstX][dstY].getImage().setImageResource(PawnGraphics.get(whiteTurn, board[dstX][dstY].getPawn().isKing()));
        }
        deleteHighlightBoard();
        board[x][y].deletePawn();
        board[dstX][dstY].levelUp();
    }

    void attackWithPawn(Pawn pawn, Pair destination) {
        Pair pawnToDelete = findPawnToDelete(pawn.getCurrentPosition(), destination);
        movePawn(pawn, destination);
        deleteFromBoard(pawnToDelete);
    }

    private Pair findPawnToDelete(Pair currentPosition, Pair destination) {
        int currentX = currentPosition.getX();
        int currentY = currentPosition.getY();
        int destinationX = destination.getX();
        int destinationY = destination.getY();
        int x = currentX;
        int y = currentY;
        if (currentX < destinationX)
            if (currentY < destinationY) {
                while (x < 7 && y < 7) {
                    if (board[x][y].getPawn() != null && board[x][y].getPawn().isWhite() != whiteTurn)
                        break;
                    x++;
                    y++;
                }
            } else {
                while (x < 7 && y > 0) {
                    if (board[x][y].getPawn() != null && board[x][y].getPawn().isWhite() != whiteTurn)
                        break;
                    x++;
                    y--;
                }
            }
        else if (currentY < destinationY) {
            while (x > 0 && y < 7) {
                if (board[x][y].getPawn() != null && board[x][y].getPawn().isWhite() != whiteTurn)
                    break;
                x--;
                y++;
            }
        } else {
            while (x > 0 && y > 0) {
                if (board[x][y].getPawn() != null && board[x][y].getPawn().isWhite() != whiteTurn)
                    break;
                x--;
                y--;
            }
        }
        return new Pair(x, y);
    }

    private void deleteFromBoard(Pair pawn) {
        int x = pawn.getX();
        int y = pawn.getY();
        board[x][y].deletePawn();
        if (whiteTurn) {
            redPawns.remove(idxOfPawn(redPawns, pawn));
        } else {
            whitePawns.remove(idxOfPawn(whitePawns, pawn));
        }
    }

    private int idxOfPawn(LinkedList<Pawn> pawns, Pair position) {
        int i = 0;
        for (Pawn pawn : pawns) {
            if (position.equals(pawn.getCurrentPosition())) {
                return i;
            }
            i++;
        }
        return -1;
    }

    void changeTurn() {
        whiteTurn = !whiteTurn;
        possibleAction();
        chosenField.unset();
        if ((whiteTurn && whitePlayer)
                || (!whiteTurn && redPlayer)) {
            actionAI();
        }
    }

    void end(int result) {

    }

    void clean() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j].deletePawn();
            }
        }
    }

    private void actionAI() {
        Ai computer = new Ai(this);
        Move move = computer.getMove();
        if (move.getDestination().size() > 1) {
            attackAI(move.getPawn(), move.getDestination());
        } else {
            movePawn(move.getPawn(), move.getDestination().get(0));
        }
    }

    LinkedList<Move> allMoves(final LinkedList<Pawn> pawns) {
        LinkedList<Move> allMoves = new LinkedList<>();
        if (attackOption.size() > 0) {
            choseAttackOption();
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

    void attackAI(Pawn pawn, LinkedList<Pair> listOfDestination) {
        Pair destination;
        listOfDestination.remove();
        while (listOfDestination.size() > 0) {
            destination = listOfDestination.poll();
            attackWithPawn(pawn, destination);
            pawn.setCurrentPosition(destination);
        }
        attackOption.clear();
        board[pawn.getCurrentPosition().getX()][pawn.getCurrentPosition().getY()].levelUp();
    }
}
