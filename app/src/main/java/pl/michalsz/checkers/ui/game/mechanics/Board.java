package pl.michalsz.checkers.ui.game.mechanics;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import pl.michalsz.checkers.R;
import pl.michalsz.checkers.ui.game.GameFragmentDirections;


public class Board implements Parcelable {

    private Pair chosenField;
    private Field[][] board = new Field[8][8];
    private Activity activity;
    private LinkedList<Pawn> whitePawns;
    private LinkedList<Pawn> redPawns;
    private PriorityQueue<Pawn> attackOption;
    private Queue<Pair> highlightsFields;
    private int drawWhite;
    private int drawRed;
    private boolean whiteTurn;
    private boolean isCopy;
    private boolean isWhitePlayer;
    private boolean isRedPlayer;


    public Board(ImageView[][] boardMain, Activity activity, boolean whitePlayer, boolean redPlayer) {
        isCopy = false;
        this.isWhitePlayer = whitePlayer;
        this.isRedPlayer = redPlayer;
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
        drawWhite = oldBoard.drawWhite;
        drawRed = oldBoard.drawRed;
        this.isCopy = isCopy;
        this.activity = null;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i % 2 == j % 2)
                    board[i][j] = new Field(oldBoard.board[i][j], this);
            }
        }
        whitePawns = new LinkedList<>();
        redPawns = new LinkedList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i % 2 == j % 2 && board[i][j].getPawn() != null) {
                    if (board[i][j].getPawn().isWhite()) {
                        whitePawns.add(board[i][j].getPawn());
                    } else {
                        redPawns.add(board[i][j].getPawn());
                    }
                }
            }
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

    int getDrawWhite() {
        return drawWhite;
    }

    int getDrawRed() {
        return drawRed;
    }

    boolean isWhiteTurn() {
        return whiteTurn;
    }

    boolean isWhitePlayer() {
        return isWhitePlayer;
    }

    boolean isRedPlayer() {
        return isRedPlayer;
    }

    boolean isCopy() {
        return isCopy;
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

    void setHighlight(Field field, int id) {
        field.setHighlight(activity, id);
        highlightsFields.add(field.getPosition());
    }

    void deleteHighlightBoard() {
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
        whiteTurn = false;
        Field.resetLatency();
        for (int i = 0; i < 4; i++) {
            board[2 * i][0].setPawn(2 * i, 0, true, R.mipmap.white_man);
            board[2 * i + 1][1].setPawn(2 * i + 1, 1, true, R.mipmap.white_man);
            board[2 * i][2].setPawn(2 * i, 2, true, R.mipmap.white_man);
            board[2 * i + 1][5].setPawn(2 * i + 1, 5, false, R.mipmap.red_man);
            board[2 * i][6].setPawn(2 * i, 6, false, R.mipmap.red_man);
            board[2 * i + 1][7].setPawn(2 * i + 1, 7, false, R.mipmap.red_man);
            whitePawns.add(board[2 * i][0].getPawn());
            whitePawns.add(board[2 * i + 1][1].getPawn());
            whitePawns.add(board[2 * i][2].getPawn());
            redPawns.add(board[2 * i + 1][5].getPawn());
            redPawns.add(board[2 * i][6].getPawn());
            redPawns.add(board[2 * i + 1][7].getPawn());
        }
        changeTurn();

    }

    void changeTurn() {

        whiteTurn = !whiteTurn;
        attackOption.clear();
        possibleAction();
        chosenField.unset();
        if (((whiteTurn && !isWhitePlayer)
                || (!whiteTurn && !isRedPlayer)) && !isCopy) {
            actionAI();
            whiteTurn = !whiteTurn;
            attackOption.clear();
            possibleAction();
            chosenField.unset();
        }
    }

    void end(final int result) {
        NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment);
        if (navController.getCurrentDestination().getId() == R.id.nav_game) {
            GameFragmentDirections.ActionNavGameToNavGameAlert action = GameFragmentDirections.actionNavGameToNavGameAlert();
            action.setWinner(result);
            navController.navigate(action);
        }
    }

    public void clean() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {
                    board[i][j].deletePawn();
                }
            }
        }
        whitePawns = new LinkedList<>();
        redPawns = new LinkedList<>();
        attackOption.clear();
        deleteHighlightBoard();
        drawWhite = 0;
        drawRed = 0;
        Field.resetLatency();
    }

    void possibleAction() {

        if (whiteTurn) {
            possibleAttack(whitePawns);
            if (attackOption.size() > 0) {
                showAllAttackOption();
            } else {
                possibleMove(whitePawns);
            }
        } else {
            possibleAttack(redPawns);
            if (attackOption.size() > 0) {
                showAllAttackOption();
            } else {
                possibleMove(redPawns);
            }
        }
    }

    private void possibleMove(LinkedList<Pawn> pawns) {
        boolean isMove = false;
        int x, y;
        for (Pawn pawn : pawns) {
            pawn.setPossibleActionEmpty();
            x = pawn.getCurrentPosition().getX();
            y = pawn.getCurrentPosition().getY();
            if (pawn.isKing()) {
                int j = 1;
                while (x + j < 8 && y + j < 8 && board[x + j][y + j].getPawn() == null) {
                    pawn.setPossibleAction(new Pair(x + j, y + j));
                    j++;
                }

                j = 1;
                while (x - j > -1 && y + j < 8 && board[x - j][y + j].getPawn() == null) {
                    pawn.setPossibleAction(new Pair(x - j, y + j));
                    j++;
                }

                j = 1;
                while (x - j > -1 && y - j > -1 && board[x - j][y - j].getPawn() == null) {
                    pawn.setPossibleAction(new Pair(x - j, y - j));
                    j++;
                }

                j = 1;
                while (x + j < 8 && y - j > -1 && board[x + j][y - j].getPawn() == null) {
                    pawn.setPossibleAction(new Pair(x + j, y - j));
                    j++;
                }
            } else {
                if (whiteTurn) {
                    if (y < 7) {
                        if (x < 7 && board[x + 1][y + 1].getPawn() == null) {
                            pawn.setPossibleAction(new Pair(x + 1, y + 1));
                        }
                        if (x > 0 && board[x - 1][y + 1].getPawn() == null) {
                            pawn.setPossibleAction(new Pair(x - 1, y + 1));
                        }
                    }
                } else {
                    if (y > 0) {
                        if (x < 7 && board[x + 1][y - 1].getPawn() == null) {
                            pawn.setPossibleAction(new Pair(x + 1, y - 1));
                        }
                        if (x > 0 && board[x - 1][y - 1].getPawn() == null) {
                            pawn.setPossibleAction(new Pair(x - 1, y - 1));
                        }
                    }
                }
            }
            if (pawn.getAmountOfActions() > 0) {
                isMove = true;
            }
        }
        if (!isMove && !isCopy) {
            if (whiteTurn) {
                end(-1);
            } else {
                end(1);
            }
        }
    }

    private void possibleAttack(LinkedList<Pawn> pawns) {
        boolean empty = true;
        int priority;
        for (Pawn pawn : pawns) {
            empty = false;
            pawn.setPossibleActionEmpty();
            if (pawn.isKing()) {
                priority = pawn.setPossibleAction(searchAttackKing(pawn.getCurrentPosition(), new LinkedList<Pair>()));
            } else {
                priority = pawn.setPossibleAction(searchAttack(pawn.getCurrentPosition(), new LinkedList<Pair>()));
            }
            if (priority > 1) {
                attackOption.add(pawn);
            } else {
                pawn.setPossibleActionEmpty();
            }
        }
        if (empty && !isCopy) {
            if (whiteTurn) {
                end(-1);
            } else {
                end(1);
            }
        } else {
            Pawn option;
            PriorityQueue<Pawn> newAttackOption = new PriorityQueue<>();
            while (attackOption.peek() != null) {
                option = attackOption.poll();
                newAttackOption.add(option);
                if (!attackOption.isEmpty() && option.getAmountOfActions() != attackOption.peek().getAmountOfActions())
                    break;
            }
            attackOption = newAttackOption;
        }

    }

    private LinkedList<LinkedList<Pair>> searchAttack(Pair pawn, LinkedList<Pair> deleted) {
        int x = pawn.getX();
        int y = pawn.getY();
        LinkedList<LinkedList<Pair>> newPossibleAttack, currentPossibleAttack = null;
        if (y < 6) {
            if (x < 6 && board[x + 1][y + 1].getPawn() != null && board[x + 1][y + 1].getPawn().isWhite() != whiteTurn
                    && board[x + 2][y + 2].getPawn() == null && !deleted.contains(new Pair(x + 1, y + 1))) {
                deleted.addLast(new Pair(x + 1, y + 1));
                currentPossibleAttack = searchAttack(new Pair(x + 2, y + 2), deleted);
                deleted.removeLast();
            }

            if (x > 1 && board[x - 1][y + 1].getPawn() != null && board[x - 1][y + 1].getPawn().isWhite() != whiteTurn
                    && board[x - 2][y + 2].getPawn() == null && !deleted.contains(new Pair(x - 1, y + 1))) {
                deleted.addLast(new Pair(x - 1, y + 1));
                newPossibleAttack = searchAttack(new Pair(x - 2, y + 2), deleted);
                deleted.removeLast();
                currentPossibleAttack = verifyNewPossibleAttack(currentPossibleAttack, newPossibleAttack);
            }
        }
        if (y > 1) {
            if (x > 1 && board[x - 1][y - 1].getPawn() != null && board[x - 1][y - 1].getPawn().isWhite() != whiteTurn
                    && board[x - 2][y - 2].getPawn() == null && !deleted.contains(new Pair(x - 1, y - 1))) {
                deleted.addLast(new Pair(x - 1, y - 1));
                newPossibleAttack = searchAttack(new Pair(x - 2, y - 2), deleted);
                deleted.removeLast();
                currentPossibleAttack = verifyNewPossibleAttack(currentPossibleAttack, newPossibleAttack);
            }

            if (x < 6 && board[x + 1][y - 1].getPawn() != null && board[x + 1][y - 1].getPawn().isWhite() != whiteTurn
                    && board[x + 2][y - 2].getPawn() == null && !deleted.contains(new Pair(x + 1, y - 1))) {
                deleted.addLast(new Pair(x + 1, y - 1));
                newPossibleAttack = searchAttack(new Pair(x + 2, y - 2), deleted);
                deleted.removeLast();
                currentPossibleAttack = verifyNewPossibleAttack(currentPossibleAttack, newPossibleAttack);
            }
        }
        if (currentPossibleAttack == null) {
            currentPossibleAttack = new LinkedList<>();
            currentPossibleAttack.add(new LinkedList<Pair>());
        }
        for (int i = 0; i < currentPossibleAttack.size(); i++)
            currentPossibleAttack.get(i).addFirst(pawn);
        return currentPossibleAttack;
    }

    private LinkedList<LinkedList<Pair>> searchAttackKing(Pair pawn, LinkedList<Pair> deleted) {
        int x = pawn.getX();
        int y = pawn.getY();
        int i = 1, j;
        LinkedList<LinkedList<Pair>> newPossibleAttack, currentPossibleAttack = null;
        while (x + i < 7 && y + i < 7) {
            if (board[x + i][y + i].getPawn() != null) {
                if (board[x + i][y + i].getPawn().isWhite() != whiteTurn
                        && board[x + i + 1][y + i + 1].getPawn() == null
                        && !deleted.contains(new Pair(x + i, y + i))) {
                    deleted.addLast(new Pair(x + i, y + i));
                    j = 1;
                    while (x + i + j < 8 && y + i + j < 8 && board[x + i + j][y + i + j].getPawn() == null) {
                        newPossibleAttack = searchAttackKing(new Pair(x + i + j, y + i + j), deleted);
                        currentPossibleAttack = verifyNewPossibleAttack(currentPossibleAttack, newPossibleAttack);
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
                        newPossibleAttack = searchAttackKing(new Pair(x - i - j, y + i + j), deleted);
                        currentPossibleAttack = verifyNewPossibleAttack(currentPossibleAttack, newPossibleAttack);
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
                        newPossibleAttack = searchAttackKing(new Pair(x - i - j, y - i - j), deleted);
                        currentPossibleAttack = verifyNewPossibleAttack(currentPossibleAttack, newPossibleAttack);
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
                        newPossibleAttack = searchAttackKing(new Pair(x + i + j, y - i - j), deleted);
                        currentPossibleAttack = verifyNewPossibleAttack(currentPossibleAttack, newPossibleAttack);
                        j++;
                    }
                    deleted.removeLast();
                }
                break;
            }
            i++;
        }

        if (currentPossibleAttack == null) {
            currentPossibleAttack = new LinkedList<>();
            currentPossibleAttack.add(new LinkedList<Pair>());
        }
        for (i = 0; i < currentPossibleAttack.size(); i++)
            currentPossibleAttack.get(i).addFirst(pawn);
        return currentPossibleAttack;
    }

    private LinkedList<LinkedList<Pair>> verifyNewPossibleAttack(LinkedList<LinkedList<Pair>> currentResult, LinkedList<LinkedList<Pair>> newResult) {

        if (currentResult != null) {
            if (currentResult.get(0).size() == newResult.get(0).size()) {
                currentResult.addAll(newResult);
            } else if (currentResult.get(0).size() < newResult.get(0).size()) {
                return newResult;
            }
        } else {
            return newResult;
        }
        return currentResult;
    }

    void showAllAttackOption() {
        int x, y;
        for (Pawn pawn : attackOption) {
            x = pawn.getCurrentPosition().getX();
            y = pawn.getCurrentPosition().getY();
            if (board[x][y].getImage() != null) {
                setHighlight(board[x][y], R.drawable.highlight_red);
            }
        }
    }

    Pair movePawn(Pawn pawn, Pair destination) {
        int x = pawn.getCurrentPosition().getX();
        int y = pawn.getCurrentPosition().getY();
        int dstX = destination.getX();
        int dstY = destination.getY();
        board[dstX][dstY].setPawn(board[x][y].getPawn());
        board[dstX][dstY].getPawn().setCurrentPosition(destination);
        if (board[dstX][dstY].getImage() != null) {
            board[dstX][dstY].setImage(whiteTurn, board[dstX][dstY].getPawn().isKing());
        }
        deleteHighlightBoard();
        board[x][y].deletePawn();
        return destination;
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

    private void actionAI() {
        Ai computer = new Ai(this);
        Move move = computer.getMove();
        actionAI(move);
    }

    void actionAI(Move move) {
        Pair update;
        if (move.getDestination().size() > 1) {
            update = attackAI(move.getPawn(), move.getDestination());
        } else {
            if (!isCopy)
                Field.addLatency();
            update = movePawn(move.getPawn(), move.getDestination().get(0));
        }
        board[update.getX()][update.getY()].levelUp();

    }

    private Pair attackAI(Pawn pawn, LinkedList<Pair> listOfDestination) {
        Pair destination = null;
        listOfDestination.remove();
        while (listOfDestination.size() > 0) {
            if (!isCopy)
                Field.addLatency();
            destination = listOfDestination.poll();
            attackWithPawn(pawn, destination);
            pawn.setCurrentPosition(destination);
        }
        return destination;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.chosenField, flags);
        int numOfArrays = 8;
        dest.writeInt(numOfArrays);
        for (int i = 0; i < numOfArrays; i++) {
            dest.writeTypedArray(board[i], flags);
        }
        dest.writeSerializable(this.attackOption);
        dest.writeInt(this.drawWhite);
        dest.writeInt(this.drawRed);
        dest.writeByte(this.whiteTurn ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isCopy ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isWhitePlayer ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isRedPlayer ? (byte) 1 : (byte) 0);
    }

    private Board(Parcel in) {
        this.chosenField = in.readParcelable(Pair.class.getClassLoader());
        int numberOfArrays = in.readInt();
        this.board = new Field[numberOfArrays][];
        for (int i = 0; i < numberOfArrays; i++) {
            this.board[i] = in.createTypedArray(Field.CREATOR);
        }
        this.activity = null;
        this.whitePawns = new LinkedList<>();
        this.redPawns = new LinkedList<>();
        this.attackOption = new PriorityQueue<>();
        this.highlightsFields = new LinkedList<>();
        this.drawWhite = in.readInt();
        this.drawRed = in.readInt();
        this.whiteTurn = in.readByte() != 0;
        this.isCopy = in.readByte() != 0;
        this.isWhitePlayer = in.readByte() != 0;
        this.isRedPlayer = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Board> CREATOR = new Parcelable.Creator<Board>() {
        @Override
        public Board createFromParcel(Parcel source) {
            return new Board(source);
        }

        @Override
        public Board[] newArray(int size) {
            return new Board[size];
        }
    };

    public void postParcelable(ImageView[][] boardMain, Activity activity) {
        this.activity = activity;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i % 2 == j % 2) {
                    board[i][j].setImage(boardMain[i][j]);
                }
            }
        }
        possibleAction();
    }
}
