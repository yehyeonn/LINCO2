$(function (){
    $(".detail-category-area").hide();
    $(".category-area").hide();
    const currentNewUrl = new URL(window.location.href);
    const perfEntries = performance.getEntriesByType("navigation");
    if (perfEntries[0].type === "reload") {
        currentNewUrl.searchParams.set("address","");
        currentNewUrl.searchParams.set("category","");
        currentNewUrl.searchParams.set("detailcategory","");
        window.location.href = currentNewUrl.toString();
    }



    $(".filter").click(function(){
        $('input[type="checkbox"][name="detail-category"]').prop('checked',false);
        $('input[type="checkbox"][name="category"]').prop('checked',false);
        $(".category-area").toggle();
        $(".socializing-item").show();
        $(".detail-category-area").hide();
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
        $(".category-area").show();
        if(savedCategory == "운동"){
            $(".detail-category-area").show();
            $('.category2-list').hide();
            $('.category3-list').hide();
        }else if (savedCategory == "공연") {
            $(".detail-category-area").show();
            $('.category1-list').hide();
            $('.category3-list').hide();
        }else if (savedCategory == "공부") {
            $(".detail-category-area").show();
            $('.category1-list').hide();
            $('.category2-list').hide();
        }
    }

    var urldetailParams = new URLSearchParams(window.location.search);
    var saveddetailCategory = urldetailParams.get('detailcategory');
    if (savedCategory) {
        $('input[type="checkbox"][name="detail-category"]').each(function () {
            if ($(this).val() === saveddetailCategory) {
                $(this).prop('checked', true);
            } else {
                $(this).prop('checked', false);
            }
        });
    }

    $('input[type="checkbox"][name="category"]').click(function(){
        $('input[type="checkbox"][name="detail-category"]').prop('checked',false);
        $(".detail-category-area").show();
        if($(this).prop('checked')){
            $('input[type="checkbox"][name="category"]').prop('checked',false);


            $(this).prop('checked',true);


            $(".detail-category-list").hide();
            $(".socializing-item").hide();

            if($(this).val() == "운동"){
                select(this);
            }else if($(this).val() == "공연"){
                select(this);
            }else if($(this).val() == "공부"){
                select(this);
            }

        }else{
            $(".detail-category-area").hide();

            $('input[type="checkbox"][name="detail-category"]').prop('checked',false);
            select("");
        }
    });



    $('input[type="checkbox"][name="detail-category"]').click(function(){
        if($(this).prop('checked')){
            $('input[type="checkbox"][name="detail-category"]').prop('checked',false);

            $(this).prop('checked',true);

            if($(this).val() == "축구"){
                detailselect(this);
            }else if($(this).val() == "야구"){
                detailselect(this);
            }else if($(this).val() == "농구"){
                detailselect(this);
            }else if($(this).val() == "전시"){
                detailselect(this);
            }else if($(this).val() == "댄스"){
                detailselect(this);
            }else if($(this).val() == "영화"){
                detailselect(this);
            }else if($(this).val() == "컴퓨터"){
                detailselect(this);
            }else if($(this).val() == "영어"){
                detailselect(this);
            }else if($(this).val() == "수학"){
                detailselect(this);
            }
        }else{
            detailselect("");
        }
    });

    $("#btn").click(function (e){

        e.preventDefault();

        var category = $('input[type="checkbox"][name="category"]:checked').val();
        var detailCategory = $('input[type="checkbox"][name="detail-category"]:checked').val();
        var searchAddress = $('#search').val();

        var queryParams = new URLSearchParams(window.location.search);

        if (category) {
            queryParams.set('category', category);
        } else {
            queryParams.delete('category');
        }

        if (detailCategory) {
            queryParams.set('detailcategory', detailCategory);
        } else {
            queryParams.delete('detailcategory');
        }

        queryParams.set('address', searchAddress);

        window.location.search = queryParams.toString();


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






    //category의 데이터를 받아 파라미터로 넘김
    function select(e){
        var selectedCategory;
        // 선택된 값 가져오기
        if(e =="" || e == null || e == "undefined"){
            selectedCategory = "";
        }else{
            selectedCategory = $(e).val();
        }

        // 현재 페이지의 URL 가져오기
        var currentUrl = window.location.href;

        // 쿼리 파라미터로 category 추가하기
        var newUrl = new URL(currentUrl);
        newUrl.searchParams.set('category', selectedCategory);
        newUrl.searchParams.set('detailcategory',"")
        newUrl.searchParams.set('page',1);

        //페이지 이동
        window.location.href = newUrl.toString();
    }


    //detailcategory의 데이터를 받아 파라미터로 넘김
    function detailselect(e){
        var selecteddetialCategory;
        // 선택된 값 가져오기
        if(e == null || e == "" || e == 'undefined'){
            selecteddetialCategory = "";
        }else{
            selecteddetialCategory = $(e).val();
        }

        // 현재 페이지의 URL 가져오기
        var currentUrl = window.location.href;

        // 쿼리 파라미터로 category 추가하기
        var newUrl = new URL(currentUrl);
        newUrl.searchParams.set('detailcategory', selecteddetialCategory);
        newUrl.searchParams.set('page',1);

        //페이지 이동
        window.location.href = newUrl.toString();
    }

    $(".socializing-item").click(function (){
        localStorage.setItem("backUrl",location.href.toString())
    });

});