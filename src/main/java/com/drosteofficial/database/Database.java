package com.drosteofficial.database;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class Database {

    public  abstract void init();
    public abstract Connection GetConnection() throws SQLException;





}
