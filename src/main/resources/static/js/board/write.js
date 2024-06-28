$(function () {
    const generalForm = document.forms["frmGeneral"];
    const clubForm = document.forms["frmClub"];
    const noticeForm = document.forms["frmNotice"];
    const categorySelectGeneral = document.getElementById('lang');
    const categorySelectClub = document.getElementById('clubWrite');
    const categorySelectNotice = document.getElementById('noticeWrite');
    const submitButton = document.querySelector('.submit');
    const clubSubmitButton = document.querySelector('.clubSubmit');
    const noticeSubmitButton = document.querySelector('.noticeSubmit');

    // 쿼리 파라미터에서 boardType 가져와서 localStorage 에 저장
    const urlParams = new URLSearchParams(window.location.search);
    const boardType = urlParams.get('boardType') || localStorage.getItem("boardType") || '2';

    toggleForms(boardType);
    localStorage.setItem("boardType", boardType);

    categorySelectGeneral.addEventListener('change', function () {
        const selectedValue = categorySelectGeneral.value;
        toggleForms(selectedValue);
        localStorage.setItem("boardType", selectedValue);

        // toggleForms(categorySelectGeneral.value);
    });

    categorySelectClub.addEventListener('change', function () {
        const selectedValue = categorySelectClub.value;
        toggleForms(selectedValue);
        localStorage.setItem("boardType", selectedValue);

        // toggleForms(categorySelectClub.value);
    });

    categorySelectNotice.addEventListener('change', function () {
        const selectedValue = categorySelectNotice.value;
        toggleForms(selectedValue);
        localStorage.setItem("boardType", selectedValue);

        // toggleForms(categorySelectNotice.value);
    })

    submitButton.addEventListener('click', function (event) {
        event.preventDefault();
        let answer = confirm("저장하시겠습니까?");
        if (answer) {
            localStorage.removeItem("boardType");
            // localStorage.setItem("boardType", 2);
            console.log("제출 버튼 클릭됨 - 일반 게시판");
            generalForm.submit(); // 폼 제출
        }
    });

    clubSubmitButton.addEventListener('click', function (event) {
        event.preventDefault();
        let answer = confirm("저장하시겠습니까?");
        if (answer) {
            localStorage.removeItem("boardType");
            // localStorage.setItem("boardType", 3);
            console.log("제출 버튼 클릭됨 - 클럽홍보");
            clubForm.submit(); // 폼 제출
        }
        // }
    });

    noticeSubmitButton.addEventListener('click', function (event) {
        event.preventDefault();
        let answer = confirm("저장하시겠습니까?");
        if (answer) {
            localStorage.removeItem("boardType");
            // localStorage.setItem("boardType", 1);
            console.log("제출 버튼 클릭됨 - 공지사항");
            noticeForm.submit();

        }
    })

    function toggleForms(selectedValue) {
        if (selectedValue === '2') {
            generalForm.style.display = 'block';
            clubForm.style.display = 'none';
            noticeForm.style.display = 'none';
        } else if (selectedValue === '1') {
            generalForm.style.display = 'none';
            clubForm.style.display = 'none';
            noticeForm.style.display = 'block';
        } else if (selectedValue === '3') {
            generalForm.style.display = 'none';
            clubForm.style.display = 'block';
            noticeForm.style.display = 'none';
        }
        categorySelectGeneral.value = selectedValue;
        categorySelectClub.value = selectedValue;
        categorySelectNotice.value = selectedValue;
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