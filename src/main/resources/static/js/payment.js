document.addEventListener('DOMContentLoaded', function () {
    const container = document.querySelector('.container-wrapper');
    const userId = container.dataset.userId;
    const venueId = container.dataset.venueId;
    const userName = document.querySelector('.name').value;
    const userEmail = document.querySelector('.email').value;
    const userTel = document.querySelector('.tell').value;
    const totalPrice = localStorage.getItem('totalPrice');
    const reserveDate = localStorage.getItem('selectedDate');
    const reserveStartTime = localStorage.getItem('startTime');
    const reserveEndTime = localStorage.getItem('endTime');

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
            buyer_tel: userTel,
        }, function (rsp) {
            if (rsp.success) {
                // 결제 성공 시 로직
                console.log('Payment succeeded');

                // 서버에 결제 정보 저장
                const paymentData = {
                    user: {
                        id: userId
                    },
                    venue: {
                        id: venueId
                    },
                    status: 'PAYED',
                    reserve_date: reserveDate,
                    reserve_start_time: reserveStartTime,
                    reserve_end_time: reserveEndTime,
                    totalPrice: totalPrice,
                    payDate: new Date().toISOString().split('T')[0]
                };

                fetch('/api/savePayment', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(paymentData)
                })
                    .then(response => response.json())
                    .then(data => {
                        console.log('Payment data saved:', data);
                        window.location.href = '/socializing/write';
                    })
                    .catch(error => {
                        console.error('Error saving payment data:', error);
                    });

            } else {
                // 결제 실패 시 로직
                alert('Payment failed', rsp.error_msg);
                console.log('Payment failed', rsp.error_msg);
            }
        });
    }
});