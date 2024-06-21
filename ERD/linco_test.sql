SELECT TABLE_NAME
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'linco'
;

select *
from USER;
select *
from AUTHORITY;
select *
from USER_AUTHORITY;
select *
from CLUB;
select *
from CLUB_USER_LIST;
select *
from BOARD_TYPE;
select *
from BOARD;
select *
from BOARD_LIKE;
select *
from ATTACHMENT;
select *
from ATTACHMENT_LIKE;
select *
from COMMENT;
select *
from VENUE;
select *
from RESERVATION;
select *
from SOCIALIZING;
select *
from USER_SOCIALIZING;

select u.user_id, a.name
from user_authority u,
     AUTHORITY a
where u.authority_id = a.id;

select u.id, u.name, b.board_type_id, b.title, b.content
from USER u,
     BOARD b
where u.id = b.user_id;

select b.user_id,
       c.user_id,
       b.title,
       b.id,
       c.board_id,
       b.board_type_id,
       b.club_id,
       c.content
from BOARD b,
     COMMENT c
where b.id = c.board_id;

select r.user_id, r.venue_id, v.id, v.address, v.open_time, v.close_time, v.limit_num, v.price, r.reserve_date, r.reserve_start_time, r.reserve_end_time
from VENUE v,
     RESERVATION r
where v.id = r.venue_id;

select s.id, u.socializing_id, u.user_id, u.role, s.meeting_date, s.meeting_time
    from SOCIALIZING s,
         USER_SOCIALIZING u
where u.socializing_id = s.id;

select *
from SOCIALIZING
where
    address like '%강남구%';
#     AND category='공부';
select *
from club;

select *
from VENUE
where venue_category = '회의실';

select *
from club_user_list;

desc BOARD;

SELECT
    count(*)
FROM COMMENT c
         LEFT JOIN USER u ON c.user_id = u.id
         LEFT JOIN ATTACHMENT a ON c.attachment_id = a.id
         LEFT JOIN BOARD b ON c.board_id = b.id
         LEFT JOIN BOARD_TYPE bt ON b.board_type_id = bt.id
WHERE c.board_id = 3
      and a.id is null
ORDER BY c.regdate DESC;



select *
from club_user_list;





