package com.tsfn.loaders.helper;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;


@Getter
@Setter
public class MapInfo {
    private String mapName;
    private Map<String, Integer> map;

    public MapInfo(String mapName, Map<String, Integer> map) {
        this.mapName = mapName;
        this.map = map;
    }


}