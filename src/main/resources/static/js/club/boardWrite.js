$(function() {
    let i = 0;

    // 첨부파일 추가
    $("#btnAdd").click(function() {
        $("#files").append(`
            <div class="input-group">
                <input class="form-control" type="file" name="upfile${i}"/>
                <button type="button" class="btn" data-fileid-del="${i}" onclick="deleteFiles(${i}); $(this).parent().remove()">삭제</button>
            </div>
        `);
        i++;
    });

    // 삭제
    $("[data-fileid-del]").click(function() {
        let fileId = $(this).attr("data-fileid-del");
        deleteFiles(fileId);
        $(this).parent().remove();
    });
});

function deleteFiles(fileId) {
    $("#delFiles").append(`<input type="hidden" name="delfile" value="${fileId}">`);
}
