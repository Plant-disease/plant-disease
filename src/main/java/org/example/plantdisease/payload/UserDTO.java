package org.example.plantdisease.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO implements Serializable {

    private UUID id;
    private String fullName;
    private String roleName;
    private String username;

//    private String email;
//    private String phoneNumber;

}
