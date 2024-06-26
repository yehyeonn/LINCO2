document.addEventListener('DOMContentLoaded', function () {
    const container = document.querySelector('.container-wrapper');
    const userId = parseInt(document.querySelector('input[name="user_id"]').value, 10);
    const venueId = parseInt(localStorage.getItem('venue_id'), 10);
    const userName = document.querySelector('.name').value;
    const userEmail = document.querySelector('.email').value;
    const userTel = document.querySelector('.tell').value;
    const totalPrice = parseInt(localStorage.getItem('totalPrice'), 10);
    const reserveDate = localStorage.getItem('selectedDate');
    const reserveStartTime = localStorage.getItem('startTime');
    const reserveEndTime = localStorage.getItem('endTime');
    const venueName = document.querySelector('.venue_name').textContent

    // function formatDate(date) {
    //     const year = date.getFullYear();
    //     const month = String(date.getMonth() + 1).padStart(2, '0');
    //     const day = String(date.getDate()).padStart(2, '0');
    //     const hours = String(date.getHours()).padStart(2, '0');
    //     const minutes = String(date.getMinutes()).padStart(2, '0');
    //     const seconds = String(date.getSeconds()).padStart(2, '0');
    //
    //     return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    // }

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

                // const payDate = formatDate(new Date());
                // alert(payDate)

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
                        name: venueName
                    },
                    status: 'PAYED',
                    reserve_date: reserveDate,
                    reserve_start_time: reserveStartTime,
                    reserve_end_time: reserveEndTime,
                    total_price: totalPrice
                    // paydate: payDate
                };

                fetch('/reservation/savePayment', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(paymentData)
                })
                    .then(response => response.json())
                    .then(data => {
                        console.log('Payment data saved:', data);
                        alert("과연 db에 저장됏을까요?")
                        // window.location.href = '/socializing/write';
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