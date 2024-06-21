$(function (){
    var fullAddress = $("#address").text();
    var tokens = fullAddress.split(' ');
    var district = tokens[0] + " " + tokens[1];
    if (district) {
        $('#address').text(district);
    }

    var geocoder = new kakao.maps.services.Geocoder();

    // 주소로 좌표 검색하기
    geocoder.addressSearch(fullAddress, function(result, status) {

        // 결과가 있으면
        if (status === kakao.maps.services.Status.OK) {

            var coords = new kakao.maps.LatLng(result[0].y, result[0].x);


            // 지도 생성 및 해당 위치로 중심 설정
            var mapContainer = document.getElementById('map'),
                mapOption = {
                    center: coords, // 주소로 얻은 좌표로 중심 설정
                    level: 3

                };

            var map = new kakao.maps.Map(mapContainer, mapOption);

            // 해당 위치로 마커 표시
            var marker = new kakao.maps.Marker({
                map: map,
                position: coords
            });

            // 마커 중심으로 설정
            map.setCenter(coords);
            map.relayout();
        }
    });

});
