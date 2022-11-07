package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProjectService projectService;
    private final TaskService taskService;


    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, @Lazy ProjectService projectService, @Lazy TaskService taskService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.projectService = projectService;
        this.taskService = taskService;
    }


    @Override
    public List<UserDTO> listAllUsers() {

        List<User> userList=userRepository.findAllIsDeletedOrderByFirstNameDesc(false);

         return userList.stream().map(userMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserName(String username) {
        return userMapper.convertToDto(userRepository.findByUserNameAndIsDeleted(username, false));
    }

    @Override
    public void save(UserDTO user) {
        userRepository.save(userMapper.convertToEntity(user));
    }

    @Override
    public void deleteByUserName(String username) {
        userRepository.deleteByUserName(username);
    }


    @Override
    public UserDTO update(UserDTO user) {

        //find current user - old user from DB
        User user1=userRepository.findByUserNameAndIsDeleted(user.getUserName(), false);

        //map update userDTO to entity object- updated user from UI
        User convertedUser=userMapper.convertToEntity(user);

        //set id to the converted object
        convertedUser.setId(user1.getId());

        //save the updated user in DB
        userRepository.save(convertedUser);

        return findByUserName(user.getUserName());
    }

    @Override
    public void delete(String username) {

        //go to db and get user with username
        User user = userRepository.findByUserNameAndIsDeleted(username, false);

        //checking if user can be deleted
        if (checkIfUserCanBeDeleted(user)) {
            //change the isDeleted field to true
            user.setIsDeleted(true);
            user.setUserName(user.getUserName()+"-"+user.getId());

            //save in the db updated object
            userRepository.save(user);
        }
    }

    @Override
    public List<UserDTO> listAllByRole(String role) {
        List<User> users = userRepository.findByRoleDescriptionIgnoreCaseAndIsDeleted(role, false);

        return users.stream().map(userMapper::convertToDto).collect(Collectors.toList());
    }

    private boolean checkIfUserCanBeDeleted(User user){
        switch(user.getRole().getDescription()){
            case "Manager":
                List<ProjectDTO> projectDTOList=projectService.listAllNonCompletedByAssignedManager(userMapper.convertToDto(user));
                return projectDTOList.size()==0;
            case "Employee":
                List<TaskDTO> taskDTOList=taskService.listAllNonCompletedByAssignedManager(userMapper.convertToDto(user));
                return taskDTOList.size()==0;
            default:
                return true;
        }
    }


}
