import model.User;
import repository.Database;
import repository.UserRepository;

import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        Database.init();
        //UserRepository.register(new User("abozzbzza", "abozzzba@abzoba.aboba", "abozba"));
    }
}
