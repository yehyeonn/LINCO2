// // board.board-type = 1 : 공지사항, 2 : 자유게시판, 3 : 클럽홍보

// ------------------------------------------------------------------------
$(document).ready(function() {
    var uri = new URL(window.location.href).searchParams.get("boardTypeId");

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