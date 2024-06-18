function showContent(content) {
    // console.log('showContent called with:', content);  // Debugging line
    // 모든 main-content > div 요소의 active 클래스를 제거
    document.querySelectorAll('.main-content > div').forEach(function(element) {
        element.classList.remove('active');
    });

    // 모든 menu-item 요소의 active 클래스를 제거
    document.querySelectorAll('.menu-item').forEach(function(element) {
        element.classList.remove('active');
    });

    // tabs 콘텐츠 처리
    if (content === 'tabs') {
        document.querySelector('.tabs').style.display = 'flex';
        showTab('club-list');  // 기본 클럽 목록 탭을 표시
        document.querySelector('.menu-item[onclick="showContent(\'tabs\')"]').classList.add('active');
    } else {
        // 다른 모든 콘텐츠를 숨기고 선택한 콘텐츠만 표시
        document.querySelector('.tabs').style.display = 'none';
        const contentElement = document.querySelector('.' + content);
        contentElement.classList.add('active');
        // console.log('Active contentElement:', contentElement);  // Debugging line
        document.querySelector('.menu-item[onclick="showContent(\'' + content + '\')"]').classList.add('active');
    }

    // 디버깅을 위한 로그
    // console.log('Active menu-item:', document.querySelector('.menu-item.active'));  // Debugging line
    // console.log('Active main-content div:', document.querySelector('.main-content > div.active'));  // Debugging line
}

function showTab(tab) {
    // console.log('showTab called with:', tab);  // Debugging line
    // 모든 tab 요소의 active 클래스를 제거
    document.querySelectorAll('.tab').forEach(function(element) {
        element.classList.remove('active');
    });

    // 클럽 목록 및 소셜라이징 목록의 active 클래스를 제거
    document.querySelectorAll('.club-list, .socializing-list').forEach(function(element) {
        element.classList.remove('active');
    });

    // 선택한 탭과 콘텐츠에 active 클래스 추가
    document.querySelector('.' + tab).classList.add('active');
    document.querySelector('.tab[onclick="showTab(\'' + tab + '\')"]').classList.add('active');

    // 디버깅을 위한 로그
    // console.log('Active tab:', document.querySelector('.tab.active'));  // Debugging line
    // console.log('Active club/socializing list:', document.querySelector('.club-list.active, .socializing-list.active'));  // Debugging line
}

// DOM이 완전히 로드된 후 기본 콘텐츠를 표시
document.addEventListener("DOMContentLoaded", function() {
    // console.log('DOM fully loaded and parsed');  // Debugging line
    showContent('tabs');  // 기본적으로 tabs를 표시
});


// 결제내역
$(document).ready(function() {
    $(window).on("load resize", function() {
        var scrollWidth = $('.tbl-content').width() - $('.tbl-content table').width();
        $('.tbl-header').css({'padding-right': scrollWidth});
    }).resize();

    // 취소버튼 결제완료일때만 생김
    $('td').each(function() {
        if ($(this).text().includes('결제완료')) {
            $(this).find('.cancelbtn').css('display', 'inline-block');
        }
    });
});