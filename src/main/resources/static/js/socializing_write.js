// const subCategories = {
//     "운동": ["축구", "야구", "농구"],
//     "공연": ["전시", "댄스", "영화"],
//     "공부": ["컴퓨터", "영어", "수학"]
// };
//
// $(document).ready(function() {
//     $('#category').on('change', function() {
//         const category = $(this).val();
//         const subCategory = $('#subCategory');
//         subCategory.empty();
//         subCategory.append('<option value="">소분류 선택</option>'); // Reset subcategories
//         if (category && subCategories[category]) {
//             $.each(subCategories[category], function(index, value) {
//                 subCategory.append('<option value="' + value + '">' + value + '</option>');
//             });
//         }
//     });
//
//     $('#fileSelect').on('click', function() {
//         $('#fileInput').click();
//     });
//
//     $('#fileInput').on('change', function() {
//         const file = this.files[0];
//         if (file) {
//             const reader = new FileReader();
//             reader.onload = function(e) {
//                 $('#previewImage').attr('src', e.target.result).show();
//                 $('#img_txt').hide();
//             };
//             reader.readAsDataURL(file);
//         } else {
//             $('#previewImage').hide();
//         }
//     });
//
//     // $('#socializingForm').on('submit', function(e) {
//     //     e.preventDefault();
//     //     const formData = new FormData(this);
//     //     $.ajax({
//     //         type: 'POST',
//     //         url: '/socializing/create',
//     //         data: formData,
//     //         processData: false,
//     //         contentType: false,
//     //         success: function(response) {
//     //             alert('소셜라이징이 성공적으로 생성되었습니다.');
//     //             // 성공 시 처리
//     //         },
//     //         error: function(error) {
//     //             alert('오류가 발생했습니다.');
//     //             console.error('Error:', error);
//     //         }
//     //     });
//     // });
// });


$(document).ready(function () {
    const subCategories = {
        "운동": ["축구", "야구", "농구"],
        "공연": ["전시", "댄스", "영화"],
        "공부": ["컴퓨터", "영어", "수학"]
    };

    $('#category').on('change', function () {
        const category = $(this).val();
        const subCategory = $('#subCategory');
        subCategory.empty();
        subCategory.append('<option value="">소분류 선택</option>'); // Reset subcategories
        if (category && subCategories[category]) {
            $.each(subCategories[category], function (index, value) {
                subCategory.append('<option value="' + value + '">' + value + '</option>');
            });
        }
    });

    $('#fileSelect').on('click', function () {
        $('#fileInput').click();
    });

    $('#fileInput').on('change', function () {
        const file = this.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function (e) {
                $('#previewImage').attr('src', e.target.result).show();
                $('#img_txt').hide();
            };
            reader.readAsDataURL(file);
        } else {
            $('#previewImage').hide();
        }
    });

    $('#mapSearch').click(function () {
        searchPlaces();
    })


    // 지도와 장소 검색을 위한 변수와 객체 초기화
    var markers = [];
    var mapContainer = document.getElementById('map');
    var mapOption = {
        center: new kakao.maps.LatLng(37.5000065, 127.0356027),
        level: 8
    };
    var map = new kakao.maps.Map(mapContainer, mapOption);
    var ps = new kakao.maps.services.Places();
    var infowindow = new kakao.maps.InfoWindow({zIndex: 1});

    function searchPlaces() {
        var keyword = $('#location').val();


        if (!keyword.replace(/^\s+|\s+$/g, '')) {
            alert('키워드를 입력해주세요!');
            return false;
        }

        ps.keywordSearch(keyword, placesSearchCB);

    }

    function placesSearchCB(data, status, pagination) {
        if (status === kakao.maps.services.Status.OK) {
            displayPlaces(data);
            displayPagination(pagination);
            $('.map_wrap').show();
            $('#menu_wrap').show();
            setTimeout(function () {
                map.relayout();
                map.setBounds(bounds);
            }, 10)

        } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
            alert('검색 결과가 존재하지 않습니다.');
        } else if (status === kakao.maps.services.Status.ERROR) {
            alert('검색 결과 중 오류가 발생했습니다.');
        }
    }

    function displayPlaces(places) {
        var listEl = $('#placesList'),
            menuEl = $('#menu_wrap'),
            fragment = $(document.createDocumentFragment()),
            bounds = new kakao.maps.LatLngBounds();

        listEl.empty();
        removeMarker();

        $.each(places, function (index, place) {
            var placePosition = new kakao.maps.LatLng(place.y, place.x),
                marker = addMarker(placePosition, index),
                itemEl = getListItem(index, place);

            bounds.extend(placePosition);
            (function (marker, title) {
                kakao.maps.event.addListener(marker, 'mouseover', function () {
                    displayInfowindow(marker, title);
                });
                kakao.maps.event.addListener(marker, 'mouseout', function () {
                    infowindow.close();
                });
                itemEl.on('mouseover', function () {
                    displayInfowindow(marker, title);
                });
                itemEl.on('mouseout', function () {
                    infowindow.close();
                });
                itemEl.on('click', function () {
                    map.setCenter(marker.getPosition());
                    map.relayout();
                    infowindow.setContent('<div style="padding:5px;z-index:1;">' + title + '</div>');
                    infowindow.open(map, marker);
                    $('.map_wrap').get(0).scrollIntoView({behavior: 'smooth'});
                });
            })(marker, place.place_name);

            fragment.append(itemEl);
        });

        listEl.append(fragment);
        menuEl.scrollTop(0);
        map.setBounds(bounds);
    }

    function getListItem(index, place) {
        var el = $('<li>'),
            itemStr = '<span class="markerbg marker_' + (index + 1) + '"></span>' +
                '<div class="info">' +
                '   <h5>' + place.place_name + '</h5>';

        if (place.road_address_name) {
            itemStr += '    <span>' + place.road_address_name + '</span>' +
                '   <span class="jibun gray">' + place.address_name + '</span>';
        } else {
            itemStr += '    <span>' + place.address_name + '</span>';
        }

        itemStr += '  <span class="tel">' + place.phone + '</span>' +
            '</div>';

        el.html(itemStr);
        el.addClass('item');

        return el;
    }

    function addMarker(position, idx) {
        var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png',
            imageSize = new kakao.maps.Size(36, 37),
            imgOptions = {
                spriteSize: new kakao.maps.Size(36, 691),
                spriteOrigin: new kakao.maps.Point(0, (idx * 46) + 10),
                offset: new kakao.maps.Point(13, 37)
            },
            markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
            marker = new kakao.maps.Marker({
                position: position,
                image: markerImage
            });

        marker.setMap(map);
        markers.push(marker);

        return marker;
    }

    function removeMarker() {
        $.each(markers, function (index, marker) {
            marker.setMap(null);
        });
        markers = [];
    }

    function displayPagination(pagination) {
        var paginationEl = $('#pagination'),
            fragment = $(document.createDocumentFragment());

        paginationEl.empty();

        for (var i = 1; i <= pagination.last; i++) {
            var el = $('<a>').attr('href', '#').text(i);

            if (i === pagination.current) {
                el.addClass('on');
            } else {
                el.on('click', (function (i) {
                    return function () {
                        pagination.gotoPage(i);
                    }
                })(i));
            }

            fragment.append(el);
        }
        paginationEl.append(fragment);
    }

    function displayInfowindow(marker, title) {
        var content = '<div style="padding:5px;z-index:1;">' + title + '</div>';
        infowindow.setContent(content);
        infowindow.open(map, marker);
    }
});