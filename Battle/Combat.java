package Battle;

import Person.Person;
import PokerHands.*;

public class Combat {
    public static void resolveRound(Person p1, HandValue h1, Person p2, HandValue h2) {
        Person attacker, defender;
        HandValue atkHand, defHand;

        if (h1.compareTo(h2) > 0) {
            attacker = p1; defender = p2;
            atkHand = h1; defHand = h2;
        } else if (h2.compareTo(h1) > 0) {
            attacker = p2; defender = p1;
            atkHand = h2; defHand = h1;
        } else {
            System.out.println("It's a draw! No damage dealt.-----------------------------------------------------------------------------------------------------%n");
            return;
        }

        int atkValue = (int)(attacker.getTable().getTypeAtk(atkHand.gethandType()) * attacker.getAtkMult());
        int defValue = (int)(defender.getTable().getTypeDef(defHand.gethandType()) * defender.getDefMult());
        int damage = Math.max(0, atkValue - defValue);

        defender.changeChips(-damage);

        System.out.printf(
                "%s attacks %s! %s (%sDMG) → %s (%sBLK)%n",
                attacker.getName(), defender.getName(),
                atkHand.getHandType(), atkValue,
                defHand.getHandType(), defValue
        );
        System.out.printf("→ Damage dealt: %d%n", damage);
        System.out.printf("%s's remaining chips: %d/%d%n-----------------------------------------------------------------------------------------------------%n",
                defender.getName(), defender.getCurrentChips(), defender.getBaseChips());
    }
}