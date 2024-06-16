package cartoland.mini_games;

import cartoland.utilities.Algorithm;

public class DragonGame extends MiniGame {
    private static int[] CARD_NUMBER = {0,0,0};

    public DragonGame() {
        Algorithm.randomCardNumber(CARD_NUMBER[0]);
        Algorithm.randomCardNumber(CARD_NUMBER[1]);
        
        //if the first and second card number are the same
        if (CARD_NUMBER[0] == CARD_NUMBER[1]) {
            
        } else { //if they are not the same
            Algorithm.randomCardNumber(CARD_NUMBER[2]);
        }
    }
    
    public 

    @Override
    public String gameName() {
        return "dragon_game";
    }
}