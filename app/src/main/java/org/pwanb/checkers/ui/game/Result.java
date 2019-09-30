package org.pwanb.checkers.application;

enum Result {

    WHITE("The WHITE player is winner"),
    RED("The RED player is winner"),
    DRAW("DRAW");

    final String output;

    Result(String output) { this.output = output;  }

}
