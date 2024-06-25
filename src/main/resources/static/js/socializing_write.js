const subCategories = {
    "운동": ["축구", "야구", "농구"],
    "공연": ["전시", "댄스", "영화"],
    "공부": ["컴퓨터", "영어", "수학"]
};


// 시작날짜(min 속성)
let sDate = new Date();


sDate.setDate(sDate.getDate());

let minStr = sDate.toISOString().split('T')[0]; // 날짜로 변환

$("#meeting_date").prop("min", minStr);

console.log('시작날짜 : ', minStr);


$(document).ready(function () {
    const detail_category = $('#detail_category');
    if ($("#category").val() == "") {
        $("#a").text("소분류 선택");
    } else {
        $("#a").text(localStorage.getItem("detail"));
    }

    $('#category').on('change', function () {
        const category = $(this).val();

        detail_category.empty();
        detail_category.append('<option value="">소분류 선택</option>');
        if (category && subCategories[category]) {
            $.each(subCategories[category], function (index, value) {
                detail_category.append('<option th:value="' + value + '">' + value + '</option>');

                // detail_category.append('<option th:value="' + value + '"  th:text="' + value + '" th:selected="' + value + ' == ${detail_category1}" th:if="${detail_category1}"></option>');
                // detail_category.append('<option th:value="' + value + '"  th:text="' + value + '" th:unless="${detail_category1}"></option>');

            });
            // alert(localStorage.getItem("detail"))
        }

    });
    $('#detail_category').on('change', function () {
        localStorage.setItem("detail", detail_category.val());
    });

    // 파일 선택 버튼 클릭 시 파일 입력 필드 클릭
    $('#fileSelect').on('click', function () {
        $('#fileInput').click();
    });

    // 파일 선택 시 미리보기 표시
    $('#fileInput').on('change', function () {
        const file = this.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function (e) {
                var previewImage = document.getElementById("previewImage");
                previewImage.src = e.target.result;
                previewImage.style.display = "block";
                var imgtxt = document.getElementById('img_txt');
                imgtxt.style.display = "none";
            }
            reader.readAsDataURL(file);
        }
    });



    // 엔터키로 form 제출 막기, 하지만 #location 필드에서는 searchPlaces() 실행
    $(document).on('keydown', function (event) {
        if (event.keyCode === 13) {
            if ($(event.target).attr('id') === 'location') {
                event.preventDefault();
                searchPlaces();
            }    else if ($(event.target).is('textarea')) {
                return; // 아무것도 하지 않음
            } else {
                event.preventDefault();
            }
        }
    });


});


// 지도와 장소 검색을 위한 변수와 객체 초기화
var markers = [];
var mapContainer = document.getElementById('map');
var mapOption = {
    center: new kakao.maps.LatLng(37.566826, 126.9786567),
    level: 2
};
var map = new kakao.maps.Map(mapContainer, mapOption);
var ps = new kakao.maps.services.Places();
var infowindow = new kakao.maps.InfoWindow({zIndex: 1});


$('#mapSearch').click(function () {
    searchPlaces();
});


function searchPlaces() {
    var keyword = $('#location').val();

    if (!keyword.replace(/^\s+|\s+$/g, '')) {
        return false;
    }

    ps.keywordSearch(keyword, placesSearchCB);

}

function placesSearchCB(data, status, pagination) {
    if (status === kakao.maps.services.Status.OK) {
        displayPlaces(data);
        displayPagination(pagination);
        $('#menu_wrap').show();
        // $('.map_wrap').show();
        map.relayout();
        const bounds = new window.kakao.maps.LatLngBounds();
        for (let i = 0; i < data.length; i++) {
            displayMarker(data[i]);
            bounds.extend(new window.kakao.maps.LatLng(data[i].y, data[i].x));
        }

        map.setBounds(bounds);

    } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
        alert('검색 결과가 존재하지 않습니다.');
    } else if (status === kakao.maps.services.Status.ERROR) {
        alert('검색 결과 중 오류가 발생했습니다.');
    }
}

// 검색 결과 목록과 마커표시
function displayMarker(place) {
    const marker = new window.kakao.maps.Marker({
        map,
        position: new window.kakao.maps.LatLng(place.y, place.x),
    });
    window.kakao.maps.event.addListener(marker, "click", function (mouseEvent) {
            props.setAddress(place);
            infowindow.setContent(`
              <span>
              ${place.place_name}
              </span>
              `);
            infowindow.open(map, marker);
            const moveLatLon = new window.kakao.maps.LatLng(place.y, place.x);
            map.panTo(moveLatLon);
        }
    );
}


function displayPlaces(places) {
    const listEl = document.getElementById("placesList");
    const menuEl = document.getElementById("menu_wrap");
    const fragment = document.createDocumentFragment();
    // const bounds = new window.kakao.maps.LatLngBounds();
    removeAllChildNods(listEl);
    removeMarker();
    for (let i = 0; i < places.length; i++) {
        const placePosition = new window.kakao.maps.LatLng(
            places[i].y,
            places[i].x
        );
        const marker = addMarker(placePosition, i);
        const itemEl = getListItem(i, places[i]);
        // bounds.extend(placePosition);
        (function (marker, title) {
            window.kakao.maps.event.addListener(
                marker,
                "mouseover",
                function () {
                    displayInfowindow(marker, title);
                }
            );

            window.kakao.maps.event.addListener(
                marker,
                "mouseout",
                function () {
                    infowindow.close();
                }
            );

            // address 값에 주소 넣어줬어용~
            itemEl.addEventListener("click", function (e) {
                displayInfowindow(marker, title);
                map.panTo(placePosition);
                // 주소
                document.getElementById('address').value = places[i].road_address_name || places[i].address_name;
                console.log(document.getElementById('address').value);

                // 타이틀
                document.getElementById('placeName').value = places[i].place_name;
                console.log(document.getElementById('placeName').value);

            });
        })(marker, places[i].place_name);

        fragment.appendChild(itemEl);
    }

    listEl?.appendChild(fragment);
    menuEl.scrollTop = 0;

    // map.panTo(bounds);
}

// 검색결과 항목을 Element로 반환하는 함수입니다
function getListItem(index, places) {

    var el = document.createElement('li'),
        itemStr = '<span class="markerbg marker_' + (index + 1) + '"></span>' +
            '<div class="info">' +
            '   <h5>' + places.place_name + '</h5>';

    if (places.road_address_name) {
        itemStr += '    <span>' + places.road_address_name + '</span>' +
            '   <span class="jibun gray">' + places.address_name + '</span>';
    } else {
        itemStr += '    <span>' + places.address_name + '</span>';
    }

    itemStr += '  <span class="tel">' + places.phone + '</span>' +
        '</div>';

    el.innerHTML = itemStr;
    el.className = 'item';

    return el;
}

// 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
function addMarker(position, idx, title) {
    var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png', // 마커 이미지 url, 스프라이트 이미지를 씁니다
        imageSize = new kakao.maps.Size(36, 37),  // 마커 이미지의 크기
        imgOptions = {
            spriteSize: new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
            spriteOrigin: new kakao.maps.Point(0, (idx * 46) + 10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
            offset: new kakao.maps.Point(13, 37) // 마커 좌표에 일치시킬 이미지 내에서의 좌표
        },
        markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
        marker = new kakao.maps.Marker({
            position: position, // 마커의 위치
            image: markerImage
        });

    marker.setMap(map); // 지도 위에 마커를 표출합니다
    markers.push(marker);  // 배열에 생성된 마커를 추가합니다
    return marker;
}

// 지도 위에 표시되고 있는 마커를 모두 제거합니다
function removeMarker() {
    for (var i = 0; i < markers.length; i++) {
        markers[i].setMap(null);
    }
    markers = [];
}

// 검색결과 목록 하단에 페이지번호를 표시는 함수입니다
function displayPagination(pagination) {
    var paginationEl = document.getElementById('pagination'),
        fragment = document.createDocumentFragment(),
        i;

    // 기존에 추가된 페이지번호를 삭제합니다
    while (paginationEl.hasChildNodes()) {
        paginationEl.removeChild(paginationEl.lastChild);
    }

    for (i = 1; i <= pagination.last; i++) {
        var el = document.createElement('a');
        el.href = "#";
        el.innerHTML = i;

        if (i === pagination.current) {
            el.className = 'on';
        } else {
            el.onclick = (function (i) {
                return function () {
                    pagination.gotoPage(i);
                }
            })(i);
        }

        fragment.appendChild(el);
    }
    paginationEl.appendChild(fragment);
}

// 검색결과 목록 또는 마커를 클릭했을 때 호출되는 함수입니다
// 인포윈도우에 장소명을 표시합니다
function displayInfowindow(marker, title) {
    var content = '<div style="padding:5px;z-index:1;">' + title + '</div>';

    infowindow.setContent(content);
    infowindow.open(map, marker);
}

// 검색결과 목록의 자식 Element를 제거하는 함수입니다
function removeAllChildNods(el) {
    while (el.hasChildNodes()) {
        el.removeChild(el.lastChild);
    }
}



