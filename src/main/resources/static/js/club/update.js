$(document).ready(function () {
    // 파일 선택 버튼 클릭 시 파일 입력 필드 클릭
    $('#fileSelect').on('click', function () {
        $('#fileInput').click();
    });

    // 파일 선택 시 미리보기 표시
    $('#fileInput').on('change', function () {
        const file = this.files[0];
        if (file) {
            const fileType = file.type;
            const validImgTypes = ["image/gif", "image/jpeg", "image/png"];
            if(!validImgTypes.includes(fileType)) {
                alert("이미지 파일만 업로드 가능합니다.");
                $('#fileInput').val('');
                var previewImage = document.getElementById("previewImage");
                previewImage.src = previousImageSrc; // 이전에 올려둔 이미지 다시 보이게 설정
                return;
            }

            const reader = new FileReader();
            reader.onload = function (e) {
                var previewImage = document.getElementById("previewImage");
                previewImage.src = e.target.result;
                previewImage.style.display = "block";

            }
            reader.readAsDataURL(file);
        }
    });


})