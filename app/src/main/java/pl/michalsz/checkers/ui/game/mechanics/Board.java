package pl.michalsz.checkers.ui.game.mechanics;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.ImageView;
import android.view.View;
import android.graphics.drawable.Drawable;


import pl.michalsz.checkers.R;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;


class Board{
    private boolean whiteTurn;
    private Pair chosenField;
    private Field[][] board = new Field[8][8];
    private Activity activity;
    private Pawn[] whitePawns = new Pawn[12];
    private Pawn[] blackPawns = new Pawn[12];
    private PriorityQueue<Pawn> attack = new PriorityQueue<>();
    private Queue<Pair> highlights = new LinkedList<>();
    private int drawWhite;
    private int drawBlack;
    private boolean isAlert;
    private boolean isCopy;


    class Field implements ImageView.OnClickListener {

        private ImageView image;
        private Pair position;
        private Pawn pawn = null;

        Field(int x, int y, ImageView img) {
            position = new Pair(x,y);
            image = img;
            image.setOnClickListener(this);
        }

        Field(Field oldField) {
            position = new Pair(oldField.position);
            image = null;
            if(oldField.pawn != null) {
                pawn = new Pawn(oldField.pawn);
            }else {
                pawn = null;
            }
        }

        void setHighlightOrange() {
            Drawable highlight = activity.getResources().getDrawable(R.drawable.highlight_organe);
            image.setBackground(highlight);
            highlights.add(new Pair (position.getX(),position.getY()));
        }

        void setHighlightRed() {
            Drawable highlight = activity.getResources().getDrawable(R.drawable.highlight_red);
            image.setBackground(highlight);
            highlights.add(new Pair (position.getX(),position.getY()));
        }

        void deleteHighlightField() { image.setBackground(null); }

        void setPawn(final int x, final int y, final boolean white,final int imageID ) {
            this.pawn = new Pawn(x, y, white);
            image.setImageResource(imageID);
        }

        void deletePawn(){
            pawn = null;
            if (image != null){
                image.setImageResource(PawnGraphics.EMPTY.get());
            }
        }

        @Override
        public void onClick(View v) {
            if (attack.size() > 0) {
                if (pawn != null)
                    attackFirstClick();
                else if (chosenField.isSet())
                    attackSecondClick();
            } else {
                if (pawn != null)
                    moveFirstClick();
                else if (chosenField.isSet())
                    moveSecondClick();
            }
        }

        private void moveFirstClick() {
            int prevX = chosenField.getX();
            int prevY = chosenField.getY();
            int X = position.getX();
            int Y = position.getY();
            if (pawn.isWhite() == whiteTurn) {
                if (chosenField.isSet()) {
                    if (X != prevX || Y != prevY) {
                        deleteHighlightBoard();
                        setHighlightOrange();
                        chosenField.set(X, Y);
                        showOptionForMove();
                    } else {
                        deleteHighlightBoard();
                        chosenField.unset();
                    }
                } else {
                    setHighlightOrange();
                    chosenField.set(X, Y);
                    showOptionForMove();
                }
            }
        }

        private void moveSecondClick() {
            int prevX = chosenField.getX();
            int prevY = chosenField.getY();
            for (LinkedList<Pair> field : board[prevX][prevY].pawn.getPossibleAction())
            {
                if (field.getFirst().equals(position)){
                    move(board[prevX][prevY].pawn, position);
                    if (board[position.getX()][position.getY()].pawn.isKing())
                        if(whiteTurn)
                            drawWhite++;
                        else
                            drawBlack++;
                    if (drawBlack > 14 && drawWhite > 14)
                    //    end(Result.DRAW.output);
                    chosenField.unset();
                    changeTurn();
                    actionAI();
                    break;
                }
            }
        }

        void showOptionForMove(){
            int x, y;
            for(LinkedList<Pair> field : pawn.getPossibleAction())
            {
                x = field.getFirst().getX();
                y = field.getFirst().getY();
                board[x][y].setHighlightOrange();
            }
        }

        private void attackFirstClick(){
            int prevX = chosenField.getX();
            int prevY = chosenField.getY();
            int X = position.getX();
            int Y = position.getY();
            if (attack.contains(pawn) && pawn.isWhite() == whiteTurn) {
                if (chosenField.isSet()) {
                    if (X != prevX || Y != prevY) {
                        deleteHighlightBoard();
                        showAttackOption();
                        setHighlightOrange();
                        chosenField.set(X, Y);
                        showOptionForAttack();
                    } else {
                        deleteHighlightBoard();
                        showAttackOption();
                        chosenField.unset();
                    }
                } else {
                    setHighlightOrange();
                    chosenField.set(X, Y);
                    showOptionForAttack();
                }
            }
        }

        private void attackSecondClick() {
            int prevX = chosenField.getX();
            int prevY = chosenField.getY();
            for (LinkedList<Pair> path : board[prevX][prevY].pawn.getPossibleAction() )
            {
                if (path.get(1).equals(position)){
                    attack.clear();
                    attack(board[prevX][prevY].pawn, position);
                    chosenField.set(position.getX(), position.getY());
                    if(!updateAttack(chosenField)) {
                        attack.add(pawn);
                        showAttackOption();
                        chosenField.unset();
                        attackFirstClick();
                    }
                    else{
                        levelUp(board[position.getX()][position.getY()].pawn);
                        chosenField.unset();
                        if (whiteTurn)
                            drawWhite = 0;
                        else
                            drawBlack = 0;
                        changeTurn();
                        deleteHighlightBoard();
                        actionAI();
                    }
                    break;
                }

            }
        }


        void showOptionForAttack(){
            int x,y;
            for(LinkedList<Pair> path: pawn.getPossibleAction()) {
                x = path.get(1).getX();
                y = path.get(1).getY();
                board[x][y].setHighlightOrange();
            }
        }

        boolean updateAttack(Pair field){
            LinkedList<Pair> current;
            Iterator<LinkedList<Pair>> itr = pawn.getPossibleAction().iterator();
            while (itr.hasNext()) {
                current = itr.next();
                current.poll();
                if (!(current.peek()).equals(field) || current.size() == 1){
                    itr.remove();
                }
            }
            return pawn.getPossibleAction().size() == 0;
        }
    }


    Board(ImageView[][] boardMain, Activity activity) {
        isCopy = false;
        isAlert = false;
        chosenField = new Pair();
        this.activity = activity;
        drawWhite = 0;
        drawBlack = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Field(i, j, boardMain[i][j]);
            }
        }
    }
    Board(Board oldBoard, boolean isCopy) {
        whiteTurn = oldBoard.whiteTurn;
        chosenField = new Pair();
        drawWhite = 0;
        drawBlack = 0;
        isAlert = oldBoard.isAlert;
        this.isCopy = isCopy;
        this.activity = oldBoard.activity;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Field(oldBoard.board[i][j]);
            }
        }
        for(int i =0 ;i<12;i++)
        {
            if(oldBoard.whitePawns[i] != null){
                whitePawns[i]= new Pawn(oldBoard.whitePawns[i]);
            }
            if(oldBoard.blackPawns[i] != null){
                blackPawns[i]= new Pawn(oldBoard.blackPawns[i]);
            }
        }
        attack = new PriorityQueue<>();
        highlights = new LinkedList<>();
    }

    Pawn[] getWhitePawns() {
        return whitePawns;
    }

    Pawn[] getBlackPawns() {
        return blackPawns;
    }

    private void deleteHighlightBoard(){
        Pair highlight;
        int x, y;
        while(highlights.peek() != null){
            highlight = highlights.poll();
            x = highlight.getX();
            y = highlight.getY();
            board[x][y].deleteHighlightField();
        }
    }

    void changeTurn() {
        whiteTurn =! whiteTurn;
    }

    void start() {
        isAlert = false;
        whiteTurn = true;
        for (int i = 0; i < 4; i++) {
            board[2*i][0].setPawn(2*i, 0, true, PawnGraphics.WHITE_PAWN.get());
            board[2*i + 1][1].setPawn(2*i+1, 1, true, PawnGraphics.WHITE_PAWN.get());
            board[2*i][2].setPawn(2*i, 2, true,PawnGraphics.WHITE_PAWN.get());
            board[2*i + 1][5].setPawn(2*i+1, 5, false,PawnGraphics.BLACK_PAWN.get());
            board[2*i][6].setPawn(2*i, 6, false,PawnGraphics.BLACK_PAWN.get());
            board[2*i + 1][7].setPawn(2*i+1, 7, false,PawnGraphics.BLACK_PAWN.get());
            whitePawns[3*i] = new Pawn(board[2*i][0].pawn);
            whitePawns[3*i+1]= new Pawn(board[2*i + 1][1].pawn);
            whitePawns[3*i+2]= new Pawn(board[2*i][2].pawn);
            blackPawns[3*i] =  new Pawn(board[2*i+1][5].pawn);
            blackPawns[3*i+1]= new Pawn(board[2*i][6].pawn);
            blackPawns[3*i+2]= new Pawn(board[2*i+1][7].pawn);
        }
        possibleAction();
    }

    void possibleAction(){
        if(whiteTurn) {
            searchAttack(whitePawns);
            if(attack.size() > 0){
                showAttackOption();

            } else {
                searchMove(whitePawns);
            }
        }
        else {
            searchAttack(blackPawns);
            if(attack.size() > 0)
                showAttackOption();
            else
                searchMove(blackPawns);
        }

    }

    private void searchMove(Pawn[] pawns) {
        boolean isMove = false;
        for (int i = 0; i < 12; i++) {
            if (pawns[i] != null) {
                int x = pawns[i].getCurrentPosition().getX();
                int y = pawns[i].getCurrentPosition().getY();
                board[x][y].pawn.setPossibleActionEmpty();
                pawns[i].setPossibleActionEmpty();
                if (pawns[i].isKing()) {
                    int j = 1;
                    while (x + j < 8 && y + j < 8 && board[x + j][y + j].pawn == null) {
                        pawns[i].setPossibleMove(new Pair(x + j, y + j));
                        board[x][y].pawn.setPossibleMove(new Pair(x + j, y + j));
                        j++;
                    }

                    j = 1;
                    while (x - j > -1 && y + j < 8 && board[x - j][y + j].pawn == null) {
                        pawns[i].setPossibleMove(new Pair(x - j, y + j));
                        board[x][y].pawn.setPossibleMove(new Pair(x - j, y + j));
                        j++;
                    }

                    j = 1;
                    while (x - j > -1 && y - j > -1 && board[x - j][y - j].pawn == null) {
                        pawns[i].setPossibleMove(new Pair(x - j, y - j));
                        board[x][y].pawn.setPossibleMove(new Pair(x - j, y - j));
                        j++;
                    }

                    j = 1;
                    while (x + j < 8 && y - j > -1 && board[x + j][y - j].pawn == null) {
                        pawns[i].setPossibleMove(new Pair(x + j, y - j));
                        board[x][y].pawn.setPossibleMove(new Pair(x + j, y - j));
                        j++;
                    }
                } else {
                    if (whiteTurn) {
                        if (y < 7) {
                            if (x < 7 && board[x + 1][y + 1].pawn == null) {
                                pawns[i].setPossibleMove(new Pair(x + 1, y + 1));
                                board[x][y].pawn.setPossibleMove(new Pair(x + 1, y + 1));
                            }
                            if (x > 0 && board[x - 1][y + 1].pawn == null) {
                                pawns[i].setPossibleMove(new Pair(x - 1, y + 1));
                                board[x][y].pawn.setPossibleMove(new Pair(x - 1, y + 1));
                            }
                        }
                    } else {
                        if (y > 0) {
                            if (x < 7 && board[x + 1][y - 1].pawn == null) {
                                pawns[i].setPossibleMove(new Pair(x + 1, y - 1));
                                board[x][y].pawn.setPossibleMove(new Pair(x + 1, y - 1));
                            }
                            if (x > 0 && board[x - 1][y - 1].pawn == null) {
                                pawns[i].setPossibleMove(new Pair(x - 1, y - 1));
                                board[x][y].pawn.setPossibleMove(new Pair(x - 1, y - 1));
                            }
                        }
                    }
                }
                if (pawns[i].getAmountOfActions() > 0)
                    isMove = true;
            }
        }
        if (!isMove){;}
            //if(whiteTurn)
                //end(Result.RED.output);
            //else
               // end(Result.WHITE.output);
    }


    private void searchAttack(Pawn[] pawns){
        boolean empty = true;
        int priority;
        for (int i =0; i<12; i++)
        {
            if(pawns[i] != null){
                empty = false;
                int x = pawns[i].getCurrentPosition().getX();
                int y = pawns[i].getCurrentPosition().getY();
                pawns[i].setPossibleActionEmpty();
                if (pawns[i].isKing())
                    priority = pawns[i].setPossibleAttack(possibleAttackKing(pawns[i].getCurrentPosition(),new LinkedList<Pair>()));
                else
                    priority = pawns[i].setPossibleAttack(possibleAttack(pawns[i].getCurrentPosition(),new LinkedList<Pair>()));


                if(priority > 1)
                {
                    attack.add(pawns[i]);
                    board[x][y].pawn = new Pawn (pawns[i]);
                }
                else
                    pawns[i].setPossibleActionEmpty();
            }
        }
        if(empty) {
            if (whiteTurn)
               ;// end(Result.RED.output);
            else
              ;//  end(Result.WHITE.output);
        }
    }

    private LinkedList<LinkedList<Pair>> possibleAttack(Pair pawn, LinkedList<Pair> deleted){
        int x = pawn.getX();
        int y = pawn.getY();
        LinkedList<LinkedList<Pair>> help,outPossibleAttack = null;
        if(y < 6)
        {
            if(x <6 && board[x+1][y+1].pawn != null && board[x+1][y+1].pawn.isWhite() != whiteTurn
                    && board[x+2][y+2].pawn == null && !deleted.contains(new Pair(x+1,y+1))) {
                deleted.addLast(new Pair(x+1,y+1));
                outPossibleAttack = possibleAttack(new Pair(x+2, y+2), deleted);
                deleted.removeLast();
            }

            if(x >1 && board[x-1][y+1].pawn != null && board[x-1][y+1].pawn.isWhite() != whiteTurn
                    &&  board[x-2][y+2].pawn == null && !deleted.contains(new Pair(x-1,y+1))){
                deleted.addLast(new Pair(x-1,y+1));
                help = possibleAttack(new Pair(x-2, y+2), deleted);
                deleted.removeLast();
                if(outPossibleAttack != null){
                    if(outPossibleAttack.get(0).size() == help.get(0).size()){
                        outPossibleAttack.addAll(help);
                    }else if (outPossibleAttack.get(0).size() < help.get(0).size()){
                        outPossibleAttack = help;
                    }
                }else{
                    outPossibleAttack = help;
                }
            }
        }

        if(y > 1){
            if(x >1 && board[x-1][y-1].pawn != null && board[x-1][y-1].pawn.isWhite() != whiteTurn
                    &&  board[x-2][y-2].pawn == null && !deleted.contains(new Pair(x-1,y-1))){
                deleted.addLast(new Pair(x-1,y-1));
                help = possibleAttack(new Pair(x-2, y-2),deleted);
                deleted.removeLast();
                if(outPossibleAttack != null){
                    if(outPossibleAttack.get(0).size() == help.get(0).size()){
                        outPossibleAttack.addAll(help);
                    }else if (outPossibleAttack.get(0).size() < help.get(0).size()){
                        outPossibleAttack = help;
                    }
                }else{
                    outPossibleAttack = help;
                }
            }

            if(x <6 && board[x+1][y-1].pawn != null && board[x+1][y-1].pawn.isWhite() != whiteTurn
                    &&  board[x+2][y-2].pawn == null&& !deleted.contains(new Pair(x+1,y-1))) {
                deleted.addLast(new Pair(x+1,y-1));
                help = possibleAttack(new Pair(x+2, y-2),deleted);
                deleted.removeLast();
                if(outPossibleAttack != null){
                    if(outPossibleAttack.get(0).size() == help.get(0).size()){
                        outPossibleAttack.addAll(help);
                    }else if (outPossibleAttack.get(0).size() < help.get(0).size()){
                        outPossibleAttack = help;
                    }
                }else{
                    outPossibleAttack = help;
                }
            }

        }
        if (outPossibleAttack == null){
            outPossibleAttack = new LinkedList<>();
            outPossibleAttack.add(new LinkedList<Pair>());
        }
        for (int i = 0; i<outPossibleAttack.size(); i++)
            outPossibleAttack.get(i).addFirst(pawn);
        return outPossibleAttack;
    }

    private LinkedList<LinkedList<Pair>> possibleAttackKing(Pair pawn, LinkedList<Pair> deleted){
        int x = pawn.getX();
        int y = pawn.getY();
        int i = 1, j;
        LinkedList<LinkedList<Pair>> help,outPossibleAttack = null;

        while(x + i <7 && y + i < 7 ) {
            if (board[x + i][y + i].pawn != null)
            {
                if(board[x + i][y + i].pawn.isWhite() != whiteTurn
                        && board[x + i + 1][y + i + 1].pawn == null
                        && !deleted.contains(new Pair(x + i, y + i)))
                {
                    deleted.addLast(new Pair(x + i, y + i));
                    j = 1;
                    while(x + i + j < 8 && y + i + j < 8 && board[x+i+j][y+i+j].pawn == null){
                        help = possibleAttackKing(new Pair(x + i + j, y + i + j), deleted);
                        if (outPossibleAttack != null)
                        {
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
        while(x - i > 0 && y + i < 7) {
            if (board[x - i][y + i].pawn != null)
            {
                if (board[x - i][y + i].pawn.isWhite() != whiteTurn
                        && board[x - i - 1][y + i + 1].pawn == null
                        && !deleted.contains(new Pair(x - i, y + i)))
                {
                    deleted.addLast(new Pair(x - i, y + i));
                    j = 1;
                    while(x - i - j > -1 && y + i + j < 8 && board[x-i-j][y+i+j].pawn == null){
                        help = possibleAttackKing(new Pair(x - i - j, y + i + j), deleted);
                        if (outPossibleAttack != null)
                        {
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
        while(x - i > 0 && y - i > 0) {
            if (board[x - i][y - i].pawn != null)
            {
                if (board[x - i][y - i].pawn.isWhite() != whiteTurn
                        && board[x - i - 1][y - i - 1].pawn == null
                        && !deleted.contains(new Pair(x - i, y - i)))
                {
                    deleted.addLast(new Pair(x - i, y - i));
                    j = 1;
                    while(x - i - j > -1 && y - i - j > -1 && board[x-i-j][y-i-j].pawn == null ){
                        help = possibleAttackKing(new Pair(x - i - j, y - i - j), deleted);
                        if (outPossibleAttack != null)
                        {
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
        while(x + i < 7 && y - i > 0) {
            if (board[x + i][y - i].pawn != null)
            {
                if(board[x + i][y - i].pawn.isWhite() != whiteTurn
                        && board[x + i + 1][y - i - 1].pawn == null
                        && !deleted.contains(new Pair(x + i, y - i)))
                {
                    deleted.addLast(new Pair(x + i, y - i));
                    j = 1;
                    while(x + i + j < 8 && y - i - j > -1 && board[x+i+j][y-i-j].pawn == null ){
                        help = possibleAttackKing(new Pair(x + i + j, y - i - j), deleted);
                        if (outPossibleAttack != null)
                        {
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

        if (outPossibleAttack == null){
            outPossibleAttack = new LinkedList<>();
            outPossibleAttack.add(new LinkedList<Pair>());
        }
        for (i = 0; i<outPossibleAttack.size(); i++)
            outPossibleAttack.get(i).addFirst(pawn);
        return outPossibleAttack;
    }

    private void choseAttackOption(){
        Pawn option = null;
        PriorityQueue<Pawn> newAttack = new PriorityQueue<>();
        while(attack.peek() != null){
            if(option != null && option.getAmountOfActions()!= attack.peek().getAmountOfActions())
                break;
            option = attack.poll();
            newAttack.add(option);
        }
        attack = newAttack;
    }

    private void showAttackOption(){
        int x, y;
        choseAttackOption();
        for (Pawn pawn : attack) {
            x = pawn.getCurrentPosition().getX();
            y = pawn.getCurrentPosition().getY();
            if(board[x][y].image != null) {
                board[x][y].setHighlightRed();
            }
        }
    }

    void move(Pawn pawn, Pair destination){
        int x = pawn.getCurrentPosition().getX();
        int y = pawn.getCurrentPosition().getY();
        int dstX = destination.getX();
        int dstY = destination.getY();
        board[dstX][dstY].pawn = new Pawn(board[x][y].pawn);
        board[dstX][dstY].pawn.setCurrentPosition(destination);
        if (board[dstX][dstY].image != null) {
            board[dstX][dstY].image.setImageResource(PawnGraphics.get(whiteTurn, board[dstX][dstY].pawn.isKing()));
        }
        if(whiteTurn) {
            int idx = idxOfPawn(whitePawns, pawn.getCurrentPosition());
            whitePawns[idx].setCurrentPosition(destination);
        }else{
            int idx = idxOfPawn(blackPawns, pawn.getCurrentPosition());
            blackPawns[idx].setCurrentPosition(destination);
        }
        deleteHighlightBoard();
        board[x][y].deletePawn();
        levelUp(board[dstX][dstY].pawn);
    }

    private void attack(Pawn pawn, Pair destination){
        int currentX= pawn.getCurrentPosition().getX();
        int currentY= pawn.getCurrentPosition().getY();
        int destinationX = destination.getX();
        int destinationY = destination.getY();
        int x = currentX,y = currentY;
        if(currentX < destinationX )
            if (currentY < destinationY)
            {
                while(x <7 && y < 7)
                {
                    if (board[x][y].pawn != null && board[x][y].pawn.isWhite() != whiteTurn)
                        break;
                    x++;
                    y++;
                }
            }
            else
            {
                while(x <7 && y > 0)
                {
                    if (board[x][y].pawn != null && board[x][y].pawn.isWhite() != whiteTurn)
                        break;
                    x++;
                    y--;
                }
            }
        else
        if (currentY < destinationY)
        {
            while(x > 0 && y < 7)
            {
                if (board[x][y].pawn != null && board[x][y].pawn.isWhite() != whiteTurn)
                    break;
                x--;
                y++;
            }
        }
        else
        {
            while(x > 0 && y > 0)
            {
                if (board[x][y].pawn != null && board[x][y].pawn.isWhite() != whiteTurn)
                    break;
                x--;
                y--;
            }
        }
        move(pawn,destination);
        delete(new Pair(x,y));
    }

    void attackAI(Pawn pawn, LinkedList<Pair> listOfDestination){
        int x,y;
        Pair destination;
        listOfDestination.remove();
        while (listOfDestination.size() > 0){
            destination = listOfDestination.poll();
            int destinationX = destination.getX();
            int destinationY = destination.getY();
            int currentX= pawn.getCurrentPosition().getX();
            int currentY= pawn.getCurrentPosition().getY();
            x = currentX;
            y = currentY;
            if(currentX < destinationX )
                if (currentY < destinationY)
                {
                    while(x <7 && y < 7)
                    {
                        if (board[x][y].pawn != null && board[x][y].pawn.isWhite() != whiteTurn)
                            break;
                        x++;
                        y++;
                    }
                }
                else
                {
                    while(x <7 && y > 0)
                    {
                        if (board[x][y].pawn != null && board[x][y].pawn.isWhite() != whiteTurn)
                            break;
                        x++;
                        y--;
                    }
                }
            else
            if (currentY < destinationY)
            {
                while(x > 0 && y < 7)
                {
                    if (board[x][y].pawn != null && board[x][y].pawn.isWhite() != whiteTurn)
                        break;
                    x--;
                    y++;
                }
            }
            else
            {
                while(x > 0 && y > 0)
                {
                    if (board[x][y].pawn != null && board[x][y].pawn.isWhite() != whiteTurn)
                        break;
                    x--;
                    y--;
                }
            }
            move(pawn,destination);
            pawn.setCurrentPosition(destination);
            delete(new Pair(x,y));
        }
        attack.clear();
        levelUp(pawn);
    }

    private void delete(Pair pawn){
        int x = pawn.getX();
        int y = pawn.getY();
        board[x][y].deletePawn();
        if(whiteTurn) {
            int idx = idxOfPawn(blackPawns, pawn);
            blackPawns[idx] = null;
        }else{
            int idx = idxOfPawn(whitePawns, pawn);
            whitePawns[idx]= null;
        }
    }

    private void levelUp(Pawn pawn){
        int X = pawn.getCurrentPosition().getX();
        int Y = pawn.getCurrentPosition().getY();
        if((Y == 7 && whiteTurn) || (Y == 0 && !whiteTurn)){
            board[X][Y].pawn.setKing();
            if(whiteTurn) {
                int idx = idxOfPawn(whitePawns, pawn.getCurrentPosition());
                whitePawns[idx].setKing();
            }else{
                int idx = idxOfPawn(blackPawns, pawn.getCurrentPosition());
                blackPawns[idx].setKing();
            }
            if(board[X][Y].image != null) {
                board[X][Y].image.setImageResource(PawnGraphics.get(whiteTurn, board[X][Y].pawn.isKing()));
            }
        }

    }

    private int idxOfPawn(Pawn[] pawns, Pair position)
    {
        for(int i = 0; i< 12; i++)
        {
            if(pawns[i]!= null && position.equals(pawns[i].getCurrentPosition()))
                return i;
        }
        return -1;
    }

    private void end(String output) {
        if (!isAlert && !isCopy) {
            isAlert = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("THE END");
            builder.setMessage(output);
            builder.setPositiveButton("Once again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int something) {
                    clean();
                    start();
                }
            });
            builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int something) {
                    activity.finish();
                    System.exit(0);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    void clean() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j].deletePawn();
            }
        }
    }

    LinkedList<Move> allMoves(final Pawn[] pawns) {
        LinkedList<Move> allMoves = new LinkedList<>();
        if (attack.size() > 0) {
            choseAttackOption();
            for (Pawn p : attack) {
                for (LinkedList<Pair> move : p.getPossibleAction()) {
                    allMoves.add(new Move(p, move));
                }
            }
        } else {
            for (int i = 0; i < 12; i++) {
                if (pawns[i] != null) {
                    for (int j = 0; j < pawns[i].getAmountOfActions();  j++) {
                        allMoves.add(new Move(pawns[i], pawns[i].getPossibleAction().get(j)));
                    }
                }
            }
        }
        return allMoves;
    }

    private void actionAI(){
        possibleAction();
        Ai computer = new Ai(this);
        Move move = computer.getMove();
        if(move.getDestination().size() > 1){
            attackAI(move.getPawn(), move.getDestination());
        } else{
            move(move.getPawn(), move.getDestination().get(0));
        }
        changeTurn();
        possibleAction();
    }


}
