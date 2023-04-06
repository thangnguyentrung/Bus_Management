package com.bus.busmanagement.DataAccess;

import javafx.collections.ObservableList;


public interface BusManagement<T> {
    public int add(T t);
    public int update(T t);
    public int delete(T t);
    public ObservableList<T> getAll();
    public T getByID(int id);
}
