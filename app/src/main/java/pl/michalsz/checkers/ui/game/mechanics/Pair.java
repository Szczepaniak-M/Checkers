package pl.michalsz.checkers.ui.game.mechanics;

import android.os.Parcel;
import android.os.Parcelable;

final class Pair implements Parcelable {
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

    int getX() {
        return X;
    }

    int getY() {
        return Y;
    }

    void set(Pair pair) {
        set = true;
        this.X = pair.X;
        this.Y = pair.Y;
    }

    void set(int x, int y) {
        set = true;
        X = x;
        Y = y;
    }

    void unset() {
        set = false;
    }

    boolean isSet() {
        return set;
    }

    @Override
    public String toString() {
        return X + " " + Y;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.set ? (byte) 1 : (byte) 0);
        dest.writeInt(this.X);
        dest.writeInt(this.Y);
    }

    private Pair(Parcel in) {
        this.set = in.readByte() != 0;
        this.X = in.readInt();
        this.Y = in.readInt();
    }

    public static final Parcelable.Creator<Pair> CREATOR = new Parcelable.Creator<Pair>() {
        @Override
        public Pair createFromParcel(Parcel source) {
            return new Pair(source);
        }

        @Override
        public Pair[] newArray(int size) {
            return new Pair[size];
        }
    };
}


