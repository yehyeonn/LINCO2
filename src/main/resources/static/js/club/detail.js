$('#deleteBtn').click(function (){
    let answer = confirm("삭제하시겠습니까?");
    answer && $("form[name = 'frmDelete']").submit();
});

$('.outBtn').click(function (){
    let answer = confirm("강퇴하시겠습니까?");
    answer && $("form[name = 'frmOut']").submit();
});

$('#leaveBtn').click(function (){
    let answer = confirm("정말 탈퇴하시겠습니까?");
    answer && $("form[name = 'frmLeave']").submit();
});

$('#backBtn').click(function (){
    history.back();
})

