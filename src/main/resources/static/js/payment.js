document.addEventListener('DOMContentLoaded', function () {

    window.requestPay = function() {
        const container = document.querySelector('.container-wrapper');
        const userId = parseInt(document.querySelector('input[name="user_id"]').value, 10);
        const venueId = parseInt(localStorage.getItem('venue_id'), 10);
        // const userEmail = localStorage.getItem('email');
        const userEmail = $('.info-box .email').val();
        const userTel = $('.info-box .tell').val();
        const reserveDate = localStorage.getItem('selectedDate');
        const venueName = document.querySelector('.venue_name').textContent
        const reserveStartTime = localStorage.getItem('startTime');
        const reserveEndTime = localStorage.getItem('endTime');
        const totalPrice = parseInt(localStorage.getItem('totalPrice'), 10);
        const userName = $('.info-box .name').val();
        const merchant = "merchant_" + new Date().getTime();

        window.IMP.init('imp28617244');
        window.IMP.request_pay({
            pg: 'html5_inicis',
            pay_method: "card",
            merchant_uid: merchant,
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
                    reservation_name: userName,
                    email: userEmail,
                    tel: userTel,
                    venue: {
                        id: venueId,
                        venue_name: venueName
                    },
                    status: 'PAYED',
                    reserve_date: reserveDate,
                    reserve_start_time: reserveStartTime,
                    reserve_end_time: reserveEndTime,
                    total_price: totalPrice,
                    merchantUid: merchant,
                };

                fetch('/reservation/savePayment', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(paymentData)
                })
                    .then(response => response.text())
                    .then(data => {
                        console.log('Payment data saved:', data);

                        const impUid = rsp.imp_uid;
                        // 결제 정보 가져오기
                        fetch(`/reservation/validate/${rsp.imp_uid}?merchant_uid=${rsp.merchant_uid}&amount=${rsp.paid_amount}`, {
                            method: "GET",
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        })
                            .then(response => response.text())
                            .then(validationData => {
                                console.log('Validation data:', validationData);
                                // 추가 로직을 여기에 추가할 수 있습니다.
                                const merUid = paymentData.merchantUid;
                                alert(merUid);
                                const venueId = paymentData.venue.id;
                                window.location.href = `/socializing/write`;
                            })
                            .catch(error => {
                                console.error('Error validating payment data:', error);
                            });
                    })
                    .catch(error => {
                        console.error('Error saving payment data:', error);
                    });

            } else {
                // 결제 실패 시 로직
                console.log('Payment failed', rsp.error_msg);
            }
        });
    }
});