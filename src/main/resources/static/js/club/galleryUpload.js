$(document).ready(function() {
    $('#fileSelect').on('click', function() {
        $('#fileInput').click();
    });

    $('#fileInput').on('change', function() {
        var input = this;
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            reader.onload = function(e) {
                $('#previewImage').attr('src', e.target.result);
                $('#img_txt').hide();
            }
            reader.readAsDataURL(input.files[0]);
        }
    });
});
