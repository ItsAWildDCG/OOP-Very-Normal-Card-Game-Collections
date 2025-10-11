import java.util.HashMap;
import java.util.LinkedHashMap;

public class TypeTable {
    private HashMap<String, HandValue> table = new LinkedHashMap<>();

    public TypeTable() {
        table.put("High Card", new HandValue(5,3));
        table.put("Pair", new HandValue(10,3));
        table.put("Two Pair", new HandValue(20,5));
        table.put("Three of a Kind", new HandValue(30,5));
        table.put("Straight", new HandValue(30,6));
        table.put("Flush", new HandValue(35,7));
        table.put("Full House", new HandValue(40,8));
        table.put("Four of a Kind", new HandValue(60,15));
        table.put("Straight Flush", new HandValue(90,25));
        table.put("Five of a Kind", new HandValue(100,25));
        table.put("Flush House", new HandValue(150,50));
        table.put("Flush Five", new HandValue(200,50));
    }

    public void setType(String type, int atk, int def){
        if (table.containsKey(type)){
            table.get(type).setAtk(atk);
            table.get(type).setDef(def);
        }
        else if (!type.isEmpty()){
            table.put(type, new HandValue(atk, def));
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
