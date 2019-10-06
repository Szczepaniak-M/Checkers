package pl.michalsz.checkers.ui.game.mechanics;


import pl.michalsz.checkers.R;

enum PawnGraphics {
    BLACK_PAWN(R.mipmap.red_man),
    BLACK_KING(R.mipmap.red_king),
    WHITE_PAWN(R.mipmap.white_man),
    WHITE_KING(R.mipmap.white_king);

    int graphic;

    PawnGraphics(int GraphicsID) { graphic=GraphicsID; }

    int get() { return graphic; }

    static int get(boolean white, boolean king){
        if(white)
        {
            if(king)
                return WHITE_KING.graphic;
            else
                return WHITE_PAWN.graphic;
        } else{
            if(king)
                return BLACK_KING.graphic;
            else
                return BLACK_PAWN.graphic;
        }
    }
}
