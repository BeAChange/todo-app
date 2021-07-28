package com.todolist.services.impl;

import com.todolist.dto.UserProfileDto;
import com.todolist.entities.UserProfile;
import com.todolist.exception.TodoException;
import com.todolist.exception.TodoNotFoundException;
import com.todolist.repositories.UserProfileRepository;
import com.todolist.services.UserService;
import com.todolist.util.PropertyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.todolist.util.Constants.REGEX_NUMERIC;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private UserProfileRepository userRepository;

    @Autowired
    public UserServiceImpl(UserProfileRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserProfile createUser(UserProfileDto userDto) {
        UserProfile userEntity = new UserProfile();
        BeanUtils.copyProperties(userDto, userEntity);
        userEntity.setCreatedDate(new Date());
        userEntity.setActive(true);
        return userRepository.save(userEntity);
    }

    @Transactional(readOnly = true)
    public List<UserProfile> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public UserProfile findUserByIdOrName(String idOrName) throws TodoException {
        UserProfile found = null;

        if (idOrName.matches(REGEX_NUMERIC)) {
            Optional<UserProfile> optionalUserProfile = userRepository.findById(Long.parseLong(idOrName));
            if (optionalUserProfile.isPresent())
                found = optionalUserProfile.get();
            log.debug("Got the user entry by Id: {}", found);
        } else {
            found = userRepository.findByUserName(idOrName);
            log.debug("Got the user entry by userName: {}", found);
        }

        if (Objects.isNull(found)) {
            throw new TodoNotFoundException("No data exists for the given userId:" + idOrName);
        }
        return found;
    }

    @Transactional(rollbackFor = {TodoNotFoundException.class})
    public UserProfile updateUser(Long userId, UserProfileDto updatedUserDto) {
        UserProfile user = null;
        Optional<UserProfile> optionalUserProfile = userRepository.findByUserId(userId);
        if (optionalUserProfile.isPresent())
            user = optionalUserProfile.get();
        log.debug("Found the user entry: {}", user);

        if (Objects.isNull(user)) {
            throw new TodoNotFoundException("No data exists with given userId:" + userId);
        }

        BeanUtils.copyProperties(updatedUserDto, user, PropertyUtil.getNullPropertiesString(updatedUserDto));
        user.setModifiedDate(new Date());
        return userRepository.save(user);
    }

    @Transactional(rollbackFor = {TodoNotFoundException.class})
    public UserProfile deleteUserById(Long userId) {
        UserProfile deleted = null;
        Optional<UserProfile> optionalUserProfile = userRepository.findByUserId(userId);
        if (optionalUserProfile.isPresent())
            deleted = optionalUserProfile.get();
        log.debug("Found the user entry: {}", deleted);

        if (Objects.isNull(deleted)) {
            throw new TodoNotFoundException("No record found with given id:" + userId);
        }

        try {
            userRepository.softDelete(deleted.getId());
        } catch (DataIntegrityViolationException dive) {
            throw new TodoException("The user appears to be having some active assigned tasks.");
        } catch (Exception exp) {
            throw new TodoException("Could not able to remove the user. Reason:" + exp.getMessage());
        }
        return deleted;
    }
}
