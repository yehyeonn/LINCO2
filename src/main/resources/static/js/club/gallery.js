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

$(document).ready(function () {
    $('.like-btn').on('click', function(){
        var attachmentId=$(this).data('img-id').val()
        likeImg(attachmentId);
    })
});

function likeImg(attachmentId) {
    // var likeButton=$('.like-btn[data-attachment-id="${attachmentId}"]')
    // var likeCount = document.getElementById("like-count-" + attachmentId);

    console.log(attachmentId + " attachmentId");
    // console.log(likeCount + "likeCount");
    // console.log(likeButton + "likeButton");
}
