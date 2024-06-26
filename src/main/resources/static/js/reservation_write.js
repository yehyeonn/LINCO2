// 전역 변수
var today = new Date();
var selectedCell;
var realMonth = today.getMonth() + 1;
var timeArr = [];


// url에서 venue_id 추출
var url = window.location.href;
var queryString = url.split('?')[1];
var params = queryString.split('&');
var paramsObj = {};
params.forEach(function (param) {
    var keyValue = param.split('=');
    paramsObj[keyValue[0]] = keyValue[1];
});
var venue_id = paramsObj.venue_id;
localStorage.setItem('venue_id', venue_id);
// console.log("venue_id:", venue_id);

// 페이지 로드 시 실행될 함수
document.addEventListener('DOMContentLoaded', function () {
    buildCalendar(); // 달력 초기화
    displaySavedDate(); // 저장된 날짜 표시
    disableReservedTimes()

});


// 예약 불가능한 시간 처리 함수
function disableReservedTimes() {
    var reservedStartTimes = [];
    var reservedEndTimes = [];

    // 예약 시작 시간과 종료 시간을 배열에 저장
    document.querySelectorAll('.reservation-timebox .reserve-start').forEach(function (element) {
        reservedStartTimes.push(element.textContent.trim());
    });
    document.querySelectorAll('.reservation-timebox .reserve-end').forEach(function (element) {
        reservedEndTimes.push(element.textContent.trim());
    });

    // 모든 시간 셀을 가져와서 예약 불가능한 시간인지 확인
    var timeCells = document.querySelectorAll('.time td');
    timeCells.forEach(function (cell) {
        var cellTime = cell.textContent.trim();
        var currentTime = parseTime(cellTime);
        // console.log('셀 타임 : ' + cellTime);
        // console.log('currentTime : ' + currentTime);

        // 모든 예약 시간에 대해 반복하면서 예약 불가능한 시간 처리
        var isReserved = false;
        for (var i = 0; i < reservedStartTimes.length; i++) {
            var startTime = parseTime(reservedStartTimes[i]);
            var endTime = parseTime(reservedEndTimes[i]);

            // console.log('시작시간 : ' + startTime);
            // console.log('종료시간 : ' + endTime);


            if (currentTime >= startTime && currentTime < endTime) {
                cell.style.backgroundColor = 'gray'; // 예약 불가능한 시간을 회색으로 표시
                cell.style.pointerEvents = 'none'; // 클릭 이벤트 비활성화
                isReserved = true;
                break;
            }
        }
        if (!isReserved) {
            cell.style.backgroundColor = '';
            cell.style.color = ''
            cell.style.pointerEvents = 'auto';
        }
    });
}

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
    var month = selectedDate.getMonth() + 1; // 월은 0부터 시작하므로 1을 더해줌
    var day = selectedDate.getDate();

    var calendarTable = document.getElementById("calendar");
    var cells = calendarTable.getElementsByTagName("td");

    for (var i = 0; i < cells.length; i++) {
        var cell = cells[i];
        if (cell.innerHTML.trim() === day.toString()) {
            if (year === today.getFullYear() && month === today.getMonth() + 1) {
                cell.style.backgroundColor = 'lightblue';
                cell.style.color = 'white';
                selectedCell = cell;
            }

            var fullMonth = month < 10 ? '0' + month : month; // 한 자리 숫자인 경우 앞에 0을 붙임
            var fullDate = `${year}-${fullMonth}-${day}`;

            // 예약 날짜 업데이트
            $('.pay-box li:nth-child(4) div').text(fullDate);
            localStorage.setItem('selectedDate', fullDate); // 로컬 스토리지에 선택한 날짜 저장

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
    var fullDate = `${year}-${fullMonth}-${selectedDate}`;

    var arrowLeft = document.getElementsByTagName("label")[0];
    var arrowRight = document.getElementsByTagName("label")[1];

    // console.log(arrowLeft.textContent.trim());
    // console.log(arrowRight.textContent.trim());

    // 월을 넘기는 화살표는 출력 안함
    if (selectedDate === arrowLeft.textContent.trim() || selectedDate === arrowRight.textContent.trim()) {
        $('.pay-box li:nth-child(4) div').text('');
    } else {
        // 예약 날짜 업데이트
        $('.pay-box li:nth-child(4) div').text(fullDate);
        selectedCell = this;
        selectedCell.style.backgroundColor = 'lightblue';
        selectedCell.style.color = 'white';
    }

    timeArr = [];
    $('.time td.selected').removeClass('selected');

    // URL 파라미터 업데이트
    var url = `http://localhost:8080/reservation/write?venue_id=${venue_id}&selectedDate=${fullDate}`;
    history.replaceState({}, '', url);

    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/reservation/write',
        data: {
            venue_id: venue_id,
            selectedDate: fullDate
        },
        success: function (htmlString) {
            var reservations = getReservationTime(htmlString);

            // console.log(reservations)
            updateDisableReservations(reservations);
            // disableReservedTimes();

            localStorage.setItem('fullDate', `${year}-${fullMonth}-${selectedDate}`);
        },
    })
});


function getReservationTime(htmlString) {
    // 예약 정보를 담을 배열
    var reservations = [];

    // HTML 문자열을 제이쿼리 객체로 변환하여 예약 정보 추출
    var $html = $(htmlString);

    // .reservation-timebox 요소에서 예약 정보 추출
    $html.find('.reservation-timebox').each(function () {
        var $timebox = $(this);
        $timebox.find('.reserve-start').each(function (index) {
            var startTime = $(this).text().trim();
            var endTime = $timebox.find('.reserve-end').eq(index).text().trim();
            reservations.push({reserve_start_time: startTime, reserve_end_time: endTime});
        });
    });
    // console.log(reservations);
    return reservations;
}

// 예약 정보를 화면에 업데이트하는 함수
function updateDisableReservations(reservations) {
    // 모든 시간을 가져와 예약 불가능한 시간 확인
    var timeCells = document.querySelectorAll('.time td');

    // 초기화: 배경색과 클릭 이벤트 초기화
    timeCells.forEach(function (cell) {
        cell.style.backgroundColor = '';
        cell.style.color = ''
        cell.style.pointerEvents = 'auto';
    });

    // 받아온 예약 정보를 기반으로 시간대를 회색으로 표시하고 클릭 이벤트 비활성화
    reservations.forEach(function (reservation) {
        var startTime = parsingTimeTable(reservation.reserve_start_time);
        // console.log(startTime)
        var endTime = parsingTimeTable(reservation.reserve_end_time);
        // console.log(endTime)

        timeCells.forEach(function (cell) {
            var cellTime = parsingTimeTable(cell.textContent.trim());
            // console.log(cellTime);

            if (startTime <= cellTime && cellTime < endTime) {
                cell.style.backgroundColor = 'gray'; // 예약 불가능한 시간을 회색으로 표시
                cell.style.pointerEvents = 'none'; // 클릭 이벤트 비활성화
            }
        });
    });
}

function parsingTimeTable(timeString) {
    var timeParts = timeString.split(':');
    var hours = parseInt(timeParts[0], 10);
    var minutes = parseInt(timeParts[1], 10);

    hours = hours < 10 ? '0' + hours : hours;
    minutes = minutes < 10 ? '0' + minutes : minutes;
    return hours + ':' + minutes;
}

// 결제하기 버튼 클릭시 input 값 스토리지에 저장
$(document).on('click', '.pay-btn', function () {
    var name = $('.info-box .name').val();
    var email = $('.info-box .email').val();
    var tell = $('.info-box .tell').val();


    localStorage.setItem('name', name);
    localStorage.setItem('email', email);
    localStorage.setItem('tell', tell);

    timeArr = [];
    $('.time td.selected').removeClass('selected');


    console.log('선택날짜' + localStorage.getItem('selectedDate'));
    console.log('시작시간' + localStorage.getItem('startTime'));
    console.log('종료시간' + localStorage.getItem('endTime'));
    console.log('예약자명' + localStorage.getItem('name'));
    console.log('이메일' + localStorage.getItem('email'));
    console.log('전화번호' + localStorage.getItem('tell'));
    console.log('베뉴아이디' + localStorage.getItem('venue_id'));
    console.log('전체날짜' + localStorage.getItem('fullDate'));
});

$(document).ready(function () {
    var startTime = null;
    var endTime = null;
    var totalPrice = 0;
    // var selectedFirstTime = null;


    $('.time td').click(function () {
        var clickedCell = this;
        var selectedTime = $(clickedCell).text().trim();
        // console.log(selectedTime);

        if ($(clickedCell).hasClass('selected')) {
            $(clickedCell).removeClass('selected');

            var index = timeArr.indexOf(selectedTime);
            if (index !== -1) {
                timeArr.splice(index, 1);
            }
        } else {
            $(clickedCell).addClass('selected');
            timeArr.push(selectedTime);
        }
        // 시간 배열 시간순 정렬
        timeArr.sort(function (a, b) {
            return setForSortTime(a) - setForSortTime(b);
        });

        if (timeArr.length > 1) {
            for (var i = 0; i < timeArr.length - 1; i++) {
                // console.log(timeArr[i]);
                // console.log(timeArr[i + 1]);
                var firstTime = setStartTime(timeArr[i]);
                // console.log(firstTime);
                var secondTime = setStartTime(timeArr[i + 1]);
                // console.log(secondTime);

                if ((parseInt(secondTime, 10) - parseInt(firstTime, 10)) === 1 ||
                    (parseInt(secondTime, 10) - parseInt(firstTime, 10)) === 0) {
                    continue;
                } else {
                    if ($(clickedCell).hasClass('selected')) {
                        $(clickedCell).removeClass('selected');

                        var index = timeArr.indexOf(selectedTime);
                        if (index !== -1) {
                            timeArr.splice(index, 1);
                        }
                    } else {
                        $(clickedCell).addClass('selected');
                        timeArr.push(selectedTime);
                    }
                    alert('연속된 시간만 예약 가능합니다.');
                    return;
                }
            }
        }

        if (timeArr.length === 0) {
            $('.pay-box li:nth-child(2) div').text('');
            $('.pay-box li:nth-child(3) div').text('');
            totalPrice = 0;
            $('.pay-box li:nth-child(5) div').text(totalPrice);
            return;
        }

        for (let i = 0; i < timeArr.length; i++) {
            // console.log(timeArr[i]);
        }
        startTime = setStartTime(timeArr[0], 10);
        endTime = setEndTime(timeArr[timeArr.length - 1], 10);

        totalPrice = parseInt($('.price').text(), 10) * (parseInt(endTime, 10) - parseInt(startTime, 10));
        $('.pay-box li:nth-child(5) div').text(totalPrice);

        localStorage.setItem('startTime', startTime + ':00');
        localStorage.setItem('endTime', endTime + ':00');
        // console.log('로컬저장소 시작시간: ' + localStorage.getItem('startTime', startTime));
        // console.log('로컬저장소 종료시간: ' + localStorage.getItem('endTime', endTime));

        $('.pay-box li:nth-child(2) div').text(startTime + ':00');
        $('.pay-box li:nth-child(3) div').text(endTime + ':00');


    });
});

function setForSortTime(time) {
    var startTime = time.split('~')[0];
    var [hour, minute] = startTime.split(':').map(Number);
    return hour * 60 + minute;
}

// 시작시간을 뽑아내기 위한 셋팅
function setStartTime(time) {
    time = time.split('~')[0];
    time = time.split(':')[0];
    return time;
}

// 종료시간ㅇ르 뽑아내기 위한 셋팅
function setEndTime(time) {
    time = time.split('~')[1];
    time = time.split(':')[0];
    return time;
}

// 시간을 숫자로 변환하는 함수
function parseTime(time) {
    var [hour, minute] = time.split(':').map(Number);
    // console.log(hour);
    // console.log(minute);
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
venue_id
fullDate
* */