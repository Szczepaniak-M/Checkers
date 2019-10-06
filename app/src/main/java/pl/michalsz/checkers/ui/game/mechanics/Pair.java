package pl.michalsz.checkers.ui.game.mechanics;

final class Pair {
    private boolean set;
    private int X;
    private int Y;

    Pair() {
        set = false;
    }

    Pair(int x, int y) {
        set = true;
        X = x;
        Y = y;
    }

    Pair(Pair oldPair) {
        set = oldPair.set;
        X = oldPair.X;
        Y = oldPair.Y;
    }

    @Override
    public String toString() {
        return X + " " + Y;
    }

    int getX() {
        return X;
    }

    int getY() {
        return Y;
    }

    void set(int x, int y) {
        set = true;
        X = x;
        Y = y;
    }

    void set(Pair pair) {
        set = true;
        this.X = pair.X;
        this.Y = pair.Y;
    }

    void unset() {
        set = false;
    }

    boolean isSet() {
        return set;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return ((Pair) o).X == X && ((Pair) o).Y == Y;
    }
}


