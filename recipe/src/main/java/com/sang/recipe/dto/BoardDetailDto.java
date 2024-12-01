package com.sang.recipe.dto;

import java.sql.Timestamp;
import java.util.List;

import com.sang.recipe.dto.BoardDetailDto;
import com.sang.recipe.model.Reply;
import com.sang.recipe.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardDetailDto {
	private int id;
	private String title;
	private String content;
	private String username; // User의 username만
	private Timestamp createDate;
	private List<Reply> reply;
	private int count;
	private int likes;
}