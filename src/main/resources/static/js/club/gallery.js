// 모달창으로 사진 열기
function openModal(img) {
    var modal = document.getElementById("myModal");
    var modalImg = document.getElementById("modalImage");
    var imgSrc = img.getAttribute("src");

    // console.log(img);
    modalImg.src = imgSrc;

    modal.style.display = "block";
}

// X 버튼으로 모달창 닫기
function closeModal() {
    var modal = document.getElementById("myModal");
    modal.style.display = "none";
}

$(document).ready(function () {

    var likedAttachmentIds = [];    // 이미 좋아요를 누른 attachmentIds 를 담을 배열
    var allAttachmentIds=[]         // 페이지에 모든 attachmentIds 를 담을 배열
    
    // 페이지에 로드된 좋아요를 누른 attachmentId를 받아 likedAttachmentIds에 push
    document.querySelectorAll('.likedAttachment .attachmentIds').forEach(function (element) {
        likedAttachmentIds.push(element.textContent.trim());
    });

    // 페이지에 로드된 모든 attachmentId를 받아 allAttachmentIds push
    document.querySelectorAll('.imgs .like-btn').forEach(function (element) {
        allAttachmentIds.push(element.value);
    });

    // likedAttachmentIds 와 allAttachmentIds 비교, 같든 버튼은 좋아요 하트를 solid 로 변경
    likedAttachmentIds.forEach(function (id) {
        if (allAttachmentIds.includes(id)) {  // Ensure id type matches
            let button = document.querySelector(`.like-btn[value='${id}']`);
            if (button) {
                button.querySelector(".fa-regular.fa-heart").style.display = 'none';
                button.querySelector(".fa-solid.fa-heart").style.display = 'inline';
            }
        }
    });

    // 좋아요 버튼, 다시 누르면 좋아요 해제
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
