package top.lrshuai.demo.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class QualityProgressBarGenerator {

    // 颜色映射定义
    private static final Color NORMAL_COLOR = Color.WHITE;      // 类型1: 正常 - 白色
    private static final Color REDUNDANT_COLOR = new Color(0, 123, 255);  // 类型2: 冗余 - 蓝色
    private static final Color ERROR_COLOR = new Color(220, 53, 69);     // 类型3: 错误 - 红色
    private static final Color MISSING_COLOR = new Color(40, 167, 69);    // 类型4: 缺失 - 绿色

    // 背景 - 白色
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    // 文字 - 黑色
    private static final Color TEXT_COLOR = Color.BLACK;

    // 图片尺寸 - 根据您的要求完全填充，不留白边
    private static final int IMAGE_WIDTH = 1000;
    private static final int IMAGE_HEIGHT = 100;


    // 图例尺寸
    private static final int LEGEND_WIDTH = 400;
    private static final int LEGEND_HEIGHT = 60;
    private static final int BOX_SIZE = 20;
    private static final int BOX_TEXT_SPACING = 10;
    private static final int ITEM_SPACING = 80;

    public static void main(String[] args) {
        // 模拟数据 - 根据您提供的JSON结构
        List<ProgressItem> progressVoList = List.of(
                new ProgressItem(1, 21.43f),
                new ProgressItem(3, 7.14f),
                new ProgressItem(1, 35.7f),
                new ProgressItem(2, 7.14f),
                new ProgressItem(1, 14.29f),
                new ProgressItem(4, 14.29f)

        );

        try {
            generateProgressBarImage(progressVoList, "D://测试图片/quality_progress_bar_%s.png".formatted(System.currentTimeMillis()));
//            generateLegendImage("D://测试图片/data_quality_legend_%s.png".formatted(System.currentTimeMillis()));
            System.out.println("图片生成成功!");
        } catch (IOException e) {
            System.err.println("生成图片时出错: " + e.getMessage());
        }
    }

    public static void generateProgressBarImage(List<ProgressItem> progressVoList, String filename) throws IOException {
        // 创建BufferedImage对象
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // 设置背景为白色 - 完全填充，不留白边
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

        // 启用抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 绘制各个进度块 - 完全填充高度
        int currentX = 0;
        for (ProgressItem item : progressVoList) {
            int width = (int) (item.getPercentage() * IMAGE_WIDTH / 100);
            if (width < 1) width = 1; // 确保至少1像素宽度

            // 根据类型设置颜色
            Color blockColor;
            switch (item.getType()) {
                case 1 -> blockColor = NORMAL_COLOR;     // 正常 - 白色
                case 2 -> blockColor = REDUNDANT_COLOR; // 冗余 - 蓝色
                case 3 -> blockColor = ERROR_COLOR;     // 错误 - 红色
                case 4 -> blockColor = MISSING_COLOR;    // 缺失 - 绿色
                default -> blockColor = Color.GRAY;      // 未知类型 - 灰色
            }

            g2d.setColor(blockColor);
            g2d.fillRect(currentX, 0, width, IMAGE_HEIGHT);
            currentX += width;
        }
        // 释放资源
        g2d.dispose();
        // 保存图片
        ImageIO.write(image, "png", new File(filename));
    }

    public static void generateLegendImage(String filename) throws IOException {
        // 创建BufferedImage对象
        BufferedImage image = new BufferedImage(LEGEND_WIDTH, LEGEND_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // 设置背景为白色
        g2d.setColor(BACKGROUND_COLOR);
        g2d.fillRect(0, 0, LEGEND_WIDTH, LEGEND_HEIGHT);

        // 启用抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 设置字体
        g2d.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));

        // 计算起始位置（水平居中）
        int startX = (LEGEND_WIDTH - (3 * ITEM_SPACING)) / 2;
        int centerY = LEGEND_HEIGHT / 2;

        // 绘制冗余图例
        g2d.setColor(REDUNDANT_COLOR);
        g2d.fillRect(startX, centerY - BOX_SIZE/2, BOX_SIZE, BOX_SIZE);
        g2d.setColor(TEXT_COLOR);
        g2d.drawString("冗余", startX + BOX_SIZE + BOX_TEXT_SPACING, centerY + 5);

        // 绘制错误图例
        g2d.setColor(ERROR_COLOR);
        g2d.fillRect(startX + ITEM_SPACING, centerY - BOX_SIZE/2, BOX_SIZE, BOX_SIZE);
        g2d.setColor(TEXT_COLOR);
        g2d.drawString("错误", startX + ITEM_SPACING + BOX_SIZE + BOX_TEXT_SPACING, centerY + 5);

        // 绘制缺失图例
        g2d.setColor(MISSING_COLOR);
        g2d.fillRect(startX + ITEM_SPACING * 2, centerY - BOX_SIZE/2, BOX_SIZE, BOX_SIZE);
        g2d.setColor(TEXT_COLOR);
        g2d.drawString("缺失", startX + ITEM_SPACING * 2 + BOX_SIZE + BOX_TEXT_SPACING, centerY + 5);

        // 释放资源
        g2d.dispose();

        // 保存图片
        ImageIO.write(image, "png", new File(filename));
    }

    // 内部类表示进度项
    static class ProgressItem {
        private final int type;
        private final float percentage;

        public ProgressItem(int type, float percentage) {
            this.type = type;
            this.percentage = percentage;
        }

        public int getType() {
            return type;
        }

        public float getPercentage() {
            return percentage;
        }
    }
}
