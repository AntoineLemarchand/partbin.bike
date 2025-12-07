package com.alemarch.partbin.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDto {
  private long id;
  private String name;
  private String email;
}
