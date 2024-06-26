package com.lec.spring.service;

import com.lec.spring.domain.Reservation;
import com.lec.spring.domain.User;
import com.lec.spring.repository.ReservationRepository;
import com.lec.spring.repository.UserRepository;
import com.lec.spring.util.U;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.AccessToken;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.annotation.PostConstruct;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private UserRepository userRepository;
    private ReservationRepository reservationRepository;

    private IamportService iamportService;

    @Autowired
    public ReservationServiceImpl(SqlSession sqlSession) {
        userRepository = sqlSession.getMapper(UserRepository.class);
        reservationRepository = sqlSession.getMapper(ReservationRepository.class);
    }

    @Override
    public int write(Reservation reservation) {
        User user = U.getLoggedUser();
//        User user = new User();

        userRepository.findById(user.getId());
        reservation.setUser(user);

//        int cnt = reservationRepository.save(reservation);

        return reservationRepository.save(reservation);
    }

    @Override
    public List<Reservation> list() {
        User user = U.getLoggedUser();
//        User user = new User();

        return reservationRepository.findByUserId(user.getId());
    }

    @Override
    @Transactional
    public int update(Reservation reservation) {
        return reservationRepository.update(reservation);

    }

    @Override
    public List<Reservation> findByVenueAndDate(Long venue_id, String reserve_date) {
        return reservationRepository.findByVenueAndDate(venue_id, reserve_date);
    }

    @Override
    public List<Reservation> findPayedReservation() {
        return reservationRepository.findPayedReservation();
    }

    @Override
    public List<Reservation> findByUserId(Long user_id) {
        return reservationRepository.findByUserId(user_id);
    }

    @Override
    @Scheduled(cron = "0 0 0 * * *")
    public void updateExpiredReservationStatus() {
        List<Reservation> expiredReservation = reservationRepository.findExpiredReservation();

        for (Reservation reservation : expiredReservation) {
            reservation.setStatus("DONE");
            reservationRepository.update(reservation);
        }
    }


    @Override
    public Reservation findById(Long id) {
        Reservation reservation = reservationRepository.findById(id);

        return reservation;
    }

    @Override
    public Reservation findByImpUid(String impUid) {
        Reservation reservation = reservationRepository.findByImpUid(impUid);

        return reservation;
    }

//    @Override
//    public void cancelPayment(String impUid) throws IamportResponseException, IOException {
//        Reservation reservation = reservationRepository.findByImpUid(impUid);
//        IamportResponse<Payment> iamResponse = iamportService.getIamportClient().paymentByImpUid(impUid);
//        if(reservation != null) {
//            Long pay = reservation.getTotal_price();
//            reservation.setStatus("CANCELED");
//            reservationRepository.update(reservation);
//        }
    }

