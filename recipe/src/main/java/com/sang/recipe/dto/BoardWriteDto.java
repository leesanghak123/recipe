package com.sang.recipe.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardWriteDto {
    @NotEmpty(message = "제목은 필수 입력입니다.")
    private String title;

    @NotEmpty(message = "내용은 필수 입력입니다.")
    private String content;
}