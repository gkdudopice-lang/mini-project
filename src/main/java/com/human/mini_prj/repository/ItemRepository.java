package com.human.mini_prj.repository;

import com.human.mini_prj.constant.ItemSellStatus;
import com.human.mini_prj.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> { // JPA 설계명세
    // 기본적인 CRUD를 모두 포함하고 있음

    // 상품명으로 상품검색
    List<Item> findByItemNm(String itemNm); // findBy = 변수이름, sql : select * from item where item_nm = ?

    // 1. Or; 상품명 또는 상품 상세 설명으로 검색
    // SQL: where item_nm = ? or item_detail = ?
    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

    // 2. LessThan : 가격이 특정 값보다 작은 상품
    // SQL : where price < ?
    List<Item> findByPriceLessThan(Integer price);

    // 3. LessThan + OrderBy : 가격이 특정 값보다 작은 상품을 가격 내림차순으로
    // SQL : where price < ? order by price desc
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    // 4. Between : 가격 범위 검색 (양 끝 값 포함)
    // SQL : where price between ? and ?
    List<Item> findByPriceBetween(Integer startPrice, Integer endPrice);

    // 5. Containing : 상품명에 특정 문자열이 포함된 상품
    // SQL : where item_nm like '%?%'
    List<Item> findByItemNmContaining(String itemNm);

    // 6. Enum 필드 검색 : 판매 상태로 검색
    // SQL : where item_sell_status = ?
    List<Item> findByItemSellStatus(ItemSellStatus itemSellStatus);

    // 7. And + GreaterThanEqual : 판매 상태가 같고 가격이 특정 값 이상인 상품
    // SQL : where item_sell_status = ? and price >= ?
    List<Item> findByItemSellStatusAndPriceGreaterThanEqual(ItemSellStatus itemSellStatus, Integer price);

    // 8. Top + OrderBy : 가장 비싼 상품 3개
    // SQL : order by price desc limit 3
    List<Item> findTop3ByOrderByPriceDesc();

    // 9. In : 가격이 목록 중 하나에 해당하는 상품
    // SQL : where price in (?, ?, ...)
    List<Item> findByPriceIn(List<Integer> prices);

    // 10. count : 판매 상태별 상품 개수 (List가 아니라 long 반환)
    // SQL : select count(*) from item where item_sell_status = ?
    long countByItemSellStatus(ItemSellStatus itemSellStatus);

    // [이동 완료 위치] JPQL 쿼리
    @Query("SELECT i FROM Item i WHERE i.itemDetail LIKE %:itemDetail% ORDER BY i.price DESC")
    List<Item> findByItemDetailLike(@Param("itemDetail") String itemDetail);

    // [이동 완료 위치] 네이티브 쿼리
    @Query(value = "SELECT * FROM item WHERE item_detail LIKE %:itemDetail% ORDER BY price DESC", nativeQuery = true)
    List<Item> findByItemDetailNative(@Param("itemDetail") String itemDetail);

}

