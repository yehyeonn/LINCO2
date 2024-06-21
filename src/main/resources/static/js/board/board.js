// // board.board-type = 1 : 공지사항, 2 : 자유게시판, 3 : 클럽홍보
// // 각 게시판 유형에 해당하는 컨텐츠 요소 선택
// const noticesContent = document.querySelector('.board-type-1');
// const freeboardContent = document.querySelector('.board-type-2');
// const clubContent = document.querySelector('.board-type-3');
//
// // 링크 요소 선택
// const boardListLink = document.getElementById('board-list');
// const boardClubLink = document.getElementById('board-club');
// const boardNoticesLink = document.getElementById('board-notices');
//
// // 초기에 자유게시판 컨텐츠를 보이도록 설정
// noticesContent.style.display = 'none';
// freeboardContent.style.display = 'block';
// clubContent.style.display = 'none';
//
// $(noticesContent).click(function (){
//     boardNoticesLink.addEventListener('click', () => {
//         noticesContent.style.display = 'block';
//         freeboardContent.style.display = 'none';
//         clubContent.style.display = 'none';
//
//     });
// })
//
// $(clubContent).click(function (){
//     // 링크 클릭 이벤트 핸들러
//     boardClubLink.addEventListener('click', () => {
//         clubContent.style.display = 'block';
//         freeboardContent.style.display = 'none';
//         noticesContent.style.display = 'none';
//     });
// })
//
// $(freeboardContent).click(function (){
//     boardListLink.addEventListener('click', () => {
//         freeboardContent.style.display = 'block';
//         noticesContent.style.display = 'none';
//         clubContent.style.display = 'none';
//     });
// })
//

$(document).ready(function() {
    var uri = new URL(window.location.href).searchParams.get("boardTypeId");
    if(uri === 3){
        $('.board-type-3').show();
        $('.board-type-1').hide();
        $('.board-type-2').hide();
    }else if(uri === 2){
        $('.board-type-2').show();
        $('.board-type-3').hide();
        $('.board-type-1').hide();
    }else if(uri == 1 ||uri == null || uri == "" || uri == 'undefined'){
        $('.board-type-1').show();
        $('.board-type-2').hide();
        $('.board-type-3').hide();
    }

    // // 초기에 자유게시판 컨텐츠를 보이도록 설정
    // $('.board-type-1').hide();
    // $('.board-type-2').show();
    // $('.board-type-3').hide();
    //
    // // 페이지 로딩 시 자유게시판 페이지네이션 초기화
    // // initPagination(2); // boardTypeId가 2인 자유게시판 페이지네이션 초기화
    //
    // // 링크 클릭 이벤트 핸들러
    // $('#board-list').click(function(e) {
    //     // e.preventDefault();
    //     $('.board-type-1').hide();
    //     $('.board-type-2').show();
    //     $('.board-type-3').hide();
    //     // AJAX를 이용한 페이지네이션 초기화
    //     // initPagination(2); // boardTypeId가 2인 자유게시판 페이지네이션 초기화
    // });
    //
    // $('#board-club').click(function(e) {
    //     // e.preventDefault();
    //     $('.board-type-1').hide();
    //     $('.board-type-2').hide();
    //     $('.board-type-3').show();
    //     // AJAX를 이용한 페이지네이션 초기화
    //     // initPagination(3); // boardTypeId가 3인 클럽홍보 페이지네이션 초기화
    // });
    //
    // $('#board-notices').click(function(e) {
    //     // e.preventDefault();
    //     $('.board-type-1').show();
    //     $('.board-type-2').hide();
    //     $('.board-type-3').hide();
    //     // AJAX를 이용한 페이지네이션 초기화
    //     // initPagination(1); // boardTypeId가 1인 공지사항 페이지네이션 초기화
    // });

    // 페이지네이션 초기화 함수
    // function initPagination(boardTypeId) {
    //     // 페이지네이션 링크 클릭 시
    //     $('.pagination a').click(function(e) {
    //         e.preventDefault();
    //         const url = $(this).attr('href'); // 페이지네이션 링크의 href 속성에서 URL을 가져옵니다.
    //
    //         // AJAX로 데이터를 불러와서 해당 섹션에 적용하는 예시
    //         $.ajax({
    //             url: url,
    //             type: 'GET',
    //             success: function(data) {
    //                 // 데이터를 받아서 처리하는 로직을 추가하세요
    //                 // 예를 들어, 불러온 데이터를 해당 섹션에 적용하고, 페이지네이션도 업데이트할 수 있습니다.
    //                 console.log(data);
    //                 $('.board-type-' + boardTypeId + ' .tbody').html(data); // 해당 섹션의 tbody에 데이터를 적용
    //             },
    //             error: function(xhr, status, error) {
    //                 console.error('Error:', error);
    //             }
    //         });
    //     });
    // }
});
