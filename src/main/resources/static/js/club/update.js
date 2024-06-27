$(document).ready(function () {
    console.log("연걸?")

    let originalImg = $("#filename").val();
    console.log(originalImg);
    // 파일 선택 버튼 클릭 시 파일 입력 필드 클릭
    $('#fileSelect').on('click', function () {
        $('#fileInput').click();
    });

    // 파일 선택 시 미리보기 표시
    $('#fileInput').on('change', function () {
        const reader = new FileReader();
        const file = this.files[0];
        console.log(file);
        if (file) {

            reader.onload = function (e) {
                var previewImage = document.getElementById("previewImage");
                previewImage.src = e.target.result;
                previewImage.style.display = "block";

            }
            reader.readAsDataURL(file);
        }
    });


})