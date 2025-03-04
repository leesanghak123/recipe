package com.sang.recipe.dto;

import java.sql.Timestamp;

import com.sang.recipe.dto.BoardFindDto;
import com.sang.recipe.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardFindDto {
	private int id;
	private String title;
	private String username; // User의 username만
	private Timestamp createDate;
	private int replyCount;// 댓글수
	private int count;
	private int likes;
}