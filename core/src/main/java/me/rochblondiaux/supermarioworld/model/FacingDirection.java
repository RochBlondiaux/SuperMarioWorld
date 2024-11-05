package me.rochblondiaux.supermarioworld.model;

public enum FacingDirection {
    LEFT, RIGHT;

    public FacingDirection opposite() {
        return this == LEFT ? RIGHT : LEFT;
    }
}
