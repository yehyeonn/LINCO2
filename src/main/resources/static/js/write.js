$(function() {
    // 첨부파일 추가 버튼 클릭 시 파일 선택 창 열기
    $("#btnAdd").click(function() {
        // 새로운 파일 선택 창 엘리먼트를 생성하고 추가
        let input = $("<input>").attr("type", "file").addClass("form-control col-xs-3").attr("name", "upfile").appendTo("#files");

        // 삭제 버튼 추가
        let deleteBtn = $("<button>").attr("type", "button").addClass("btn btn-outline-danger").text("삭제").click(function() {
            $(this).parent().remove(); // 부모 요소인 <div> 삭제
        }).appendTo(input.parent()); // 삭제 버튼을 파일 선택 창의 부모 요소에 추가

        // 파일 선택 창 열기
        input.click();
    });
});
