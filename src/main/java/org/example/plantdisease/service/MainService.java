package org.example.plantdisease.service;


import lombok.RequiredArgsConstructor;
import org.example.plantdisease.entity.LeafCondition;
import org.example.plantdisease.entity.Treatment;
import org.example.plantdisease.entity.User;
import org.example.plantdisease.enums.RoleType;
import org.example.plantdisease.exception.RestException;
import org.example.plantdisease.repository.LeafConditionRepository;
import org.example.plantdisease.repository.TreatmentRepository;
import org.example.plantdisease.repository.UserRepository;
import org.example.plantdisease.utils.CommonUtils;
import org.example.plantdisease.utils.MessageConstants;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MainService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TreatmentRepository treatmentRepository;
    private final LeafConditionRepository leafConditionRepository;

    public String getInstructionByDiseaseId(List<Long> numbers) {

        Long mostRepeated = findMostRepeated(numbers);

        System.out.println("ENG KO'P TAKRORLANGAN SON: " + mostRepeated);
        if (mostRepeated == 0)
            return "HECH QAYSI KASALLIK TURIGA TEGISHLI EMAS";
        else {

            LeafCondition leafCondition = leafConditionRepository.findById(mostRepeated).orElseThrow(() -> RestException.notFound("LEAF_CONDITION"));

            if (leafCondition.getName().equals("Healthy"))
                return "BARGNING HOLATI: " + leafCondition.getName();

            Treatment treatment = treatmentRepository.findAllByLeafConditionId(mostRepeated);

            return "BARGNING HOLATI: " + treatment.getLeafCondition().getName() + "   \n" + "INSTRUKTSIYA: " + treatment.getInstruction();
        }

    }

    public boolean checkDuplicateNumber(List<UUID> attachmentIdList) {

        for (int i = 0; i < attachmentIdList.size(); i++) {

            for (int j = i + 1; j < attachmentIdList.size(); j++) {

                if (attachmentIdList.get(i).equals(attachmentIdList.get(j)))
                    return true;

            }
        }
        return false;
    }

    public Long findMostRepeated(List<Long> numbers) {

        Map<Long, Integer> countMap = new HashMap<>();

        for (Long num : numbers) {
            if (countMap.containsKey(num))
                countMap.put(num, countMap.get(num) + 1);
            else
                countMap.put(num, 1);
        }

        int maxCount = 0;
        int number = 0;
        Long mostRepeated = 0L;
        for (Map.Entry<Long, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostRepeated = entry.getKey();
            } else if (maxCount != 0 && entry.getValue() == maxCount)
                number = maxCount;
        }

        return number == maxCount ? 0 : mostRepeated;
    }



    /**
     * TIZIMDAGI (SECURITY_CONTEXT DAGI) MAVJUD USERNI ANIQLAB OLAMIZ
     */
    public User getUserFromSecurityContextIfNullThrow() {
        User userFromSecurityContext = CommonUtils.getUserFromSecurityContext();
        if (Objects.isNull(userFromSecurityContext))
            throw RestException.restThrow(MessageConstants.USER_NOT_FOUND, HttpStatus.UNAUTHORIZED);
        return userFromSecurityContext;
    }

    //USERNI AVVAL REQISTRATSIYADAN O'TGANLIGINI TEKSHIRADI
    public User checkUserForRegister(String phoneNumber) {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        User user = new User();
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            if (!Objects.equals(user.getRole().getType(), RoleType.GUEST)) {
                throw RestException.restThrow("YOU HAVE ALREADY REGISTERED!!", HttpStatus.CONFLICT);
            }
        }
        return user;
    }

    public void checkPasswordAndPrePassword(String password, String prePassword) {
        if (!Objects.equals(password, prePassword)) {
            throw RestException.restThrow("PASSWORD AND PRE PASSWORD ARE NOT SAME!!", HttpStatus.BAD_REQUEST);
        }
    }

    //USERNING PAROLI TO`G`RILIGINI TEKSHIRISH
    public void checkUserPassword(String enteringPassword, String userEncodedPassword) {
        if (Objects.isNull(enteringPassword))
            throw RestException.restThrow(MessageConstants.PASSWORD_SHOULD_NOT_BE_EMPTY, HttpStatus.BAD_REQUEST);

        if (!passwordEncoder.matches(enteringPassword, userEncodedPassword))
            throw RestException.restThrow(MessageConstants.PASSWORD_ERROR, HttpStatus.BAD_REQUEST);
    }

    public static String datePattern(Timestamp timestamp) {
        Date date = new Date(timestamp.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }
}
