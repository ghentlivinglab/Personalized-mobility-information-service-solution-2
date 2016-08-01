package controllers;

import DTO.mappers.EventMapper;
import DTO.mappers.UserMapper;
import DTO.models.ChangeEmailDTO;
import DTO.models.ChangePasswordDTO;
import DTO.models.EventDTO;
import DTO.models.RefreshTokenDTO;
import DTO.models.UserDTO;
import DTO.models.VerificationDTO;
import backend.AppProperties;
import database_v2.controlLayer.Database;
import database_v2.exceptions.*;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import models.event.Event;
import models.exceptions.ExpiredException;

import models.exceptions.InvalidPasswordException;
import models.exceptions.InvalidPhoneNumberException;
import models.services.Email;
import models.users.Password;
import models.users.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.javatuples.Pair;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Handles incoming requests related to users.
 */
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    private final Database database;
    private final TaskExecutor executor;
    private final UserMapper usermapper;
    private final EventMapper eventmapper;

    /**
     *
     * @param database Interface to communicate with the database.
     */
    public UserController(Database database, TaskExecutor executor) {
        this.database = database;
        this.executor = executor;
        this.usermapper = new UserMapper();
        this.eventmapper = new EventMapper();
    }

    /**
     * Get all users.
     *
     * @param accessToken
     * @return A list of all users.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserDTO>> getUsers(@RequestHeader("Authorization") String accessToken) {
        try {
            //check if user is admin
            String userid = accessToken.split("&t=")[0];
            User admin = database.getUser(Integer.parseInt(userid));

            if (!admin.isAdmin()) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
 
            // first we get all users from the database
            List<Pair<Integer, User>> allUsers = database.listAllUsers();
            // and convert them to DTO models
            List<UserDTO> out = new ArrayList<>(allUsers.size());
            for (Pair<Integer, User> user : allUsers) {
                out.add(usermapper.convertToDTO(
                        user.getValue1(),
                        Integer.toString(user.getValue0())
                ));
            }
            return new ResponseEntity<>(out, HttpStatus.OK);
        } catch (DataAccessException | RecordNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Add a new user.
     *
     * @param userdto The user we want to add.
     * @return The user that we created.
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO userdto) {

        try {
            // first we convert the DTO to an application model
            User user = usermapper.convertFromDTO(userdto, false);
            // this model we make persistent
            int userId = database.createUser(user);
            String email = user.getEmailAsString();
            String pin = generatePin(email);
            String subject = "Validatie emailadres Mobiligent.";
            String text = "Gelieve op onderstaande link te klikken om uw emailadres te valideren: \n"
               + "https://vopro6.ugent.be/app/#/validation?user="+email+"&pin="+pin;

            executor.execute(new MailThread(email, subject, text));

            //send the validation email
            //sendEmail(user.getEmail(), pin, Integer.toString(userId));
            // and finally we return the user object with filled in id
            return new ResponseEntity<>(usermapper.convertToDTO(user, userId), HttpStatus.CREATED);
        } catch (InvalidPasswordException | AddressException | DataAccessException | UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AlreadyExistsException ex) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    /**
     * Get a specific user.
     *
     * @param userId The id of the user we want to retrieve.
     * @param accessToken
     * @return The user with the corresponding id.
     */
    @RequestMapping(value = {"/{user_id}"}, method = RequestMethod.GET)
    public ResponseEntity<UserDTO> getUser(@PathVariable("user_id") String userId,
                                            @RequestHeader("Authorization") String accessToken) {

        try {
            User user = database.getUser(Integer.parseInt(userId));
            return new ResponseEntity<>(usermapper.convertToDTO(user, userId), HttpStatus.OK);
        } catch (NumberFormatException | DataAccessException ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } 

    }
    
    /**
     * Get all events of this user
     * @param userIdString The id of the user
     * @param accessToken
     * @return List of all related events (locations & routes) from this user
     */
    @RequestMapping(value = {"/{user_id}/events"}, method = RequestMethod.GET)
    public ResponseEntity getUserEvents(@PathVariable("user_id") String userIdString,
                                            @RequestHeader("Authorization") String accessToken) {

        try {
            int userId = Integer.parseInt(userIdString);
            Set<Pair<String, Event>> recentEvents =
                    database.getRecentEventsOfUser(userId);
            List<EventDTO> events = new ArrayList<>(recentEvents.size());
            recentEvents.stream().forEach(e -> events.add(
                    eventmapper.convertToDTO(e.getValue1(), e.getValue0())));
            return new ResponseEntity<>(events, HttpStatus.OK);
        } catch (NumberFormatException | DataAccessException ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } 

    }

    /**
     * Edit a certain user.
     *
     * @param userId The id of the user we want to adjust.
     * @param userdto The adjusted user-object.
     * @param accessToken
     * @return The adjusted user.
     */
    @RequestMapping(value = "/{user_id}", method = RequestMethod.PUT)
    public ResponseEntity<UserDTO> editUser(@PathVariable("user_id") String userId,
                                            @RequestBody UserDTO userdto,
                                            @RequestHeader("Authorization") String accessToken) {

        try {
            User oldUser = database.getUser(Integer.parseInt(userId));
            User newUser = usermapper.convertFromDTO(userdto, true);
            oldUser.updateData(newUser);
            database.updateUser(Integer.parseInt(userId), oldUser);
            return new ResponseEntity<>(usermapper.convertToDTO(oldUser, userId), HttpStatus.OK);
        } catch (InvalidPhoneNumberException | InvalidPasswordException
                | AddressException | NumberFormatException | DataAccessException ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AlreadyExistsException ex) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } 

    }

    /**
     * Delete a certain user.
     *
     * @param userId The id of the user we want to remove from the database.
     * @param accessToken
     * @return An appropriate status message.
     */
    @RequestMapping(value = "/{user_id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteUser(@PathVariable("user_id") String userId,
                                            @RequestHeader("Authorization") String accessToken) {

        try {
            database.deleteUser(Integer.parseInt(userId));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException | DataAccessException  e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    /**
     * Verify a new user.
     *
     * @param userId
     * @param verify
     * @return Status 200
     */
    @RequestMapping(value = "/{user_id}/verify", method = RequestMethod.POST)
    public ResponseEntity verifyUser(@PathVariable("user_id") String userId,
                                                @RequestBody VerificationDTO verify) {

        try {
            Pair <Integer, User> user = database.getUserByEmail(userId);

            if (!checkPin(verify.getEmailPin(), userId)){
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            user.getValue1().getEmail().validate();
            database.updateUser(user.getValue0(), user.getValue1());

            return new ResponseEntity(HttpStatus.OK);
        } catch (DataAccessException | UnsupportedEncodingException | NoSuchAlgorithmException | ExpiredException  ex) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } catch (AlreadyExistsException ex) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }

    }
    
    /**
     *
     * @param email
     * @return
     */
    @RequestMapping(value = "/forgot_password", method=RequestMethod.POST)
    public ResponseEntity userForgotPassword(@RequestBody String email) {
        try {
            Pair<Integer, User> user = database.getUserByEmail(email);
            String pass = RandomStringUtils.randomAlphanumeric(8)+RandomStringUtils.randomAlphabetic(1)+RandomStringUtils.randomNumeric(1);
            Password password  = new Password(pass);
            user.getValue1().setPassword(password);
            database.updateUser(user.getValue0(), user.getValue1());
            sendEmail(user.getValue1().getEmail(), pass);
            return new ResponseEntity(HttpStatus.OK);
        } catch (DataAccessException | RecordNotFoundException | InvalidPasswordException | AlreadyExistsException ex) {
            return new ResponseEntity(HttpStatus.OK);
        }
    }
    
    /**
     *
     * @param userId
     * @param email
     * @param accessToken
     * @return
     */
    @RequestMapping(value = "/{user_id}/reverify_email", method=RequestMethod.POST)
    public ResponseEntity userReverifyEmail (@PathVariable("user_id") String userId,
                                            @RequestBody String email,
                                            @RequestHeader("Authorization") String accessToken) {
        try {
            String pin = generatePin(email);
            String subject = "Validatie emailadres Mobiligent.";
            String text = "Gelieve op onderstaande link te klikken om uw emailadres te valideren: \n"
               + "https://vopro6.ugent.be/app/#/validation?user="+email+"&pin="+pin;

            executor.execute(new MailThread(email, subject, text));

            return new ResponseEntity(HttpStatus.OK);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     *
     * @param userId
     * @param emails
     * @param accessToken
     * @return
     */
    @RequestMapping(value = "/{user_id}/change_email", method=RequestMethod.POST)
    public ResponseEntity userChangeEmail (@PathVariable("user_id") String userId,
                                           @RequestBody ChangeEmailDTO emails,
                                           @RequestHeader("Authorization") String accessToken) {
        try {
            User user = database.getUser(Integer.parseInt(userId));
            String email = emails.getNewEmail();
            if (user.getEmailAsString().equals(emails.getOldEmail())) {
                user.setEmail(email);
            } else {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }
            String token = TokenController.createRefreshToken(user.getEmailAsString(), user.getPassword().getStringPassword());
            user.setRefreshToken(token);
            user.getEmail().inValidate();
            database.updateUserEmail(Integer.parseInt(userId), user, emails.getOldEmail());
            
            String pin = generatePin(email);
            String subject = "Validatie emailadres Mobiligent.";
            String text = "Gelieve op onderstaande link te klikken om uw emailadres te valideren: \n"
               + "https://vopro6.ugent.be/app/#/validation?user="+email+"&pin="+pin;

            executor.execute(new MailThread(email, subject, text));
            
            String userurl = "/user/"+userId;
            String role = "user";
            
            if(user.isAdmin()) {
                role = "administrator";
            } else if(user.isOperator()) {
                role = "operator";
            }
            
            return new ResponseEntity(new RefreshTokenDTO(token, userId, userurl, role),HttpStatus.OK);
        } catch (InvalidPhoneNumberException | InvalidPasswordException
                | AddressException | NumberFormatException | DataAccessException | NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AlreadyExistsException ex) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } 
    }
    
    /**
     *
     * @param userId
     * @param passwords
     * @param accessToken
     * @return
     */
    @RequestMapping(value = "/{user_id}/change_password", method=RequestMethod.POST)
    public ResponseEntity userChangePassword (@PathVariable("user_id") String userId,
                                           @RequestBody ChangePasswordDTO passwords,
                                           @RequestHeader("Authorization") String accessToken) {
        try {
            User user = database.getUser(Integer.parseInt(userId));
            if (user.getPassword().checkSamePassword(passwords.getOldPassword())) {
                user.setPassword(passwords.getNewPassword());
            } else {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }
            String token = TokenController.createRefreshToken(user.getEmailAsString(), user.getPassword().getStringPassword());
            user.setRefreshToken(token);
            database.updateUser(Integer.parseInt(userId), user);
            
            String userurl = "/user/"+userId;
            String role = "user";
            
            if(user.isAdmin()) {
                role = "administrator";
            } else if(user.isOperator()) {
                role = "operator";
            }
            
            return new ResponseEntity(new RefreshTokenDTO(token, userId, userurl, role),HttpStatus.OK);
        } catch (InvalidPhoneNumberException | InvalidPasswordException | NumberFormatException | DataAccessException | NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AlreadyExistsException ex) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
    
    @RequestMapping(value = "/{user_id}/push_token", method=RequestMethod.POST)
    public ResponseEntity userRegisterPushToken (@PathVariable("user_id") String userId,
                                           @RequestBody String token) {
        try {
            User user = database.getUser(Integer.parseInt(userId));
            user.setPushToken(token);
            database.updateUser(Integer.parseInt(userId), user);
            return new ResponseEntity(HttpStatus.OK);
        } catch (DataAccessException ex) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AlreadyExistsException ex) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    private String generatePin(String email) throws UnsupportedEncodingException, NoSuchAlgorithmException{
        DateFormat f = new SimpleDateFormat("HH");
        String salt = f.format(new Date());
        byte [] saltbytes = salt.getBytes("UTF-8");

        String pepper = "F9J0dZLRhq7M@@G&9ypyX1%S9j#$56";
        byte [] pepperbytes = pepper.getBytes("UTF-8");

        byte [] emailbytes = email.getBytes("UTF-8");

        byte [] pinbytes = new byte[saltbytes.length + emailbytes.length + pepperbytes.length];
        System.arraycopy(saltbytes, 0, pinbytes, 0, saltbytes.length);
        System.arraycopy(emailbytes, 0, pinbytes, saltbytes.length, emailbytes.length);
        System.arraycopy(pepperbytes, 0, pinbytes, saltbytes.length+emailbytes.length, pepperbytes.length);

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(pinbytes);

        byte [] digest = md.digest();
        String hashed = String.format("%064x", new java.math.BigInteger(1, digest));

        char first = salt.charAt(0);
        char second = salt.charAt(1);

        return first+hashed+second;
    }

    private boolean checkPin(String pin, String email) throws UnsupportedEncodingException, NoSuchAlgorithmException, ExpiredException {
        String hourStr = ""+ pin.charAt(0) + pin.charAt(pin.length()-1);
        int hour = Integer.parseInt(hourStr);

        DateFormat f = new SimpleDateFormat("HH");
        String salt = f.format(new Date());

        byte [] saltbytes = hourStr.getBytes("UTF-8");

        String pepper = "F9J0dZLRhq7M@@G&9ypyX1%S9j#$56";
        byte [] pepperbytes = pepper.getBytes("UTF-8");

        byte [] emailbytes = email.getBytes("UTF-8");

        byte [] pinbytes = new byte[saltbytes.length + emailbytes.length + pepperbytes.length];
        System.arraycopy(saltbytes, 0, pinbytes, 0, saltbytes.length);
        System.arraycopy(emailbytes, 0, pinbytes, saltbytes.length, emailbytes.length);
        System.arraycopy(pepperbytes, 0, pinbytes, saltbytes.length+emailbytes.length, pepperbytes.length);

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(pinbytes);

        byte [] digest = md.digest();
        String hashed = String.format("%064x", new java.math.BigInteger(1, digest));

        char first = pin.charAt(0);
        char second = pin.charAt(pin.length()-1);

        return pin.equals(first+hashed+second);

    }

    private void sendEmail (Email to,  String password){
        String from = AppProperties.instance().getProp(AppProperties.PROP_KEY.MAIL_SERVER_FROM);

        String host = AppProperties.instance().getProp(AppProperties.PROP_KEY.MAIL_SERVER_HOST);

        String subject = "Nieuw wachtwoord account Mobiligent.";

        String text = "Het passwoord van je account is gereset naar: <strong>" + password+"</strong><br>"+
                "Gelieve dit te veranderen bij je eerstvolgende aanmelding!<br><br>"
                + "Indien u gebruik maakt van onze mobiele applicatie kan u ook <a href=\"vop.groep06.mobiligent://reset_password/"+to.getEmailAddress()+"/"+password+"\">hier</a> klikken "
                + "om het wachtwoord te veranderen!";

        Properties properties = new Properties();

        properties.put("mail.smtp.host", host);

        Session session = Session.getDefaultInstance(properties);

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            message.addRecipient(Message.RecipientType.TO, to.getEmailAddress());

            message.setSubject(subject);

            message.setContent(text, "text/html; charset=utf-8");
            Transport.send(message);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }

    }

}
