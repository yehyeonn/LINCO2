$(document).ready(function () {
    console.log("gallery.js 지롱~");
});

function openModal(img) {
    var modal = document.getElementById("myModal");
    var modalImg = document.getElementById("modalImage");
    var imgSrc = img.getAttribute("src");

    // console.log(img);
    modalImg.src = imgSrc;

    modal.style.display = "block";
}

function closeModal() {
    var modal = document.getElementById("myModal");
    modal.style.display = "none";
}

$(document).ready(function() {
    $('.like-btn').click(function() {
        var attachmentId = $(this).val();

        console.log(attachmentId);

        $.ajax({
            type: 'POST',
            url: '/club/gallery',
            contentType: "application/json",
            data: {attachmentId: attachmentId},
            success: function(response) {
                alert("내가 좋아요한 사진의 id 는?" + attachmentId)
                
                // 좋아요 수 업데이트
                // $('#like-count-' + attachmentId).text(response.likeCount);
            },
            error: function() {
                alert('에러~');
            }
        });
    });
});
