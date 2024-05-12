package org.example.plantdisease.service.impl;


import lombok.RequiredArgsConstructor;
import org.example.plantdisease.entity.Role;
import org.example.plantdisease.entity.User;
import org.example.plantdisease.enums.Permission;
import org.example.plantdisease.enums.RoleType;
import org.example.plantdisease.exception.RestException;
import org.example.plantdisease.payload.ApiResult;
import org.example.plantdisease.payload.ChangeRoleDTO;
import org.example.plantdisease.payload.LongNameDTO;
import org.example.plantdisease.payload.UserDTO;
import org.example.plantdisease.repository.RoleRepository;
import org.example.plantdisease.repository.UserRepository;
import org.example.plantdisease.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public ApiResult<UserDTO> me(User user) {
        return ApiResult.successResponse(toUserDTO(user));
    }


    @Override
    public ApiResult<?> getAllUsers() {

        List<User> all = userRepository.findAllByRoleTypeNot(RoleType.SUPER_ADMIN);
        List<UserDTO> userDTOList = toUserDTO(all);

        return ApiResult.successResponse(userDTOList);
    }

    @Override
    public ApiResult<?> changeUserRole(ChangeRoleDTO changeRoleDTO) {

        User user = userRepository.findById(changeRoleDTO.getUserId()).orElseThrow(() -> RestException.notFound("USER"));
        Role role = roleRepository.findById(changeRoleDTO.getRoleId()).orElseThrow(() -> RestException.notFound("ROLE"));

        if (role.getType().equals(RoleType.SUPER_ADMIN))
            throw new RestException("YOU_CANNOT_CHANGE_TO_SUPER_ADMIN_ROLE", HttpStatus.BAD_REQUEST);

        user.setRole(role);
        userRepository.save(user);

        return ApiResult.successResponse("USER ROLE CHANGED SUCCESSFULLY");
    }

    @Override
    public ApiResult<?> getAllRole() {

        List<Role> roleList = roleRepository.findAllByTypeNot(RoleType.SUPER_ADMIN);
        List<LongNameDTO> roleDTOList = toRoleDTO(roleList);

        return ApiResult.successResponse(roleDTOList);
    }

    public void checkPermission(List<Permission> permissions, User user) {

        Set<Permission> userPermissions = user.getRole().getPermissions();

        for (Permission permission : permissions) {
            boolean contains = userPermissions.contains(permission);
            if (contains) return;
        }
        throw RestException.noAuthority();

    }

    public UserDTO toUserDTO(User user) {

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFullName(user.getFullName());
        userDTO.setUsername(user.getUsername());
        userDTO.setRoleName(user.getRole().getName());

        return userDTO;
    }

    public List<UserDTO> toUserDTO(List<User> userList) {

        List<UserDTO> userDTOList = new ArrayList<>();

        for (User user : userList) {

            userDTOList.add(toUserDTO(user));
        }

        return userDTOList;
    }

    public LongNameDTO toRoleDTO(Role role) {

       LongNameDTO longNameDTO = new LongNameDTO();
       longNameDTO.setId(role.getId());
       longNameDTO.setName(role.getName());

        return longNameDTO;
    }

    public List<LongNameDTO> toRoleDTO(List<Role> roleList) {

        List<LongNameDTO> longNameDTOList = new ArrayList<>();

        for (Role role : roleList) {

            longNameDTOList.add(toRoleDTO(role));
        }

        return longNameDTOList;
    }
}
