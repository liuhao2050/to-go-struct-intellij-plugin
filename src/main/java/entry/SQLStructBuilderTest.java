package entry;

import org.junit.Test;

import static org.junit.Assert.*;

public class SQLStructBuilderTest {

    Builder builder = new SQLStructBuilder();

    @Test
    public void gen() {
        String s = "CREATE TABLE IF NOT EXISTS `live_product`\n" +
                "(\n" +
                "    `id`         INT(11)     NOT NULL AUTO_INCREMENT,\n" +
                "    `room_id`    VARCHAR(32) NOT NULL COMMENT '房间id',\n" +
                "    `live_id`    VARCHAR(32) NOT NULL COMMENT '直播id',\n" +
                "\n" +
                "    `product_id` INT(11)     NOT NULL,\n" +
                "    `pos`        INT(11)     NOT NULL COMMENT '排序值，从0开始，越小越靠前',\n" +
                "\n" +
                "    `status`     INT(11)     NOT NULL DEFAULT 1 COMMENT '1: 正常，2：已删除',\n" +
                "    `create_at`  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',\n" +
                "    `update_at`  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',\n" +
                "\n" +
                "    KEY `idx` (room_id),\n" +
                "    PRIMARY KEY (`id`),\n" +
                "    UNIQUE KEY `idx_room_id_live_id` (`room_id`, `live_id`, `product_id`)\n" +
                ") ENGINE = INNODB\n" +
                "  CHARSET = utf8mb4 COMMENT '直播商品列表';";
        System.out.println(builder.gen(s));
    }
}