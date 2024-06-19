$(function (){

    var nowParam = new URLSearchParams(window.location.search);
    var selctedCategory = nowParam.get("category")
    alert(selctedCategory);
    if(selctedCategory!=null || selctedCategory!=""){
        $(".category-area").show();
    }else{
        $(".detail-category-area").hide();
        $(".category-area").hide();
    }
    // $(".detail-category-area").hide();
    // $(".category-area").hide();

    $(".filter").click(function(){
        $(".category-area").toggle();
        $(".socializing-item").show();
    });




    // 페이지 로드 시 URL에서 카테고리 값을 가져와 체크박스에 반영
    var urlParams = new URLSearchParams(window.location.search);
    var savedCategory = urlParams.get('category');
    if (savedCategory) {
        $('input[type="checkbox"][name="category"]').each(function () {
            if ($(this).val() === savedCategory) {
                $(this).prop('checked', true);
            } else {
                $(this).prop('checked', false);
            }
        });
    }


    $('input[type="checkbox"][name="category"]').click(function(){
        $(".detail-category-area").show();
        if($(this).prop('checked')){
            $('input[type="checkbox"][name="category"]').prop('checked',false);

            $(this).prop('checked',true);
           category =  $(this).val();

            $(".detail-category-list").hide();
            $(".socializing-item").hide();

            if($(this).val() == "운동"){
                $(".category1-list").show();
                // 선택된 값 가져오기
                var selectedCategory = $(this).val();

                // 현재 페이지의 URL 가져오기
                var currentUrl = window.location.href;

                // 쿼리 파라미터로 category 추가하기
                var newUrl = new URL(currentUrl);
                newUrl.searchParams.set('category', selectedCategory);
                localStorage.setItem(category,selectedCategory);
                alert(category);

                //페이지 이동
                window.location.href = newUrl.toString();

            }else if($(this).val() == "공연"){
                $(".category2-list").show();
                // 선택된 값 가져오기
                var selectedCategory = $(this).val();

                // 현재 페이지의 URL 가져오기
                var currentUrl = window.location.href;

                // 쿼리 파라미터로 category 추가하기
                var newUrl = new URL(currentUrl);
                newUrl.searchParams.set('category', selectedCategory);

                // 페이지 이동
                window.location.href = newUrl.toString();
            }else if($(this).val() == "공부"){
                $(".category3-list").show();
                // 선택된 값 가져오기
                var selectedCategory = $(this).val();

                // 현재 페이지의 URL 가져오기
                var currentUrl = window.location.href;

                // 쿼리 파라미터로 category 추가하기
                var newUrl = new URL(currentUrl);
                newUrl.searchParams.set('category', selectedCategory);

                // 페이지 이동
                window.location.href = newUrl.toString();
            }

        }else{
            $(".detail-category-area").hide();
        }
    });



    $('input[type="checkbox"][name="detail-category"]').click(function(){
        if($(this).prop('checked')){
            $('input[type="checkbox"][name="detail-category"]').prop('checked',false);

            $(this).prop('checked',true);

            detail_category = $(this).val();
            alert(detail_category);
            $(".socializing-item").hide();

        }else {
            $('input[type="checkbox"][name="category"]').each(function () {
                if($(this).is(':checked')){
                    category = $(this).val();
                }
            });
        }
    });


    // 주소에서 토큰을 활용하여 구,군 만 출력

    $('.socializing-item').each(function() {
        var fullAddress = $(this).find('#address').text();
        var tokens = fullAddress.split(' ');
        var district = tokens[1];
        if (district) {
            $(this).find('#address').text(district);
        }
    });


    var address = localStorage.getItem('searchAddress');
    if (address) {
        document.getElementById('search').value = address;
    }

    // 입력 필드 값이 변경될 때마다 localStorage에 저장
    document.getElementById('search').addEventListener('input', function() {
        localStorage.setItem('searchAddress', this.value);
    });



})