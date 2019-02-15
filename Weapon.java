public abstract class Weapon extends Item {

    protected String name;
    protected ItemType type;

    public Weapon (String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }
}