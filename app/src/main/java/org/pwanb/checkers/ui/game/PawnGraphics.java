package org.pwanb.checkers.application;


enum PawnGraphics {
    BLACK_PAWN(R.drawable.black_pawn),
    BLACK_KING(R.drawable.black_king),
    WHITE_PAWN(R.drawable.white_pawn),
    WHITE_KING(R.drawable.white_king),
    EMPTY(R.drawable.empty);

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
