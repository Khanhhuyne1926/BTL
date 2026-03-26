package mygame.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.InputStream;
import java.awt.GradientPaint;
import java.awt.RenderingHints;
import java.awt.BasicStroke;

public class UI {

    GamePanel gp;

    Font titleFont;
    Font smallFont;
    Font bigFont;

    // Nút qua màn
    public Rectangle nextLevelBtn = new Rectangle(340, 380, 340, 80);
    public Rectangle backBtn = new Rectangle(340, 490, 340, 80);
    
    public Rectangle continueBtn;
    public Rectangle menuBtn;

    public UI(GamePanel gp) {
        this.gp = gp;

        // load font pixel
        titleFont = loadFont(16f);
        smallFont = loadFont(12f);
        bigFont = loadFont(28f);
        
        continueBtn = new Rectangle(340, 360, 340, 80);
        menuBtn = new Rectangle(340, 470, 340, 80);
    }

    public void draw(Graphics2D g2) {
        drawPlayerHUD(g2);

        if (gp.gameState == gp.STATE_LEVEL_COMPLETE) {
            drawLevel1WinScreen(g2);
        } else if (gp.gameState == gp.STATE_GAME_WIN) {
            drawGameWinScreen(g2);
        } else if (gp.gameState == gp.STATE_GAME_COMPLETED) {
            drawGameCompletedScreen(g2);
        } else if (gp.gameState == gp.STATE_PAUSE) {
            drawPauseScreen(g2);
        }
    }

    private void drawPlayerHUD(Graphics2D g2) {
        if (gp.player == null) return;

        int panelX = 16;
        int panelY = 16;
        int panelWidth = 180;
        int panelHeight = 60;

        // nền HUD
        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRect(panelX, panelY, panelWidth, panelHeight);

        // viền pixel style
        g2.setColor(new Color(255, 240, 200));
        g2.drawRect(panelX, panelY, panelWidth, panelHeight);

        // ===== NAME =====
        g2.setFont(titleFont);
        g2.setColor(Color.WHITE);
        g2.drawString(gp.player.name, panelX + 10, panelY + 18);

        // ===== HP BAR =====
        int barX = panelX + 10;
        int barY = panelY + 26;
        int barWidth = 110;
        int barHeight = 10;

        // viền ngoài
        g2.setColor(Color.BLACK);
        g2.fillRect(barX - 2, barY - 2, barWidth + 4, barHeight + 4);

        // nền bar
        g2.setColor(new Color(60, 60, 60));
        g2.fillRect(barX, barY, barWidth, barHeight);

        // lượng máu
        int currentBar = (int) ((double) gp.player.health / gp.player.maxHealth * barWidth);

        if (gp.player.health > 60) {
            g2.setColor(new Color(80, 220, 120));
        } else if (gp.player.health > 30) {
            g2.setColor(new Color(255, 190, 80));
        } else {
            g2.setColor(new Color(255, 80, 80));
        }

        g2.fillRect(barX, barY, currentBar, barHeight);

        // highlight
        g2.setColor(new Color(255, 255, 255, 80));
        g2.fillRect(barX, barY, currentBar, 2);

        // text HP
        g2.setFont(smallFont);
        g2.setColor(Color.WHITE);
        g2.drawString(gp.player.health + "/" + gp.player.maxHealth,
                barX + barWidth + 8, barY + 9);

        // ===== EGG STATUS =====
        String eggText = gp.player.hasEgg ? "EGG: YES" : "EGG: NO";

        g2.setColor(gp.player.hasEgg ? new Color(255, 230, 90) : Color.LIGHT_GRAY);
        g2.drawString(eggText, panelX + 10, panelY + 48);
    }
    //SỬA
    private void drawLevel1WinScreen(Graphics2D g2) {
        // 1. Vẽ nền tối làm mờ màn hình game (Toàn màn hình)
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // 2. Định nghĩa vị trí và kích thước KHUNG CHIẾN THẮNG (Tương tự JDialog)
        int frameW = 420; // Chiều rộng khung
        int frameH = 250; // Chiều cao khung (tăng một chút để chứa 3 dòng chữ)
        int frameX = (gp.screenWidth - frameW) / 2; // Căn giữa X
        int frameY = (gp.screenHeight - frameH) / 2; // Căn giữa Y

        // 3. Vẽ THÂN KHUNG (Màu Gradient gỗ nâu tương tự GameOverDialog)
        // Dùng Gradient để tạo độ sâu cho khung gỗ
        Color topColor = new Color(85, 55, 25);
        Color bottomColor = new Color(45, 28, 12);
        GradientPaint gpPaint = new GradientPaint(frameX, frameY, topColor, frameX, frameY + frameH, bottomColor);
        g2.setPaint(gpPaint);
        g2.fillRoundRect(frameX, frameY, frameW, frameH, 30, 30); // Bo góc 30px

        // 4. Vẽ VIỀN KHUNG (Màu vàng đậm, nét dày)
        g2.setColor(new Color(150, 120, 70)); // Vàng đồng đậm
        g2.setStroke(new java.awt.BasicStroke(4)); // Viền dày 4px
        g2.drawRoundRect(frameX, frameY, frameW, frameH, 30, 30); // Bo góc 30px

        // === VẼ NỘI DUNG VĂN BẢN VÀO TRONG KHUNG ===
        // (Sử dụng Font Arial để tránh lỗi dấu tiếng Việt như bạn gặp ở lần trước)
        int centerY = frameY + 25; // Bắt đầu vẽ từ đây

        // Dòng 1: CHIẾN THẮNG VANG DỘI! (To, Bold, Màu Vàng)
        g2.setFont(new Font("Arial", Font.BOLD, 28));
        g2.setColor(new Color(255, 230, 90));
        String title = "CHIẾN THẮNG VANG DỘI!";
        int titleX = frameX + (frameW - g2.getFontMetrics().stringWidth(title)) / 2;
        g2.drawString(title, titleX, centerY += 35); // Y = 60

        // Dòng 2: Mô tả (Màu Trắng, cỡ vừa)
        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        g2.setColor(Color.WHITE);
        String sub1 = "Lũ gà đã bất lực nhìn " + gp.player.name + " mang trứng đi!";
        int sub1X = frameX + (frameW - g2.getFontMetrics().stringWidth(sub1)) / 2;
        g2.drawString(sub1, sub1X, centerY += 45); // Y = 105

        // Dòng 3: Thách thức (In nghiêng, màu Cam nhạt)
        g2.setFont(new Font("Arial", Font.ITALIC, 16));
        g2.setColor(new Color(255, 180, 100));
        String sub2 = "Thử thách thực sự đang chờ đợi bạn ở phía trước...";
        int sub2X = frameX + (frameW - g2.getFontMetrics().stringWidth(sub2)) / 2;
        g2.drawString(sub2, sub2X, centerY += 35); // Y = 140

        // === VẼ NÚT BẤM VÀO TRONG KHUNG ===
        // Cập nhật vị trí nút bấm (nextLevelBtn) để nằm bên trong khung
        int btnW = 140; // Nút to hơn một chút
        int btnH = 45;
        int btnX = frameX + (frameW - btnW) / 2; // Căn giữa nút trong khung
        int btnY = frameY + frameH - btnH - 25; // Nằm cách đáy khung 25px

        // Cập nhật Bounds cho nút (để MouseHandler nhận diện click)
        nextLevelBtn.setBounds(btnX, btnY, btnW, btnH);

        // Vẽ nút bấm
        boolean hover = nextLevelBtn.contains(gp.mouseH.mouseX, gp.mouseH.mouseY);
        g2.setColor(hover ? new Color(255, 210, 90) : new Color(255, 180, 50));
        g2.fillRoundRect(nextLevelBtn.x, nextLevelBtn.y, nextLevelBtn.width, nextLevelBtn.height, 20, 20); // Bo góc nút

        g2.setColor(new Color(50, 30, 0)); // Màu viền nút
        g2.setStroke(new java.awt.BasicStroke(2)); // Viền dày 2px
        g2.drawRoundRect(nextLevelBtn.x, nextLevelBtn.y, nextLevelBtn.width, nextLevelBtn.height, 20, 20);

        // Chữ trên nút
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.setColor(new Color(50, 30, 0)); // Màu chữ đậm
        String btnText = "NEXT LEVEL"; // Tiếng Việt có dấu hơi chật, dùng không dấu cho sạch
        int btnTextX = nextLevelBtn.x + (nextLevelBtn.width - g2.getFontMetrics().stringWidth(btnText)) / 2;
        g2.drawString(btnText, btnTextX, nextLevelBtn.y + 30);
    }
 

    private void drawLevelCompleteScreen(Graphics2D g2) {
        // nền tối
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // tiêu đề
        g2.setFont(loadFont(34f));
        g2.setColor(new Color(255, 230, 90));
        String title = "LEVEL COMPLETE!";
        int titleX = gp.screenWidth / 2 - g2.getFontMetrics().stringWidth(title) / 2;
        g2.drawString(title, titleX, 220);

        // mô tả
        g2.setFont(loadFont(18f));
        g2.setColor(Color.WHITE);
        String sub = "Ban da hoan thanh man choi";
        int subX = gp.screenWidth / 2 - g2.getFontMetrics().stringWidth(sub) / 2;
        g2.drawString(sub, subX, 280);

        // hover nút
        boolean hover = nextLevelBtn.contains(gp.mouseH.mouseX, gp.mouseH.mouseY);

        // nền nút
        g2.setColor(hover ? new Color(255, 210, 90) : new Color(255, 180, 50));
        g2.fillRoundRect(nextLevelBtn.x, nextLevelBtn.y, nextLevelBtn.width, nextLevelBtn.height, 25, 25);

        // viền nút
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(nextLevelBtn.x, nextLevelBtn.y, nextLevelBtn.width, nextLevelBtn.height, 25, 25);

        // chữ nút
        g2.setFont(loadFont(22f));
        g2.setColor(Color.BLACK);
        String btnText = "NEXT LEVEL";
        int btnTextX = nextLevelBtn.x + nextLevelBtn.width / 2 - g2.getFontMetrics().stringWidth(btnText) / 2;
        int btnTextY = nextLevelBtn.y + 48;
        g2.drawString(btnText, btnTextX, btnTextY);
    }

    // ===== LOAD FONT =====
    private Font loadFont(float size) {
        try {
            InputStream is = getClass().getResourceAsStream("/res/fonts/ThaleahFat.ttf");

            if (is == null) {
                throw new RuntimeException("Không tìm thấy font!");
            }

            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            return font.deriveFont(size);

        } catch (Exception e) {
            e.printStackTrace();
            return new Font("Arial", Font.BOLD, (int) size);
        }
    }
    private void drawGameWinScreen(Graphics2D g2) {
        // nền tối
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // tiêu đề
        g2.setFont(loadFont(40f));
        g2.setColor(new Color(255, 230, 90));
        String title = "YOU WIN!";
        int titleX = gp.screenWidth / 2 - g2.getFontMetrics().stringWidth(title) / 2;
        g2.drawString(title, titleX, 240);

        // mô tả
        g2.setFont(loadFont(20f));
        g2.setColor(Color.WHITE);
        String sub = "BAN DA HOAN THANH TOAN BO GAME";
        int subX = gp.screenWidth / 2 - g2.getFontMetrics().stringWidth(sub) / 2;
        g2.drawString(sub, subX, 300);

        // hover nút
        boolean hover = backBtn.contains(gp.mouseH.mouseX, gp.mouseH.mouseY);

        // nền nút
        g2.setColor(hover ? new Color(255, 210, 90) : new Color(255, 180, 50));
        g2.fillRoundRect(backBtn.x, backBtn.y, backBtn.width, backBtn.height, 25, 25);

        // viền nút
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(backBtn.x, backBtn.y, backBtn.width, backBtn.height, 25, 25);

        // chữ nút
        g2.setFont(loadFont(22f));
        g2.setColor(Color.BLACK);
        String btnText = "BACK TO MENU";
        int btnTextX = backBtn.x + backBtn.width / 2 - g2.getFontMetrics().stringWidth(btnText) / 2;
        int btnTextY = backBtn.y + 48;
        g2.drawString(btnText, btnTextX, btnTextY);
       }
   private void drawGameCompletedScreen(Graphics2D g2) {
    // Vẽ nền tối
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // Tiêu đề
        g2.setFont(loadFont(34f));
        g2.setColor(new Color(255, 230, 90));
        String title = "BAN DA HOAN THANH GAME!";
        int titleX = gp.screenWidth / 2 - g2.getFontMetrics().stringWidth(title) / 2;
        g2.drawString(title, titleX, 220);

        // Mô tả
        g2.setFont(loadFont(18f));
        g2.setColor(Color.WHITE);
        String sub = "Choi lai hoac quay ve man hinh chinh";
        int subX = gp.screenWidth / 2 - g2.getFontMetrics().stringWidth(sub) / 2;
        g2.drawString(sub, subX, 280);

        // Nút "Chơi lại"
        boolean playAgainHover = nextLevelBtn.contains(gp.mouseH.mouseX, gp.mouseH.mouseY);
        g2.setColor(playAgainHover ? new Color(255, 210, 90) : new Color(255, 180, 50));
        g2.fillRoundRect(nextLevelBtn.x, nextLevelBtn.y, nextLevelBtn.width, nextLevelBtn.height, 25, 25);
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(nextLevelBtn.x, nextLevelBtn.y, nextLevelBtn.width, nextLevelBtn.height, 25, 25);
        g2.setFont(loadFont(22f));
        String playAgainText = "CHOI LAI";
        int playAgainX = nextLevelBtn.x + nextLevelBtn.width / 2 - g2.getFontMetrics().stringWidth(playAgainText) / 2;
        int playAgainY = nextLevelBtn.y + 48;
        g2.drawString(playAgainText, playAgainX, playAgainY);

        // Nút "Quay về Menu"
        boolean backToMenuHover = backBtn.contains(gp.mouseH.mouseX, gp.mouseH.mouseY);
        g2.setColor(backToMenuHover ? new Color(255, 210, 90) : new Color(255, 180, 50));
        g2.fillRoundRect(backBtn.x, backBtn.y, backBtn.width, backBtn.height, 25, 25);
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(backBtn.x, backBtn.y, backBtn.width, backBtn.height, 25, 25);
        g2.setFont(loadFont(22f));
        String backToMenuText = "QUAY VE MENU";
        int backToMenuX = backBtn.x + backBtn.width / 2 - g2.getFontMetrics().stringWidth(backToMenuText) / 2;
        int backToMenuY = backBtn.y + 48;
        g2.drawString(backToMenuText, backToMenuX, backToMenuY);
    }
   private void drawPauseScreen(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setFont(loadFont(34f));
        g2.setColor(new Color(255, 230, 90));
        String title = "PAUSED";
        int titleX = gp.screenWidth / 2 - g2.getFontMetrics().stringWidth(title) / 2;
        g2.drawString(title, titleX, 220);

        g2.setFont(loadFont(18f));
        g2.setColor(Color.WHITE);
        String sub = "Chon tiep tuc hoac quay ve menu";
        int subX = gp.screenWidth / 2 - g2.getFontMetrics().stringWidth(sub) / 2;
        g2.drawString(sub, subX, 280);

        boolean continueHover = continueBtn.contains(gp.mouseH.mouseX, gp.mouseH.mouseY);
        g2.setColor(continueHover ? new Color(255, 210, 90) : new Color(255, 180, 50));
        g2.fillRoundRect(continueBtn.x, continueBtn.y, continueBtn.width, continueBtn.height, 25, 25);
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(continueBtn.x, continueBtn.y, continueBtn.width, continueBtn.height, 25, 25);

        g2.setFont(loadFont(22f));
        String continueText = "CONTINUE";
        int continueX = continueBtn.x + continueBtn.width / 2 - g2.getFontMetrics().stringWidth(continueText) / 2;
        int continueY = continueBtn.y + 48;
        g2.drawString(continueText, continueX, continueY);

        boolean menuHover = menuBtn.contains(gp.mouseH.mouseX, gp.mouseH.mouseY);
        g2.setColor(menuHover ? new Color(255, 210, 90) : new Color(255, 180, 50));
        g2.fillRoundRect(menuBtn.x, menuBtn.y, menuBtn.width, menuBtn.height, 25, 25);
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(menuBtn.x, menuBtn.y, menuBtn.width, menuBtn.height, 25, 25);

        String menuText = "MAIN MENU";
        int menuX = menuBtn.x + menuBtn.width / 2 - g2.getFontMetrics().stringWidth(menuText) / 2;
        int menuY = menuBtn.y + 48;
        g2.drawString(menuText, menuX, menuY);
    }
}