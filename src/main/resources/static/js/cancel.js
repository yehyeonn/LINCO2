function cancelPay(event) {
    var button = event.target;
    var row = button.closest('tr');
    var merchantUid = row.getAttribute('data-merchant');
    var totalPrice = parseInt(row.getAttribute('data-total-price'));
    var reason = "결제 취소";

    console.log(merchantUid);
    console.log(totalPrice)
    console.log(reason);

    jQuery.ajax({
        url: "/user/cancel",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            "merchant_uid": merchantUid,
            "cancel_request_amount": totalPrice,
            "reason": reason
        }),
        dataType: "text",
        success: function (response) {
            alert('결제 취소가 완료되었습니다.');
            location.reload(); // 필요 시 페이지를 새로고침하거나 다른 작업 수행
        },
        error: function (xhr, status, error) {
            alert('결제 취소 중 오류가 발생했습니다.');
            console.error('Error details:', status, error);
        }
    });
}