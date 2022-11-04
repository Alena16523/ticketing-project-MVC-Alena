package com.cydeo.mapper;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    private final ModelMapper projectMapper;

    public ProjectMapper(ModelMapper projectMapper) {
        this.projectMapper = projectMapper;
    }

    public Project convertToEntity(ProjectDTO dto){
        return projectMapper.map(dto, Project.class);
    }

    public ProjectDTO convertToDto(Project entity){
        return projectMapper.map(entity, ProjectDTO.class);
    }


}
