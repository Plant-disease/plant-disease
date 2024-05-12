package org.example.plantdisease.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenDTO {

    private String accessToken;
    private String refreshToken;
    private Boolean isAdmin = null;
    private Boolean isSuperAdmin = null;

//    @JsonProperty("isSuperAdmin")
//    @JsonInclude(JsonInclude.Include.NON_NULL)

    public TokenDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }


    public TokenDTO(String accessToken, String refreshToken, boolean isAdmin) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.isAdmin = isAdmin;
    }


    public TokenDTO(String accessToken, boolean isSuperAdmin, String refreshToken) {
        this.accessToken = accessToken;
        this.isSuperAdmin = isSuperAdmin;
        this.refreshToken = refreshToken;
    }
}
