-- mysql
CREATE TABLE `student`
(
    `id`   int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(255) DEFAULT NULL,
    `age`  int(11)      DEFAULT NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


INSERT INTO zhang.student (name, age)
VALUES ('zhangsan', 18),
       ('wangwu', 20)
;

