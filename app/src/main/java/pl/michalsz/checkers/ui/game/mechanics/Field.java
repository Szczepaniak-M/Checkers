package pl.michalsz.checkers.ui.game.mechanics;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import java.util.Iterator;
import java.util.LinkedList;

import pl.michalsz.checkers.R;

class Field implements ImageView.OnClickListener {

    private Board board;
    private ImageView image;
    private Pair position;
    private Pawn pawn;

    Field(Board board, int x, int y, ImageView img) {
        this.board = board;
        position = new Pair(x, y);
        image = img;
        image.setOnClickListener(this);
        pawn = null;
    }

    Field(Field oldField, Board newBoard) {
        this.board = newBoard; //TODO tu moze byc problem z AI
        position = new Pair(oldField.position);
        image = null;
        if (oldField.pawn != null) {
            pawn = new Pawn(oldField.pawn);
        } else {
            pawn = null;
        }
    }

    Pair getPosition() {
        return position;
    }

    Pawn getPawn() {
        return pawn;
    }

    ImageView getImage() {
        return image;
    }

    void setPawn(Pawn pawn) {
        this.pawn = pawn;
    }

    void setPawn(int x, int y, boolean white, int imageID) {
        this.pawn = new Pawn(x, y, white);
        image.setImageResource(imageID);
    }

    void setHighlight(final Activity activity, int id) {
        Drawable highlight = activity.getDrawable(id);
        image.setBackground(highlight);
    }

    void setImage(boolean white, boolean king) {
        if (white) {
            if (king) {
                image.setImageResource(R.mipmap.white_king);
            } else {
                image.setImageResource(R.mipmap.white_man);
            }
        } else {
            if (king) {
                image.setImageResource(R.mipmap.red_king);
            } else {
                image.setImageResource(R.mipmap.red_man);
            }
        }
    }

    void deleteHighlightField() {
        image.setBackground(null);
    }

    void deletePawn() {
        pawn = null;
        if (image != null) {
            image.setImageResource(R.mipmap.empty);
        }
    }

    @Override
    public void onClick(View v) {
        Pair chosenField = board.getChosenField();
        if (board.getAttackOption().size() > 0) {
            if (pawn != null) {
                attackFirstClick(chosenField);
            } else if (chosenField.isSet()) {
                attackSecondClick(chosenField);
            }
        } else {
            if (pawn != null) {
                moveFirstClick(chosenField);
            } else if (board.getChosenField().isSet()) {
                moveSecondClick(chosenField);
            }
        }
    }

    private void moveFirstClick(Pair chosenField) {
        if (pawn.isWhite() == board.isWhiteTurn()) {
            if (chosenField.isSet()) {
                if (!chosenField.equals(position)) {
                    board.deleteHighlightBoard();
                    board.setHighlight(this, R.drawable.highlight_organe);
                    chosenField.set(position);
                    showFieldMoveOption();
                } else {
                    board.deleteHighlightBoard();
                    chosenField.unset();
                }
            } else {
                board.setHighlight(this, R.drawable.highlight_organe);
                chosenField.set(position);
                showFieldMoveOption();
            }
        }
    }

    private void moveSecondClick(Pair chosenField) {
        Pawn chosenPawn = board.getPawn(chosenField);
        for (LinkedList<Pair> field : chosenPawn.getPossibleAction()) {
            if (field.getFirst().equals(position)) {
                board.movePawn(chosenPawn, position);
                if (chosenPawn.isKing()) {
                    if (board.isWhiteTurn()) {
                        board.addDrawWhite();
                    } else {
                        board.addDrawRed();
                    }
                }
                if (board.getDrawRed() > 14 && board.getDrawWhite() > 14) {
                    board.end(0);
                }
                board.changeTurn();
                break;
            }
        }
    }

    private void showFieldMoveOption() {
        for (LinkedList<Pair> field : pawn.getPossibleAction()) {
            board.setHighlight(board.getField(field.getFirst()), R.drawable.highlight_organe);
        }
    }

    private void attackFirstClick(Pair chosenField) {
        if (board.getAttackOption().contains(pawn) && pawn.isWhite() == board.isWhiteTurn()) {
            if (chosenField.isSet()) {
                if (!chosenField.equals(position)) {
                    board.deleteHighlightBoard();
                    board.setHighlight(this, R.drawable.highlight_organe);
                    chosenField.set(position);
                    board.showAllAttackOption();
                    showFieldAttackOption();
                } else {
                    board.deleteHighlightBoard();
                    board.showAllAttackOption();
                    chosenField.unset();
                }
            } else {
                board.setHighlight(this, R.drawable.highlight_organe);
                chosenField.set(position);
                showFieldAttackOption();
            }
        }
    }

    private void attackSecondClick(Pair chosenField) {
        Pawn chosenPawn = board.getPawn(chosenField);
        for (LinkedList<Pair> path : chosenPawn.getPossibleAction()) {
            if (path.get(1).equals(position)) {
                board.deleteAttackOption();
                board.attackWithPawn(chosenPawn, position);
                chosenField.set(position.getX(), position.getY());
                if (!updateAttack(chosenField)) {
                    board.addAttackOption(pawn);
                    board.showAllAttackOption();
                    chosenField.unset();
                    attackFirstClick(chosenField);
                } else {
                    this.levelUp();
                    if (board.isWhiteTurn()) {
                        board.setDrawWhite();
                    } else {
                        board.setDrawRed();
                    }
                    board.changeTurn();
                }
                break;
            }

        }
    }

    private void showFieldAttackOption() {
        for (LinkedList<Pair> path : pawn.getPossibleAction()) {
            board.setHighlight(board.getField(path.get(1)), R.drawable.highlight_organe);
        }
    }

    private boolean updateAttack(Pair field) {
        LinkedList<Pair> current;
        Iterator<LinkedList<Pair>> itr = pawn.getPossibleAction().iterator();
        while (itr.hasNext()) {
            current = itr.next();
            current.poll();
            if (!(current.peek()).equals(field) || current.size() == 1) {
                itr.remove();
            }
        }
        return pawn.getPossibleAction().size() == 0;
    }

    void levelUp() {
        int Y = position.getY();
        if ((Y == 7 && board.isWhiteTurn()) || (Y == 0 && !board.isWhiteTurn())) {
            pawn.setKing();
            if (image != null) {
                this.setImage(board.isWhiteTurn(), pawn.isKing());
            }
        }
    }
}
