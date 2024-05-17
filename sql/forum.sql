/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80031 (8.0.31)
 Source Host           : localhost:3306
 Source Schema         : forum

 Target Server Type    : MySQL
 Target Server Version : 80031 (8.0.31)
 File Encoding         : 65001

 Date: 17/05/2024 18:00:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for db_account
-- ----------------------------
DROP TABLE IF EXISTS `db_account`;
CREATE TABLE `db_account`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `register_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_account
-- ----------------------------
INSERT INTO `db_account` VALUES (1, 'test', '$2a$10$kKjgwT1WOrQlmeQHlO7il.oVEiN8lh0OcEvVsRgrLseC3ITGBDyGa', 'yuzoii@qq.com', 'user', '/avatar/1f3ad6ed59bf4bb798e93793c063d818', '2023-08-27 00:18:20');
INSERT INTO `db_account` VALUES (3, 'testnew', '$2a$10$xpvoyySeiyZCofnXtH7V3ebs4.v2S8MlS6b4Vnv6qaq/S2/OtY9Xy', 'toxicokita@outlook.com', 'user', NULL, '2024-05-16 11:16:22');

-- ----------------------------
-- Table structure for db_account_details
-- ----------------------------
DROP TABLE IF EXISTS `db_account_details`;
CREATE TABLE `db_account_details`  (
  `id` int NOT NULL,
  `gender` tinyint NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `qq` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `wx` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_account_details
-- ----------------------------
INSERT INTO `db_account_details` VALUES (1, 0, '15123421421', '2132132132', 'asdasdq', 'asdc');
INSERT INTO `db_account_details` VALUES (3, 1, '15123512312', '1234451233', 'acgdsg', 'aas');

-- ----------------------------
-- Table structure for db_account_privacy
-- ----------------------------
DROP TABLE IF EXISTS `db_account_privacy`;
CREATE TABLE `db_account_privacy`  (
  `id` int NOT NULL,
  `phone` tinyint NULL DEFAULT NULL,
  `email` tinyint NULL DEFAULT NULL,
  `wx` tinyint NULL DEFAULT NULL,
  `qq` tinyint NULL DEFAULT NULL,
  `gender` tinyint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_account_privacy
-- ----------------------------
INSERT INTO `db_account_privacy` VALUES (1, 1, 1, 1, 1, 1);
INSERT INTO `db_account_privacy` VALUES (3, 1, 1, 1, 1, 1);

-- ----------------------------
-- Table structure for db_image_store
-- ----------------------------
DROP TABLE IF EXISTS `db_image_store`;
CREATE TABLE `db_image_store`  (
  `uid` int NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `time` datetime NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_image_store
-- ----------------------------
INSERT INTO `db_image_store` VALUES (1, '/cache/20240423/e41f5216785b42f4aa7a8ce4e62fe37e', '2024-04-23 17:11:02');
INSERT INTO `db_image_store` VALUES (1, '/cache/20240423/e73097a6b6e5495db9ead6cb23e76770', '2024-04-23 17:14:09');
INSERT INTO `db_image_store` VALUES (1, '/cache/20240503/37832dd9126c43a1953ba62a3eae4ccd', '2024-05-03 22:23:17');
INSERT INTO `db_image_store` VALUES (1, '/cache/20240503/12c6b3f54aa243d8b08bda224ed77bc9', '2024-05-03 22:23:42');

-- ----------------------------
-- Table structure for db_notification
-- ----------------------------
DROP TABLE IF EXISTS `db_notification`;
CREATE TABLE `db_notification`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `uid` int NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_notification
-- ----------------------------

-- ----------------------------
-- Table structure for db_topic
-- ----------------------------
DROP TABLE IF EXISTS `db_topic`;
CREATE TABLE `db_topic`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `uid` int NULL DEFAULT NULL,
  `type` int NULL DEFAULT NULL,
  `time` datetime NULL DEFAULT NULL,
  `top` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_topic
-- ----------------------------
INSERT INTO `db_topic` VALUES (1, 'sdasdasdasd', '{\"ops\":[{\"insert\":\"sadasdasdasdasdasdsadasdsadad\\n\"}]}', 1, 2, '2023-10-09 22:57:36', 0);
INSERT INTO `db_topic` VALUES (3, '我今天结婚了', '{\"ops\":[{\"insert\":\"年年如约的长沙·中国 1024 程序员节，承载着科技的不断进步和开源精神的传承，是万千开发者共探发展、共赢成长的重要机遇；历代传承的岳麓之约，更是新技术时代不断更迭、开源开放的生动缩影。\\n智能新基建，研发新生态。10 月 23-25 日，2023 长沙·中国 1024 程序员节”（1024.csdn.net）再度精彩来袭。院士、十大全球研发中心掌门人，国内大模型掌门人、海外开源掌门人，重磅亮相。十余场技术干货主题论坛于长沙、北京、杭州等多城开启；还有超级码工厂编程大赛、AI 主题展以及2023 AI 开发者生态报告、花样程序员等欢庆嘉年华。大咖的集结，技术的碰撞，灵感的迸发，开发者的狂欢，1024 程序员节邀您赴会！\\n岳麓千年之约，技术英雄论道研发新基建\\n今年的 1024 程序员节中，岳麓对话重磅邀请全球研发中心负责人、研发新力量·国内八大大模型掌门人开坛论道。同步开启盛会还有 2023 技术英雄会，以及囊括全球开源技术掌门人高峰论坛，新程序员全球人工智能高峰论坛、AI 模型技术及应用论坛、AI 算力专场、AI 模型工程化论坛、开源发展与开源商业化论坛、数据技术论坛、自动驾驶与智能汽车论坛等等十余场主题论坛，邀请了国内外多位技术研发掌门人、人工智能&开源技术掌门人、以及技术领袖、行业精英共同赴会。 聚焦“智能新基建，研发新生态”的主题，各领域大咖将畅聊云计算、人工智能、开源、算力、数据库等技术趋势，洞察机遇，共话中国研发技术新生态。\\n\\n\"}]}', 1, 4, '2023-10-13 18:11:38', 0);
INSERT INTO `db_topic` VALUES (4, '你们Navicat是不是在用盗版？', '{\"ops\":[{\"insert\":\"千万别用了，小心数据库被勒索！\\n\"}]}', 1, 1, '2023-10-13 18:23:49', 1);
INSERT INTO `db_topic` VALUES (5, '大家千万别在淘宝回收手机，坑死人', '{\"ops\":[{\"insert\":\"我今天回收了一个最新款苹果手机，结果那边收到货之后说我手机各种有问题，只给我一般的价格出售，我说不接受，那边直接说把我手机都拆报废了，寄回来全是零件，后果自负，我只接好家伙！\\n\"}]}', 1, 5, '2023-10-13 18:27:12', 1);
INSERT INTO `db_topic` VALUES (6, '论坛出现超大BUG赶紧修', '{\"ops\":[{\"insert\":\"为什么不能直接给我匹配一个女友，要同城的，最好同学校\\n\"}]}', 1, 3, '2023-10-13 18:28:30', 0);
INSERT INTO `db_topic` VALUES (8, '千万别在咸鱼上到手刀', '{\"ops\":[{\"insert\":\"太坑了\\n\"}]}', 1, 5, '2023-10-13 19:34:26', 0);
INSERT INTO `db_topic` VALUES (9, '测试我是测试帖子', '{\"ops\":[{\"insert\":\"测试一下缓存是否生效\\n\"}]}', 1, 2, '2023-10-13 19:35:43', 0);
INSERT INTO `db_topic` VALUES (10, '撒电话卡进度哈健康四大会计说', '{\"ops\":[{\"insert\":\"大大叔大婶肯定撒户籍大刷卡机打撒科技大\\n\"}]}', 1, 1, '2023-10-13 19:49:27', 0);
INSERT INTO `db_topic` VALUES (15, '请问项目的更新速度能不能再快一点', '{\"ops\":[{\"insert\":\"太少了不够看啊，能不能再更新快一点！！！！！\\n\"},{\"attributes\":{\"width\":\"321\"},\"insert\":{\"image\":\"http://localhost:8080/images/cache/20231023/c292300bb3bc4adcb71fe88e13fea9c1\"}},{\"insert\":\"\\n代码写多一点，我好拿来当毕设\\n\"},{\"attributes\":{\"width\":\"344\"},\"insert\":{\"image\":\"http://localhost:8080/images/cache/20231023/fca00bb202d7425fa60ce0eac2d7ee34\"}},{\"insert\":\"\\n\"}]}', 1, 3, '2023-10-23 16:55:02', 0);
INSERT INTO `db_topic` VALUES (16, '兄妹喝奶奶捡回的未开封饮料被送医抢救，饮料检出三甲基硅醇', '{\"ops\":[{\"insert\":\"一个多月前，深圳宝安，6岁的哥哥和4岁妹妹喝了奶奶从外面垃圾桶捡回来的未开封的饮料，随即出现呕吐、腹痛症状，被送医抢救。\\n\"},{\"attributes\":{\"width\":\"193\"},\"insert\":{\"image\":\"http://localhost:8080/images/cache/20231023/6c95e362dbfe4b11b3b89a45f07bbd83\"}},{\"insert\":\"\\n4岁1个月大的中毒女孩病情严重\\n10月22日，兄妹俩住院治疗38天，表姑妈黎女士告诉华商报大风新闻记者，“兄妹俩当时分着喝了一小部分，就上吐下泻，后来确定孩子喝了这个饮料中毒，里面检出了三甲基硅醇。”\\n奶奶捡回饮料爷爷帮忙给打开\\n兄妹俩只喝了一点就上吐下泻\\n10月22日，表姑妈黎女士接受华商报大风新闻记者采访表示，6岁的表侄子和4岁的表侄女在深圳儿童医院住院抢救已经38天。\\n黎女士介绍，事发9月16日，孩子60来岁的奶奶拾废品，捡回来一瓶未过期的饮料。\\n“因为捡回来的饮料瓶子根本没有开封，是一个完好无损的饮料瓶，孩子自己都打不开，是孩子爷爷帮忙打开给他们喝的。倒出来只喝了一点，两个孩子都翻了，上吐下泻，兄妹俩应该喝的量是差不多，因为孩子一喝就感觉味道不对，兄妹俩分着喝了一会儿就不行了。”\\n黎女士解释称，虽然是孩子奶奶把饮料捡回来的，但老人也是无心之举，表弟家里经济条件有限。“都是农村出来的，孩子奶奶觉得没有开封可能没有问题，就带给孩子喝。他们家庭这两年不太走运，孩子父母欠了很多钱，奶奶捡拾废品卖钱也是贴补家用。”\\n黎女士说：“事发后，孩子奶奶很后悔，心理压力非常大，说孩子救不回来她赔命，孩子爸爸妈妈还要经常安慰老人，怕老人想不开。”\\n据黎女士介绍，辖区民警当天去儿童医院，也向孩子奶奶了解情况，但目前还没有调查结果。\\n医院收治时就觉得毒性不正常\\n送检检出三甲基硅醇怀疑投毒\\n黎女士告诉记者，“9月16号当天就赶紧送到深圳儿童医院抢救，就去做了检测，发现里面含有三甲基硅醇。当时在收治时，医院就觉得这个毒物毒性不正常，当天下午医院就报了警。”\\n“9月17号毒物检测出了报告，这属于工业毒品中毒，医院说不排除是有人故意投毒。”\\n毒物检测报告单显示，在送检的血液、胃液和不明液体中，检测到三甲基硅醇，其中血液中的浓度为270ng/ml。\\n\"},{\"attributes\":{\"width\":\"275\"},\"insert\":{\"image\":\"http://localhost:8080/images/cache/20231023/85903aad7db046a38d78ac5eb4714504\"}},{\"insert\":\"\\n毒物检测报告单显示，在送检的血液、胃液和不明液体中，检测到三甲基硅醇\\n据黎女士介绍，兄妹俩目前住院救治累计已经花费十几万元，“发生这个事情，救治的费用总要负担。孩子父母都在深圳这边打工，家里经济条件不好，现在治疗费可以亲戚朋友凑一凑，但是后期如果实在太多了，估计也没办法了。”\\n\"}]}', 1, 1, '2023-10-23 19:15:08', 0);
INSERT INTO `db_topic` VALUES (25, 'sba', '{\"ops\":[{\"insert\":\"qwe\\n\"}]}', 1, 3, '2024-05-03 16:50:48', 0);
INSERT INTO `db_topic` VALUES (26, 'jsssj', '{\"ops\":[{\"insert\":\"qwe\\n\"}]}', 1, 1, '2024-05-03 16:56:02', 0);
INSERT INTO `db_topic` VALUES (27, 'rr', '{\"ops\":[{\"insert\":\"rr\\n\"}]}', 1, 4, '2024-05-03 16:56:28', 0);
INSERT INTO `db_topic` VALUES (28, 'qwe', '{\"ops\":[{\"insert\":\"1wwdiqowjdoq\"},{\"insert\":{\"image\":\"http://localhost:8081/image/cache/20240503/37832dd9126c43a1953ba62a3eae4ccd\"}},{\"insert\":{\"image\":\"http://localhost:8081/image/cache/20240503/12c6b3f54aa243d8b08bda224ed77bc9\"}},{\"insert\":\"\\n\"}]}', 1, 1, '2024-05-03 22:23:55', 0);
INSERT INTO `db_topic` VALUES (29, '不许玩原神', '{\"ops\":[{\"insert\":\"呵呵额\\nsb\\n\"}]}', 1, 1, '2024-05-04 17:42:06', 0);

-- ----------------------------
-- Table structure for db_topic_comment
-- ----------------------------
DROP TABLE IF EXISTS `db_topic_comment`;
CREATE TABLE `db_topic_comment`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `uid` int NULL DEFAULT NULL,
  `tid` int NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `time` datetime NULL DEFAULT NULL,
  `quote` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_topic_comment
-- ----------------------------
INSERT INTO `db_topic_comment` VALUES (1, 1, 29, '{\"ops\":[{\"insert\":\"傻逼\\n\"}]}', '2024-05-14 10:15:01', '-1');
INSERT INTO `db_topic_comment` VALUES (2, 1, 29, '{\"ops\":[{\"insert\":\"？\\n\"}]}', '2024-05-14 10:18:00', '1');
INSERT INTO `db_topic_comment` VALUES (4, 1, 29, '{\"ops\":[{\"insert\":\"nmsl\\n\"}]}', '2024-05-14 10:25:54', '2');
INSERT INTO `db_topic_comment` VALUES (6, 1, 29, '{\"ops\":[{\"insert\":\"nmxs\\n\"}]}', '2024-05-14 10:39:23', '4');
INSERT INTO `db_topic_comment` VALUES (7, 1, 29, '{\"ops\":[{\"insert\":\"666\\n\\n\"}]}', '2024-05-14 10:39:55', '6');
INSERT INTO `db_topic_comment` VALUES (9, 1, 29, '{\"ops\":[{\"insert\":\"嗯嗯\\n\"}]}', '2024-05-14 10:55:02', '7');
INSERT INTO `db_topic_comment` VALUES (10, 1, 29, '{\"ops\":[{\"insert\":\"不是？\\n\"}]}', '2024-05-14 11:09:25', '9');
INSERT INTO `db_topic_comment` VALUES (11, 1, 29, '{\"ops\":[{\"insert\":\"byd\\n\"}]}', '2024-05-14 11:10:30', '10');
INSERT INTO `db_topic_comment` VALUES (12, 1, 29, '{\"ops\":[{\"insert\":\"jj\\n\"}]}', '2024-05-14 11:23:48', '11');
INSERT INTO `db_topic_comment` VALUES (14, 1, 29, '{\"ops\":[{\"insert\":\"99jj\\n\"}]}', '2024-05-14 11:24:57', '13');
INSERT INTO `db_topic_comment` VALUES (16, 1, 29, '{\"ops\":[{\"insert\":\"retard\\n\"}]}', '2024-05-14 11:44:05', '14');
INSERT INTO `db_topic_comment` VALUES (17, 3, 29, '{\"ops\":[{\"insert\":\"啊？\\n\"}]}', '2024-05-16 11:16:55', '14');
INSERT INTO `db_topic_comment` VALUES (18, 3, 28, '{\"ops\":[{\"insert\":\"牛的\\n\"}]}', '2024-05-16 11:58:08', '-1');

-- ----------------------------
-- Table structure for db_topic_interact_collect
-- ----------------------------
DROP TABLE IF EXISTS `db_topic_interact_collect`;
CREATE TABLE `db_topic_interact_collect`  (
  `tid` int NULL DEFAULT NULL,
  `uid` int NULL DEFAULT NULL,
  `time` datetime NULL DEFAULT NULL,
  UNIQUE INDEX `tid_uid_collect`(`tid` ASC, `uid` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_topic_interact_collect
-- ----------------------------
INSERT INTO `db_topic_interact_collect` VALUES (16, 1, '2023-10-23 21:19:09');
INSERT INTO `db_topic_interact_collect` VALUES (15, 1, '2023-10-23 21:50:35');

-- ----------------------------
-- Table structure for db_topic_interact_like
-- ----------------------------
DROP TABLE IF EXISTS `db_topic_interact_like`;
CREATE TABLE `db_topic_interact_like`  (
  `tid` int NULL DEFAULT NULL,
  `uid` int NULL DEFAULT NULL,
  `time` datetime NULL DEFAULT NULL,
  UNIQUE INDEX `tid_uid_like`(`tid` ASC, `uid` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_topic_interact_like
-- ----------------------------
INSERT INTO `db_topic_interact_like` VALUES (16, 1, '2023-10-23 21:19:10');

-- ----------------------------
-- Table structure for db_topic_type
-- ----------------------------
DROP TABLE IF EXISTS `db_topic_type`;
CREATE TABLE `db_topic_type`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `color` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_topic_type
-- ----------------------------
INSERT INTO `db_topic_type` VALUES (1, '日常闲聊', '在这里分享你的各种日常', '#1E90FF');
INSERT INTO `db_topic_type` VALUES (2, '真诚交友', '在校园里寻找与自己志同道合的朋友', '#CE1EFF');
INSERT INTO `db_topic_type` VALUES (3, '问题反馈', '反馈你在校园里遇到的问题', '#E07373');
INSERT INTO `db_topic_type` VALUES (4, '恋爱官宣', '向大家展示你的恋爱成果', '#E0CE73');
INSERT INTO `db_topic_type` VALUES (5, '踩坑记录', '将你遇到的坑分享给大家，防止其他人再次入坑', '#3BB62A');

SET FOREIGN_KEY_CHECKS = 1;
