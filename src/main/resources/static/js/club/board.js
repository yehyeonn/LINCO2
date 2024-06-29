$('#deleteBtn').click(function (){
    let answer = confirm("삭제하시겠습니까?");
    answer && $("form[name = 'frmDelete']").submit();
});

$('#backBtn').click(function (){
    history.back();
})

