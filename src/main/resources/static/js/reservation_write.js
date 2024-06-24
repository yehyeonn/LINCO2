// 전역 변수
var today = new Date();
var selectedCell;
var realMonth = today.getMonth() + 1;
var realToDay = today.getDate();


// 페이지 로드 시 실행될 함수
document.addEventListener('DOMContentLoaded', function () {
    buildCalendar(); // 달력 초기화
    displaySavedDate(); // 저장된 날짜 표시

});

// 저장된 날짜 표시 함수
function displaySavedDate() {
    var savedDate = localStorage.getItem('selectedDate');
    if (savedDate) {
        var selectedDate = new Date(savedDate);
        selectDateOnCalendar(selectedDate);
    }
}

// 선택된 날짜 달력에 표시하는 함수
function selectDateOnCalendar(selectedDate) {
    var year = selectedDate.getFullYear();
    var month = selectedDate.getMonth();
    var day = selectedDate.getDate();

    var calendarTable = document.getElementById("calendar");
    var cells = calendarTable.getElementsByTagName("td");

    for (var i = 0; i < cells.length; i++) {
        var cell = cells[i];
        if (cell.innerHTML.trim() === day.toString()) {
            if (year === today.getFullYear() && month === today.getMonth()) {
                cell.style.backgroundColor = 'lightblue';
                cell.style.color = 'white';
                selectedCell = cell;
            }
            // 월 계산 0부터, 10보다 작으면 앞에 '0' 붙이고, 아님 그대로 저장
            var fullMonth = (month + 1) < 10 ? '0' + (month + 1) : (month + 1);
            var fullDate = `${year}년 ${fullMonth}월 ${day}일`;
            $('.pay-box li:nth-child(4) div').text(fullDate);
            break;
        }
    }
}

// 달력 날짜 클릭 시 pay-box 에 출력 및 선택 날짜 색상 변경
$(document).on('click', '#calendar td', function () {
    var selectedDate = $(this).text().trim();
    var year = today.getFullYear();
    var month = realMonth;

    var fullMonth = month < 10 ? '0' + month : month; // 한 자리 숫자인 경우 앞에 0을 붙임
    var fullDate = `${year}년 ${fullMonth}월 ${selectedDate}일`; // 년 월 일 형식으로 문자열 생성


    // 예약 날짜 업데이트
    $('.pay-box li:nth-child(4) div').text(fullDate);

    // 선택된 셀 스타일 업데이트
    selectedCell = this;
    selectedCell.style.backgroundColor = 'lightblue';
    selectedCell.style.color = 'white';

    // 선택된 날짜 로컬 저장
    localStorage.setItem('selectedDate', `${year}-${fullMonth}-${selectedDate}`);
});

// 결제하기 버튼 클릭시 input 값 스토리지에 저장
$(document).on('click', '.pay-btn', function () {
    var name = $('.info-box .name').val();
    var email = $('.info-box .email').val();
    var tell = $('.info-box .tell').val();


    localStorage.setItem('name', name);
    localStorage.setItem('email', email);
    localStorage.setItem('tell', tell);

    console.log('제발 나와라...name : ', localStorage.getItem('name'));
    console.log('제발 나와라...email : ', localStorage.getItem('email'));
    console.log('제발 나와라...tell : ', localStorage.getItem('tell'));
});

$(document).ready(function () {
    var startTime = null;
    var endTime = null;
    var usageTime = null;

    $('.time td').click(function () {
        var clickedCell = this;
        var selectedTime = $(clickedCell).text().trim();

        // 첫 번째 선택 - 시작 시간
        if (startTime === null) {
            $(clickedCell).css('background-color', '#4075e2');
            $(clickedCell).css('color', 'white');
            startTime = selectedTime;
            localStorage.setItem('startTime', startTime);
            console.log('시작시간 : ' + startTime);
        } else if (endTime === null) {
            // 두 번째 선택 - 종료 시간
            $(clickedCell).css('background-color', '#4075e2');
            $(clickedCell).css('color', 'white');
            endTime = selectedTime;
            localStorage.setItem('endTime', endTime);
            console.log('종료 시간: ' + endTime);

            // parseTime : 문자열을 숫자로 변환하기 위한 메서드
            startTime = parseTime(startTime);
            endTime = parseTime(endTime);

            // 숫자로 변환된 startTime 이 endTime 보다 크다면
            if (startTime > endTime) {
                resetTime();
                console.log('잘못선택해찌롱~');
                $('.pay-box li:nth-child(2) div').text('');
                $('.pay-box li:nth-child(3) div').text('');
                $('.pay-box li:nth-child(5) div').text('');

            } else {
                //총 이용시간
                usageTime = endTime - startTime;

                // 숫자로 변환한 시간을 다시 문자열로 변환하여 화면에 출력
                startTime += ':00';
                endTime += ':00';
                $('.pay-box li:nth-child(2) div').text(startTime);
                $('.pay-box li:nth-child(3) div').text(endTime);

                // 가격 계산
                var price = $('.info-box .price').text();
                // console.log('price = ' + price);
                $('.pay-box li:nth-child(5) div').text(usageTime * price);
            }
        } else {
            // 이미 시작, 종료시간을 선택한 뒤에 다른 시간을 다시 선택하면 이전 선택들 취소
            // & 다시 선택된 시간이 시작시간으로 등록
            resetTime();
            startTime = selectedTime;
            localStorage.setItem('startTime', startTime);
            $(clickedCell).css('background-color', '#4075e2');
            $(clickedCell).css('color', 'white');
            console.log('다시 시작 시간: ' + startTime);
        }
    });

    // 선택시간 리셋
    function resetTime() {
        $('.time td').css('background-color', '');
        $('.time td').css('color', '');
        startTime = null;
        endTime = null;
        usageTime = null;
        localStorage.removeItem('startTime');
        localStorage.removeItem('endTime');
    }
});

// 시간을 숫자로 변환하는 함수
function parseTime(time) {
    var [hour, minute] = time.split(':').map(Number);
    return hour;
}

/*
스토리지에 저장된 이름
selectedDate
startTime
emdTime
name
email
tell
* */