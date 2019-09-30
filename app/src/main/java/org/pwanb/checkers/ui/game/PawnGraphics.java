package org.pwanb.checkers.ui.game;


import org.pwanb.checkers.R;

enum PawnGraphics {
    BLACK_PAWN(R.mipmap.red_man),
    BLACK_KING(R.mipmap.red_king),
    WHITE_PAWN(R.mipmap.white_man),
    WHITE_KING(R.mipmap.white_king),
    EMPTY(R.mipmap.empty);

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
