$(document).ready(function () {
    $('.venue-item').on('click', function () {
        const venueId = $(this).data('venue-id');
        window.location.href = '/venue/detail' + venueId;
    });
});

