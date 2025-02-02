package com.sang.recipe.model;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Board {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotBlank
	@Column(nullable = false, length = 100)
	private String title;
	
	@NotBlank
	@Column(columnDefinition = "longtext")
	private String content;
	
	private int count;
	
	private int likes;
	
	private int replyCnt;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId") // 칼럼 이름
	private User user;
	
	@OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties({"board", "user"})
	@OrderBy("id desc")
	private List<Reply> reply;
	
	@CreationTimestamp
	private Timestamp createDate;
	
	@Version
	private Long version;
}
