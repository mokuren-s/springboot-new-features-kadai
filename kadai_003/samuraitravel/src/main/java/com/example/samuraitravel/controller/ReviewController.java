package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.ReviewService;

@Controller
@RequestMapping("/houses/{houseId}/reviews")
public class ReviewController {
	private final ReviewRepository reviewRepository;
	private final HouseRepository houseRepository;
	private final ReviewService reviewService;
	
	public ReviewController(ReviewRepository reviewRepository, HouseRepository houseRepository, ReviewService reviewService) {
		this.reviewRepository = reviewRepository;
		this.houseRepository = houseRepository;
		this.reviewService= reviewService;
	}
	
	// 対象となる宿のレビューを一覧表示
	@GetMapping
	public String index(@PathVariable(name = "houseId") Integer houseId,
			            @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
			            Model model) {
		// Houseクラスのhouseインスタンスを作成し、houseIdを用いてhouseRepositoryから取得
		House house = houseRepository.getReferenceById(houseId);
		// Page<Review>型のreviewPageインスタンスを作成し、houseとpageableを用いてreviewRepositoryから取得
		Page<Review> reviewPage = reviewRepository.findByHouseOrderByCreatedAtDesc(house, pageable);
		// それぞれのmodelに "house"・"reviewPage" をキーとして house・reviewPage の属性を追加
		model.addAttribute("house", house);
		model.addAttribute("reviewPage", reviewPage);
		// index.html に表示
		return "reviews/index";
	}
	
	// 対象となる宿のレビューの投稿フォームの表示
	@GetMapping("/register")
	public String register(@PathVariable(name = "houseId") Integer houseId, Integer rating, String comments,
			               Model model) {
		
		House house = houseRepository.getReferenceById(houseId);
        
		model.addAttribute("house", house);
        model.addAttribute("reviewRegisterForm", new ReviewRegisterForm(rating, comments));
        
        return "reviews/register";
	}
	
	// 対象となる宿のレビューを作成・送付して「投稿」の表示
	@PostMapping("/create")
	public String create(@PathVariable(name = "houseId") Integer houseId,
			             @AuthenticationPrincipal UserDetailsImpl userDetailsImpl, // ユーザー情報を紐づけ
			             @ModelAttribute @Validated ReviewRegisterForm reviewRegisterForm, // バリデートしてビューに渡す
			             BindingResult bindingResult,
			             RedirectAttributes redirectAttributes,
			             Model model) {
		
		House house = houseRepository.getReferenceById(houseId);
		User user = userDetailsImpl.getUser();
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("house", house);
			
            return "reviews/register";
		}
		
		reviewService.create(house, user, reviewRegisterForm);
		redirectAttributes.addFlashAttribute("successMessage", "レビューを投稿しました。");
		
		return "redirect:/houses/{houseId}";
	}
	
	// 対象となる宿のレビューを編集してURLに紐づける
	@GetMapping("/{reviewId}/edit")
	public String edit(@PathVariable(name = "houseId") Integer houseId,
			           @PathVariable(name = "reviewId") Integer reviewId,
			           Model model) {
		
		House house = houseRepository.getReferenceById(houseId);		
		Review review = reviewRepository.getReferenceById(reviewId);
		
		ReviewEditForm reviewEditForm = new ReviewEditForm(review.getId(), review.getRating(), review.getComments());
		
		model.addAttribute("house", house);
		model.addAttribute("review", review);
		model.addAttribute("reviewEditForm", reviewEditForm);
		
		return "reviews/edit";
	}
		
	// 対象となる宿のレビューを更新して「編集」の表示
	@PostMapping("/{reviewId}/update")
	public String update(@PathVariable(name = "houseId") Integer houseId,
			             @PathVariable(name = "reviewId") Integer reviewId,
			             //@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, 
			             @ModelAttribute @Validated ReviewEditForm reviewEditForm,
			             BindingResult bindingResult,
			             RedirectAttributes redirectAttributes,
			             Model model) {
		
		House house = houseRepository.getReferenceById(houseId);
		Review review = reviewRepository.getReferenceById(reviewId);
		//User user = userDetailsImpl.getUser();
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("house", house);
			model.addAttribute("review", review);
			
            return "reviews/edit";
        }
		
		reviewService.update(reviewEditForm);
		redirectAttributes.addFlashAttribute("successMessage", "レビューを編集しました。");
		
		return "redirect:/houses/{houseId}";
	}
	
	// 対象となる宿のレビューを削除する
	@PostMapping("/{reviewId}/delete")
	public String delete(@PathVariable(name = "reviewId") Integer reviewId,
			             RedirectAttributes redirectAttributes) {
		
		reviewRepository.deleteById(reviewId);
		
		redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました。");
		
		return "redirect:/houses/{houseId}";
	}
}
