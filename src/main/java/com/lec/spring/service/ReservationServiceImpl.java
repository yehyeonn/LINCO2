package com.lec.spring.service;

import com.lec.spring.domain.Reservation;
import com.lec.spring.domain.User;
import com.lec.spring.repository.ReservationRepository;
import com.lec.spring.repository.UserRepository;
import com.lec.spring.util.U;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private UserRepository userRepository;
    private ReservationRepository reservationRepository;

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


}
