package com.danis.exception;

import java.sql.SQLException;

public class DaoException extends RuntimeException  {
    public DaoException(SQLException throwables) {
        super(throwables);
    }
}
