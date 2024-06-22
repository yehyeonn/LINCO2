// $(function() {
//     // 첨부파일 추가 버튼 클릭 시 파일 선택 창 열기
//     $("#btnAdd").click(function() {
//         // 파일 선택 창과 삭제 버튼을 감싸는 div 요소 생성
//         let fileDiv = $("<div>").addClass("file-input-group").appendTo("#files");
//
//         // 파일 선택 창 엘리먼트를 생성하고 추가
//         let input = $("<input>").attr("type", "file").addClass("form-control").attr("name", "upfile").appendTo(fileDiv);
//
//         // 삭제 버튼 추가
//         let deleteBtn = $("<button>").attr("type", "button").addClass("btn btn-danger").text("삭제").click(function() {
//             $(this).parent().remove(); // 부모 요소인 <div> 삭제
//         }).appendTo(fileDiv);
//
//         // 파일 선택 창 열기
//         input.click();
//     });
// });

$(function (){
    let i = 0;

    $("#btnAdd").click(function (){
        $("#files").append(`
        <div class="input-group mb-2">
            <input class="form-control col-xs-3" type="file" name="upfile${i}"/>
            <button type="button" class="btn btn-outline-danger" onclick="$(this).parent().remove()">삭제</button>
        </div>
        `);
        i++;
    })
})