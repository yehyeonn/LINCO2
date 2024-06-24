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
from club;

select *
from VENUE
where venue_category = '회의실';

select *
from club_user_list;

desc BOARD;

update socializing
set content ='6월 15일 토요일 06시30분 ~ 10시 갈현체육공원 야구장

몸풀고 펑고좀 받고 바로 경기 진행하겠습니다
빠른경기진행으로 9회 목표로 해보겠습니다

경기장:갈현체육공원 야구장 (인조잔디)
주소 : 인천광역시 계양구 갈현동 산49-2
참가비:1.8 (심판,시합구포함,단기보험가입비포함)

(징스파이크안됨,카본배트안됨)

기업 993-002864-01-022 공병성
1.8 (댓글로 신청후 10분안에 입금부탁드립니다)

댓글 신청 예) 1루측 포수 홍길동 단기보험

포수는 무료입니다 (경기후 환불해드립니다)
취소의경우 대체자가 구해지면 환불해드립니다
경기 3일전 취소는 환불이 안됩니다
지타의경우 불참자나 부상자 생길시 수비 투입할수있는점 참고해주세요

갈현체육공원 야구장은 보험가입이 필수입니다
방장이 보헙가입을 대신 해드립니다
이름 , 주민번호(뒷자리까지)
카톡으로 보내주세요
카톡id : sbskong
(지난번에 보냈더라도 다시 보내주세요)

1년짜리 보험이 가입한분은 댓글로 신청하실때
꼭 1년보험 가입이라고 적어주세요

★ 경기 당일 신분증 가지고 오세요 ★

심판 : 김기동

1루측 선공
타순
지타 (10,000) - 김응현 (입완) 1년보험
구원 - 우진혁 (입완) 단기
포수 (경기후환불) - 정원희 (입완) 단기
우익수 - 이상순 단기
중견수 - 김성구 (입완) 단기
좌익수 - 김경원 (입완) 단기
선발 - 문영길 (입완) 1년보험
1루수 - 김홍선 (입완) 단기
2루수 - 강정완 (입완) 단기
3루수 - 오정빈 (입완) 단기
유격수 - 박종범 (입완) 1년보험

3루측 후공
타순
지타 (10,000) - 원유민 (입완) 단기
구원 - 박원석 (입완) 단기
포수 (경기후환불) - 공병성 (입완) 1년보험
우익수 - 윤여웅(입완) 단기
중견수 - 정명균 (입완)1년보험(레이더스)
좌익수 - 이강진 (입완) 1년보험(레이더스)
선발 - 윤형구 (입완) 1년보험
1루수 - 윤현수 (입완) 1년보험 (계양야구클럽)
2루수 - 장기호 (입완) 1년보험(레이더스)
3루수 - 이후승 (입완) 1년보험(레이더스)
유격수 - 김동현 (입완) 1년보험(레이더스)'
where id = 1;







select *
from club_user_list;





