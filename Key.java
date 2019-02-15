public class Key extends Weapon {

    private ItemType type;
    private String code;
    private Stats stats;
    
    public Key(String name, String code) {
        super(name);
        this.type = ItemType.KEY;
    }

    public void put(ServerThread player) {
        player.setWeapon(this);
        player.getStats().increaseAttack(0);
        player.getStats().increaseDefence(0);
    }

    public void remove(ServerThread player) {
        player.getStats().decreaseAttack(0);
        player.getStats().decreaseDefence(0);
        player.setWeapon(null);
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }
}
