package org.example.plantdisease.controller.impl;


import lombok.RequiredArgsConstructor;
import org.example.plantdisease.aop.CheckPermission;
import org.example.plantdisease.controller.UserController;
import org.example.plantdisease.entity.User;
import org.example.plantdisease.enums.Permission;
import org.example.plantdisease.payload.ApiResult;
import org.example.plantdisease.payload.ChangeRoleDTO;
import org.example.plantdisease.payload.UserDTO;
import org.example.plantdisease.service.UserService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    private final UserService userService;

    @Override
    public ApiResult<UserDTO> me(User user) {
        return userService.me(user);
    }

    @CheckPermission(permissions = {Permission.GET_ALL_USERS})
    @Override
    public ApiResult<?> getAllUsers() {
        return userService.getAllUsers();

    }

    @CheckPermission(permissions = {Permission.GET_ALL_ROLES})
    @Override
    public ApiResult<?> getAllRole() {
        return userService.getAllRole();
    }

    @CheckPermission(permissions = {Permission.CHANGE_USER_ROLE})
    @Override
    public ApiResult<?> changeUserRole(ChangeRoleDTO changeRoleDTO) {
        return userService.changeUserRole(changeRoleDTO);
    }
}
