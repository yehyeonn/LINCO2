$(function (){
    $(".detail-category-area").hide();
    $(".category-area").hide();

    $(".filter").click(function(){
        $(".category-area").toggle();
        $(".detail-category-area").hide();$('input[type="checkbox"][name="category"]').prop('checked',false);
        $('input[type="checkbox"][name="detail-category"]').prop('checked',false);
        $(".socializing-item").show();
    });


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
            }else if($(this).val() == "공연"){
                $(".category2-list").show();
            }else if($(this).val() == "공부"){
                $(".category3-list").show();
            }

            $('.socializing-item').each(function () {
                if ($(this).find('#category').text() == category) {
                    $(this).show();
                }
            });

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

            $('.socializing-item').each(function () {
                if ($(this).find('#detail_category').text() == detail_category) {
                    $(this).show();
                }
            });
        }else {
            $('input[type="checkbox"][name="category"]').each(function () {
                if($(this).is(':checked')){
                    category = $(this).val();
                    $('.socializing-item').each(function () {
                        if ($(this).find('#category').text() == category) {
                            $(this).show();
                        }

                    });
                }
            });
        }
    });


    // 주소에서 토큰을 활용하여 구,군 만 출력

    $('.socializing-item').each(function() {
        var fullAddress = $(this).find('#address').text();
        var tokens = fullAddress.split(' ');
        var district = tokens.find(token => token.includes('구'));
        if (district) {
            $(this).find('#address').text(district);
        }
    });




    // 해당 구 군을 검색하여 출력
    $('#btn').click(function () {
        selectSocial();
    })
    function selectSocial() {
        var regex = /[ㄱ-ㅣ가-힣]+/;
        $('.socializing-item').hide();
        let selectaddress = $("#search").val().trim().toLowerCase();
        if (selectaddress == "") {
            $('.socializing-item').show();
        } else if (!(regex.test(selectaddress))) {
            alert("한글로 작성해주세요");
            $('#search').focus();
            $('#search').val("");
        } else {
            $('.socializing-item').each(function () {
                if ($(this).find('#address').text().toLowerCase().indexOf(selectaddress) > -1) {
                    $(this).show();
                }
            });

        }
    }



})