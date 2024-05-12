package org.example.plantdisease.utils;


import org.example.plantdisease.entity.User;
import org.example.plantdisease.exception.RestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class CommonUtils {

    public Map<String, Object> smsServiceSendSms(String phoneNumber, String originator, String otp) {
        Map<String, Object> messages = new LinkedHashMap<>();
        List<Map<String, Object>> messageList = new ArrayList<>();

        Map<String, Object> content = new HashMap<>();
        content.put("text", otp);

        Map<String, Object> SMS = new HashMap<>();
        SMS.put("originator", originator);
        SMS.put("content", content);

        Map<String, Object> message = new HashMap<>();
        message.put("recipient", phoneNumber);
        message.put("priority", "");
        message.put("sms", SMS);

        Date messageDate = new Date();
        message.put("message-id", messageDate.getTime());
        messageList.add(message);
        messages.put("messages", messageList);

        return messages;
    }

    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

//         this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    public static HttpServletRequest currentRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return Optional.ofNullable(servletRequestAttributes).map(ServletRequestAttributes::getRequest).orElse(null);
    }

    public static boolean getSecretKeyForAttachment() {
        HttpServletRequest httpServletRequest = currentRequest();
        if (Objects.isNull(httpServletRequest)) {
            return false;
        }
        String header = httpServletRequest.getHeader(RestConstants.GET_ATTACHMENT_KEY);
        return Objects.equals(header, RestConstants.SECRET_KEY_FOR_ATTACHMENT);
    }

    public static void checkPassword(String password, String prePassword) {
        if (!Objects.equals(password, prePassword)) {
            throw RestException.restThrow("PASSWORD AND PRE PASSWORD ARE NOT SAME!!", HttpStatus.BAD_REQUEST);
        }
    }

    public static User getUserFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals("" + authentication.getPrincipal()))
            return null;
        return (User) authentication.getPrincipal();
    }

    public static User getCurrentUserOrNewUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals("" + authentication.getPrincipal()))
            return new User();
        return (User) authentication.getPrincipal();
    }

    public static UUID getCurrentUserIdOrNull() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals("" + authentication.getPrincipal()))
            return null;
        User principal = (User) authentication.getPrincipal();
        return principal.getId();
    }


}
