public class Stats {

    private int attack;
    private int defence;

    public Stats(){

    }

    public Stats(int attack, int defence) {
        this.attack = attack;
        this.defence = defence;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getAttack() {
        return this.attack;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public int getDefence() {
        return this.defence;
    }

    public void increaseDefence(int defence) {
        this.defence += defence;
    }

    public void increaseAttack(int attack) {
        this.attack += attack;
    }

    public void decreaseAttack(int attack) {
        this.attack = (this.attack - attack < 0 ? 0 : this.attack - attack);
    }

    public void decreaseDefence(int defence) {
        this.defence = (this.defence - defence < 0 ? 0 : this.defence - defence);
    }
}