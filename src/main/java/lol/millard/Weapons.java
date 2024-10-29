package lol.millard;

import javax.swing.ImageIcon;

public class Weapons {
    private String name;
    private int damage;
    private double weight;
    private ImageIcon sprite;

    public Weapons(String name, int damage, double weight, ImageIcon sprite) {
        this.name = name;
        this.damage = damage;
        this.weight = weight;
        this.sprite = sprite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public ImageIcon getSprite() {
        return sprite;
    }

    public void setSprite(ImageIcon sprite) {
        this.sprite = sprite;
    }

    @Override
    public String toString() {
        return "Weapon{" +
                "name='" + name + '\'' +
                ", damage=" + damage +
                ", weight=" + weight +
                '}';
    }
}