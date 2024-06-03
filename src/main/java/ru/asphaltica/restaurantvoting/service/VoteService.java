package ru.asphaltica.restaurantvoting.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.asphaltica.restaurantvoting.common.error.AccessDeniedToChangeVoteException;
import ru.asphaltica.restaurantvoting.common.error.NotFoundException;
import ru.asphaltica.restaurantvoting.model.Menu;
import ru.asphaltica.restaurantvoting.model.User;
import ru.asphaltica.restaurantvoting.model.Vote;
import ru.asphaltica.restaurantvoting.repository.MenuRepository;
import ru.asphaltica.restaurantvoting.repository.VoteRepository;
import ru.asphaltica.restaurantvoting.util.DateTimeUtil;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final MenuRepository menuRepository;
    private final UserService userService;
    private final DateTimeUtil dateTimeUtil;

    @Transactional
    public Vote create(String userName, int restaurantId) {
        //Проверяем есть ли в данном ресторане меню доступные для голосования
        Menu menu = menuRepository.findByAvailableDateAndOwnRestaurant(LocalDate.now(), restaurantId).orElseThrow(
                () -> new NotFoundException("The restaurant with id = " + restaurantId + " has no menu today"));
        //Готовим сущность голос к записи
        User user = userService.findByMail(userName);
        final Vote vote = new Vote(LocalDate.now(), user, menu);
        //Проверяем, возможно пользователь уже голосовал сегодня
        voteRepository.findByCreatedAtAndAndUser(LocalDate.now(), user).ifPresent(updated ->
                {
                    //проверяем не истекло ли время для того, чтобы изменить решение по голосованию
                    if (!dateTimeUtil.isVoteChangePeriod()) {
                        throw new AccessDeniedToChangeVoteException("Время доступа к повторному голосованию истекло!");
                    }
                    vote.setId(updated.getId());
                }
        );
        //Если да то присвоим ему id и тогда данные обновятся,
        // сохранение без id приведет к созданию новой записи в базе данных
        return voteRepository.save(vote);
    }

    public Vote findById(int id) {
        return voteRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Entity with id=" + id + " not found"));
    }

    public Vote findByUserEmailToday(String email) {
        return voteRepository.findByUserEmailToday(email, LocalDate.now()).orElseThrow(
                () -> new NotFoundException("No voting results found for today"));
    }

    public List<Vote> findAllByUserEmail(String email) {
        return voteRepository.findAllByUserEmail(email).orElseThrow(
                () -> new NotFoundException("No voting history")
        );
    }
}
