$(document).ready(function () {
    const detailCategoryMap = {
        "운동": ["축구", "야구", "농구"],
        "공연": ["전시", "댄스", "영화"],
        "공부": ["컴퓨터", "영어", "수학"]
    };

    // 페이지 로드 시 선택된 카테고리의 소분류를 표시
    const detailCategory = $('#detailCategory').val();

    console.log(detailCategory)

    console.log("여기까지는 오지롱~")
    const address = $('#address').val();
    const placeName = $('#placeName').val();
    console.log(placeName)

    var geocoder = new kakao.maps.services.Geocoder();

// 주소로 좌표 검색
    geocoder.addressSearch(address, function(result, status) {

        // 정상적으로 검색이 완료됐으면
        if (status === kakao.maps.services.Status.OK) {
            console.log("검색했지롱~!")
            var coords = new kakao.maps.LatLng(result[0].y, result[0].x);

            // 지도 생성 및 해당 위치로 중심 설정
            var mapContainer = document.getElementById('map'),
                mapOption = {
                    center: coords, // 주소로 얻은 좌표로 중심 설정
                    level: 3

                };

            var map = new kakao.maps.Map(mapContainer, mapOption);

            // 결과값으로 받은 위치를 마커로 표시합니다
            var marker = new kakao.maps.Marker({
                map: map,
                position: coords
            });

            //인포윈도우로 장소에 대한 설명을 표시합니다
            var infowindow = new kakao.maps.InfoWindow({
                content:  '<div style="width:150px;text-align:center;padding:6px 0;">' + placeName + '</div>'
            });
            infowindow.open(map, marker);

            // 마커 중심으로 설정
            map.setCenter(coords);
            map.relayout();

            displayInfowindow(marker);
            map.panTo(placePosition);
        }
    });
});