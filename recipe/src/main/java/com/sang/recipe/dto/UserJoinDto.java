package com.sang.recipe.dto;

import java.sql.Timestamp;

import com.sang.recipe.model.RoleType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinDto {
	private String username;
	private String password;
	private String email;
	private RoleType role;
}