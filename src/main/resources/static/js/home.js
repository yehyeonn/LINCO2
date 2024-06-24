$(document).ready(function() {
    // 기존의 스크롤 핸들러
    const sections = $('.category');
    let currentSectionIndex = 0;
    let isScrolling = false;

    const showSection = (index) => {
        sections.each((i, section) => {
            if (i === index) {
                $(section).css('display', 'flex');
            } else {
                $(section).css('display', 'none');
            }
        });
    };

    const scrollHandler = (event) => {
        if (isScrolling) return;

        if (event.originalEvent.deltaY > 0 && currentSectionIndex < sections.length - 1) {
            currentSectionIndex++;
        } else if (event.originalEvent.deltaY < 0 && currentSectionIndex > 0) {
            currentSectionIndex--;
        }

        showSection(currentSectionIndex);
        isScrolling = true;

        setTimeout(() => {
            isScrolling = false;
        }, 500); // 스크롤 이벤트 사이의 딜레이를 0.5초로 설정
    };

    $(window).on('wheel', scrollHandler);
    showSection(currentSectionIndex);  // 처음에 첫 번째 섹션을 표시

    // 데이터 가져오기
    fetchEvents();

});

function fetchEvents() {
    const apiUrl = 'http://openapi.seoul.go.kr:8088/476753474c73777338374b73494b4b/json/culturalEventInfo/1/1000';

    $.getJSON(apiUrl, function(data) {
        if (data.culturalEventInfo && data.culturalEventInfo.row) {
            const events = data.culturalEventInfo.row;
            const today = new Date();
            const filteredEvents = events.filter(event => {
                const endDate = new Date(event.DATE.split('~')[1].trim()); // 종료 날짜
                return endDate >= today;
            }).sort((a, b) => {
                const startDateA = new Date(a.DATE.split('~')[0].trim()); // 시작 날짜
                const startDateB = new Date(b.DATE.split('~')[0].trim());
                return startDateA - startDateB;
            });
            renderEvents(filteredEvents);
        } else {
            console.error('No data found');
        }
    }).fail(function() {
        console.error('Error fetching events');
    });
}


function shuffleArray(array) {
    for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
    }
    return array;
}

function renderEvents(events) {
    const columnsContainer = $('#event-columns');
    columnsContainer.empty(); // 기존 내용을 초기화합니다.

    const shuffledEvents = shuffleArray(events).slice(0, 8); // 랜덤으로 섞은 후 8개의 이벤트만 선택

    // 각 열을 생성하여 두 개의 열에 이벤트를 분배합니다.
    const leftColumn = $('<div>').addClass('column');
    const rightColumn = $('<div>').addClass('column');

    shuffledEvents.forEach((event, index) => {
        const anchor = $('<a>').attr('href', event.ORG_LINK);

        const imgBox = $('<div>').addClass('img-box');

        const img = $('<img>')
            .attr('src', event.MAIN_IMG || 'https://via.placeholder.com/60')
            .on('error', function () {
                $(this).attr('src', '@{/img/no_img.jpg}');
            });

        const imgTitle = $('<div>').addClass('img-title').text(event.TITLE);

        imgBox.append(img);
        imgBox.append(imgTitle);
        anchor.append(imgBox);

        // 첫 4개는 왼쪽 열에, 나머지 4개는 오른쪽 열에 추가합니다.
        if (index < 4) {
            leftColumn.append(anchor);
        } else {
            rightColumn.append(anchor);
        }
    });

    columnsContainer.append(leftColumn);
    columnsContainer.append(rightColumn);
}

