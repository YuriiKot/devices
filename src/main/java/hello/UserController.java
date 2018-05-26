package hello;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class UserController extends BaseController {

    @RequestMapping("/users")
    public ResponseEntity getUsers() {
        ArrayList<User> users = new ArrayList<>();
        SQLiteRepository sqLiteRepository = new SQLiteRepository();
        ResultSet rs;
        try {
            rs = sqLiteRepository.displayUsers();
            while (rs.next()) {
                users.add(new User(rs.getInt("id"), rs.getString("username"), rs.getString("password")));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return getErrorResponse(e);
        }

        return ResponseEntity.status(200).body(users);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping("/devices")
    public ResponseEntity getDevices(String username, String password) {
        ArrayList<Device> devices = new ArrayList<>();
        SQLiteRepository sqLiteRepository = new SQLiteRepository();
        try {
            devices.addAll(resultSetToArrayList(sqLiteRepository.displayDevices("yurii", "kot")));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return getErrorResponse(e);
        }

        return ResponseEntity.status(200).body(devices);
    }

    public List resultSetToArrayList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        List<Map<String, Object>> list = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> row = new HashMap<>(columns);
            for (int i = 1; i <= columns; ++i) {
                row.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(row);
        }

        return list;
    }

    @RequestMapping("/addUser")
    public ResponseEntity addUser(@RequestParam String fName,
                                  @RequestParam String lName) {
        SQLiteRepository sqLiteRepository = new SQLiteRepository();
        try {
            sqLiteRepository.addUser(fName, lName);
        } catch (SQLException e) {
            e.printStackTrace();
            return getErrorResponse(e);
        }

        return ResponseEntity.status(200).build();
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping("/updateTargetTemperature")
    public ResponseEntity updateTargetTemperature(@RequestParam String username, @RequestParam String password,
                                                  @RequestParam Integer deviceId, @RequestParam Integer temp) {
        SQLiteRepository sqLiteRepository = new SQLiteRepository();
        try {
            if (temp == null) throw new RuntimeException("Temperature shouldn't be null");
            sqLiteRepository.updateDeviceTemp(username, password, deviceId, temp, null);
        } catch (Exception e) {
            e.printStackTrace();
            return getErrorResponse(e);
        }

        return ResponseEntity.status(200).build();
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping("/updateRealTemperature")
    public ResponseEntity updateRealTemperature(@RequestParam String username, @RequestParam String password,
                                                @RequestParam Integer deviceId, @RequestParam Integer temp) {
        SQLiteRepository sqLiteRepository = new SQLiteRepository();
        try {
            sqLiteRepository.updateDeviceTemp(username, password, deviceId, null, temp);
        } catch (SQLException e) {
            e.printStackTrace();
            return getErrorResponse(e);
        }

        return ResponseEntity.status(200).build();
    }


}