# sql 테이블 작성

DROP TABLE IF EXISTS ATTACHMENT_LIKE;
DROP TABLE IF EXISTS COMMENT;
DROP TABLE IF EXISTS USER_AUTHORITY;
DROP TABLE IF EXISTS CLUB_USER_LIST;
DROP TABLE IF EXISTS ATTACHMENT;
DROP TABLE IF EXISTS RESERVATION;
DROP TABLE IF EXISTS USER_SOCIALIZING;
DROP TABLE IF EXISTS SOCIALIZING;
DROP TABLE IF EXISTS VENUE;
DROP TABLE IF EXISTS BOARD;
DROP TABLE IF EXISTS BOARD_TYPE;
DROP TABLE IF EXISTS CLUB;
DROP TABLE IF EXISTS USER;
DROP TABLE IF EXISTS AUTHORITY;

CREATE TABLE USER
(
    id              INT          NOT NULL AUTO_INCREMENT,
    tel             VARCHAR(20)  NULL,
    username        VARCHAR(50)  NOT NULL COMMENT '이메일',
    password        VARCHAR(250) NOT NULL,
    name            VARCHAR(20)  NOT NULL,
    address         VARCHAR(50)  NULL,
    gender          VARCHAR(10)  NULL,
    birthday        DATETIME     NULL,
    profile_picture VARCHAR(100) NULL,
    regdate         DATETIME     NULL DEFAULT now(),
    PRIMARY KEY (id)
);

ALTER TABLE USER
    ADD CONSTRAINT UQ_username UNIQUE (username);

CREATE TABLE ATTACHMENT
(
    id         INT          NOT NULL AUTO_INCREMENT,
    board_id   INT          NULL,
    club_id    INT          NULL,
    sourcename VARCHAR(100) NOT NULL,
    filename   VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE ATTACHMENT_LIKE
(
    user_id       INT NOT NULL,
    attachment_id INT NOT NULL,
    PRIMARY KEY (user_id, attachment_id)
);

CREATE TABLE AUTHORITY
(
    id   INT         NOT NULL AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL COMMENT '권한명',
    PRIMARY KEY (id)
);

CREATE TABLE BOARD
(
    id            INT          NOT NULL AUTO_INCREMENT,
    user_id       INT          NOT NULL COMMENT 'user_id',
    club_id       INT          NULL,
    board_type_id INT          NOT NULL COMMENT '게시글위치',
    title         VARCHAR(100) NOT NULL COMMENT '제목',
    content       LONGTEXT     NULL COMMENT '내용',
    viewcnt       INT          NULL DEFAULT 0 COMMENT '조회수',
    regdate       DATETIME     NULL DEFAULT now() COMMENT '작성일',
    PRIMARY KEY (id)
);


CREATE TABLE BOARD_TYPE
(
    id   INT         NOT NULL AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE CLUB
(
    id                     INT          NOT NULL AUTO_INCREMENT,
    name                   VARCHAR(20)  NOT NULL,
    category               VARCHAR(20)  NOT NULL,
    detail_category        VARCHAR(20)  NOT NULL,
    intro                  TEXT         NULL COMMENT '소개글',
    content                LONGTEXT     NULL COMMENT '상세',
    representative_picture VARCHAR(100) NULL,
    PRIMARY KEY (id)
);

CREATE TABLE CLUB_USER_LIST
(
    user_id INT                       NOT NULL,
    club_id INT                       NOT NULL,
    role    ENUM ('MASTER', 'MEMBER') NOT NULL DEFAULT 'MEMBER',
    regdate DATETIME                  NULL DEFAULT NOW(),
    PRIMARY KEY (user_id, club_id)
);

CREATE TABLE COMMENT
(
    id            INT      NOT NULL AUTO_INCREMENT,
    user_id       INT      NOT NULL,
    board_id      INT      NULL,
    attachment_id INT      NULL,
    content       TEXT     NOT NULL COMMENT '내용',
    regdate       DATETIME NULL DEFAULT now() COMMENT '작성일',
    PRIMARY KEY (id)
);

# CREATE TABLE VENUE
# (
#     id                 INT          NOT NULL AUTO_INCREMENT,
#     venue_name         VARCHAR(50)  NOT NULL,
#     address            VARCHAR(100) NOT NULL,
#     limit_num          INT          NOT NULL,
#     venue_category     VARCHAR(20)  NOT NULL,
#     info_tel           VARCHAR(50)  NULL,
#     price              INT          NOT NULL,
#     posible_start_date VARCHAR(20)         NOT NULL COMMENT '이용가능시작',
#     posible_end_date   VARCHAR(20)         NOT NULL COMMENT '이용가능끝',
#     open_time          VARCHAR(20)         NOT NULL COMMENT '영업시작',
#     close_time         VARCHAR(20)         NOT NULL COMMENT '영업끝',
#     img                VARCHAR(500) NULL,
#     PRIMARY KEY (id)
# );

CREATE TABLE RESERVATION
(
    id                 INT                                NOT NULL AUTO_INCREMENT,
    user_id            INT                                NOT NULL,
    reservation_name   VARCHAR(20)                        NOT NULL,
    merchantUid        VARCHAR(100)                       NOT NULL,
    impUid             VARCHAR(100)                       NULL,
    email              VARCHAR(50)                        NOT NULL,
    tel                VARCHAR(20)                        NOT NULL,
    venue_id           INT                                NOT NULL,
    venue_name         VARCHAR(50)                        NOT NULL,
    status             ENUM ('CANCELED', 'PAYED', 'DONE') NOT NULL,
    reserve_date       DATE                               NOT NULL COMMENT '예약날짜',
    reserve_start_time TIME                               NOT NULL COMMENT '예약시작',
    reserve_end_time   TIME                               NOT NULL COMMENT '예약끝',
    total_price        INT                                NOT NULL,
    paydate            DATETIME                           NULL DEFAULT now(),
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES USER (id),
    FOREIGN KEY (venue_id) REFERENCES VENUE (id)
);

CREATE TABLE SOCIALIZING
(
    id                INT          NOT NULL AUTO_INCREMENT,
    venue_id          INT          NULL,
    socializing_title VARCHAR(20)  NOT NULL,
    category          VARCHAR(10)  NOT NULL,
    detail_category   VARCHAR(20)  NOT NULL,
    address           VARCHAR(100) NOT NULL,
    place_name        VARCHAR(100) NULL,
    meeting_date      DATE         NOT NULL COMMENT '약속날짜시간',
    meeting_time      TIME         NOT NULL,
    limit_num         int          NOT NULL COMMENT '2명 이상',
    content           LONGTEXT     NULL,
    total_price       INT          NULL DEFAULT 0,
    img               VARCHAR(255) NULL COMMENT '대표사진',
    regdate           DATETIME     NULL DEFAULT now(),
    PRIMARY KEY (id)
);

CREATE TABLE USER_AUTHORITY
(
    user_id      INT NOT NULL,
    authority_id INT NOT NULL,
    PRIMARY KEY (user_id, authority_id)
);

CREATE TABLE USER_SOCIALIZING
(
    user_id        INT                       NOT NULL,
    socializing_id INT                       NOT NULL,
    role           ENUM ('MASTER', 'MEMBER') NOT NULL DEFAULT 'MEMBER',
    subscription   DATETIME                      NULL DEFAULT NOW(),
    PRIMARY KEY (user_id, socializing_id)
);


ALTER TABLE USER_AUTHORITY
    ADD CONSTRAINT FK_USER_TO_USER_AUTHORITY
        FOREIGN KEY (user_id)
            REFERENCES USER (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE USER_AUTHORITY
    ADD CONSTRAINT FK_AUTHORITY_TO_USER_AUTHORITY
        FOREIGN KEY (authority_id)
            REFERENCES AUTHORITY (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE BOARD
    ADD CONSTRAINT FK_USER_TO_BOARD
        FOREIGN KEY (user_id)
            REFERENCES USER (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE BOARD
    ADD CONSTRAINT FK_CLUB_TO_BOARD
        FOREIGN KEY (club_id)
            REFERENCES CLUB (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE BOARD
    ADD CONSTRAINT FK_BOARD_TYPE_TO_BOARD
        FOREIGN KEY (board_type_id)
            REFERENCES BOARD_TYPE (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE CLUB_USER_LIST
    ADD CONSTRAINT FK_CLUB_TO_CLUB_USER_LIST
        FOREIGN KEY (club_id)
            REFERENCES CLUB (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE CLUB_USER_LIST
    ADD CONSTRAINT FK_USER_TO_CLUB_USER_LIST
        FOREIGN KEY (user_id)
            REFERENCES USER (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE ATTACHMENT
    ADD CONSTRAINT FK_BOARD_TO_ATTACHMENT
        FOREIGN KEY (board_id)
            REFERENCES BOARD (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE ATTACHMENT
    ADD CONSTRAINT FK_CLUB_TO_ATTACHMENT
        FOREIGN KEY (club_id)
            REFERENCES CLUB (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE COMMENT
    ADD CONSTRAINT FK_BOARD_TO_COMMENT
        FOREIGN KEY (board_id)
            REFERENCES BOARD (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE COMMENT
    ADD CONSTRAINT FK_USER_TO_COMMENT
        FOREIGN KEY (user_id)
            REFERENCES USER (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE COMMENT
    ADD CONSTRAINT FK_ATTACHMENT_TO_COMMENT
        FOREIGN KEY (attachment_id)
            REFERENCES ATTACHMENT (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE ATTACHMENT_LIKE
    ADD CONSTRAINT FK_USER_TO_ATTACHMENT_LIKE
        FOREIGN KEY (user_id)
            REFERENCES USER (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE ATTACHMENT_LIKE
    ADD CONSTRAINT FK_ATTACHMENT_TO_ATTACHMENT_LIKE
        FOREIGN KEY (attachment_id)
            REFERENCES ATTACHMENT (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE USER_SOCIALIZING
    ADD CONSTRAINT FK_USER_TO_USER_SOCIALIZING
        FOREIGN KEY (user_id)
            REFERENCES USER (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE USER_SOCIALIZING
    ADD CONSTRAINT FK_SOCIALIZING_TO_USER_SOCIALIZING
        FOREIGN KEY (socializing_id)
            REFERENCES SOCIALIZING (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE RESERVATION
    ADD CONSTRAINT FK_USER_TO_RESERVATION
        FOREIGN KEY (user_id)
            REFERENCES USER (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE RESERVATION
    ADD CONSTRAINT FK_VENUE_TO_RESERVATION
        FOREIGN KEY (venue_id)
            REFERENCES VENUE (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

ALTER TABLE SOCIALIZING
    ADD CONSTRAINT FK_VENUE_TO_SOCIALIZING
        FOREIGN KEY (venue_id)
            REFERENCES VENUE (id)
            ON UPDATE RESTRICT
            ON DELETE CASCADE;

show tables;