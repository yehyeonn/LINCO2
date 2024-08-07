
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
        console.log("id : " + id);
        console.log("content : " + content);
        console.log("regdate : " + regdate);
        console.log("userName : " + username);
        console.log("name : " + name);
        let user_id = parseInt(comment.user.id);
        let delBtn = (logged_id === user_id) ? `<button class="btn" id="del-button" data-cmtdel-id="${id}" title="삭제">삭제</button>` : '';

        // 댓글 row 생성
        let row = `
            <tr>
                <td class="text">
                    <span><strong>${username}&nbsp;&nbsp;(${name})</strong></span>
                </td>
                <td class="text"><span>${content}</span></td>
                <td class="text">
                    <span class="text-secondary" style="font-size: 18px;">${regdate}</span>&nbsp;&nbsp;${delBtn}
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
        const deletedRow = $(this).closest("tr"); // 삭제할 댓글의 행 가져오기

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
                    // 삭제한 댓글의 행을 화면에서 제거
                    deletedRow.remove();
                    updateCommentCount(); // 댓글 개수 업데이트
                }
            },
            error: function(xhr, status, error) {
                console.error("Error:", error);
            }
        });
    });
}

// 댓글 개수 업데이트 함수
function updateCommentCount() {
    const currentCount = parseInt($("#cmt_cnt").text().trim());
    $("#cmt_cnt").text(currentCount - 1);
}

