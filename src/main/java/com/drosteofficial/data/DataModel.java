package com.drosteofficial.data;

public enum DataModel {
    MYSQL,
    SQLITE;

    public static DataModel fromString(String s){
        switch (s){
            case "MYSQL":
                return MYSQL;
            case "SQLITE":
                return SQLITE;
            default:
                return null;
        }
    }
}
