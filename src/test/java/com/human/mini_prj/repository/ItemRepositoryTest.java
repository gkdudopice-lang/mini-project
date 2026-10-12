package com.human.mini_prj.repository;

import com.human.mini_prj.constant.ItemSellStatus;
import com.human.mini_prj.entity.Item;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@SpringBootTest
@Transactional
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    // 테스트용 데이터 대량 생성 메서드
    public void createTestData() {
        for (int i = 1; i <= 10; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(1000 * i); // 1000원부터 10000원까지 설정
            item.setItemDetail("테스트 상품 상세 설명" + i);

            // 짝수 번호는 품절(SOLD_OUT), 홀수 번호는 판매중(SELL)으로 설정
            if (i % 2 == 0) {
                item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            } else {
                item.setItemSellStatus(ItemSellStatus.SELL);
            }

            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품 Or 검색 테스트 (1번)")
    public void findByItemNmOrItemDetailTest() {
        this.createTestData();
        List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품1", "테스트 상품 상세 설명5");
        for (Item item : itemList) {
            log.info("결과: {}", item.toString());
        }
    }

    @Test
    @DisplayName("가격 LessThan 테스트 (2번)")
    public void findByPriceLessThanTest() {
        this.createTestData();
        List<Item> itemList = itemRepository.findByPriceLessThan(5000);
        for (Item item : itemList) {
            log.info("결과: price = {}", item.getPrice());
        }
    }

    @Test
    @DisplayName("가격 LessThan 및 정렬 테스트 (3번)")
    public void findByPriceLessThanOrderByPriceDescTest() {
        this.createTestData();
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(8000);
        for (Item item : itemList) {
            log.info("결과: price = {}", item.getPrice());
        }
    }

    @Test
    @DisplayName("가격 Between 테스트 (4번)")
    public void findByPriceBetweenTest() {
        this.createTestData();
        List<Item> itemList = itemRepository.findByPriceBetween(3000, 7000);
        for (Item item : itemList) {
            log.info("결과: price = {}", item.getPrice());
        }
    }

    @Test
    @DisplayName("상품명 Containing (Like) 테스트 (5번)")
    public void findByItemNmContainingTest() {
        this.createTestData();
        List<Item> itemList = itemRepository.findByItemNmContaining("상품3");
        for (Item item : itemList) {
            log.info("결과: name = {}", item.getItemNm());
        }
    }

    @Test
    @DisplayName("판매 상태(Enum) 검색 테스트 (6번)")
    public void findByItemSellStatusTest() {
        this.createTestData();
        List<Item> itemList = itemRepository.findByItemSellStatus(ItemSellStatus.SELL);
        for (Item item : itemList) {
            log.info("결과: name = {}, status = {}", item.getItemNm(), item.getItemSellStatus());
        }
    }

    @Test
    @DisplayName("And 및 GreaterThanEqual 테스트 (7번)")
    public void findByItemSellStatusAndPriceGreaterThanEqualTest() {
        this.createTestData();
        List<Item> itemList = itemRepository.findByItemSellStatusAndPriceGreaterThanEqual(ItemSellStatus.SELL, 5000);
        for (Item item : itemList) {
            log.info("결과: name = {}, price = {}, status = {}", item.getItemNm(), item.getPrice(), item.getItemSellStatus());
        }
    }

    @Test
    @DisplayName("Top 비싼 상품 3개 조회 테스트 (8번)")
    public void findTop3ByOrderByPriceDescTest() {
        this.createTestData();
        List<Item> itemList = itemRepository.findTop3ByOrderByPriceDesc();
        for (Item item : itemList) {
            log.info("결과: name = {}, price = {}", item.getItemNm(), item.getPrice());
        }
    }

    @Test
    @DisplayName("가격 In 검색 테스트 (9번)")
    public void findByPriceInTest() {
        this.createTestData();
        List<Integer> priceList = Arrays.asList(2000, 4000, 6000);
        List<Item> itemList = itemRepository.findByPriceIn(priceList);
        for (Item item : itemList) {
            log.info("결과: price = {}", item.getPrice());
        }
    }

    @Test
    @DisplayName("판매 상태별 개수 카운트 테스트 (10번)")
    public void countByItemSellStatusTest() {
        this.createTestData();
        long count = itemRepository.countByItemSellStatus(ItemSellStatus.SELL);
        log.info("판매중(SELL)인 상품 개수: {}", count);
    }

    @Test
    @DisplayName("native query 조회")
    public void findByItemDetailNativeTest() {
        this.createTestData();
        // ItemRepository에 선언한 메서드 이름(findByItemDetailNative)과 일치시킵니다.
        // 데이터 생성 시 "테스트 상품 상세 설명"으로 저장했으므로 검색어 오타도 확인합니다.
        List<Item> itemList = itemRepository.findByItemDetailNative("테스트 상품 상세 설명1");

        for (Item item : itemList) {
            log.info("native query 조회: {}", item);
        }

        }


}