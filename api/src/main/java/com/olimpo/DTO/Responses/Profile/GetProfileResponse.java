package com.olimpo.DTO.Responses.Profile;

import com.olimpo.DTO.Responses.APIResponse;
import com.olimpo.Enums.AccountStatus;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GetProfileResponse extends APIResponse{
    private String username;
    private String tag;
    private String email;
    private String description;
    private String valorant_username;
    private String valorant_tag;
    private AccountStatus accountStatus;

    public GetProfileResponse(String username, String tag, String email, 
                                String description, String valorant_username, String valorant_tag, 
                                AccountStatus acc_status, String message) {
        super(message);
        this.username = username;
        this.tag = tag;
        this.email = email;
        this.description = description;
        this.valorant_username = valorant_username;
        this.valorant_tag = valorant_tag;
        this.accountStatus = acc_status;
    }
}
