package Game;

import Cards.*;
import Items.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class GameUI extends JFrame {

    private static final Color PRIMARY_BG = new Color(30, 30, 30);
    private static final Color BUTTON_BG = new Color(178, 34, 34);
    private static final Color TEXT_COLOR = new Color(255, 255, 255);
    private static final Color GAME_BG = new Color(0, 80, 0);

    private GameController controller;

    private JPanel mainPanel;
    private CardLayout cardLayout;

    // --- Panel riêng ---
    private JPanel playerPanel = new JPanel(null);
    private JPanel communityPanel = new JPanel(null);
    private JPanel enemyPanel = new JPanel(null);
    private JPanel shopPanel = new JPanel(null);

    // --- Component khác ---
    private JLabel infoLabel; // Hiển thị thông tin lượt/discard

    // --- Thanh máu ---
    private JProgressBar playerHPBar;
    private JProgressBar enemyHPBar;

    // --- Tên ---
    private JPanel playerInfoPanel;
    private JPanel enemyInfoPanel;

    private JLabel playerNameLabel;
    private JLabel enemyNameLabel;

    // --- Loại bài và chỉ số --- 
    private JLabel playerATKDEFLabel;
    private JLabel playerHandTypeLabel;
    private JLabel enemyATKDEFLabel;
    private JLabel enemyHandTypeLabel;
    private JPanel playerStatPanel;
    private JPanel enemyStatPanel;

    // --- Các button ---
    private JButton callBtn;
    private JButton raiseBtn;
    private JButton foldBtn;
    private JButton nextTurnBtn;
    private JButton saveBtn;
    
    // --- Logic Discard ---
    public List<CardUI> selectedCards = new ArrayList<>();
    private JButton discardBtn;

    // --- Vật phẩm ---
    private JPanel[] itemPanels = new JPanel[6]; 
    public List<Item> currentShopItems;   

    private JPanel shopPanelContainer; 
    private JPanel itemGridPanel;

    public GameUI() {
        controller = new GameController();

        controller.setUI(this);

        setTitle("Out of Poker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createMenuPanel(), "menu");
        mainPanel.add(createGamePanel(), "game");

        mainPanel.add(createShopPanel(), "Shop");

        add(mainPanel);
    }

    private JPanel createMenuPanel() {
        // [Giữ nguyên code MenuPanel]
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_BG);

        JLabel titleLabel = new JLabel("Out of Poker", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 60));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(60, 0, 40, 0));

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 20, 20));
        buttonPanel.setBackground(PRIMARY_BG);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 300, 40, 300));

        JButton startGame = createButton("START NEW GAME", e -> {
            Saves.Saver.deleteSave();
            controller.startNewGame("Player");
            updateUI();
            showScreen("game");
        });

        JButton continueGame = createButton("CONTINUE", e -> {
            controller.continueGame();
            updateUI();
            showScreen("game");
        });

        JButton exit = createButton("EXIT GAME", e -> System.exit(0));

        buttonPanel.add(startGame);
        if (controller.hasData()) buttonPanel.add(continueGame);
        buttonPanel.add(exit);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);

        updateStatLabels();

        return panel;
    }

    private JPanel createShopPanel() {
        int columnCount = 3;
        int gap = 20;

        // 1. Tạo Panel chứa các Item (dùng GridLayout)
        itemGridPanel = new JPanel(new GridLayout(1, columnCount, gap, 0)); 
        itemGridPanel.setBackground(new Color(50, 50, 100)); 
        itemGridPanel.setBorder(BorderFactory.createTitledBorder("Shop - Buy Item (Costs Base Chips)"));

        // 2. Tạo Panel tổng thể (Container) để chứa Item và Nút Exit
        shopPanelContainer = new JPanel(new BorderLayout()); // <--- CÓ LAYOUT RÕ RÀNG
        shopPanelContainer.setBounds(300, 200, 700, 350); 
        
        shopPanelContainer.add(itemGridPanel, BorderLayout.CENTER); // Đặt Grid Panel vào CENTER

        // 3. Thêm nút Exit Shop
        JButton exitBtn = new JButton("Exit Shop");
        exitBtn.addActionListener(e -> exitShop());
        shopPanelContainer.add(exitBtn, BorderLayout.SOUTH);
        
        return shopPanelContainer;
    }

    private JPanel createGamePanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(GAME_BG);

        int panelWidth = 650;
        int panelHeight = 150;
        int spacing = 70;
        int centerX = (1000 - panelWidth) / 2 - 80;

        int infoBoxWidth = 150;
        int infoBoxHeight = 90;

        int infoX = 80;
        int hpWidth = 150;
        int hpHeight = 15;

        int enemyInfoY = 75;
        int enemyHPY = enemyInfoY + infoBoxHeight + 5;

        int playerInfoY = 95 + 2 * (panelHeight + spacing);
        int playerHPY = playerInfoY - hpHeight - 5;

        int statPanelWidth = 150; 
        int statPanelHeight = 75;

        int communityY = 50 + panelHeight + spacing; // Vị trí Y của Community Panel
        int communityHeight = panelHeight;

        int statY = communityY + (communityHeight / 2) - (statPanelHeight / 2);
        int statPlayerY = statY + 110;
        int statEnemyY = statY - 90;

        // --- ENEMY PANEL ---
        enemyPanel.setOpaque(false);
        enemyPanel.setBounds(centerX, 50, panelWidth, panelHeight);
        panel.add(enemyPanel);

        // --- ENEMY INFO PANEL (Chỉ chứa Tên) ---
        enemyInfoPanel = new JPanel(new BorderLayout()); // Dùng BorderLayout để căn giữa tên
        enemyInfoPanel.setBackground(Color.LIGHT_GRAY); 
        enemyInfoPanel.setBounds(infoX, enemyInfoY, infoBoxWidth, infoBoxHeight); // Đặt vị trí
        panel.add(enemyInfoPanel);

        enemyNameLabel = new JLabel("Enemy Name", SwingConstants.CENTER); // Căn giữa Text
        enemyNameLabel.setForeground(TEXT_COLOR);
        enemyNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        enemyInfoPanel.add(enemyNameLabel, BorderLayout.CENTER); // Thêm vào trung tâm Info Panel


        // --- ENEMY HP BAR (NẰM NGOÀI INFO PANEL) ---
        enemyHPBar = createHPBar(Color.RED);
        enemyHPBar.setBounds(infoX, enemyHPY, hpWidth, hpHeight); // Vị trí tuyệt đối
        panel.add(enemyHPBar);

        // --- ENEMY STAT PANEL  ---
        // Enemy Stat Panel sẽ ở bên TRÁI của điểm trung tâm X
        int enemyStatX = infoX;

        enemyStatPanel = createStatPanel(statPanelWidth, statPanelHeight, "Enemy");
        enemyStatPanel.setBounds(enemyStatX, statEnemyY, statPanelWidth, statPanelHeight);
        panel.add(enemyStatPanel);

        enemyATKDEFLabel = (JLabel) enemyStatPanel.getComponent(0);
        enemyHandTypeLabel = (JLabel) enemyStatPanel.getComponent(1);

        // --- INFO LABEL ---
        infoLabel = new JLabel("Round 1 | Turn 1", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        infoLabel.setForeground(TEXT_COLOR);
        infoLabel.setBounds(centerX + panelWidth + 80, 50, 150, 30);
        panel.add(infoLabel);

        // --- COMMUNITY PANEL ---
        communityPanel.setOpaque(false);
        communityPanel.setBounds(centerX, communityY, panelWidth, panelHeight);
        panel.add(communityPanel);

        // --- PLAYER PANEL ---
        playerPanel.setOpaque(false);
        playerPanel.setBounds(centerX, 50 + 2 * (panelHeight + spacing), panelWidth, panelHeight);
        panel.add(playerPanel);

        // --- PLAYER INFO PANEL (Chỉ chứa Tên) ---
        playerInfoPanel = new JPanel(new BorderLayout()); // Dùng BorderLayout để căn giữa tên
        playerInfoPanel.setBackground(Color.LIGHT_GRAY);
        playerInfoPanel.setBounds(infoX, playerInfoY, infoBoxWidth, infoBoxHeight); // Đặt vị trí
        panel.add(playerInfoPanel);

        playerNameLabel = new JLabel("Player Name", SwingConstants.CENTER); // Căn giữa Text
        playerNameLabel.setForeground(TEXT_COLOR);
        playerNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        playerInfoPanel.add(playerNameLabel, BorderLayout.CENTER); // Thêm vào trung tâm Info Panel

        // --- PLAYER HP BAR ---
        playerHPBar = createHPBar(Color.GREEN);
        playerHPBar.setBounds(infoX, playerHPY, hpWidth, hpHeight); // Vị trí tuyệt đối
        panel.add(playerHPBar);

        // --- PLAYER STAT PANEL (Bên phải trung tâm) ---
        int playerStatX = infoX;

        playerStatPanel = createStatPanel(statPanelWidth, statPanelHeight, "Player");
        playerStatPanel.setBounds(playerStatX, statPlayerY, statPanelWidth, statPanelHeight);
        panel.add(playerStatPanel);

        playerATKDEFLabel = (JLabel) playerStatPanel.getComponent(0);
        playerHandTypeLabel = (JLabel) playerStatPanel.getComponent(1);

        // --- ACTION BUTTONS ---
        JPanel actionPanel = new JPanel(null);
        actionPanel.setBounds(840, 100, 120, 240);
        actionPanel.setOpaque(false);

        
        callBtn = createActionButton("Call", e -> {
            controller.playerCall();
            handleShowdownSequence();
            updateStatLabels();
        });

        raiseBtn = createActionButton("Raise", e -> {
            controller.playerRaise();
            handleShowdownSequence();
            updateStatLabels();
        });
        
        foldBtn = createActionButton("Fold", e -> {
            controller.playerFold();
            handleShowdownSequence();
            updateStatLabels();
        });

        saveBtn = createActionButton("Save", e ->{
            controller.saveGame();
            showScreen("menu");
        });
        
        // THÊM NÚT DISCARD
        discardBtn = createActionButton("Discard (2)", e -> handleDiscard());
        
        callBtn.setBounds(0, 0, 120, 40);
        raiseBtn.setBounds(0, 50, 120, 40);
        foldBtn.setBounds(0, 100, 120, 40);
        discardBtn.setBounds(0, 150, 120, 40); // Nút Discard
        saveBtn.setBounds(840, 10, 120, 40);

        panel.add(saveBtn);
        actionPanel.add(callBtn);
        actionPanel.add(raiseBtn);
        actionPanel.add(foldBtn);
        actionPanel.add(discardBtn);

        panel.add(actionPanel);

        nextTurnBtn = createActionButton("Resolve", e -> handleNextAction());
        nextTurnBtn.setBounds(840, 600, 120, 40); 
        panel.add(nextTurnBtn);
        nextTurnBtn.setEnabled(false);

        JPanel itemPanel = createItemPanel();

        Item test = new RegainChips();
        addItemToSlot(test);

        int itemPanelX = 840;
        int itemPanelY = 350;
        int itemPanelWidth = itemPanel.getPreferredSize().width;
        int itemPanelHeight = itemPanel.getPreferredSize().height;

        itemPanel.setBounds(itemPanelX, itemPanelY, itemPanelWidth, itemPanelHeight);
        panel.add(itemPanel);

        return panel;
    }

    private JProgressBar createHPBar(Color color) {
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setStringPainted(true);
        bar.setForeground(color);
        bar.setBackground(Color.DARK_GRAY);
        return bar;
    }

    private JButton createButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 22));
        button.setBackground(BUTTON_BG);
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
        button.addActionListener(action);
        return button;
    }
    
    private JButton createActionButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(BUTTON_BG);
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
        button.addActionListener(action);
        return button;
    }

    // Panel dành cho người chơi và kẻ địch
    private JPanel createStatPanel(int width, int height, String owner) {
        // Dùng BorderLayout để dễ dàng sắp xếp 2 Label
        JPanel statPanel = new JPanel(new BorderLayout());
        statPanel.setPreferredSize(new Dimension(width, height));
        statPanel.setBackground(new Color(30, 30, 30)); // Nền tối
        statPanel.setBorder(BorderFactory.createLineBorder(Color.CYAN, 1));

        // Label 1: ATK / DEF (Đặt ở NORTH)
        JLabel atkDef = new JLabel("ATK: - DEF: -", SwingConstants.CENTER);
        atkDef.setFont(new Font("Arial", Font.BOLD, 12));
        atkDef.setForeground(Color.YELLOW);
        statPanel.add(atkDef, BorderLayout.NORTH);

        // Label 2: Hand Type (Đặt ở CENTER)
        JLabel handType = new JLabel("HAND: N/A", SwingConstants.CENTER);
        handType.setFont(new Font("Arial", Font.PLAIN, 11));
        handType.setForeground(Color.WHITE);
        statPanel.add(handType, BorderLayout.CENTER);

        return statPanel;
    }

    // Vật phẩm 
    private JPanel createItemPanel() {
        int squareSlotSize = 60; // Ví dụ: mỗi ô vuông có cạnh 70px
        int gap = 8;
        // 1. Dùng GridLayout 3 hàng x 2 cột
        int itemContainerWidth = (2 * squareSlotSize) + gap;
        int itemContainerHeight = (3 * squareSlotSize) + (2 * gap);

        JPanel itemContainerPanel = new JPanel(new GridLayout(3, 2, gap, gap));
        itemContainerPanel.setOpaque(false);

        for (int i = 0; i < 6; i++) {
            // Tạo Panel cho từng ô (Item Slot)
            JPanel slotPanel = new JPanel(new BorderLayout()); 
            slotPanel.setBackground(Color.WHITE); 
            slotPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            
            // Lưu trữ tham chiếu và gán MouseListener
            itemPanels[i] = slotPanel;
            setupItemSlotListener(slotPanel, i);
            
            itemContainerPanel.add(slotPanel);
        }

        itemContainerPanel.setPreferredSize(new Dimension(itemContainerWidth, itemContainerHeight));

        // Ban đầu, cập nhật UI để hiển thị 6 ô trống
        updateItemPanel(); 
        updateStatLabels();
        
        return itemContainerPanel;
    }

    private void setupItemSlotListener(JPanel slotPanel, int slotIndex) {
        slotPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Item currentItem = controller.getItemSlots()[slotIndex];
                
                if (currentItem != null) {
                    // Kích hoạt phương thức use() riêng của item con
                    currentItem.use(controller); 
                    
                    // Sau khi dùng, xóa item khỏi slot
                    controller.getItemSlots()[slotIndex] = null;
                    updateItemPanel();
                } 
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                Item currentItem = controller.getItemSlots()[slotIndex];
                if (currentItem != null) {
                    // Tạo chuỗi HTML để hiển thị nhiều dòng thông tin trong Tooltip
                    String tooltipText = String.format("<html><b>%s</b><br><i>Cách dùng: %s</i><br>%s</html>", 
                                                    currentItem.getName(), 
                                                    currentItem.getHowToUse(), 
                                                    currentItem.getDescription());
                                                    
                    slotPanel.setToolTipText(tooltipText);
                    slotPanel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2)); // Hiệu ứng highlight
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                slotPanel.setToolTipText(null); // Xóa tooltip khi di chuột ra
                slotPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Trả lại border cũ
            }
        });
    }

    public void addItemToSlot(Item newItem) {
        for (int i = 0; i < controller.getItemSlots().length; i++) {
            if (controller.getItemSlots()[i] == null) {
                controller.getItemSlots()[i] = newItem;
                System.out.println("Added " + newItem.getName() + " to slot " + (i + 1));
                updateItemPanel(); 
                return;
            }
        }
        System.out.println("Item slots are full. Cannot add " + newItem.getName());
    }

    // Phương thức cập nhật giao diện tổng thể của Panel Item
    private void updateItemPanel() {
        for (int i = 0; i < controller.getItemSlots().length; i++) {
            JPanel slot = itemPanels[i];
            slot.removeAll(); // Xóa nội dung cũ

            Item item = controller.getItemSlots()[i];
            
            if (item != null) {
                // KHÔNG CẦN RESIZE LẠI Ở ĐÂY, vì item.getIcon() đã trả về icon đúng kích thước
                ImageIcon itemIcon = item.getIcon();
                
                if (itemIcon != null) {
                    JLabel iconLabel = new JLabel(itemIcon, SwingConstants.CENTER);
                    slot.add(iconLabel, BorderLayout.CENTER);
                } else {
                    JLabel emptyLabel = new JLabel("NO ICON", SwingConstants.CENTER);
                    emptyLabel.setForeground(Color.RED);
                    slot.add(emptyLabel, BorderLayout.CENTER);
                }
            }           
            else {
                // Nếu slot trống, chỉ hiển thị tên slot (hoặc để trống)
                JLabel emptyLabel = new JLabel("", SwingConstants.CENTER);
                emptyLabel.setForeground(Color.LIGHT_GRAY);
                slot.add(emptyLabel, BorderLayout.CENTER);
            }
            
            slot.revalidate();
            slot.repaint();
        }
    }

    private void showScreen(String name) {
        cardLayout.show(mainPanel, name);
    }
    
    public void updateUI() {
        updateCardRows();
        updateHPBars();
        updateInfoLabel();
        checkGameState();
        updateStatLabels();
        updateItemPanel();
    }

    private void handleShowdownSequence(){

        setActionButtonsEnabled(false);
        nextTurnBtn.setEnabled(true); // Kích hoạt nút Next/Resolve
        updateCardRows(true); // Lật bài địch
        updateStatLabels();
    }

    private void handleNextAction() {
        // 1. Vô hiệu hóa nút Next
        nextTurnBtn.setEnabled(false);
        
        // 2. Xử lý Combat. resolveCombat() sẽ tự động gọi nextTurn/nextEnemy
        controller.resolveCombat();
        
        // 3. Kích hoạt lại các nút hành động cho lượt mới
        setActionButtonsEnabled(true); 
        // updateUI() đã được gọi trong resolveCombat -> nextTurn/nextEnemy
    }

    private void setActionButtonsEnabled(boolean enabled) {
        callBtn.setEnabled(enabled);
        raiseBtn.setEnabled(enabled);
        foldBtn.setEnabled(enabled);
        discardBtn.setEnabled(enabled);
    }


    // --- Cập nhật UI bài ---
    private void updateCardRow(JPanel panel, List<Card> cards, boolean isEnemy) {
        // Chỉ xóa CardUI, giữ lại HP Bar (nếu có)
        Component[] comps = panel.getComponents();
        for (Component c : comps) {
            if (c instanceof CardUI) panel.remove(c);
        }
        
        selectedCards.clear(); // Xóa lựa chọn khi cập nhật bài mới

        int nameX = 10;
        int hpX = nameX + 80;
        int cardXStart = hpX + 80; // Vị trí X bắt đầu cho lá bài
        
        int x = cardXStart;
        int index = 1;
        for (Card c : cards) {
            // Thay đổi: Bài kẻ thù (isEnemy=true) sẽ được úp
            CardUI cardUI = new CardUI(c, isEnemy); 
            cardUI.setBounds(x, 20, 90, 120); // Kích thước thẻ nhỏ hơn
            
            if (!isEnemy) {
                // Thêm logic nhấp chuột cho bài người chơi
                final int finalIndex = index;
                cardUI.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        toggleCardSelection(cardUI, finalIndex);
                    }
                });
            }
            
            panel.add(cardUI);
            x += 100;
            index++;
        }
        panel.revalidate();
        panel.repaint();
    }

    public void updateCardRows(boolean revealEnemy) {
        try {
            // Bài người chơi và bài chung luôn hiển thị, bài kẻ thù úp
            updateCardRow(playerPanel, controller.getPlayerCards(), false);
            updateCardRow(communityPanel, controller.getCommunityCards(), false);
            updateCardRow(enemyPanel, controller.getEnemyCards(), !revealEnemy); 
        } catch (Exception ex) {
            // bỏ qua
        }
    }

    public void updateCardRows() {
        try {
            // Bài người chơi và bài chung luôn hiển thị, bài kẻ thù úp
            updateCardRow(playerPanel, controller.getPlayerCards(), false);
            updateCardRow(communityPanel, controller.getCommunityCards(), false);
            updateCardRow(enemyPanel, controller.getEnemyCards(), true); 
        } catch (Exception ex) {
            // bỏ qua
        }
    }

    private void updateHPBars() {
        try {
            // Cập nhật thanh máu và tên người chơi
            playerNameLabel.setText(controller.getYou().getName());

            playerHPBar.setMaximum(controller.getPlayerMaxHP());
            playerHPBar.setValue(controller.getPlayerHP());
            playerHPBar.setString(controller.getPlayerHP() + " / " + controller.getPlayerMaxHP());

            // Tương tự với kẻ địch
            enemyNameLabel.setText(controller.getCurrentEnemy().getName());

            enemyHPBar.setMaximum(controller.getEnemyMaxHP());
            enemyHPBar.setValue(controller.getEnemyHP());
            enemyHPBar.setString(controller.getEnemyHP() + " / " + controller.getEnemyMaxHP());
        } catch (Exception ex) {
            // bỏ qua
        }
    }

    // Cập nhật Info 
    private void updateStatLabels() {
        if (controller.getYou() == null || controller.getCurrentEnemy() == null) return;
        
        int pAtk = controller.getPlayerAttack();
        int pDef = controller.getPlayerDefense();
        int eAtk = controller.getEnemyAttack();
        int eDef = controller.getEnemyDefense();
        
        boolean showcase = controller.isShowcaseActive();
        int winnerID = showcase ? controller.getWinnerID() : -1; // Lấy ID người thắng khi showcase

        // --- CẬP NHẬT NGƯỜI CHƠI ---
        updatePlayerStats(pAtk, pDef, winnerID);
        
        // --- CẬP NHẬT KẺ ĐỊCH ---
        updateEnemyStats(eAtk, eDef, winnerID, showcase);
    }

    private void updatePlayerStats(int pAtk, int pDef, int winnerID) {
        Color handColor = Color.WHITE;
        Color atkColor = Color.YELLOW; // Màu mặc định cho chỉ số
        Color defColor = Color.YELLOW;

        String currentAction = controller.getPlayerAction();

        if (currentAction.equals("Fold")) { // Player là Defender khi Fold
             // Tăng DEF x3
            defColor = Color.CYAN; 
        } else if (currentAction.equals("Raise") && winnerID == 0) { // Player là Attacker khi Raise (và thắng)
             // Tăng ATK x1.5
            atkColor = Color.RED; // Tô màu đỏ để nổi bật ATK
        }

        if (winnerID == 1 || currentAction.equals("Fold")) { 
            handColor = new Color(173, 216, 230); // Light Blue
            defColor = Color.BLUE; // Chỉ tô màu DEF
            atkColor = Color.YELLOW;
        } else if (winnerID == 0) { 
            handColor = Color.RED;
            defColor = Color.YELLOW; 
            atkColor = Color.RED;
        }

        // Hiển thị ATK/DEF với màu sắc riêng (sử dụng HTML)
        String atkDefText = String.format("<html><font color='%s'>ATK: %d</font> <font color='%s'>DEF: %d</font></html>", 
                                        toHtmlColor(atkColor), pAtk, 
                                        toHtmlColor(defColor), pDef);
        playerATKDEFLabel.setText(atkDefText);

        // Cập nhật Hand Type
        String playerHand = controller.getPlayerBestHand().getHandType();
        playerHandTypeLabel.setText("HAND: " + playerHand);
        playerHandTypeLabel.setForeground(handColor);
    }

    private void updateEnemyStats(int eAtk, int eDef, int winnerID, boolean showcase) {
        Color handColor = Color.WHITE;
        Color atkColor = Color.YELLOW;
        Color defColor = Color.YELLOW;

        if (!showcase) {
            // Giấu ATK/DEF
            enemyATKDEFLabel.setText("<html><font color='#AAAAAA'>ATK: ??</font> <font color='#AAAAAA'>DEF: ??</font></html>");
            
            // Giấu loại bài
            enemyHandTypeLabel.setText("HAND: HIDDEN");
            enemyHandTypeLabel.setForeground(Color.WHITE);
            return; // Thoát khỏi hàm
        }

        if (winnerID == 1) { // Kẻ địch thắng (Tấn công)
            handColor = Color.RED;
            defColor = Color.YELLOW; // Chỉ tô màu ATK
            atkColor = Color.RED;
        } else if (winnerID == 0) { // Kẻ địch thua (Phòng thủ)
            handColor = new Color(173, 216, 230); // Light Blue
            defColor = Color.BLUE; // Chỉ tô màu DEF
            atkColor = Color.YELLOW;
        }

        // Hiển thị ATK/DEF với màu sắc riêng (sử dụng HTML)
        String atkDefText = String.format("<html><font color='%s'>ATK: %d</font> <font color='%s'>DEF: %d</font></html>", 
                                        toHtmlColor(atkColor), eAtk, 
                                        toHtmlColor(defColor), eDef);
        enemyATKDEFLabel.setText(atkDefText);

        // Cập nhật Hand Type (CHỈ HIỂN THỊ KHI SHOWCASE)
        
        String enemyHand = controller.getEnemyBestHand().getHandType();
        enemyHandTypeLabel.setText("HAND: " + enemyHand);
        enemyHandTypeLabel.setForeground(handColor);
        
    }

    // Phương thức tiện ích để chuyển đổi Color sang chuỗi Hex cho HTML
    private String toHtmlColor(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }
    
    public void updateInfoLabel() {
        String enemyName = controller.getCurrentEnemy().getName();
        int round = controller.getRound();
        int turn = controller.getTurn();
        int discards = controller.getDiscards(); 
        
        infoLabel.setText(String.format("Round %d | Turn %d", round, turn));
        
        discardBtn.setEnabled(discards > 0);

        updateDiscardButtonText();
    }

    // ========================
    // LOGIC DISCARD
    // ========================
    
    // Đánh dấu thẻ đã chọn
    private void toggleCardSelection(CardUI cardUI, int position) {
        // Lấy trạng thái ban đầu để kiểm tra sự thay đổi
        boolean wasEmpty = selectedCards.isEmpty();

        if (selectedCards.contains(cardUI)) {
            selectedCards.remove(cardUI);
            cardUI.setBorder(null); // Bỏ highlight
        } else {
            if (selectedCards.size() < 2) {
                selectedCards.add(cardUI);
                // Highlight thẻ đã chọn
                cardUI.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3)); 
            } else {
                return;
            }
        }
        boolean isEmpty = selectedCards.isEmpty();

        // Nếu trạng thái rỗng thay đổi (chọn thẻ đầu tiên hoặc bỏ chọn thẻ cuối cùng)
        if (wasEmpty != isEmpty) {
            updateDiscardButtonText();
        }
    }

    private void updateDiscardButtonText() {
        int discardsLeft = controller.getDiscards();
        
        // Nếu có ít nhất một thẻ được chọn, hiển thị "Discarding"
        if (!selectedCards.isEmpty()) {
            discardBtn.setText("Discarding");
        } else {
            // Nếu không có thẻ nào được chọn, hiển thị số lượt còn lại
            discardBtn.setText(String.format("Discard (%d)", discardsLeft));
        }
    }
    
    // Xử lý khi nhấn nút Discard
    private void handleDiscard() {
        if (selectedCards.isEmpty() || selectedCards.size() > 2) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 hoặc 2 lá bài để bỏ.");
            return;
        }
        
        // Lấy vị trí của các lá bài đã chọn (vị trí 1-indexed)
        List<Integer> positions = new ArrayList<>();
        List<Card> playerCards = controller.getPlayerCards();
        
        for(CardUI cUI : selectedCards) {
            // Tìm vị trí của CardUI trong danh sách thẻ bài hiện tại (vì thứ tự có thể thay đổi)
            int index = playerCards.indexOf(cUI.getCard()); 
            if (index != -1) {
                positions.add(index + 1); // position là 1-indexed
            }
        }
        
        if (controller.playerDiscard(positions)) {
            // Nếu bỏ bài thành công, cập nhật giao diện
            updateCardRows();
            updateInfoLabel();
        } else {
            // Logic này chỉ nên xảy ra nếu có lỗi không mong muốn (ví dụ: đã hết lượt discard)
            JOptionPane.showMessageDialog(this, "Không thể bỏ bài. Đã hết lượt hoặc lỗi nội bộ.");
        }
    }

    // ========================
    // LOGIC GAME STATE
    // ========================
    
    private void checkGameState() {
        if (controller.getPlayerHP() <= 0) {
            JOptionPane.showMessageDialog(this, "GAME OVER! Bạn đã thua. Bắt đầu lại.");
            Saves.Saver.deleteSave();
            controller.setState("");
            showScreen("menu");
        } else if (controller.getRound() > controller.getGauntletLength()) {
            JOptionPane.showMessageDialog(this, "Chúc mừng! Bạn đã chiến thắng tất cả kẻ thù.");
            showScreen("menu");
        }
    }

    // Shop Item
    private void updateShopItems(List<Item> shopItems) {
        itemGridPanel.removeAll();
        
        for (Item item : shopItems) {
            JPanel itemSlot = createShopItemSlot(item);
            itemGridPanel.add(itemSlot); // Thêm vào itemGridPanel
        }
        itemGridPanel.revalidate();
        itemGridPanel.repaint();
    }

    private JPanel createShopItemSlot(Item item) {
        JPanel slot = new JPanel(new BorderLayout(5, 5));
        slot.setBackground(new Color(80, 80, 120));
        slot.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        // Hiển thị Icon và Tên
        JLabel iconLabel;
        if (item.getIcon() != null) {
            iconLabel = new JLabel(item.getIcon(), SwingConstants.CENTER); // Sử dụng icon đã resize
        } else {
            iconLabel = new JLabel("No Icon", SwingConstants.CENTER); // Fallback text
            iconLabel.setForeground(Color.RED);
        } // Reuse Item's icon
        
        slot.add(iconLabel, BorderLayout.CENTER);

        // Nút mua hàng
        JButton buyButton = new JButton(String.format("Buy (Cost: %d Max HP)", item.getBaseChipCost()));
        
        buyButton.addActionListener(e -> {
            // LOGIC MUA HÀNG
            int cost = item.getBaseChipCost(); // Giả sử Item có phương thức getBaseChipCost()

            if (controller.getYou().getBaseChips() > cost) {
                // 1. Giảm Max HP
                controller.getYou().setBaseChips(controller.getYou().getBaseChips() - cost);
                
                // 2. Thêm Item vào Inventory
                addItemToSlot(item); 
                
                int indexToRemove = this.currentShopItems.indexOf(item);
                if (indexToRemove != -1) {                

                // Tạo Item ngẫu nhiên MỚI và KHÔNG TRÙNG
                List<Item> newItemsList = ItemGenerator.generateRandomItems(1, currentShopItems); 

                Item newItem = null;
                if (!newItemsList.isEmpty()) {
                    newItem = newItemsList.get(0);
                }
                if (newItem != null) {
                    // Thay thế Item đã mua bằng Item mới
                    this.currentShopItems.set(indexToRemove, newItem);
                } else {
                    // Xử lý khi hết Item để refill (ví dụ: ItemSlot bị xóa hoặc để trống)
                    this.currentShopItems.remove(indexToRemove); 
                }
            }
                // 3. Thoát Shop
                updateShopItems(this.currentShopItems);
                itemGridPanel.revalidate();
                itemGridPanel.repaint();
                updateHPBars();
            } 
            else {
                JOptionPane.showMessageDialog(this, "Không đủ Max HP để mua Item này!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            }
        });

        slot.add(buyButton, BorderLayout.SOUTH);
        
        // Tooltip chi tiết
        slot.setToolTipText(String.format("<html><b>%s</b><br>%s</html>", item.getName(), item.getDescription()));

        return slot;
    }
    
    public void showShopScreen(List<Item> shopItems) {
        currentShopItems = shopItems;
        // 1. Cập nhật nội dung 3 Item ngẫu nhiên trong Shop Panel
        updateShopItems(shopItems); 
        
        // 2. CHUYỂN SANG MÀN HÌNH SHOP DÙNG CardLayout
        showScreen("Shop"); // <-- Gọi phương thức của bạn
    }

    // Phương thức để thoát Shop (sau khi mua hoặc thoát)
    private void exitShop() {
        // 1. TRỞ VỀ MÀN HÌNH GAME CHÍNH DÙNG CardLayout
        showScreen("game"); // <-- Giả sử tên Panel Game của bạn là "Game"
        
        // 2. Bắt đầu lượt chơi/trận chiến tiếp theo
        controller.nextEnemy(); 
        updateUI(); // Cập nhật lại thanh máu, Item, v.v.
    }
    
    
    
    // Note: Cần đảm bảo lớp CardUI có constructor: public CardUI(Card card, boolean isHidden)
}