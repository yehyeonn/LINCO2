$(document).ready(function () {
    const detailCategoryMap = {
        "운동": ["축구", "야구", "농구"],
        "공연": ["전시", "댄스", "영화"],
        "공부": ["컴퓨터", "영어", "수학"]
    };

    // 페이지 로드 시 선택된 카테고리의 소분류를 표시
    const selectedCategory = [[${updatesocializing.category}]];
    const detailCategory = $('#detail_category');
    detailCategory.empty();
    detailCategory.append(new Option([[${updatesocializing.detail_category}]], [[${updatesocializing.detail_category}]]));
    if (selectedCategory && detailCategoryMap[selectedCategory]) {
        detailCategoryMap[selectedCategory].forEach(function (item) {
            detailCategory.append(new Option(item, item));
        });
    };


    const mapContainer = document.getElementById('map');
    const mapOption = {
        center: new kakao.maps.LatLng(37.566826, 126.9786567),
        level: 3
    };
    const map = new kakao.maps.Map(mapContainer, mapOption);

    const geocoder = new kakao.maps.services.Geocoder();
    const address = $('#address').val();

    geocoder.addressSearch(address, function(result, status) {
        if (status === kakao.maps.services.Status.OK) {
            const coords = new kakao.maps.LatLng(result[0].y, result[0].x);
            const marker = new kakao.maps.Marker({
                map: map,
                position: coords
            });
            map.setCenter(coords);
        }
    });
});