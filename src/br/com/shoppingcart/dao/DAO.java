package br.com.shoppingcart.dao;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T> {
    void insert(T object) throws SQLException;
    List<T> getAll() throws SQLException;
    void update(T object) throws SQLException;
    void delete(int id) throws SQLException;
}
