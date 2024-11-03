package me.rochblondiaux.supermarioworld.configuration;

import java.util.Map;

import lombok.Data;

@Data
public class LevelInformation {

    private int id;
    private String name;
    private String minimap;
    private Map<String, String> maps;

}
