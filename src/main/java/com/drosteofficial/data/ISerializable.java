package com.drosteofficial.data;

import java.sql.SQLException;

public interface ISerializable<T, R> {

    R serializeDAta(T t);
    T deserializeData(R r)throws SQLException;

}
