package controllers;

import DTO.mappers.UserMapper;
import DTO.models.AccessTokenDTO;
import DTO.models.CredentialsDTO;
import DTO.models.RefreshTokenDTO;
import database_v2.controlLayer.Database;
import database_v2.exceptions.AlreadyExistsException;
import database_v2.exceptions.DataAccessException;
import database_v2.exceptions.RecordNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import models.exceptions.ExpiredException;
import models.users.User;
import org.javatuples.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles all requests regarding to refresh- & accesstokens
 */
@CrossOrigin
@RestController
public class TokenController {
    
    private final Database database;
    private static final String ACCESS_TOKEN_SALT = "94@7%b9TDfH*vI%aDykjaWfT5yZs9jzdW0RyyuXA!QT0P903#C";
    private static final String REFRESH_TOKEN_PEPPER = "k%3VFgR3IU9Sj&cvYGFli93yVwhMWde2a8k9naKBmA0*8Hq7zv";

    /**
     *
     * @param database Interface to communicate with the database.
     */
    public TokenController(Database database) {
        this.database = database;
    }
    
    /**
     * Makes & returns a refresh token for a certain user
     * @param credentials CredentialsDTO object with the email/password of the user
     * @return RefreshtokenDTO with the role & refreshtoken of the user
     */
    @RequestMapping(value = {"/refresh_token/regular"}, method = RequestMethod.POST)
    public ResponseEntity addRefreshToken(@RequestBody CredentialsDTO credentials) {
        try {
            Pair <Integer, User> userPair = database.getUserByEmail(credentials.getEmail());
            User user = userPair.getValue1();
            if(!user.getPassword().checkSamePassword(credentials.getPassword())){
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
            
            String token;
            if(user.getRefreshToken() == null) {
                token = createRefreshToken(credentials.getEmail(), credentials.getPassword());
                user.setRefreshToken(token);
                database.updateUser(userPair.getValue0(), user);
            }else{
                token = user.getRefreshToken();
            }
            String userid = Integer.toString(userPair.getValue0());
            String userurl = "/user/"+userid;
            String role = "user";
            
            if(user.isAdmin()) {
                role = "administrator";
            } else if(user.isOperator()) {
                role = "operator";
            }
            
            
            return new ResponseEntity(new RefreshTokenDTO(token, userid , userurl, role), HttpStatus.OK);
        } catch (DataAccessException | NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } catch (AlreadyExistsException ex) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }
    
    /**
     * Creates & sends an access token for an user
     * @param refreshToken The refreshtokenDTO of this user
     * @param refreshTokenHeader The token from the refreshtokenDTO as an extra check
     * @return AccessTokenDTO with the accesstoken & expire time of this token
     */
    @RequestMapping(value= {"/access_token"}, method = RequestMethod.POST)
    public ResponseEntity addAccessToken (@RequestBody RefreshTokenDTO refreshToken,
                                           @RequestHeader("Authorization") String refreshTokenHeader) {
        try {
            int userId = Integer.parseInt(refreshToken.getUserId());
            User user = database.getUser(userId);
            
            if(!refreshToken.getToken().equals(refreshTokenHeader) || !user.getRefreshToken().equals(refreshToken.getToken())) {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
            
            Pair<String, String> accessToken = createAccessToken(user.getEmailAsString(), refreshToken.getUserId());
            
            return new ResponseEntity(new AccessTokenDTO(accessToken.getValue0(), accessToken.getValue1()), HttpStatus.OK);
        } catch (DataAccessException | NoSuchAlgorithmException ex) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        
    }
    
    /**
     * Method to create an accesstoken
     * @param email email of the user
     * @param userid id of the user
     * @return Pair of accesstoken & expire time in milliseconds
     * @throws NoSuchAlgorithmException should never be thrown
     */
    protected static Pair<String, String> createAccessToken (String email, String userid) throws NoSuchAlgorithmException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR, +1);
       
        Long time = cal.getTimeInMillis();
        String timeStr = Long.toString(time);
        
        byte [] accessSalt = ACCESS_TOKEN_SALT.getBytes();
        byte [] emailBytes = email.getBytes();
        byte [] timeBytes = timeStr.getBytes();
        
        byte [] accessBytes = new byte [accessSalt.length + emailBytes.length + timeBytes.length];
        System.arraycopy(accessSalt,  0, accessBytes, 0, accessSalt.length);
        System.arraycopy(emailBytes, 0, accessBytes, accessSalt.length, emailBytes.length);
        System.arraycopy(timeBytes, 0, accessBytes, accessSalt.length + emailBytes.length, timeBytes.length);
        
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(accessBytes);
        
        byte [] digest = md.digest();
        
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
        Date date = cal.getTime();
        
        String isoTime = df.format(date);
        return new Pair(userid+"&t="+String.format("%064x", new java.math.BigInteger(1, digest))+"&tm="+timeStr, isoTime);
    }
    
    /**
     * Method to create a refresh token
     * @param email email of the user
     * @param password password of the user
     * @return the refreshtoken as string
     * @throws NoSuchAlgorithmException should never be thrown
     */
    protected static String createRefreshToken (String email, String password) throws NoSuchAlgorithmException {
        SecureRandom rnd = new SecureRandom();
        byte [] salt = new byte [32];
        rnd.nextBytes(salt);
        
        byte [] emailbytes = email.getBytes();
        byte [] passwordbytes = password.getBytes();
        byte [] pepperbytes = REFRESH_TOKEN_PEPPER.getBytes();
        
        byte [] tokenbytes = new byte [salt.length + emailbytes.length + passwordbytes.length + pepperbytes.length];
        
        System.arraycopy(salt, 0, tokenbytes, 0, salt.length);
        System.arraycopy(emailbytes, 0, tokenbytes, salt.length, emailbytes.length);
        System.arraycopy(passwordbytes, 0, tokenbytes, salt.length+emailbytes.length, passwordbytes.length);
        System.arraycopy(pepperbytes, 0, tokenbytes, salt.length+emailbytes.length + passwordbytes.length, pepperbytes.length);
        
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(tokenbytes);
        
        byte [] digest = md.digest();
        
        return String.format("%064x", new java.math.BigInteger(1, digest));
    }
    
    /**
     * Method to check an accesstoken
     * @param accessToken accesstoken given in the header
     * @param email email of the user
     * @return true if the accesstoken is valid, false otherwise
     * @throws ParseException should never be thrown
     * @throws NoSuchAlgorithmException should never be thrown
     * @throws ExpiredException exception if the accesstoken has expired
     */
    protected static boolean validateToken (String accessToken, String email) throws ParseException, NoSuchAlgorithmException, ExpiredException {
        String [] userid = accessToken.split("&t=");
        String [] token = userid[1].split("&tm=");
        String timeStr = token[1];
        Long ttl = Long.parseLong(timeStr);
        Date date = new Date(ttl);
        Date dateNow = new Date();
        
        if(dateNow.after(date)) {
            throw new ExpiredException();
        }
        
        byte [] accessSalt = ACCESS_TOKEN_SALT.getBytes();
        byte [] emailBytes = email.getBytes();
        byte [] timeBytes = timeStr.getBytes();
        
        byte [] accessBytes = new byte [accessSalt.length + emailBytes.length + timeBytes.length];
        System.arraycopy(accessSalt,  0, accessBytes, 0, accessSalt.length);
        System.arraycopy(emailBytes, 0, accessBytes, accessSalt.length, emailBytes.length);
        System.arraycopy(timeBytes, 0, accessBytes, accessSalt.length + emailBytes.length, timeBytes.length);
        
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(accessBytes);
        
        byte [] digest = md.digest();
        
        String refToken = String.format("%064x", new java.math.BigInteger(1, digest));
        
        return refToken.equals(token[0]);
    }
}
