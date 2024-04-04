package com.drosteofficial.data;

import java.sql.SQLException;

public interface ISerializable<T, R> {

    T deserializeData(R r)throws SQLException;

}
