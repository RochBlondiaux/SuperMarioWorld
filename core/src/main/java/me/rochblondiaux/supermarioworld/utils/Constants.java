package me.rochblondiaux.supermarioworld.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    public static final float PPM = 45;
    public static final float GRAVITY = -9.81f;
    public static final float JUMP_FORCE = 4.5f;
    public static final float WALK_SPEED = 1.5f;
    public static final float RUN_SPEED = 2.5f;

    public static final float MAX_X_SPEED = 2.5f;
    public static final float MAX_Y_SPEED = 4.5f;

    public static final short BIT_GROUND = 2;
    public static final short BIT_ENTITIES = 4;
    public static final short BIT_PLAYER = 8;

}
