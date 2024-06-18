$(document).ready(function () {
    const buttons = document.querySelectorAll('.category-area button a');

    buttons.addEventListener('click', function(event){
        event.preventDefault();

        var venue_category = $(this).text().trim();

        $.ajax({
            url: '/venue/list/' + venue_category,
            type: 'GET',
            success: function (data) {
                console.log(data)
            },
            error : function(xhr, status, error){

            }
        })
    })

    function redirectToCategory(category) {
        // 카테고리 값과 함께 URL 생성
        var url = '/venue/list/' + encodeURIComponent(category);

        // 페이지 이동
        window.location.href = url;
    }


});

