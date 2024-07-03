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

$(document).ready(function () {

    var likedattachmentIds = [];
    var allAttachmentIds=[]


    document.querySelectorAll('.likedAttachment .attachmentIds').forEach(function (element) {
        likedattachmentIds.push(element.textContent.trim());
    });

    document.querySelectorAll('.imgs .like-btn').forEach(function (element) {
        allAttachmentIds.push(element.value);
    });

    likedattachmentIds.forEach(function (id) {
        if (allAttachmentIds.includes(id)) {  // Ensure id type matches
            let button = document.querySelector(`.like-btn[value='${id}']`);
            if (button) {
                button.querySelector(".fa-regular.fa-heart").style.display = 'none';
                button.querySelector(".fa-solid.fa-heart").style.display = 'inline';
            }
        }
    });

    // likedattachmentIds.forEach(element => {
    //     console.log("attachmentId: " + element);
    // });
    //
    // allAttachmentIds.forEach(element => {
    //     console.log("allAttachmentIds: " + element);
    // });


    $('.like-btn').click(function () {
        var $this = $(this);
        var attachmentId = $this.val(); // 버튼의 value 속성에서 attachmentId를 가져옴
        var $regularHeart = $this.find('.fa-regular.fa-heart');
        var $solidHeart = $this.find('.fa-solid.fa-heart');
        var userId = $('body').data('user-id');

        console.log("attachment Id : " + attachmentId);
        console.log("유저 아이디: " + userId);


        $.ajax({
            type: 'POST',
            url: '/club/galleryLike',
            contentType: "application/json",
            data: JSON.stringify({userId: userId, attachmentId: attachmentId}),
            success: function (response) {
                // 아이콘 전환
                if ($regularHeart.is(':visible')) {
                    $regularHeart.hide();
                    $solidHeart.show();
                } else {
                    $regularHeart.show();
                    $solidHeart.hide();
                }

                // 좋아요 수 업데이트
                // $('#like-count-' + attachmentId).text(response.likeCount);
            },
            error: function (xhr, status, error) {
                console.error('Error:', error);
                console.error('Status:', status);
                console.error('Response:', xhr.responseText);
                alert('에러 발생!');
            }
        });
    });
});
