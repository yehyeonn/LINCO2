$(document).ready(function(){
    $('.calendar').datepicker({
        format: 'yyyy-mm-dd',   // 데이터 포맷 형식
        startDate: '-10d',  // 달력에서 선택 할 수 있는 가장 빠른 날짜 (이전은 선택 불가능)
        endDate: '+10d',  // 달력에서 선택할 수 있는 가장 느린 날짜. (이후는 선택 불가능)
        autoclose: false,    // 사용자가 날짜를 클릭하면 자동으로 캘린더가 닫히는 옵션
        calendarWeeks: false,   // 캘린더 옆에 몇 주차인지 보여주는 옵션
        clearBtn: false,    // 날짜 선택한 값을 초기화 해주는 버튼 보여주는 옵션
        datesDisabled: ['2024-08-15', '2024-08-16'], // 선택 불가능한 날짜 설정
        daysOfWeekDisabled: [0], // 선택 불가능한 요일 설정, 0 : 일요일
        daysOfWeekHighlighted: [6],  // 강조해야 하는 요일 설정
        disableTouchKeyboard: false, // 모바일에서 플러그인 작동 여부, false: 작동(기본값), true: 작동안함
        immediateUpdates: false,    // 사용자가 보는 화면으로 바로 날짜를 변경할지 여부
        multidate: false, // 여러 날짜 선택할 수 있게 하는 옵션
        multidateSeparator: ',',    // 여러 날짜를 선택했을 때 날짜 사이 구분
        templates: {
            leftArrow: '&laquo;',
            rightArrow: '&raquo;',
        },  // 다음달 이전달로 넘어가는 화살표 모양 커스텀
        showWeekDays: true, // 위에 요일 보여주는 옵션 값
        title: '예약 날짜 선택',
        todayHighlight: true,   // 오늘 날짜 하이라이트 기능
        toggleActive: true, // 이미 선택된 날짜를 선택하면 false(기본값)인 경우 그대로 유지, true인 경우 날짜 삭제
        weekStart: 0,   // 달력 시작 요일 선택, 기본값 0 (일요일)
        language: 'ko',  // 달력 언어 선택
    }).on('changeDate', function (e) {
        console.log('선택된 날짜:', e.format('yyyy-mm-dd'));

        // 선택된 날짜를 특정 요소에 표시 (여기서는 #selected-date에 표시)
        $('#selected-date').text('선택된 날짜: ' + e.format('yyyy-mm-dd'));

        // 선택 불가능한 날짜 선택 시 alert
        const disabledDates = ['2024-08-15', '2024-08-16'];
        if (disabledDates.includes(e.format('yyyy-mm-dd'))) {
            alert('해당 날짜는 선택할 수 없습니다.');
        }
    });
});