package org.example.plantdisease.service;


import org.example.plantdisease.entity.User;
import org.example.plantdisease.payload.ApiResult;
import org.example.plantdisease.payload.ChangeRoleDTO;
import org.example.plantdisease.payload.UserDTO;

public interface UserService {

    ApiResult<UserDTO> me(User user);

    ApiResult<?> getAllUsers();

    ApiResult<?> changeUserRole(ChangeRoleDTO changeRoleDTO);

    ApiResult<?> getAllRole();
}
