package Battle;

import Person.*;
import PokerHands.*;

public class Combat {
    public static void resolveRound(Player player, HandValue h1, Person enemy, HandValue h2, String playerAction, int wager){
        Person attacker, defender;
        HandValue atkHand, defHand;

        if (playerAction.equals("Fold")){
            attacker = enemy; defender = player;
            atkHand = h2; defHand = h1;
        }
        else if (h1.compareTo(h2) > 0) {
            attacker = player; defender = enemy;
            atkHand = h1; defHand = h2;
        } else if (h2.compareTo(h1) > 0) {
            attacker = enemy; defender = player;
            atkHand = h2; defHand = h1;
        } else {
            if (wager > 0) {
                player.changeChips(wager);
                System.out.printf("Draw! Wager of %d chips returned.\n", wager);
            }
            System.out.println("It's a draw! No damage dealt.\n-----------------------------------------------------------------------------------------------------");
            return;
        }

        int atkValue = (int)(attacker.getTable().getTypeAtk(atkHand.gethandType()) * attacker.getAtkMult());
        int defValue = (int)(defender.getTable().getTypeDef(defHand.gethandType()) * defender.getDefMult());

        if (attacker.isPlayer()){
            if (playerAction.equals("Raise")){
                atkValue = (int)(atkValue*1.5);
                player.changeChips(wager);
                System.out.println("Raise successful! Damage dealt boosted.");
            }
            if (player.getVampbuff()>0){
                player.changeChips(atkValue);
                System.out.printf("Lifesteal activated! Regained %d chips\n", atkValue);
                player.changeVampbuff(-1);
            }
        }
        else{
            if (playerAction.equals("Fold")){
                defValue = defValue*3;
                System.out.print("Folded. Spent 5 chips to reduce damage this turn. ");

            }
            else if (playerAction.equals("Raise")){
                System.out.print("Raise failed... Lost 20 to the wager. ");
            }
                        
        }
        int damage = Math.max(0, atkValue - defValue);

        defender.changeChips(-damage);

        System.out.printf(
                "%s attacks %s! %s (%sDMG) → %s (%sBLK)%n",
                attacker.getName(), defender.getName(),
                atkHand.getHandType(), atkValue,
                defHand.getHandType(), defValue
        );
        System.out.printf("→ Damage dealt: %d%n", damage);
        System.out.printf("%s's remaining chips: %d/%d\n-----------------------------------------------------------------------------------------------------\n",
                defender.getName(), defender.getCurrentChips(), defender.getBaseChips());
    }
}