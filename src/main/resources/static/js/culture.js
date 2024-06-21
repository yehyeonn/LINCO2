$(document).ready(function() {
    const cardContainer = $('.card-container');
    const apiUrl = 'http://openapi.seoul.go.kr:8088/476753474c73777338374b73494b4b/json/culturalEventInfo/1/1000';
    const cardsPerSlide = 5; // 한 번에 보여줄 카드 개수
    let allEvents = [];
    let currentEvents = [];
    let currentIndex = 0;
    let isCategorySelected = false;

    function fetchAllEvents() {
        $.ajax({
            url: apiUrl,
            method: 'GET',
            success: function(data) {
                console.log('API response data:', data);

                if (data.culturalEventInfo && data.culturalEventInfo.row) {
                    const today = new Date();
                    allEvents = data.culturalEventInfo.row
                        .filter(event => new Date(event.DATE.split('~')[1]) >= today)
                        .sort((a, b) => new Date(a.DATE.split('~')[0]) - new Date(b.DATE.split('~')[0]));
                    console.log('All events:', allEvents);

                    // 페이지 로드 시 랜덤한 5개 이벤트 표시
                    displayRandomEvents();
                } else {
                    console.error('No events found in API response.');
                }
            },
            error: function(error) {
                console.error('Error fetching data:', error);
            }
        });
    }

    function getRandomEvents(events, count) {
        const shuffled = [...events].sort(() => 0.5 - Math.random());
        return shuffled.slice(0, count);
    }

    function displayEvents(startIndex, events) {
        const eventsToShow = events.slice(startIndex, startIndex + cardsPerSlide);

        if (eventsToShow.length === 0) {
            cardContainer.html('<p>선택하신 카테고리에 해당하는 행사가 없습니다.</p>');
            return;
        }

        const newCards = eventsToShow.map(event => {
            return `
                <div class="card-article">
                    <img src="${event.MAIN_IMG}" alt="culture-image" class="card-img" style="width: 350px; height: 250px;">
                    <div class="card-data">
                        <ul class="card-description">
                            <li>제목: <span>${event.TITLE}</span></li>
                            <li>시간: <span>${event.DATE}</span></li>
                            <li>장소: <span>${event.PLACE}</span></li>
                            <li>대상: <span>${event.USE_TRGT}</span></li>
                            <li>요금: <span>${event.USE_FEE}</span></li>
                        </ul>
                        <div class="card-button-container">
                            <a href="${event.HMPG_ADDR}" class="custom-btn btn-1 card-button"><span>Let's GO!</span><span>상세</span></a>
                            <a href="${event.ORG_LINK}" class="custom-btn btn-4 card-button"><span>Let's GO!</span><span>예약</span></a>
                        </div>
                    </div>
                </div>`;
        });

        cardContainer.fadeOut(200, function() {
            cardContainer.empty().append(newCards).fadeIn(200);
        });
    }

    function displayRandomEvents() {
        currentEvents = getRandomEvents(allEvents, cardsPerSlide);
        currentIndex = 0;
        displayEvents(currentIndex, currentEvents);
    }

    // 페이지 로딩 시 전체 데이터를 한 번 받아옵니다.
    fetchAllEvents();

    // CODENAME 버튼 클릭 시 데이터 필터링
    $('.code-btn').click(function() {
        const codenamesData = $(this).data('codename');
        if (codenamesData) {
            const codenames = codenamesData.split(',').map(name => name.trim());
            currentEvents = allEvents.filter(event => codenames.includes(event.CODENAME));
        } else {
            currentEvents = [];
        }
        currentIndex = 0;
        isCategorySelected = true;
        displayEvents(currentIndex, currentEvents);
    });

    // NEXT 버튼 클릭 시 다음 데이터 로드
    $('.slide-next').click(function() {
        if (isCategorySelected) {
            currentIndex += cardsPerSlide;
            if (currentIndex >= currentEvents.length) {
                currentIndex = 0; // 처음으로 돌아갑니다.
            }
        } else {
            currentIndex += cardsPerSlide;
            if (currentIndex >= allEvents.length) {
                currentIndex = 0; // 처음으로 돌아갑니다.
            }
        }
        displayEvents(currentIndex, isCategorySelected ? currentEvents : allEvents);
    });

    // PREV 버튼 클릭 시 이전 데이터 로드
    $('.slide-prev').click(function() {
        if (isCategorySelected) {
            currentIndex -= cardsPerSlide;
            if (currentIndex < 0) {
                currentIndex = currentEvents.length - (currentEvents.length % cardsPerSlide); // 마지막 페이지로 갑니다.
            }
        } else {
            currentIndex -= cardsPerSlide;
            if (currentIndex < 0) {
                currentIndex = allEvents.length - (allEvents.length % cardsPerSlide); // 마지막 페이지로 갑니다.
            }
        }
        displayEvents(currentIndex, isCategorySelected ? currentEvents : allEvents);
    });
});
