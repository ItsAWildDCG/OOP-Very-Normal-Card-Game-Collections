package Battle;

import Person.*;
import PokerHands.*;

public class Combat {
    public static void resolveRound(Person player, HandValue h1, Person enemy, HandValue h2){
        Person attacker, defender;
        HandValue atkHand, defHand;
        if (player.getState().equals("Fold")){
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
            System.out.println("It's a draw! No damage dealt.\n-----------------------------------------------------------------------------------------------------");
            try {
                Thread.sleep(1000);  // 1.5 seconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return;
        }

        int atkValue = (int)(attacker.getTable().getTypeAtk(atkHand.gethandType()) * attacker.getAtkMult());
        int defValue = (int)(defender.getTable().getTypeDef(defHand.gethandType()) * defender.getDefMult());

        if (attacker.isPlayer()){
            if (attacker.getState().equals("Raise")){
                atkValue = (int)(atkValue*1.5);
                System.out.println("Raise successful! Damage dealt boosted.");
            }
        }
        else{
            if (defender.getState().equals("Fold")){
                defValue = defValue*3;
                System.out.print("Folded. Spent 5 chips to reduce damage this turn. ");
                defender.changeChips(-5);
                System.out.printf("(Remaining: %d/%d)\n", defender.getCurrentChips(), defender.getBaseChips());

            }
            else if (defender.getState().equals("Raise")){
                System.out.print("Raise failed... Lost 20 to the wager. ");
                defender.changeChips(-20);
                System.out.printf("(Remaining: %d/%d)\n", defender.getCurrentChips(), defender.getBaseChips());
            }
        }
        try {
            Thread.sleep(1000);  // 1.5 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
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
        try {
            Thread.sleep(1000);  // 1.5 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}