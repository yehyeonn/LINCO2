function cancelPay(event) {
    var button = event.target;
    var row = button.closest('tr');
    var merchantUid = row.getAttribute('data-merchant'); // 고유 결제 키
    var totalPrice = parseInt(row.getAttribute('data-total-price'));
    var reason = "결제 취소";
    var impId = row.getAttribute('data-impId'); // imp_uid

    console.log(impId)
    console.log(merchantUid);
    console.log(totalPrice);
    console.log(reason);

    jQuery.ajax({
        url: "/user/cancel",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            "imp_uid": impId,
            "merchant_uid": merchantUid,
            "total_price": totalPrice,
            "reason": reason
        }),
        dataType: "text", // 데이터 타입을 text로 설정
        success: function (response) {
            alert(response); // 반환된 텍스트를 알림으로 표시
            location.reload(); // 필요 시 페이지를 새로고침하거나 다른 작업 수행
        },
        error: function (xhr, status, error) {
            alert('결제 취소 중 오류가 발생했습니다.');
            console.error('Error details:', status, error);
        }
    });
}