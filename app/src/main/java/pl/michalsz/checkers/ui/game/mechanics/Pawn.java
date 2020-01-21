package pl.michalsz.checkers.ui.game.mechanics;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Collections;
import java.util.LinkedList;

public class Pawn implements Comparable<Pawn>, Parcelable {
    private boolean king;
    private boolean white;
    private int amountOfActions;
    private Pair currentPosition;
    private LinkedList<LinkedList<Pair>> possibleAction = new LinkedList<>();


    Pawn(int x, int y, boolean white) {
        king = false;
        this.white = white;
        currentPosition = new Pair(x, y);
        amountOfActions = 0;
    }

    Pawn(Pawn oldPawn) {
        white = oldPawn.white;
        king = oldPawn.king;
        amountOfActions = oldPawn.amountOfActions;
        currentPosition = new Pair(oldPawn.currentPosition.getX(), oldPawn.currentPosition.getY());
        for (LinkedList<Pair> action : oldPawn.possibleAction)
            possibleAction.add(new LinkedList<>(action));
    }

    Pair getCurrentPosition() {
        return currentPosition;
    }

    LinkedList<LinkedList<Pair>> getPossibleAction() {
        return possibleAction;
    }

    int getAmountOfActions() {
        return amountOfActions;
    }

    void setCurrentPosition(Pair currentPosition) {
        this.currentPosition = currentPosition;
    }

    int setPossibleAction(LinkedList<LinkedList<Pair>> possibleAttack) {
        setPossibleActionEmpty();
        amountOfActions = possibleAttack.get(0).size();
        for (int i = 0; i < possibleAttack.size(); i++)
            this.possibleAction.add(new LinkedList<>(possibleAttack.get(i)));
        return amountOfActions;
    }

    void setPossibleAction(Pair possibleMove) {
        this.possibleAction.add(new LinkedList<>(Collections.singletonList(possibleMove)));
        amountOfActions = possibleAction.size();
    }

    void setPossibleActionEmpty() {
        for (int i = 0; i < possibleAction.size(); i++)
            possibleAction.get(i).clear();
        possibleAction.clear();
        amountOfActions = 0;
    }

    void setKing() {
        this.king = true;
    }

    boolean isKing() {
        return king;
    }

    boolean isWhite() {
        return white;
    }

    @Override
    public int compareTo(Pawn other) {
        return (this.amountOfActions - other.amountOfActions) * -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return ((Pawn) o).currentPosition.equals(currentPosition);
    }

    @Override
    public String toString() {
        return "Pawn: " + currentPosition.getX() + " " + currentPosition.getY() + " ";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.king ? (byte) 1 : (byte) 0);
        dest.writeByte(this.white ? (byte) 1 : (byte) 0);
        dest.writeInt(this.amountOfActions);
        dest.writeParcelable(this.currentPosition, flags);
        dest.writeList(this.possibleAction);
    }

    private Pawn(Parcel in) {
        this.king = in.readByte() != 0;
        this.white = in.readByte() != 0;
        this.amountOfActions = in.readInt();
        this.currentPosition = in.readParcelable(Pair.class.getClassLoader());
        this.possibleAction = new LinkedList<>();
    }

    public static final Parcelable.Creator<Pawn> CREATOR = new Parcelable.Creator<Pawn>() {
        @Override
        public Pawn createFromParcel(Parcel source) {
            return new Pawn(source);
        }

        @Override
        public Pawn[] newArray(int size) {
            return new Pawn[size];
        }
    };
}