public class HandValue {
    private int atk, def;

    public HandValue(int atk, int def) {
        setAtk(atk);
        setDef(def);
    }

    public HandValue(){
        this.atk = 0;
        this.def = 0;
    }


    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        if (atk<0){
            this.atk = 0;
            return;
        }
        this.atk = atk;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        if (def<0){
            this.def = 0;
            return;
        }
        this.def = def;
    }

    public String toString(){
        return String.format("%dATK x %dDEF",atk,def);
    }
}
