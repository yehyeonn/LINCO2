// $(function (){  // document.ready()
//     // 글 (삭제) 버튼
//     $("#btnDel").click(function () {
//         let answer = confirm("삭제하시겠습니까?")
//         answer && $("form[name='frmDelete']").submit();
//     });
//
//     // 현재 글의 id 값 (삭제 기능 부분에 있음)
//     const id = $("input[name='id']").val().trim();
//
//     // 현재 글의 댓글들을 불러온다
//     loadComment(id);
//
//     // 댓글 작성 버튼 누르면 댓글 등록 하기.
//     // 1. 어느글에 대한 댓글인지? --> 위에 id 변수에 담겨있다
//     // 2. 어느 사용자가 작성한 댓글인지? --> logged_id 값
//     // 3. 댓글 내용은 무엇인지?  --> 아래 content
//     $("#btn_comment").click(function (){
//         const content = $("#input_comment").val().trim();       // 댓글
//
//         // 검증 (비어있을 경우 제출 못 하게)
//         if (!content){
//             alert("댓글 입력을 하세요");
//             $("#input_comment").focus();
//             return;
//         }
//
//         // POST 방식으로 전달할 parameter 준비
//         const data = {
//             "board_id": id,      // CommentController 에 지정한 이름으로 사용해야 함
//             "user_id": logged_id,
//             "content": content,
//         };
//         // console.log("board_id: " + id);
//         // console.log("user_id: " + logged_id);
//         // console.log("content: " + content);
//
//         $.ajax({
//             url: "/comment/write",
//             type: "POST",
//             data: data,
//             cache: false,
//             success: function (data, status){
//                 if (status == "success"){
//                     if (data.status !== "OK"){      // OK 가 아니면 리턴
//                         alert(data.status);
//                         return;
//                     }
//                     loadComment(id);        // 댓글 목록 다시 업데이트
//                     $("#input_comment").val(''); // 그 후 입력 칸은 비우기
//                 }
//             },
//         })
//     });
// });
//
//
// // 특정 글(board_id) 의 댓글 목록 읽어오기
// function loadComment(board_id){
//     $.ajax({
//         url: "/comment/list/" + board_id,
//         type: "GET",
//         cache: false,
//         success: function (data, status){
//             if (status == "success"){
//                 if (data.status !== "OK"){
//                     alert(data.status);
//                     return;
//                 }
//                 buildComment(data.data);
//                 addDelete();
//             }
//         },
//     });
// }
//
// function buildComment(comments){
//     $("#cmt_cnt").text(comments.length); // 댓글의 총 개수
//
//     const out = [];
//     comments.forEach(comment => {
//         let id = comment.id;
//         let content = comment.content.trim();
//         let regdate = comment.regdate;
//
//         let user_id = parseInt(comment.user.id);
//         let username = comment.user.username;
//         let name = comment.user.name;
//
//         const delBtn = (logged_id !== user_id) ? '' : `
//             <i class="btn fa-solid fa-delete-left text-danger" data-bs-toggle="tooltip"  data-cmtdel-id="${id}" title="삭제"></i>
//         `;
//
//         const row = `
//             <tr>
//                 <td class="text"><span><strong>${username}</strong><br><small class="text-secondary">(${name})</small></span></td>
//                 <td class="text"><span>${content}</span></td>
//                 <td class="text"><span><small class="text-secondary">${regdate}</small></span>${delBtn}</td>
//             </tr>
//         `;
//         out.push(row);
//     });
//
//     $("#cmt_list").html(out.join('\n'));
// }
//
// // 댓글 삭제 버튼이 눌렸을 때, 해당 댓글이 삭제하는 이벤트를 등록
// function addDelete(){
//     // 현재 글의 id
//     const id = $("input[name='id']").val().trim();
//
//     // 댓글 삭제 버튼마다 준 attribute 가져옴
//     $("[data-cmtdel-id]").click(function (){
//         if (!confirm("댓글을 삭제하시겠습니까?")) return;
//
//         // 삭제할 댓글의 id
//         const comment_id = $(this).attr("data-cmtdel-id");      // 여기에(data-cmtdel-id) 댓글의 id 를 담아둠
//
//         $.ajax({
//             url: "/comment/delete",
//             type: "POST",
//             cache: false,
//             data: {"id": comment_id},
//             success: function (data, status){
//                 if (status == "success"){
//                     if (data.status !== "OK"){
//                         alert(data.status);
//                         return;
//                     }
//
//                     // 삭제 후에도 댓글 목록 불러와야함
//                     loadComment(id);
//                 }
//             },
//         });
//     })
// }

$(function() {
    // 글 (삭제) 버튼
    $("#btnDel").click(function() {
        let answer = confirm("삭제하시겠습니까?");
        answer && $("form[name='frmDelete']").submit();
    });

    // 현재 글의 id 값
    const id = $("input[name='id']").val().trim();

    // 현재 글의 댓글들을 불러오는 함수 호출
    loadComment(id);

    // 댓글 작성 버튼 클릭 시
    $("#btn_comment").click(function() {
        const content = $("#input_comment").val().trim();

        // 댓글 입력 확인
        if (!content) {
            alert("댓글을 입력하세요.");
            $("#input_comment").focus();
            return;
        }

        // POST 방식으로 전송할 데이터 준비
        const data = {
            "board_id": id,
            "user_id": logged_id,
            "content": content
        };

        // 댓글 등록 AJAX 호출
        $.ajax({
            url: "/comment/write",
            type: "POST",
            data: data,
            cache: false,
            success: function(data, status) {
                if (status == "success") {
                    if (data.status !== "OK") {
                        alert(data.status);
                        return;
                    }
                    loadComment(id); // 댓글 목록 다시 불러오기
                    $("#input_comment").val(''); // 입력 칸 비우기
                }
            },
            error: function(xhr, status, error) {
                console.error("Error:", error);
            }
        });
        console.log(data);
    });
});

// 특정 글(board_id)의 댓글 목록 읽어오기
function loadComment(board_id) {
    $.ajax({
        url: "/comment/list/" + board_id,
        type: "GET",
        cache: false,
        success: function(data, status) {
            if (status == "success") {
                if (data.status !== "OK") {
                    alert(data.status);
                    return;
                }
                buildComment(data.data);
            }
        },
        error: function(xhr, status, error) {
            console.error("Error:", error);
        }
    });
}

// 댓글 목록 생성 및 출력 함수
function buildComment(comments) {
    $("#cmt_cnt").text(comments.length); // 댓글 개수 업데이트

    // 댓글 리스트 초기화
    $("#cmt_list").empty();

    comments.forEach(comment => {
        let id = comment.id;
        let content = comment.content.trim();
        let regdate = comment.regdate;
        let username = comment.user.username;
        let name = comment.user.name;
        let user_id = parseInt(comment.user.id);
        let delBtn = (logged_id === user_id) ? `<i class="btn" data-cmtdel-id="${id}" title="삭제">댓글 삭제</i>` : '';

        // 댓글 row 생성
        let row = `
            <tr>
                <td class="text">
                    <span><strong>${username}</strong><br>
                    <small class="text-secondary">(${name})</small></span>
                </td>
                <td class="text"><span>${content}</span></td>
                <td class="text">
                    <span><small class="text-secondary">${regdate}</small></span>
                    ${delBtn}
                </td>
            </tr>
        `;

        // 댓글 리스트에 추가
        $("#cmt_list").append(row);
    });

    // 댓글 삭제 버튼 이벤트 추가
    addDelete();
}

// 댓글 삭제 버튼 이벤트 등록
function addDelete() {
    $("[data-cmtdel-id]").click(function() {
        if (!confirm("댓글을 삭제하시겠습니까?")) return;

        const comment_id = $(this).attr("data-cmtdel-id");

        $.ajax({
            url: "/comment/delete",
            type: "POST",
            cache: false,
            data: { "id": comment_id },
            success: function(data, status) {
                if (status == "success") {
                    if (data.status !== "OK") {
                        alert(data.status);
                        return;
                    }
                    loadComment(id); // 삭제 후 댓글 목록 다시 불러오기
                }
            },
            error: function(xhr, status, error) {
                console.error("Error:", error);
            }
        });
    });
}
