package com.example.samuraitravel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewRegisterForm;
import com.example.samuraitravel.repository.ReviewRepository;

@Service
public class ReviewService {
	//private final HouseRepository houseRepository;
	//private final UserRepository userRepository;
	private final ReviewRepository reviewRepository;
	
	public ReviewService(//HouseRepository houseRepository, UserRepository userRepository, 
			             ReviewRepository reviewRepository) {
		//this.houseRepository = houseRepository;
		//this.userRepository = userRepository;
		this.reviewRepository = reviewRepository;
	}
	
	// 作成したレビューをDBに保存する
	@Transactional
	public void create(House house, User user, ReviewRegisterForm reviewRegisterForm) {
		Review review = new Review();
		
		//House house = houseRepository.getReferenceById(reviewRegisterForm.getHouseId());
		//User user = userRepository.getReferenceById(reviewRegisterForm.getUserId());
		
		review.setHouse(house);
		review.setUser(user);
		review.setRating(reviewRegisterForm.getRating());
		review.setComments(reviewRegisterForm.getComments());
		
		reviewRepository.save(review);
	}
	
	// レビューをDBに更新する
	public void update(ReviewEditForm reviewEditForm) {
		Review review = new Review();
		
		review.setRating(reviewEditForm.getRating());
		review.setComments(reviewEditForm.getComments());
		
		reviewRepository.save(review);
	}
	
	public boolean hasUserAlreadyReviewed(House house, User user) {
		return reviewRepository.findByHouseAndUser(house, user) != null;
	}
}
