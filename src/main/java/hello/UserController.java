package hello;

import com.fasterxml.jackson.databind.ser.Serializers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController extends BaseController{

    @RequestMapping("/users")
    public ResponseEntity getUsers() {
        ArrayList<User> users = new ArrayList<>();
        SQLiteRepository sqLiteRepository = new SQLiteRepository();
        ResultSet rs;
        try {
            rs = sqLiteRepository.displayUsers();
            while (rs.next()) {
                users.add(new User(rs.getInt("id"), rs.getString("name"), rs.getString("password")));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return getErrorResponse(e);
        }

        return ResponseEntity.status(200).body(users);
    }

    @RequestMapping("/addUser")
    public ResponseEntity addUser(@RequestParam(value = "name", defaultValue = "") String fName,
                                  @RequestParam(value = "password", defaultValue = "") String lName) {
        SQLiteRepository sqLiteRepository = new SQLiteRepository();
        try {
            sqLiteRepository.addUser(fName, lName);
        } catch (SQLException e) {
            e.printStackTrace();
            return getErrorResponse(e);
        }

        return ResponseEntity.status(200).build();
    }

}