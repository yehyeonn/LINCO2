$(function() {
    // const $generalForm = $("#generalForm");
    const generalForm = document.forms["frmGeneral"];
    const clubForm = document.forms["frmClub"];
    const noticeForm = document.forms["frmNotice"];
    const categorySelectGeneral = document.getElementById('lang');
    const categorySelectClub = document.getElementById('clubWrite');
    const categorySelectNotice = document.getElementById('noticeWrite');
    const submitButton = document.querySelector('.submit');
    const clubSubmitButton = document.querySelector('.clubSubmit');
    const noticeSubmitButton = document.querySelector('.noticeSubmit');
    const clubUserList = document.getElementById('userclubs');

    // 초기 상태 설정
    toggleForms(categorySelectGeneral.value);

    categorySelectGeneral.addEventListener('change', function () {
        toggleForms(categorySelectGeneral.value);
    });

    categorySelectClub.addEventListener('change', function () {
        toggleForms(categorySelectClub.value);
    });

    categorySelectNotice.addEventListener('change', function (){
        toggleForms(categorySelectNotice.value);
    })

    clubUserList.addEventListener('change', function (){
        console.log("Club 정보 : " + clubUserList);
    })

    submitButton.addEventListener('click', function (event) {
        event.preventDefault();
        let answer = confirm("저장하시겠습니까?");
        if (answer) {
            console.log("제출 버튼 클릭됨 - 일반 게시판");
            // $generalForm.submit();
            generalForm.submit(); // 폼 제출
        }
    });

    clubSubmitButton.addEventListener('click', function (event) {
        event.preventDefault();
        let answer = confirm("저장하시겠습니까?");
        if (answer) {
            console.log("제출 버튼 클릭됨 - 클럽홍보");
            clubForm.submit(); // 폼 제출
        }
    });

    noticeSubmitButton.addEventListener('click', function (event){
        event.preventDefault();
        let answer = confirm("저장하시겠습니까?");
        if (answer){
            console.log("제출 버튼 클릭됨 - 공지사항");
            noticeForm.submit();
        }
    })

    function toggleForms(selectedValue) {
        if (selectedValue === '2') {
            // $generalForm.show();
            generalForm.style.display = 'block';
            clubForm.style.display = 'none';
            noticeForm.style.display = 'none';

            categorySelectGeneral.value = selectedValue;
            categorySelectClub.value = selectedValue;
            categorySelectNotice.value = selectedValue;
        } else if (selectedValue === '3') {
            // $generalForm.hide();
            generalForm.style.display = 'none';
            clubForm.style.display = 'block';
            noticeForm.style.display = 'none';

            categorySelectGeneral.value = selectedValue;
            categorySelectClub.value = selectedValue;
            categorySelectNotice.value = selectedValue;
        } else if (selectedValue === '1'){
            generalForm.style.display = 'none';
            clubForm.style.display = 'none';
            noticeForm.style.display = 'block';

            categorySelectGeneral.value = selectedValue;
            categorySelectClub.value = selectedValue;
            categorySelectNotice.value = selectedValue;
        }
    }

    let i = 0;
    $(".btnAdd").click(function () {
        $(".files").append(`
        <div class="input-group mb-2">
            <input class="form-control col-xs-3" type="file" name="upfile${i}"/>
            <button type="button" class="btn btn-outline-danger" onclick="$(this).parent().remove()">삭제</button>
        </div>
        `);
        i++;
    });
});


// $(function () {
//     let i = 0;
//
//     $(".btnAdd").click(function () {
//         $(".files").append(`
//         <div class="input-group mb-2">
//             <input class="form-control col-xs-3" type="file" name="upfile${i}"/>
//             <button type="button" class="btn btn-outline-danger" onclick="$(this).parent().remove()">삭제</button>
//         </div>
//         `);
//         i++;
//     });
// });