function checkDuplicate() {
    var clubName = $("#name").val();
    $.ajax({
        url: "/club/checkDuplicate",
        type: "POST",
        data: {clubName: clubName},
        success: function (response) {
            if (response.duplicate) {
                console.log("찍히냐?!!!")
                $("#duplicateMessage").html('<span class="text-danger">이미 존재하는 클럽 이름입니다.</span>');
                $("#name").addClass('is-invalid').focus();
            } else {
                $("#duplicateMessage").empty();
                $("#name").removeClass('is-invalid');
            }
        },
        error: function (xhr, status, error) {
            console.error("Error:", error);
            // 에러 발생 시 처리
        }
    });
}
$(document).ready(function () {
    // 파일 선택 버튼 클릭 시 파일 입력 필드 클릭
    $('#fileSelect').on('click', function () {
        $('#fileInput').click();
    });

    // 파일 선택 시 미리보기 표시
    $('#fileInput').on('change', function () {
        const file = this.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function (e) {
                var previewImage = document.getElementById("previewImage");
                previewImage.src = e.target.result;
                previewImage.style.display = "block";
                var imgtxt = document.getElementById('img_txt');
                imgtxt.style.display = "none";
            }
            reader.readAsDataURL(file);
        }
    });
});