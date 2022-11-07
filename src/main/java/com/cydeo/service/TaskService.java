package com.cydeo.service;

import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Task;

import java.util.List;

public interface TaskService {

    void save(TaskDTO taskDTO);
    void update(TaskDTO taskDTO);
    void delete(Long id);

    List<TaskDTO> listAllTasks();

    TaskDTO findById(Long Id);

    int totalNonCompletedTask(String projectCode);
    int totalCompletedTask(String projectCode);


}
