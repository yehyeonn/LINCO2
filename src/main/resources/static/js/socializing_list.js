$(function (){
    $(".detail-category-area").hide();
    $(".category-area").hide();

    $(".filter").click(function(){
        $(".category-area").toggle();
        $(".detail-category-area").hide();$('input[type="checkbox"][name="category"]').prop('checked',false);
        $('input[type="checkbox"][name="detail-category"]').prop('checked',false);
    });


    $('input[type="checkbox"][name="category"]').click(function(){
        $(".detail-category-area").show();
        if($(this).prop('checked')){
            $('input[type="checkbox"][name="category"]').prop('checked',false);

            $(this).prop('checked',true);


            $(".detail-category-list").hide();
            if($(this).val() == "스포츠"){
                $(".category1-list").show();
            }else if($(this).val() == "공연"){
                $(".category2-list").show();
            }else if($(this).val() == "관광"){
                $(".category3-list").show();
            }

        }else{
            $(".detail-category-area").hide();
        }
    });

    $('input[type="checkbox"][name="detail-category"]').click(function(){
        if($(this).prop('checked')){
            $('input[type="checkbox"][name="detail-category"]').prop('checked',false);

            $(this).prop('checked',true);

        }
    });


    $('#btn').click(function () {
        selectPlayer();
    })
    // alert($('.con .player').eq(0).attr('name'));
    function selectPlayer() {
        var regex = /[ㄱ-ㅣ가-힣]+/;
        // for(i=0;i)
        // $('.con .player').addClass('hidden');
        $('.con .player').hide();
        // $('.con .player').css('display', 'none');
        let selectname = $("#search").val().trim().toLowerCase();
        if (selectname == "") {
            $('.con .player').show();
        } else if (!(regex.test(selectname))) {
            alert("한글로 작성해주세요");
            $('#search').focus();
            $('#search').val("");
        } else {
            $('.socializing-item').each(function () {
                if ($(this).attr('name').toLowerCase().indexOf(selectname) > -1) {
                    $(this).show();
                }
            });

        }
    }



})