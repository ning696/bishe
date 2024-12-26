CREATE table qd_sys_user(
user_id BIGINT UNSIGNED NOT NULL comment '用户id（主键）',
user_account VARCHAR(20) NOT NULL COMMENT '账号',
password VARCHAR(100) NOT NULL COMMENT '密码',
create_by bigint unsigned NOT NULL COMMENT '创建人',
create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
update_by bigint unsigned NOT NULL COMMENT '更新人',
update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (user_id),
unique key `idx_user_account` (`user_account`)
);
/*
id不设置为自增
char和varchar
*/
