$(document).ready(function () {

    console.log("예약페이지로 넘어왔지롱~")
    let param = new URLSearchParams(window.location.search);
    let selectedDate = param.get('selectedDate');
    let venueId = param.get('venue_id');

    console.log(selectedDate);
    console.log(venueId);

    $('#selectedDateDisplay').text(selectedDate);
    $('#venueIdDisplay').text(venueId);

    if (selectedDate) {
        let dateParts = selectedDate.split('-');
        let year = parseInt(dateParts[0]);
        let month = parseInt(dateParts[1]);
        let day = parseInt(dateParts[2]);

        // 달력에 선택된 날짜 적용
        buildCalendar(year, month, day);
    }
})




