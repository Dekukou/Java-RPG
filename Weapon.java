public abstract class Weapon extends Item {

    protected String name;
    protected ItemType type;
    protected String code = null;
    
    public Weapon (String name) {
        this.name = name;
    }

    public Weapon (String name, String code) {
        this.name = name;
	this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
	return code;
    }

    public void setName (String name) {
        this.name = name;
    }
}