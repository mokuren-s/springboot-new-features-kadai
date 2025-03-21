package com.example.samuraitravel.form;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewRegisterForm {
	
	@NotNull(message = "評価を選択してください。")
	@Range(min = 1, max = 5, message = "評価は5段階のいずれかで選択してください。")
	private Integer rating;
	
	@NotBlank(message = "コメントを入力してください。")
	@Length(max = 300, message = "コメントは300文字以内で入力してください。。")
	private String comments;

	//private Integer HouseId;
	//private Integer UserId;
	
	// @AllArgsConstructor を使わないと以下が必要
	//public ReviewRegisterForm(Integer rating, String comments, Integer houseId, Integer userId) {
		//this.rating = rating;
		//this.comments = comments;
		//this.HouseId = houseId;
		//this.UserId = userId;
	//}
}