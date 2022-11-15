package com.povobolapo.organizer.controller.model.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRequestBody {

    @Size(max = 32)
    @NotNull
    private String login;

    @Size(max = 32)
    private String password;

    @Email
    @Size(max = 32)
    private String mail;

    @Size(max = 64)
    private String name;

    @Schema(description = "Передавать contentInfoId после загрузки контента на сервер")
    private String avatar;
}
