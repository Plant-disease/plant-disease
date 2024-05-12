package org.example.plantdisease.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor
@Getter
public enum Permission {

    //ROLE
    GET_ALl_ROLES(Set.of(RoleType.ADMIN,RoleType.SUPER_ADMIN)),
    GET_ROLE_BY_ID(Set.of(RoleType.ADMIN,RoleType.SUPER_ADMIN)),
    //EMPLOYEE,
    GET_ALL_USERS(Set.of(RoleType.SUPER_ADMIN)),
    GET_ALL_ROLES(Set.of(RoleType.SUPER_ADMIN)),
    CHANGE_USER_ROLE(Set.of(RoleType.SUPER_ADMIN)),
    //DISEASE,
    ADD_DISEASE(Set.of(RoleType.ADMIN,RoleType.SUPER_ADMIN)),
    GET_ALL_DISEASE(Set.of(RoleType.ADMIN,RoleType.GUEST,RoleType.SUPER_ADMIN)),
    EDIT_DISEASE(Set.of(RoleType.ADMIN,RoleType.SUPER_ADMIN)),
    DELETE_DISEASE(Set.of(RoleType.ADMIN,RoleType.SUPER_ADMIN)),
    //FRUIT
    ADD_FRUIT(Set.of(RoleType.ADMIN,RoleType.SUPER_ADMIN)),
    GET_ALL_FRUIT(Set.of(RoleType.ADMIN,RoleType.GUEST,RoleType.SUPER_ADMIN)),
    EDIT_FRUIT(Set.of(RoleType.ADMIN,RoleType.SUPER_ADMIN)),
    DELETE_FRUIT(Set.of(RoleType.ADMIN,RoleType.SUPER_ADMIN)),
    //LEAF_CONDITION
    ADD_LEAF_CONDITION(Set.of(RoleType.ADMIN,RoleType.SUPER_ADMIN)),
    GET_ALL_LEAF_CONDITION(Set.of(RoleType.ADMIN,RoleType.GUEST,RoleType.SUPER_ADMIN)),
    EDIT_LEAF_CONDITION(Set.of(RoleType.ADMIN,RoleType.SUPER_ADMIN)),
    DELETE_LEAF_CONDITION(Set.of(RoleType.ADMIN,RoleType.SUPER_ADMIN)),
    //TREATMENT
    ADD_TREATMENT(Set.of(RoleType.ADMIN,RoleType.SUPER_ADMIN)),
    GET_ALL_TREATMENT(Set.of(RoleType.ADMIN,RoleType.GUEST,RoleType.SUPER_ADMIN)),
    EDIT_TREATMENT(Set.of(RoleType.ADMIN,RoleType.SUPER_ADMIN)),
    DELETE_TREATMENT(Set.of(RoleType.ADMIN,RoleType.SUPER_ADMIN)),
    //LEAF
    ADD_LEAF_DATA(Set.of(RoleType.ADMIN,RoleType.SUPER_ADMIN)),
    CHECK_LEAF_DISEASE_BY_KNN(Set.of(RoleType.ADMIN,RoleType.GUEST,RoleType.SUPER_ADMIN)),
    DOWNLOAD_EXCEL(Set.of(RoleType.ADMIN,RoleType.GUEST,RoleType.SUPER_ADMIN)),

    ;

    private final Set<RoleType> roleTypes;

}
