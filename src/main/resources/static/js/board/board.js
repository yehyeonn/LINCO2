// board.board-type = 1 : 공지사항, 2 : 자유게시판, 3 : 클럽홍보
$(document).ready(function (){
    // 각 게시판 유형에 해당하는 컨텐츠 요소 선택
    const noticesContent = document.querySelector('.board-type-1');
    const freeboardContent = document.querySelector('.board-type-2');
    const clubContent = document.querySelector('.board-type-3');

    // 링크 요소 선택
    const boardListLink = document.getElementById('board-list');
    const boardClubLink = document.getElementById('board-club');
    const boardNoticesLink = document.getElementById('board-notices');

    // 초기에 자유게시판 컨텐츠를 보이도록 설정
    noticesContent.style.display = 'block';
    freeboardContent.style.display = 'none';
    clubContent.style.display = 'none';

    // 링크 클릭 이벤트 핸들러
    boardListLink.addEventListener('click', () => {
        freeboardContent.style.display = 'block';
        noticesContent.style.display = 'none';
        clubContent.style.display = 'none';
    });

    boardClubLink.addEventListener('click', () => {
        clubContent.style.display = 'block';
        freeboardContent.style.display = 'none';
        noticesContent.style.display = 'none';

    });

    boardNoticesLink.addEventListener('click', () => {
        noticesContent.style.display = 'block';
        freeboardContent.style.display = 'none';
        clubContent.style.display = 'none';

    });

})

