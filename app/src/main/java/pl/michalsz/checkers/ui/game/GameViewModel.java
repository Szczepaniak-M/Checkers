package pl.michalsz.checkers.ui.game;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.LinkedList;

import pl.michalsz.checkers.ui.game.mechanics.Pawn;

public class GameViewModel extends ViewModel {

    private MutableLiveData<Boolean> playerWhite;
    private MutableLiveData<Boolean> playerRed;
    private MutableLiveData<LinkedList<Pawn>> whitePawns;
    private MutableLiveData<LinkedList<Pawn>> redPawns;

    public GameViewModel() {
        playerWhite = new MutableLiveData<>();
        playerWhite.setValue(true);

        playerRed = new MutableLiveData<>();
        playerRed.setValue(true);
    }

    public MutableLiveData<Boolean> getPlayerWhite() {
        return playerWhite;
    }

    public MutableLiveData<Boolean> getPlayerRed() {
        return playerRed;
    }

    public void setPlayerWhite(MutableLiveData<Boolean> playerWhite) {
        this.playerWhite = playerWhite;
    }

    public void setPlayerRed(MutableLiveData<Boolean> playerRed) {
        this.playerRed = playerRed;
    }

    public void setRedPawns(LinkedList<Pawn> redPawns) {
        this.redPawns = new MutableLiveData<>();
        this.redPawns.setValue(redPawns);
    }

    public void setWhitePawns(LinkedList<Pawn> whitePawns) {
        this.whitePawns = new MutableLiveData<>();
        this.whitePawns.setValue(whitePawns);
    }
}

