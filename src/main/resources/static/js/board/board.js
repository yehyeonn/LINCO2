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

// ------------------------------------------------------------------------
$(document).ready(function() {
    var uri = new URL(window.location.href).searchParams.get("boardTypeId");
    console.log("uri: "  + uri);

    if(uri == 3){
        $('.board-type-3').show();
        $('.board-type-1').hide();
        $('.board-type-2').hide();
    }else if(uri == 2 ||uri == null || uri == "" || uri == 'undefined'){
        $('.board-type-2').show();
        $('.board-type-3').hide();
        $('.board-type-1').hide();
    }else if(uri == 1){
        $('.board-type-1').show();
        $('.board-type-2').hide();
        $('.board-type-3').hide();
    }
});


// ----------------------------------------------------------------