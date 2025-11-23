package Battle;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class TypeTable {
    private HashMap<String, HandStats> table = new LinkedHashMap<>();

    public TypeTable() {
        table.put("None", new HandStats(0,0));
        table.put("High Cards", new HandStats(5,2));
        table.put("One Pair", new HandStats(10,3));
        table.put("Two Pair", new HandStats(20,5));
        table.put("Three of a Kind", new HandStats(30,5));
        table.put("Straight", new HandStats(30,6));
        table.put("Flush", new HandStats(35,7));
        table.put("Full House", new HandStats(40,8));
        table.put("Four of a Kind", new HandStats(60,15));
        table.put("Straight Flush", new HandStats(90,25));
        table.put("Five of a Kind", new HandStats(100,25));
    }

    public void setType(String type, int atk, int def){
        if (table.containsKey(type)){
            table.get(type).setAtk(atk);
            table.get(type).setDef(def);
        }
        else if (!type.isEmpty()){
            table.put(type, new HandStats(atk, def));
        }
    }

    public int getTypeAtk(String type){
        return table.get(type).getAtk();
    }

    public int getTypeDef(String type){
        return table.get(type).getDef();
    }

    public void display(){
        for (String i: table.keySet()){
            System.out.println(i + ":  " + table.get(i));
        }
    }
}
