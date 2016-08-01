package DTO.mappers;

import DTO.models.UserDTO;
import DTO.models.ValidatedDTO;
import javax.mail.internet.AddressException;
import models.exceptions.InvalidPasswordException;
import models.services.Email;
import models.users.User;

/**
 * Class to convert an user object from/into an userDTO (json) object
 */
public class UserMapper {

    /**
     * Method to convert User into UserDTO
     * @param user User object to convert
     * @param id id of the user
     * @return UserDTO object
     */
    public UserDTO convertToDTO(User user, String id) {

        Email email = user.getEmail();
        ValidatedDTO validated = new ValidatedDTO(email.isValidated());
        
        UserDTO userdto = new UserDTO(id,
                user.getEmail().getEmailAddress().toString(),
                user.isMuted(),
                validated);
        
        if (user.getFirstName()!=null) {
            userdto.setFirstName(user.getFirstName());
        }
        
        if (user.getLastName()!=null) {
            userdto.setLastName(user.getLastName());
        }
        
        userdto.setPassword(""); //adjust?
        
        return userdto;
    }

    /**
     * Method to convert User into UserDTO
     * @param user User object to convert
     * @param id id of the user
     * @return UserDTO object
     */
    public UserDTO convertToDTO(User user, int id) {
        return convertToDTO(user, Integer.toString(id));
    }

    /**
     * Method to convert UserDTO into User
     * @param userdto UserDTO object to convert
     * @param update boolean if it is a new entry (POST) or an update (PUT) request
     * @return User object
     * @throws InvalidPasswordException
     * @throws AddressException
     */
    public User convertFromDTO(UserDTO userdto, boolean update)
            throws InvalidPasswordException, AddressException {
        
        //TEMP
        if (update) {
            userdto.setPassword("default123");
        }
        
        User user = new User(
                userdto.getFirstName(),
                userdto.getLastName(),
                userdto.getPassword(),
                userdto.getEmail(),
                userdto.isMuteNotifications()
        );
        
        if (userdto.getValidated().isEmail()) {
            user.getEmail().validate();
        }
        
        return user;
    }

}
