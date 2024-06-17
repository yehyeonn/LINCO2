const subCategories = {
    "운동": ["축구", "야구", "농구"],
    "공연": ["전시", "댄스", "영화"],
    "공부": ["컴퓨터", "영어", "수학"]
};

$(document).ready(function() {
    $('#category').on('change', function() {
        const category = $(this).val();
        const subCategory = $('#subCategory');
        subCategory.empty();
        subCategory.append('<option value="">소분류 선택</option>'); // Reset subcategories
        if (category && subCategories[category]) {
            $.each(subCategories[category], function(index, value) {
                subCategory.append('<option value="' + value + '">' + value + '</option>');
            });
        }
    });

    $('#fileSelect').on('click', function() {
        $('#fileInput').click();
    });

    $('#fileInput').on('change', function() {
        const file = this.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                $('#previewImage').attr('src', e.target.result).show();
                $("span").hide();
            };
            reader.readAsDataURL(file);
        } else {
            $('#previewImage').hide();
        }
    });

    $('#socializingForm').on('submit', function(e) {
        e.preventDefault();
        const formData = new FormData(this);
        $.ajax({
            type: 'POST',
            url: '/socializing/write',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                alert('소셜라이징이 성공적으로 생성되었습니다.');
                // 성공 시 처리
            },
            error: function(error) {
                alert('오류가 발생했습니다.');
                console.error('Error:', error);
            }
        });
    });
});