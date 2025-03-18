CREATE TABLE `user_additional_infos`
(
    `grade`          int    NOT NULL,
    `is_updated`     bit(1) NOT NULL,
    `student_id`     int    NOT NULL,
    `student_status` ENUM('LEAVE_OF_ABSENCE', 'ENROLLED', 'GRADUATED') DEFAULT NULL,
    `created_at`     datetime(6) DEFAULT NULL,
    `deleted_at`     datetime(6) DEFAULT NULL,
    `id`             bigint NOT NULL AUTO_INCREMENT,
    `updated_at`     datetime(6) DEFAULT NULL,
    `department`     varchar(255) DEFAULT NULL,
    `email`          varchar(255) DEFAULT NULL,
    `phone_number`   varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `user_additional_infos_chk_1` CHECK (`student_status` IN ('LEAVE_OF_ABSENCE', 'ENROLLED', 'GRADUATED'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `users`
(
    `created_at`              datetime(6) DEFAULT NULL,
    `deleted_at`              datetime(6) DEFAULT NULL,
    `id`                      bigint       NOT NULL AUTO_INCREMENT,
    `updated_at`              datetime(6) DEFAULT NULL,
    `user_additional_info_id` bigint       DEFAULT NULL,
    `name`                    varchar(255) DEFAULT NULL,
    `nickname`                varchar(255) DEFAULT NULL,
    `profile_image_url`       varchar(255) DEFAULT NULL,
    `provider_id`             varchar(255) NOT NULL,
    `provider`                enum('GOOGLE','GITHUB','NAVER','KAKAO') NOT NULL,
    `role`                    enum('ROLE_GUEST','ROLE_USER','ROLE_ACTIVE_USER','ROLE_SEMINAR_WRITER','ROLE_ADMIN') NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_al9u78quwjga8847et0d9uvs5` (`user_additional_info_id`),
    UNIQUE KEY `UK_2ty1xmrrgtn89xt7kyxx6ta7h` (`nickname`),
    CONSTRAINT `FKvllbfrdnl261khxkgtcaxvaf` FOREIGN KEY (`user_additional_info_id`) REFERENCES `user_additional_infos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `boards`
(
    `created_at`      datetime(6) DEFAULT NULL,
    `deleted_at`      datetime(6) DEFAULT NULL,
    `id`              bigint      NOT NULL AUTO_INCREMENT,
    `updated_at`      datetime(6) DEFAULT NULL,
    `user_id`         bigint      NOT NULL,
    `view_count`      bigint      NOT NULL,
    `title`           varchar(30) NOT NULL,
    `attach_file_url` varchar(500) DEFAULT NULL,
    `head_image_url`  varchar(500) DEFAULT NULL,
    `board_type`      enum('SEMINAR','NOTICE') NOT NULL,
    `content`         tinytext    NOT NULL,
    `status`          enum('DRAFT','PUBLISHED') NOT NULL,
    PRIMARY KEY (`id`),
    KEY               `FK7kt8hby5livgmjj15f79e9t6v` (`user_id`),
    CONSTRAINT `FK7kt8hby5livgmjj15f79e9t6v` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `categories`
(
    `created_at` datetime(6) DEFAULT NULL,
    `deleted_at` datetime(6) DEFAULT NULL,
    `id`         bigint      NOT NULL AUTO_INCREMENT,
    `updated_at` datetime(6) DEFAULT NULL,
    `name`       varchar(50) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `board_categories`
(
    `board_id`    bigint NOT NULL,
    `category_id` bigint NOT NULL,
    `created_at`  datetime(6) DEFAULT NULL,
    `deleted_at`  datetime(6) DEFAULT NULL,
    `id`          bigint NOT NULL AUTO_INCREMENT,
    `updated_at`  datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY           `FKnckgrc1tex85xdljrc6f6cuv` (`board_id`),
    KEY           `FKm0u2950uv41ohm0wptsoe207d` (`category_id`),
    CONSTRAINT `FKm0u2950uv41ohm0wptsoe207d` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`),
    CONSTRAINT `FKnckgrc1tex85xdljrc6f6cuv` FOREIGN KEY (`board_id`) REFERENCES `boards` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `board_comments`
(
    `board_id`   bigint       NOT NULL,
    `created_at` datetime(6) DEFAULT NULL,
    `deleted_at` datetime(6) DEFAULT NULL,
    `group_id`   bigint DEFAULT NULL,
    `id`         bigint       NOT NULL AUTO_INCREMENT,
    `updated_at` datetime(6) DEFAULT NULL,
    `user_id`    bigint       NOT NULL,
    `content`    varchar(500) NOT NULL,
    PRIMARY KEY (`id`),
    KEY          `FKip5oj5s3op3lkw2jh11lo29lh` (`board_id`),
    KEY          `FKjfgn0pcgnjwauax9d0nbedrkh` (`group_id`),
    KEY          `FK1r54umtvkyu1ai4pfownmgc0p` (`user_id`),
    CONSTRAINT `FK1r54umtvkyu1ai4pfownmgc0p` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `FKip5oj5s3op3lkw2jh11lo29lh` FOREIGN KEY (`board_id`) REFERENCES `boards` (`id`),
    CONSTRAINT `FKjfgn0pcgnjwauax9d0nbedrkh` FOREIGN KEY (`group_id`) REFERENCES `board_comments` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `image_files`
(
    `board_id`   bigint DEFAULT NULL,
    `created_at` datetime(6) DEFAULT NULL,
    `deleted_at` datetime(6) DEFAULT NULL,
    `id`         bigint       NOT NULL AUTO_INCREMENT,
    `updated_at` datetime(6) DEFAULT NULL,
    `url`        varchar(500) NOT NULL,
    PRIMARY KEY (`id`),
    KEY          `FKeqe1c0bwm5vqajrd42ykct77i` (`board_id`),
    CONSTRAINT `FKeqe1c0bwm5vqajrd42ykct77i` FOREIGN KEY (`board_id`) REFERENCES `boards` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `likes`
(
    `board_id`   bigint NOT NULL,
    `created_at` datetime(6) DEFAULT NULL,
    `deleted_at` datetime(6) DEFAULT NULL,
    `id`         bigint NOT NULL AUTO_INCREMENT,
    `updated_at` datetime(6) DEFAULT NULL,
    `user_id`    bigint NOT NULL,
    PRIMARY KEY (`id`),
    KEY          `FKkr7j7p7tbklbqsjn73kv3xeb2` (`board_id`),
    KEY          `FKnvx9seeqqyy71bij291pwiwrg` (`user_id`),
    CONSTRAINT `FKkr7j7p7tbklbqsjn73kv3xeb2` FOREIGN KEY (`board_id`) REFERENCES `boards` (`id`),
    CONSTRAINT `FKnvx9seeqqyy71bij291pwiwrg` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `notifications`
(
    `board_id`        bigint       NOT NULL,
    `created_at`      datetime(6) DEFAULT NULL,
    `deleted_at`      datetime(6) DEFAULT NULL,
    `id`              bigint       NOT NULL AUTO_INCREMENT,
    `invoker_id`      bigint       NOT NULL,
    `updated_at`      datetime(6) DEFAULT NULL,
    `sender_nickname` varchar(255) NOT NULL,
    `invoker_type`    enum('BOARD_LIKE','BOARD_COMMENT','RECRUITMENT_BOARD_COMMENT') NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `notifications_users`
(
    `is_read`         bit(1) NOT NULL,
    `created_at`      datetime(6) DEFAULT NULL,
    `deleted_at`      datetime(6) DEFAULT NULL,
    `id`              bigint NOT NULL AUTO_INCREMENT,
    `notification_id` bigint NOT NULL,
    `updated_at`      datetime(6) DEFAULT NULL,
    `user_id`         bigint NOT NULL,
    PRIMARY KEY (`id`),
    KEY               `FKkk801l9ilfliaollxjn5jcf01` (`notification_id`),
    KEY               `FKl6lio9ubryaoecvsnv2bpv3b5` (`user_id`),
    CONSTRAINT `FKkk801l9ilfliaollxjn5jcf01` FOREIGN KEY (`notification_id`) REFERENCES `notifications` (`id`),
    CONSTRAINT `FKl6lio9ubryaoecvsnv2bpv3b5` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `newsletters`
(
    `mentoring_notice`       bit(1)       NOT NULL,
    `project_notice`         bit(1)       NOT NULL,
    `seminar_content_notice` bit(1)       NOT NULL,
    `study_notice`           bit(1)       NOT NULL,
    `created_at`             datetime(6) DEFAULT NULL,
    `deleted_at`             datetime(6) DEFAULT NULL,
    `id`                     bigint       NOT NULL AUTO_INCREMENT,
    `updated_at`             datetime(6) DEFAULT NULL,
    `email`                  varchar(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_6wb7lqopcr3mh7t0ic6fkbi42` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `recruitment_boards`
(
    `current_member_num`   int           DEFAULT NULL,
    `recruitment_num`      int           DEFAULT NULL,
    `activity_finish`      datetime(6) DEFAULT NULL,
    `activity_start`       datetime(6) DEFAULT NULL,
    `created_at`           datetime(6) DEFAULT NULL,
    `deleted_at`           datetime(6) DEFAULT NULL,
    `id`                   bigint NOT NULL AUTO_INCREMENT,
    `recruitment_deadline` datetime(6) DEFAULT NULL,
    `updated_at`           datetime(6) DEFAULT NULL,
    `user_id`              bigint NOT NULL,
    `activity_cycle`       varchar(50)   DEFAULT NULL,
    `host`                 varchar(50)   DEFAULT NULL,
    `recruitment_target`   varchar(50)   DEFAULT NULL,
    `title`                varchar(50)   DEFAULT NULL,
    `summary`              varchar(100)  DEFAULT NULL,
    `content`              varchar(1000) DEFAULT NULL,
    `status`               enum('DRAFT','PUBLISHED') NOT NULL,
    `tag`                  enum('FRONTEND','BACKEND','AI','MOBILE','SECURITY','ETC') DEFAULT NULL,
    `type`                 enum('STUDY','PROJECT','MENTORING') DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY                    `FKs8oyu73ud9pwaw3v0lp6h24kj` (`user_id`),
    CONSTRAINT `FKs8oyu73ud9pwaw3v0lp6h24kj` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `recruitment_applications`
(
    `created_at`           datetime(6) DEFAULT NULL,
    `deleted_at`           datetime(6) DEFAULT NULL,
    `id`                   bigint NOT NULL AUTO_INCREMENT,
    `recruitment_board_id` bigint NOT NULL,
    `updated_at`           datetime(6) DEFAULT NULL,
    `user_id`              bigint NOT NULL,
    PRIMARY KEY (`id`),
    KEY                    `FKsfjrbh2myjpxg1poxvgyjnwhf` (`recruitment_board_id`),
    KEY                    `FKbswo3q2lam7sgyik45awpljlw` (`user_id`),
    CONSTRAINT `FKbswo3q2lam7sgyik45awpljlw` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `FKsfjrbh2myjpxg1poxvgyjnwhf` FOREIGN KEY (`recruitment_board_id`) REFERENCES `recruitment_boards` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `recruitment_form_questions`
(
    `is_essential`         bit(1) DEFAULT NULL,
    `number`               int          NOT NULL,
    `created_at`           datetime(6) DEFAULT NULL,
    `deleted_at`           datetime(6) DEFAULT NULL,
    `id`                   bigint       NOT NULL AUTO_INCREMENT,
    `recruitment_board_id` bigint       NOT NULL,
    `updated_at`           datetime(6) DEFAULT NULL,
    `question`             varchar(100) NOT NULL,
    `type`                 enum('CHOICE','DESCRIPTION','CHECKBOX') DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY                    `FK2sncm9uo3fdhg5booa1jqeswd` (`recruitment_board_id`),
    CONSTRAINT `FK2sncm9uo3fdhg5booa1jqeswd` FOREIGN KEY (`recruitment_board_id`) REFERENCES `recruitment_boards` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `recruitment_form_answers`
(
    `number`                       int         NOT NULL,
    `created_at`                   datetime(6) DEFAULT NULL,
    `deleted_at`                   datetime(6) DEFAULT NULL,
    `id`                           bigint      NOT NULL AUTO_INCREMENT,
    `recruitment_form_question_id` bigint      NOT NULL,
    `updated_at`                   datetime(6) DEFAULT NULL,
    `answer`                       varchar(50) NOT NULL,
    PRIMARY KEY (`id`),
    KEY                            `FK847iaqdq37sy94v8gdyu8xegi` (`recruitment_form_question_id`),
    CONSTRAINT `FK847iaqdq37sy94v8gdyu8xegi` FOREIGN KEY (`recruitment_form_question_id`) REFERENCES `recruitment_form_questions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `recruitment_application_descriptive_answers`
(
    `created_at`                   datetime(6) DEFAULT NULL,
    `deleted_at`                   datetime(6) DEFAULT NULL,
    `id`                           bigint NOT NULL AUTO_INCREMENT,
    `recruitment_application_id`   bigint NOT NULL,
    `recruitment_form_question_id` bigint NOT NULL,
    `updated_at`                   datetime(6) DEFAULT NULL,
    `answer`                       varchar(1000) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY                            `FKd1sw0n39t4f95ht4b5jtmhni4` (`recruitment_application_id`),
    KEY                            `FKd1r3krj5lb0taoxujfi0ug2qm` (`recruitment_form_question_id`),
    CONSTRAINT `FKd1r3krj5lb0taoxujfi0ug2qm` FOREIGN KEY (`recruitment_form_question_id`) REFERENCES `recruitment_form_questions` (`id`),
    CONSTRAINT `FKd1sw0n39t4f95ht4b5jtmhni4` FOREIGN KEY (`recruitment_application_id`) REFERENCES `recruitment_applications` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `recruitment_application_optional_answers`
(
    `created_at`                 datetime(6) DEFAULT NULL,
    `deleted_at`                 datetime(6) DEFAULT NULL,
    `id`                         bigint NOT NULL AUTO_INCREMENT,
    `recruitment_application_id` bigint NOT NULL,
    `recruitment_form_answer_id` bigint NOT NULL,
    `updated_at`                 datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY                          `FK4nwo1yvcvbqaorlgbs333xnps` (`recruitment_application_id`),
    KEY                          `FKnv6qoia74dioa0djx6nw4p2wn` (`recruitment_form_answer_id`),
    CONSTRAINT `FK4nwo1yvcvbqaorlgbs333xnps` FOREIGN KEY (`recruitment_application_id`) REFERENCES `recruitment_applications` (`id`),
    CONSTRAINT `FKnv6qoia74dioa0djx6nw4p2wn` FOREIGN KEY (`recruitment_form_answer_id`) REFERENCES `recruitment_form_answers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `recruitment_board_comments`
(
    `created_at`           datetime(6) DEFAULT NULL,
    `deleted_at`           datetime(6) DEFAULT NULL,
    `group_id`             bigint DEFAULT NULL,
    `id`                   bigint       NOT NULL AUTO_INCREMENT,
    `recruitment_board_id` bigint       NOT NULL,
    `updated_at`           datetime(6) DEFAULT NULL,
    `user_id`              bigint       NOT NULL,
    `content`              varchar(500) NOT NULL,
    PRIMARY KEY (`id`),
    KEY                    `FK1oran8p3dfg3yxlkxiqlotv8p` (`recruitment_board_id`),
    KEY                    `FKhpa5w4coucda4nopby8gr8o84` (`group_id`),
    KEY                    `FKcwehmsknk557cxc4gyew6reww` (`user_id`),
    CONSTRAINT `FK1oran8p3dfg3yxlkxiqlotv8p` FOREIGN KEY (`recruitment_board_id`) REFERENCES `recruitment_boards` (`id`),
    CONSTRAINT `FKcwehmsknk557cxc4gyew6reww` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `FKhpa5w4coucda4nopby8gr8o84` FOREIGN KEY (`group_id`) REFERENCES `recruitment_board_comments` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `report`
(
    `board_comment_id`             bigint NOT NULL,
    `created_at`                   datetime(6) DEFAULT NULL,
    `deleted_at`                   datetime(6) DEFAULT NULL,
    `id`                           bigint NOT NULL AUTO_INCREMENT,
    `recruitment_board_comment_id` bigint NOT NULL,
    `updated_at`                   datetime(6) DEFAULT NULL,
    `user_id`                      bigint NOT NULL,
    PRIMARY KEY (`id`),
    KEY                            `FKt1u19a9omt9hasj4yjq72mvqg` (`board_comment_id`),
    KEY                            `FKj850xwpfcqf0sfgq328dc9898` (`recruitment_board_comment_id`),
    KEY                            `FKq50wsn94sc3mi90gtidk0k34a` (`user_id`),
    CONSTRAINT `FKj850xwpfcqf0sfgq328dc9898` FOREIGN KEY (`recruitment_board_comment_id`) REFERENCES `recruitment_board_comments` (`id`),
    CONSTRAINT `FKq50wsn94sc3mi90gtidk0k34a` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `FKt1u19a9omt9hasj4yjq72mvqg` FOREIGN KEY (`board_comment_id`) REFERENCES `board_comments` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `seminar_applications`
(
    `grade`              int    NOT NULL,
    `preferred_date`     date         DEFAULT NULL,
    `created_at`         datetime(6) DEFAULT NULL,
    `deleted_at`         datetime(6) DEFAULT NULL,
    `id`                 bigint NOT NULL AUTO_INCREMENT,
    `updated_at`         datetime(6) DEFAULT NULL,
    `user_id`            bigint NOT NULL,
    `department`         varchar(255) DEFAULT NULL,
    `estimated_duration` varchar(255) DEFAULT NULL,
    `name`               varchar(255) DEFAULT NULL,
    `phone_number`       varchar(255) DEFAULT NULL,
    `presentation_topic` varchar(255) DEFAULT NULL,
    `seminar_name`       varchar(255) DEFAULT NULL,
    `student_id`         varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY                  `FKk3h5asfr7cfi19leh90o3jbd9` (`user_id`),
    CONSTRAINT `FKk3h5asfr7cfi19leh90o3jbd9` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;