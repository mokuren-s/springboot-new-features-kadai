package com.example.samuraitravel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
	// ページネーション機能を実装するため、Page で取得
	public Page<Favorite> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
	// Favoriteエンティティとしてhouseとuser情報を取得
	public Favorite findByHouseAndUser(House house, User user);
}
