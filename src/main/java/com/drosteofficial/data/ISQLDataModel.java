package com.drosteofficial.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public interface ISQLDataModel<T, U> extends IData<T, U>, ISerializable<T, ResultSet> {

    @Override
    ResultSet serializeDAta(T t);

    @Override
    T deserializeData(ResultSet resultSet) throws SQLException;


}
