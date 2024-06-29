function showContent(content) {
    // console.log('showContent called with:', content);  // Debugging line
    // 모든 main-content > div 요소의 active 클래스를 제거
    $('.main-content > div').removeClass('active');

    // 모든 menu-item 요소의 active 클래스를 제거
    $('.menu-item').removeClass('active');

    // tabs 콘텐츠 처리
    if (content === 'tabs') {
        $('.tabs').css('display', 'flex');
        showTab('club-list');  // 기본 클럽 목록 탭을 표시
        $('.menu-item[onclick="showContent(\'tabs\')"]').addClass('active');
    } else {
        // 다른 모든 콘텐츠를 숨기고 선택한 콘텐츠만 표시
        $('.tabs').css('display', 'none');
        const contentElement = $('.' + content);
        contentElement.addClass('active');
        // console.log('Active contentElement:', contentElement);  // Debugging line
        $('.menu-item[onclick="showContent(\'' + content + '\')"]').addClass('active');
    }

    // 디버깅을 위한 로그
    // console.log('Active menu-item:', $('.menu-item.active'));  // Debugging line
    // console.log('Active main-content div:', $('.main-content > div.active'));  // Debugging line
}

function showTab(tab) {
    // console.log('showTab called with:', tab);  // Debugging line
    // 모든 tab 요소의 active 클래스를 제거
    $('.tab').removeClass('active');

    // 클럽 목록 및 소셜라이징 목록의 active 클래스를 제거
    $('.club-list, .socializing-list').removeClass('active');

    // 선택한 탭과 콘텐츠에 active 클래스 추가
    $('.' + tab).addClass('active');
    $('.tab[onclick="showTab(\'' + tab + '\')"]').addClass('active');

    // 디버깅을 위한 로그
    // console.log('Active tab:', $('.tab.active'));  // Debugging line
    // console.log('Active club/socializing list:', $('.club-list.active, .socializing-list.active'));  // Debugging line
}

function sample4_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
            var roadAddr = data.roadAddress; // 도로명 주소 변수

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            $('#sample4_roadAddress').val(roadAddr);

            var guideTextBox = $('#guide');

            // guide 요소가 존재하는지 확인
            if (guideTextBox.length) {
                // 사용자가 '선택 안함'을 클릭한 경우, 예상 주소라는 표시를 해준다.
                if (data.autoRoadAddress) {
                    var expRoadAddr = data.autoRoadAddress;
                    guideTextBox.html('(예상 도로명 주소 : ' + expRoadAddr + ')').css('display', 'block');
                } else if (data.autoJibunAddress) {
                    var expJibunAddr = data.autoJibunAddress;
                    guideTextBox.html('(예상 지번 주소 : ' + expJibunAddr + ')').css('display', 'block');
                } else {
                    guideTextBox.html('').css('display', 'none');
                }
            }
        }
    }).open();
}

// DOM이 완전히 로드된 후 기본 콘텐츠를 표시
$(document).ready(function() {
    // console.log('DOM fully loaded and parsed');  // Debugging line
    showContent('tabs');  // 기본적으로 tabs를 표시

// 결제내역
$(document).ready(function() {
    $(window).on("load resize", function () {
        var scrollWidth = $('.tbl-content').width() - $('.tbl-content table').width();
        $('.tbl-header').css({'padding-right': scrollWidth});
    }).resize();

    // // 취소버튼 결제완료일때만 생김
    // $('td').each(function () {
    //     if ($(this).text().includes('결제완료')) {
    //         $(this).find('.cancel-button').css('display', 'inline-block');
    //     }
    // });

    // $('.cancel-button').onclick (function cancelPay() {
    //     jQuery.ajax({
    //         // 예: http://www.myservice.com/payments/cancel
    //         "url": "{환불정보를 수신할 가맹점 서비스 URL}",
    //         "type": "POST",
    //         "contentType": "application/json",
    //         "data": JSON.stringify({
    //             "merchant_uid": "{결제건의 주문번호}", // 예: ORD20180131-0000011
    //             "cancel_request_amount": 2000, // 환불금액
    //             "reason": "결제 취소", // 환불사유
    //         }),
    //         "dataType": "json"
    //     });
    // });
});