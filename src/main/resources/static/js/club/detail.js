$('#deleteBtn').click(function (){
    let answer = confirm("삭제하시겠습니까?");
    answer && $("form[name = 'frmDelete']").submit();
});

$('#outBtn').click(function (){
    let answer = confirm("내보내시겠습니까?");
    answer && $("form[name = 'frmOut']").submit();
});

$('#backBtn').click(function (){
    window.location.href = localStorage.getItem("backUrl")
})