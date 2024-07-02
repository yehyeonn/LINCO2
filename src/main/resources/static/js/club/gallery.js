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
