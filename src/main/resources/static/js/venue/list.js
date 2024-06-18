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
});

