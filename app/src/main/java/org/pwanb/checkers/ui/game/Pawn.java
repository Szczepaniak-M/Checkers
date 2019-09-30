package org.pwanb.checkers.application;

import java.util.Collections;
import java.util.LinkedList;

class Pawn implements Comparable<Pawn>{
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
        for(int i = 0; i < oldPawn.possibleAction.size(); i++)
            possibleAction.add(new LinkedList<>(oldPawn.possibleAction.get(i)));
    }

    @Override
    public int compareTo(Pawn other) { return (this.amountOfActions - other.amountOfActions)* -1; }

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
    public String toString(){
        return "Pawn: " + currentPosition.getX() + " " + currentPosition.getY() + " ";
    }

    Pair getCurrentPosition() { return currentPosition; }

    LinkedList<LinkedList<Pair>> getPossibleAction() { return possibleAction; }

    int getAmountOfActions() {return  amountOfActions; }

    int setPossibleAttack(LinkedList<LinkedList<Pair>> possibleAttack){
        setPossibleActionEmpty();
        amountOfActions = possibleAttack.get(0).size();
        for(int i = 0; i < possibleAttack.size(); i++)
            this.possibleAction.add(new LinkedList<>(possibleAttack.get(i)));
        return amountOfActions;
    }

    void setPossibleMove(Pair possibleMove) {
        this.possibleAction.add(new LinkedList<>(Collections.singletonList(possibleMove)));
        amountOfActions = possibleAction.size();
    }

    void setPossibleActionEmpty(){
        for(int i = 0; i < possibleAction.size(); i++)
            possibleAction.get(i).clear();
        possibleAction.clear();
        amountOfActions = 0;
    }

    void setKing() { this.king = true; }

    void setCurrentPosition(Pair currentPosition) { this.currentPosition = currentPosition; }

    boolean isKing() { return king; }

    boolean isWhite() { return white; }

}