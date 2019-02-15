public class Sword extends Weapon {

    private ItemType type;
    private Stats stats;

    public Sword(String name) {
        super(name);
        this.type = ItemType.SWORD;
    }

    public void put(ServerThread player) {
        player.setWeapon(this);
        player.getStats().increaseAttack(stats.getAttack());
        player.getStats().increaseDefence(stats.getDefence());
    }

    public void remove(ServerThread player) {
        player.getStats().decreaseAttack(stats.getAttack());
        player.getStats().decreaseDefence(stats.getDefence());
        player.setWeapon(null);
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }
}