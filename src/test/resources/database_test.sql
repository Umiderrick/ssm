

drop table IF EXISTS admin_accl;
CREATE TABLE IF NOT EXISTS `admin_accl` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '组id',
  `resource_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '资源id',
  `access_type` enum('allow','deny') COLLATE utf8mb4_bin DEFAULT 'allow' COMMENT '限制类型',
  `dt_create` datetime NOT NULL DEFAULT '2016-1-1 00:00:00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_group_resource_id` (`group_id`,`resource_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;



drop table IF EXISTS admin_action_log;
CREATE TABLE IF NOT EXISTS `admin_action_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '操作人',
  `action_type` varchar(64) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '操作类型，例如modify_address',
  `body` text COLLATE utf8mb4_bin NOT NULL COMMENT '具体内容，json格式，{address:"origin",address_to:"target"',
  `dt_create` datetime NOT NULL DEFAULT '2016-1-1 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;



drop table IF EXISTS admin_all_resource;
CREATE TABLE IF NOT EXISTS `admin_all_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `resource_name` varchar(64) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '名称',
  `resource_body` varchar(255) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '资源具体内容',
  `dt_create` datetime NOT NULL DEFAULT '2016-1-1 00:00:00',
  `dt_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_resource_name` (`resource_name`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


drop table IF EXISTS admin_auth;
CREATE TABLE IF NOT EXISTS `admin_auth` (
  `identity` varchar(128) NOT NULL DEFAULT '',
  `pid` varchar(128) NOT NULL DEFAULT '',
  `password` varchar(128) NOT NULL DEFAULT '',
  `hash` varchar(32) NOT NULL DEFAULT '',
  `salt` varchar(128) DEFAULT NULL,
  UNIQUE KEY `key_unique` (`identity`),
  KEY `key_index` (`identity`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


drop table IF EXISTS admin_authority_group;
CREATE TABLE IF NOT EXISTS `admin_authority_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(128) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '组名称',
  `is_administrator` int(11) NOT NULL DEFAULT '0' COMMENT '是否超级管理员',
  `desc` varchar(255) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '描述',
  `dt_create` datetime NOT NULL DEFAULT '2016-1-1 00:00:00',
  `dt_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_group_name` (`group_name`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


drop table IF EXISTS admin_user;
CREATE TABLE IF NOT EXISTS `admin_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `identity` varchar(128) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '用户名',
  `pid` varchar(128) COLLATE utf8mb4_bin NOT NULL DEFAULT '''credentials''',
  `full_name` varchar(128) CHARACTER SET utf8mb4 NOT NULL DEFAULT '',
  `dt_create` datetime NOT NULL DEFAULT '2016-1-1 00:00:00',
  `dt_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_user_name` (`identity`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


drop table IF EXISTS admin_user_group;
CREATE TABLE IF NOT EXISTS `admin_user_group` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_user_group`(`user_id`,`group_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


drop table IF EXISTS amount;
CREATE TABLE IF NOT EXISTS `amount` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `total` int(11) DEFAULT '0',
  `sold_quantities` int(11) DEFAULT '0',
  `adjust` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


drop table IF EXISTS discount_code;
CREATE TABLE IF NOT EXISTS `discount_code` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `code` varchar(64) NOT NULL DEFAULT '',
  `discount_price` decimal(12,2) NOT NULL DEFAULT '300.00',
  `is_used` int(11) NOT NULL DEFAULT '0' COMMENT '0 表示没有使用过',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_unique` (`code`),
  KEY `code_index` (`code`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


drop table IF EXISTS invoice;
CREATE TABLE IF NOT EXISTS `invoice` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NOT NULL DEFAULT '0',
  `user_id` bigint(20) NOT NULL,
  `category` int(11) NOT NULL DEFAULT '0',
  `title` varchar(256) NOT NULL DEFAULT '',
  `tax_num` varchar(128) NOT NULL DEFAULT '',
  `address` varchar(256) NOT NULL DEFAULT '',
  `phone_num` varchar(32) NOT NULL DEFAULT '',
  `bank` varchar(128) NOT NULL DEFAULT '',
  `bank_account_number` varchar(128) NOT NULL DEFAULT '',
  `price` decimal(12,2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_order_id` (`order_id`),
  KEY `phonenum_index` (`user_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;



CREATE TABLE IF NOT EXISTS `invoice_old` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `title` varchar(256) NOT NULL DEFAULT '',
  `address` varchar(256) NOT NULL DEFAULT '',
  `price` decimal(12,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `phonenum_index` (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;



CREATE TABLE IF NOT EXISTS `order_code` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NOT NULL,
  `code` varchar(128) NOT NULL DEFAULT '',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;



CREATE TABLE IF NOT EXISTS `order_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NOT NULL DEFAULT '0',
  `source` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT 'None',
  `dt_create` datetime NOT NULL DEFAULT '2016-1-1 00:00:00',
  `dt_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;



CREATE TABLE IF NOT EXISTS `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `description` text COLLATE utf8mb4_bin NOT NULL,
  `img_list` text COLLATE utf8mb4_bin NOT NULL,
  `dt_create` datetime NOT NULL DEFAULT '2016-1-1 00:00:00',
  `dt_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;



CREATE TABLE IF NOT EXISTS `product_spec` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) NOT NULL DEFAULT '0',
  `name` varchar(255) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `description` text COLLATE utf8mb4_bin NOT NULL,
  `price` decimal(12,2) NOT NULL DEFAULT '99999.99',
  `img_list` text COLLATE utf8mb4_bin NOT NULL,
  `dt_create` datetime NOT NULL DEFAULT '2016-1-1 00:00:00',
  `dt_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


drop table IF EXISTS userinfo;
CREATE TABLE IF NOT EXISTS `userinfo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `phonenum` varchar(20) COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `phonenum` (`phonenum`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


drop table IF EXISTS user_order;
CREATE TABLE IF NOT EXISTS `user_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NOT NULL DEFAULT '0',
  `user_id` bigint(20) NOT NULL DEFAULT '0',
  `payment_status` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `payment_method` text COLLATE utf8mb4_bin NOT NULL,
  `total_payment` decimal(12,2) NOT NULL DEFAULT '99999.99',
  `act_total_payment` decimal(12,2) NOT NULL DEFAULT '99999.99',
  `discount_code` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `address` text COLLATE utf8mb4_bin NOT NULL,
  `dt_create` datetime NOT NULL DEFAULT '2016-1-1 00:00:00',
  `dt_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_invoiced` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


drop table IF EXISTS user_order_spec;
CREATE TABLE IF NOT EXISTS `user_order_spec` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NOT NULL DEFAULT '0',
  `product_id` bigint(20) NOT NULL DEFAULT '0',
  `product_price` decimal(12,2) NOT NULL DEFAULT '99999.99',
  `product_count` int(11) NOT NULL DEFAULT '0',
  `act_payment` decimal(12,2) NOT NULL DEFAULT '99999.99',
  `discount_code` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `dt_create` datetime NOT NULL DEFAULT '2016-1-1 00:00:00',
  `dt_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `remark` varchar(255) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;



CREATE TABLE IF NOT EXISTS `weishang` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `identity` varchar(128) NOT NULL DEFAULT '',
  `code` varchar(128) NOT NULL DEFAULT '',
  `create_time` datetime NOT NULL DEFAULT '2016-1-1 00:00:00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `identity_unique` (`identity`),
  KEY `identity_index` (`identity`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


ALTER TABLE user_order
  add COLUMN order_status VARCHAR(32) NOT NULL COMMENT '订单状态' AFTER user_id,
  add COLUMN `version` int(11) NOT NULL DEFAULT '1' COMMENT '更新版本号' AFTER id,
  MODIFY COLUMN `dt_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
<<<<<<< Updated upstream
  add UNIQUE INDEX `uniq_order_id`(`order_id`),
  add INDEX `idx_dc_status`(`dt_create`,`order_status`);
-- user_order_spec表增加当时商品的快照
=======
  add UNIQUE INDEX `uniq_order_id`(`order_id`);

>>>>>>> Stashed changes
ALTER TABLE user_order_spec
  ADD COLUMN product_snapshot TEXT NOT NULL COMMENT '商品快照' AFTER product_count,
  MODIFY COLUMN `dt_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

CREATE TABLE IF NOT EXISTS `user_order_changelog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL DEFAULT '1' COMMENT '订单修改版本号',
  `order_id` bigint(20) NOT NULL DEFAULT '0',
  `operator_type` VARCHAR(16) NOT NULL COMMENT '操作人类型user/admin...',
  `operator` VARCHAR(32) NOT NULL DEFAULT '0' COMMENT '操作人,例如:用户 管理员xxx',
  `action_type` VARCHAR(64) NOT NULL COMMENT '操作类型,例如modify_address,comment',
  `body` TEXT NOT NULL COMMENT '实际操作内容',
  `dt_create` datetime NOT NULL DEFAULT '2016-1-1 00:00:00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_order_id_version` (`order_id`,`version`)
)ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

alter table admin_action_log add index `idx_create` (`dt_create`,`user_id`);

ALTER TABLE admin_accl
  add COLUMN resource VARCHAR(128) NOT NULL COMMENT '资源,支持通配符*/?' AFTER id,
  drop index uniq_group_resource_id,
  drop column resource_id;

ALTER TABLE admin_authority_group
    drop COLUMN is_administrator,
    add COLUMN user_count int(11) NOT NULL DEFAULT '0' COMMENT '权限组中的人数' AFTER `desc`;

drop table admin_user;
drop table admin_auth;
drop table admin_all_resource;



ALTER TABLE userinfo
  add column `phone_validate_code` varchar(16) NOT NULL DEFAULT '' COMMENT '上一次发送的验证码',
  add column `dt_create` datetime NOT NULL DEFAULT '2016-1-1 00:00:00',
  add column `dt_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;


create TABLE `admin_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` VARCHAR (128) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '用户email',
  `full_name` VARCHAR (128) CHARACTER SET utf8mb4 NOT NULL DEFAULT '',
  `password` VARCHAR (128) NOT NULL DEFAULT '' COMMENT 'hash后的password',
  `salt` VARCHAR (128) NOT NULL DEFAULT '' COMMENT '盐',
  `is_freeze` TINYINT (1) NOT NULL DEFAULT '0' COMMENT '是否冻结',
  `dt_create` datetime NOT NULL DEFAULT '2016-1-1 00:00:00',
  `dt_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_email`(`email`)
)ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;



CREATE TABLE IF NOT EXISTS `payment_callback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NOT NULL DEFAULT '0',
  `pay_type` VARCHAR(32) NOT NULL COMMENT '支付方式',
  `total_payment` decimal(12,2) NOT NULL DEFAULT '99999.99' COMMENT '支付的金额',
  `payment_id` VARCHAR(255) NOT NULL COMMENT '支付方的支付id',
  `record` TEXT NOT NULL COMMENT '支付回调的原始文本',
  `dt_create` datetime NOT NULL DEFAULT '2016-1-1 00:00:00',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`)
)ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;