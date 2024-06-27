
$(document).ready(function () {
    var nameflag = false;
    var clubName = $("#club-name").val();
    // 파일 선택 버튼 클릭 시 파일 입력 필드 클릭
    $('#fileSelect').on('click', function () {
        $('#fileInput').click();
    });

    $('#checkname').click(function (){
        clubName = $("#club-name").val();
        checkDuplicate();
    })

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




    function checkDuplicate() {
            $.ajax({
                url: "/club/checkDuplicate",
                type: "POST",
                data: {clubName: clubName},
                success: function (response) {
                    if (response.duplicate) {
                        $("#duplicateMessage").html('<span class="text-danger">이미 존재하는 클럽 이름입니다.</span>');
                        $("#club-name").addClass('is-invalid').focus();
                        nameflag = false;
                    } else {
                        $("#duplicateMessage").html('<span style="color: chartreuse">사용 가능한 클럽 이름입니다.</span>');
                        $("#club-name").removeClass('is-invalid');
                        nameflag = true;
                    }
                },
                error: function (xhr, status, error) {
                    console.error("Error:", error);
                    // 에러 발생 시 처리
                }
            });

    }
        $('#submitbtn').click(function (event){
            if(!nameflag){
                if(!clubName){
                    $('form[name = clubcreate]').submit();
                }else{
                    $("#duplicateMessage").html('<span class="text-danger">이미 존재하는 클럽 이름입니다.</span>');
                }
                $('#club-name').focus();
            }else{
                $('form[name = clubcreate]').submit();
            }
        })

});

