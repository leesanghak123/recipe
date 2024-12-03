package com.sang.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sang.recipe.model.Board;

public interface BoardRepository extends JpaRepository<Board, Integer>{

}