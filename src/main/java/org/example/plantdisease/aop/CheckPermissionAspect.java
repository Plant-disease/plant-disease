package org.example.plantdisease.aop;


import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.plantdisease.entity.User;
import org.example.plantdisease.exception.RestException;
import org.example.plantdisease.service.impl.UserServiceImpl;
import org.example.plantdisease.utils.CommonUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
public class CheckPermissionAspect {
    private final UserServiceImpl userServiceImpl;

    @Before(value = "@annotation(checkPermission)")
    public void checkRoleExecutor(CheckPermission checkPermission) {
        User user = CommonUtils.getUserFromSecurityContext();
        if (Objects.isNull(user))
            throw RestException.noAuthority();

        userServiceImpl.checkPermission(Arrays.asList(checkPermission.permissions()), user);
    }
}
