DROP DATABASE IF EXISTS zhihu;
CREATE DATABASE zhihu
    DEFAULT CHARACTER SET utf8mb4 -- 乱码问题
    DEFAULT COLLATE utf8mb4_bin -- 英文大小写不敏感问题
;
USE zhihu;

set foreign_key_checks = off;

DROP TABLE IF EXISTS users;
CREATE TABLE users
(
    `id`        BIGINT AUTO_INCREMENT COMMENT '用户ID',
    `name`      VARCHAR(15) COMMENT '用户名称',
    `pwd`       VARCHAR(15) COMMENT '密码',
    `email`     VARCHAR(31) COMMENT '邮箱',
    `avatarUrl` VARCHAR(255) COMMENT '头像',
    `headline`  VARCHAR(63) COMMENT '座右铭',
    PRIMARY KEY (id)
) ENGINE = InnoDB
    COMMENT '用户表';

DROP TABLE IF EXISTS articles;
CREATE TABLE articles
(
    `id`        BIGINT AUTO_INCREMENT COMMENT '文章ID',
    `title`     VARCHAR(31) COMMENT '标题',
    `content`   TEXT COMMENT '内容',
    `excerpt`   VARCHAR(255) COMMENT '摘要',
    `creatorId` BIGINT COMMENT '作者ID，物理外键',
    `type`      INT COMMENT '类型',
    `coverUrl`  VARCHAR(255) COMMENT '封面',
    PRIMARY KEY (`id`),
    foreign key (`creatorId`) references users (`id`)
) ENGINE = InnoDB
    COMMENT '文章表';


DROP TABLE IF EXISTS questions;
CREATE TABLE questions
(
    `id`          BIGINT AUTO_INCREMENT COMMENT '问题ID',
    `title`       VARCHAR(31) COMMENT '标题',
    `description` VARCHAR(255) COMMENT '描述',
    `excerpt`     VARCHAR(255) COMMENT '摘要',
    `creatorId`   BIGINT COMMENT '作者ID，物理外键',
    `type`        INT COMMENT '类型',
    PRIMARY KEY (`id`),
    foreign key (`creatorId`) references users (`id`)
) ENGINE = innodb
    COMMENT = '问题表';

drop table if exists answers;
create table answers
(
    `id`         BIGINT AUTO_INCREMENT COMMENT '回答ID',
    `content`    TEXT COMMENT '内容',
    `excerpt`    varchar(255) COMMENT '摘要',
    `creatorId`  BIGINT COMMENT '作者ID，物理外键',
    `questionId` BIGINT COMMENT '问题ID，物理外键',
    PRIMARY KEY (`id`),
    foreign key (`creatorId`) references users (`id`),
    foreign key (`questionId`) references questions (`id`)
) ENGINE = innodb
    COMMENT = '回答表';

drop table if exists status;
create table status
(
    `id`         BIGINT AUTO_INCREMENT COMMENT '状态ID',
    `voteup`     BIGINT COMMENT '支持',
    `votedown`   BIGINT COMMENT '反对',
    `favorite`   BIGINT COMMENT '收藏',
    `thanks`     BIGINT COMMENT '感谢',
    `targetType` INT COMMENT '目标类型：文章或问题',
    `targetId`   BIGINT COMMENT '目标ID，逻辑外键',
    PRIMARY KEY (`id`)
) ENGINE = innodb
    COMMENT = '状态表';

drop table if exists comments;
create table comments
(
    `id`         BIGINT AUTO_INCREMENT COMMENT '评论ID',
    `type`       INT COMMENT '类型，一级评论或二级评论',
    `creatorId`  BIGINT COMMENT '作者ID',
    `content`    TEXT COMMENT '内容',
    `targetType` INT COMMENT '目标类型',
    `targetId`   BIGINT COMMENT '目标ID，逻辑外键',
    PRIMARY KEY (`id`)
) ENGINE = innodb
    COMMENT = '状态表';

set foreign_key_checks = on;

