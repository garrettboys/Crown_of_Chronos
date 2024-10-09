package lol.millard;

import javax.swing.*;
import java.awt.*;

public class Character {

    protected int x, y, dx, dy, width, height, health, damage;
    protected ImageIcon sprite;
    protected ImageIcon portrait;

    public Character() {
        x = 0;
        y = 0;
        dx = 0;
        dy = 0;
        width = 0;
        height = 0;
        health = 0;
        damage = 0;
        sprite = null;
        portrait = null;
    }

    public String toString() {
        return getClass().getName();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public ImageIcon getSprite() {
        return sprite;
    }

    public void setSprite(ImageIcon sprite) {
        this.sprite = sprite;
    }

    public ImageIcon getPortrait() {
        return this.portrait;
    }
}


