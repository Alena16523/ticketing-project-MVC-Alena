package com.cydeo.service;

import com.cydeo.dto.RoleDTO;

import java.util.List;

public interface CrudService <T,ID>{

    T save(T role);
    T findById(ID userName);
    List<T> findAll();
    void deleteById(ID userName);

    void update(T object);
}
