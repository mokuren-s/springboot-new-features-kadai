package com.example.samuraitravel.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.repository.HouseRepository;

@Controller
public class HomeController {
	private final HouseRepository houseRepository;
	
	public HomeController(HouseRepository houseRepository) {
		this.houseRepository = houseRepository;
	}
	
	@GetMapping("/")
	// Modelは画面と値のやり取りをするために必要なもの
	// model.addAttributeで画面に値を渡している
	//　※メソッドの引数にnewHousesを受け取っている場合は、model.addAttribute("newHouses")を省略できる
	public String index(Model model) {
		List<House> newHouses = houseRepository.findTop10ByOrderByCreatedAtDesc();
		model.addAttribute("newHouses", newHouses);
		
		// 次に表示させたいページを指定（index.html）
		return "index";
	}
}