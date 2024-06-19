var today = new Date();

function buildCalendar(){
    var row = null;
    var cnt = 0;

    var calendarTable = document.getElementById("calendar");
    var calendarTableTitle = document.getElementById("calendarTitle");
    calendarTableTitle.innerHTML = today.getFullYear() + "년 " + (today.getMonth() + 1) + "월";

    var firstDate = new Date(today.getFullYear(), today.getMonth(), 1);
    var lastDate = new Date(today.getFullYear(), today.getMonth()+1, 0);

    while(calendarTable.rows.length > 2){
        calendarTable.deleteRow(calendarTable.rows.length -1);
    }

    row = calendarTable.insertRow();

    for(i = 0; i < firstDate.getDay(); i++){
        cell = row.insertCell();
        cnt += 1;
    }

    for(i = 1; i <= lastDate.getDate(); i++){
        if (cnt % 7 == 0){
            row = calendarTable.insertRow();
        }

        //
        // cnt += 1;

        cell = row.insertCell();
        cell.setAttribute('id', i);
        cell.innerHTML = i;
        cell.align = "center";
        cnt +=1;

        // 색 표시
        if(cnt % 7 == 1){
            cell.innerHTML = "<font color=#F79DC2>" + i + "</font>";
        }
        if (cnt % 7 == 0){
            cell.innerHTML = "<font color=skyblue>" + i + "</font>";
            row = calendar.insertRow();
        }



    // 선택한 날짜 출력
    cell.onclick = function (){
        clickedYear = today.getFullYear();
        clickedMonth = (1 + today.getMonth());  // 0월부터 시작하니까
        clickedDate = this.getAttribute('id');

        clickedDate = clickedDate >= 10 ? clickedDate : '0' + clickedDate;  // 0~9 면 앞에 0 입력
        clickedMonth = clickedMonth >= 10 ? clickedMonth : '0' + clickedMonth;
        clickedYMD = clickedYear + "-" + clickedMonth + "-" + clickedDate;  // date 타입으로 값 세팅

        opener.document.getElementById("date").value = clickedYMD;
        self.close();

        }
    }
    if(cnt % 7 != 0) {
        for(i =0 ; i < 7 - (cnt %7); i++){
            cell = row.insertCell();
        }
    }

};

function prevCalendar() {
    today = new Date(today.getFullYear(), today.getMonth() - 1, today.getDate());
    buildCalendar();
};

function nextCalendar(){
    today = new Date(today.getFullYear(), today.getMonth() + 1, today.getDate());
    buildCalendar();
};

// // api 값 가져오기
// var startDate = new Date(posibleStartDate);
// var endDate = new Date(posibleEndDate);
// console.log(startDate);
// console.log(endDate);
// var thisMonthFullDateList = [];
//
// for (var currentDate = new Date(startDate); currentDate <= endDate; currentDate.setDate(currentDate.getDate() + 1)) {
//     thisMonthFullDateList.push(new Date(currentDate));
// }
//
// // 배열 내용을 확인합니다.
// console.log("This Month Full Date List:", thisMonthFullDateList);

document.addEventListener('DOMContentLoaded', function() {
    // API에서 가져온 시작 날짜와 종료 날짜
    var startDate = new Date(posibleStartDate); // JSP에서 전달된 시작 날짜
    var endDate = new Date(posibleEndDate); // JSP에서 전달된 종료 날짜

    console.log(startDate);
    console.log(endDate);
    // 날짜 범위 내의 모든 날짜를 포함하는 배열을 초기화합니다.
    var thisMonthFullDateList = [];

    // 시작 날짜에서 종료 날짜까지의 모든 날짜를 배열에 추가합니다.
    for (var currentDate = new Date(startDate); currentDate <= endDate; currentDate.setDate(currentDate.getDate() + 1)) {
        thisMonthFullDateList.push(new Date(currentDate));
    }

    // 배열 내용을 확인합니다.
    console.log("This Month Full Date List:");
    thisMonthFullDateList.forEach(function(date) {
        console.log(date.toISOString().split('T')[0]); // 날짜를 'YYYY-MM-DD' 형식으로 출력
    });
});