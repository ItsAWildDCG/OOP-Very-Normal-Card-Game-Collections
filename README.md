Nhóm 4: Ứng dụng các trò chơi bài
Tổng quát:
- Ứng dụng là tổng hợp một số trò chơi bài được lấy cảm hứng từ những trò chơi bài có thực.
-	Người chơi sẽ chơi các trò chơi một mình hoặc với máy ở các cấp độ khác nhau với mục tiêu giành chiến thắng. 
  
Chức năng:
-	Tạo ra trò chơi Poker với một số biến thể.
-	Chế độ chơi một mình hoặc chơi với máy tùy trò chơi.
-	Lưu kết quả, tiến trình và kỷ lục của người chơi.
-	Lưu trữ dữ liệu của nhiều người chơi khác nhau.
	 
Các class quan trọng:
-	Class Lá bài (Card) được thiết kế để mô tả các lá bài trong trò chơi Poker cũng như một số lá bài đặc biệt. Mỗi lá bài có các thuộc tính quan trọng như: tên bậc (rank_name) – danh sách tên của từng bậc lá bài, tên (name) – tên lá bài trong game, bậc (rank) – giá trị bậc của lá bài, chất (suit) – chất của lá bài, và ngửa mặt (faceUp) – cho biết lá bài đang ở trạng thái ngửa hay úp.
-	Ngoài ra, class này còn cung cấp các phương thức để thao tác với lá bài: có thể xem bậc hiện tại (getRank()), thay đổi bậc của lá bài (setRank()), kiểm tra chất (getSuit()), chỉnh sửa chất (setSuit()), lấy tên hiển thị của lá bài (getName()), và cuối cùng là hiển thị lá bài ra màn hình (displayCard()).

-	Class Nhân vật (Character) được dùng để đại diện cho người chơi hoặc máy trong trò chơi. Mỗi nhân vật có các thuộc tính quan trọng như: tên nhân vật (character_name) để xác định danh tính, tay bài (hand) là danh sách các lá bài hiện tại mà nhân vật đang giữ, và vật phẩm (items) – một tập hợp các vật phẩm cùng số lượng mà nhân vật đang sở hữu.
-	Class này còn cung cấp các chức năng hỗ trợ người chơi trong quá trình chơi game. Cụ thể, nhân vật có thể sử dụng vật phẩm (useItem()) để kích hoạt hiệu ứng đặc biệt mà vật phẩm mang lại, hoặc rút bài (drawFrom()) – tức là lấy một số lá bài nhất định từ một vị trí được chỉ định và thêm vào tay bài của mình.

-	Class Vật phẩm (Item) được xây dựng để quản lý các loại vật phẩm trong trò chơi. Mỗi vật phẩm có các thuộc tính cơ bản gồm: số thứ tự (ID) để định danh trong hệ thống, tên hiển thị (item_name) giúp phân biệt và gọi tên vật phẩm, cùng với miêu tả (description) để cung cấp thông tin ngắn gọn về công dụng hoặc đặc điểm của vật phẩm.
-	Ngoài ra, class còn có phương thức sử dụng vật phẩm (use()), cho phép kích hoạt hiệu ứng hoặc hành động đặc biệt mà vật phẩm đó mang lại trong trò chơi.

-	Class Bộ bài (Deck) được thiết kế để quản lý toàn bộ các lá bài trong trò chơi. Bộ bài có các thuộc tính quan trọng như: danh sách lá bài (cards) – tập hợp tất cả lá bài hiện có trong bộ, và loại (type) – cho biết bộ bài thuộc kiểu nào khi hiển thị trong game.
-	Về chức năng, class này hỗ trợ nhiều thao tác với bộ bài: có thể kiểm tra (check()) để xem các lá bài hiện tại trong bộ, thêm lá bài mới (addCard()) để mở rộng bộ bài, hoặc loại bỏ một lá bài (removeCard()) khi cần chỉnh sửa hay cập nhật nội dung bộ bài.


Phân công công việc:
-	Nguyễn Tuấn Kiệt - B23DCCN472: Thiết kế lớp lá bài Card
-	Nguyễn An Huy -  B23DCCN392: Thiết kế các lớp người chơi Player/Dealer/…
-	Nguyễn Mạnh Đức - B23DCKH028: Thiết kế các lớp trò chơi Poker
-	Dương Gia Nguyên - B23DCDT179: Thiết kế đồ họa, UI.
-	Lê Anh Đức - B23DCVT094: Thiết kế hệ thống file lưu kết quả, thông tin
-	

