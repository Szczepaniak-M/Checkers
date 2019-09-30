package org.pwanb.checkers.ui.game;

enum Result {

    WHITE("The WHITE player is winner"),
    RED("The RED player is winner"),
    DRAW("DRAW");

    final String output;

    Result(String output) { this.output = output;  }

}
