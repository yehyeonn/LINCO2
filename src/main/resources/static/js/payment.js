document.addEventListener('DOMContentLoaded', function () {
    // 서버에서 가져온 데이터 (예제 데이터로 대체)
    const userName = document.querySelector('.name').dataset.name;
    const userEmail = document.querySelector('.email').dataset.email;
    const userTell = document.querySelector('.tell').dataset.tell;
    const totalPrice = localStorage.getItem('totalPrice');

    window.requestPay = function() {
        window.IMP.init('imp28617244');
        window.IMP.request_pay({
            pg: 'html5_inicis',
            pay_method: "card",
            merchant_uid: "merchant_" + new Date().getTime(),
            name: "Linco 대관 예약",
            amount: totalPrice,
            buyer_email: userEmail,
            buyer_name: userName,
            buyer_tel: userTell,
        }, function (rsp) {
            if (rsp.success) {
                // 결제 성공 시 로직
                console.log('Payment succeeded');
                window.location.href = '/socializing/write';
            } else {
                // 결제 실패 시 로직
                alert('Payment failed', rsp.error_msg);
                console.log('Payment failed', rsp.error_msg);
                // 추가로 실행할 로직을 여기에 작성
            }
        });
    }
});