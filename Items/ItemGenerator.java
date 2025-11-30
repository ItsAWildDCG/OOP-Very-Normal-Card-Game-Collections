package Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemGenerator {
    private static final Random RANDOM = new Random();

    // Danh sách tất cả các loại Item có thể xuất hiện trong Shop
    private static final List<Item> ALL_ITEMS = List.of(
        new RegainChips(),
        new CardDraw(),
        new RankDec(),
        new RankInc()
    );

    // Phương thức tạo ra N Item ngẫu nhiên, không trùng lặp
    public static List<Item> generateRandomItems(int count) {
        if (count > ALL_ITEMS.size()) {
            count = ALL_ITEMS.size();
        }
        
        List<Item> shuffledList = new ArrayList<>(ALL_ITEMS);
        java.util.Collections.shuffle(shuffledList, RANDOM);
        
        return shuffledList.subList(0, count);
    }

    public static List<Item> generateRandomItems(int count, List<Item> currentShopItems) {
        if (count > ALL_ITEMS.size()) {
            count = ALL_ITEMS.size();
        }
        List<Item> availableItems = new ArrayList<>(ALL_ITEMS);
        availableItems.removeAll(currentShopItems);
        List<Item> shuffledList = new ArrayList<>(availableItems);
        java.util.Collections.shuffle(shuffledList, RANDOM);
        
        return shuffledList.subList(0, count);
    }
}