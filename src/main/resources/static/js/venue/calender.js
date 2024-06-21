var today = new Date();
var date = new Date();

var getDayLimit = 1000000;
var possibleDay = getDayLimit.toString().split('');
// console.log(possibleDay)


// 사용자가 클릭한 셀 객체
var selectedCell;
var realMonth = date.getMonth() + 1;    // 오늘이전 불가능
var realToDay = date.getDate();

// 사용자가 클릭한 일자의 월, 일 객체
var selectedMonth = null;
var selectedDate = null;


function buildCalendar() {
    var row = null;
    var cnt = 0;

    var calendarTable = document.getElementById("calendar");
    var calendarTableTitle = document.getElementById("calendarTitle");
    calendarTableTitle.innerHTML = today.getFullYear() + "년 " + (today.getMonth() + 1) + "월";

    var firstDate = new Date(today.getFullYear(), today.getMonth(), 1);
    var lastDate = new Date(today.getFullYear(), today.getMonth() + 1, 0);

    while (calendarTable.rows.length > 2) {
        calendarTable.deleteRow(calendarTable.rows.length - 1);
    }

    row = calendarTable.insertRow();

    for (i = 0; i < firstDate.getDay(); i++) {
        cell = row.insertCell();
        cnt += 1;
    }

    for (i = 1; i <= lastDate.getDate(); i++) {
        noCount = 0;    // 예약을 못하면 1씩 증가
        etp = exchangeToPosibleDay(cnt) * 1;
        if (cnt % 7 == 0) {
            row = calendarTable.insertRow();
        }

        cell = row.insertCell();
        cell.setAttribute('id', i);
        cell.innerHTML = i;
        cell.align = "center";
        cnt += 1;

        // 색 표시
        if (cnt % 7 === 1) { // 일요일
            cell.textContent = i;
            cell.classList.add('disabled'); // 선택 불가능하게 하기 위해 클래스 추가
            cell.style.pointerEvents = 'none';  // 일요일은 호버 기능 삭제
        } else if (cnt % 7 === 0) { // 토요일
            cell.style.color = 'blue';
            cell.textContent = i;
            cell.classList.add('saturday');
            row = calendarTable.insertRow();
        }

        // console.log(noCount)

        // 선택한 날짜 출력
        if (cell.innerHTML === '') {
            cell.classList.add('empty');
            cell.style.pointerEvents = 'none';
        }
        if (noCount > 0 || cell.classList.contains('disabled')) {
            cell.style.color = "#E0E0E0";
            cell.innerHTML = "<font color='#C6C6C6' >" + i + "</font>";
        } else {
            cell.onclick = function () {
                if (selectedCell) {
                    selectedCell.style.backgroundColor = '';
                    selectedCell.style.color = '';
                    if (selectedCell.classList.contains('saturday')) {
                        selectedCell.style.color = 'blue'; // 토요일은 다시 파란색으로 설정
                    }
                }
                this.style.backgroundColor = 'lightblue';
                this.style.color = 'white';
                selectedCell = this;

                seelctMonth = today.getMonth() + 1;
                selectedDate = this.getAttribute('id');

                clickedYear = today.getFullYear();
                clickedMonth = (1 + today.getMonth());  // 0월부터 시작하니까
                clickedDate = this.getAttribute('id');

                clickedDate = clickedDate >= 10 ? clickedDate : '0' + clickedDate;  // 0~9 면 앞에 0 입력
                clickedMonth = clickedMonth >= 10 ? clickedMonth : '0' + clickedMonth;
                clickedYMD = clickedYear + "-" + clickedMonth + "-" + clickedDate;  // date 타입으로 값 세팅

                document.getElementById("selectedDate").value = clickedYMD; // 같은 창의 입력 필드에 설정
            };
        }
    }

    if (cnt % 7 != 0) {
        for (i = 0; i < 7 - (cnt % 7); i++) {
            cell = row.insertCell();
        }
    }

    nowMonth = today.getMonth() + 1;

// 예약 불가 일자 분류 1
    if (nowMonth > realMonth && i > realToDay) {
        noCount += 1;
    } else if (possibleDay[etp] == 0) {
        noCount += 1;
    }
};

function prevCalendar() {
    today = new Date(today.getFullYear(), today.getMonth() - 1, today.getDate());
    buildCalendar();
};

function nextCalendar() {
    today = new Date(today.getFullYear(), today.getMonth() + 1, today.getDate());
    buildCalendar();
};

document.addEventListener('DOMContentLoaded', function () {
    var posibleStartDate = document.getElementById('start_date').textContent.trim();
    var posibleEndDate = document.getElementById('end_date').textContent.trim();

    var startDate = new Date(posibleStartDate);
    var endDate = new Date(posibleEndDate);

    // console.log("시작 날짜는요~? " + startDate);
    // console.log("종료 날짜는요~?" + endDate);


    var thisMonthFullDateList = [];

    // 시작 날짜에서 종료 날짜까지의 모든 날짜를 배열에 추가합니다.
    for (var currentDate = new Date(startDate); currentDate <= endDate; currentDate.setDate(currentDate.getDate() + 1)) {
        var formattedDate = currentDate.toISOString().split('T')[0]; // 날짜를 'YYYY-MM-DD' 형식으로 변환
        thisMonthFullDateList.push(formattedDate);
    }

    // 날짜가 잘 들어갔을까요~?
    console.log("This Month Full Date List:");
    // console.log(thisMonthFullDateList);

    var priceStr = document.getElementById('venue_price').textContent.trim().split(' ')[0];
    console.log(priceStr)
    var price = Number(priceStr);
    console.log(price)


});


// 예약 불가능 일자 계산 함수
function exchangeToPosibleDay(num) {
    result = num % 7;
    result -= 1;    // 배열 인덱스로 사용, 0부터 시작하기 때문에 -1
    if (result == 1) {
        result = 6;
    }
    return result;  // cnt 를 넣어 현재 일이 무슨 요일인지 반환(일~토)
    // i 는 월의 첫 날(1일), cnt 는 첫행 첫 셀(빈 칸을 수도 있음 => 요일 확인 가능)
};

