package com.example.samuraitravel.form;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewEditForm {
	
	@NotNull
	private Integer id;
	
	@NotNull(message = "評価を選択してください。")
	@Range(min = 1, max = 5, message = "1～5で評価してください。")
	private Integer rating;
	
	@NotBlank(message = "コメントを入力してください。")
	@Length(max = 300, message = "コメントは300文字以内にしてください。。")
	private String comments;
}
