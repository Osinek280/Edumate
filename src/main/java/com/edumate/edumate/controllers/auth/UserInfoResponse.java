package com.edumate.edumate.controllers.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoResponse {
  private String email;
  private String firstname;
}
