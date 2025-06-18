package org.example.qlthuvien.services;

import lombok.RequiredArgsConstructor;
import org.example.qlthuvien.entity.User;
import org.example.qlthuvien.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void addXp(User user, int amount) {
        int currentXp = user.getXp() != null ? user.getXp() : 0;
        int newXp = Math.max(currentXp + amount, 0);

        user.setXp(newXp);
        userRepository.save(user);
    }
}
