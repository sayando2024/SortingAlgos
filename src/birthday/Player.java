package birthday;

import java.util.*;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Player {
    private String name;
    int score = 0;
    private int bonus;

    public Player(String str) {
        name = str;
    }

    public int getBonus() {
        bonus = (int) (Math.random() * 8);
        return bonus;
    }
    public int getRoundScore(int cupPoints) {
        int roundScore = cupPoints + getBonus();
        return roundScore;
    }
}
