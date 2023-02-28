package com.driver.service.impl;

import com.driver.model.request.UserDetailsRequestModel;
import com.driver.model.response.OperationStatusModel;
import com.driver.model.response.UserResponse;
import com.driver.service.UserService;
import com.driver.shared.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl {

    @Autowired
    UserService userService;
    private UserResponse getUserResponse(UserDto userDto) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(userDto.getUserId());
        userResponse.setEmail(userDto.getEmail());
        userResponse.setFirstName(userDto.getFirstName());
        userResponse.setLastName(userDto.getLastName());

        return userResponse;
    }

    @GetMapping(path = "/{id}")
    public UserResponse getUser(@PathVariable String id) throws Exception{
        UserDto userDto = userService.getUserByUserId(id);

        return getUserResponse(userDto);
    }

    @PostMapping()
    public UserResponse createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception{
        UserDto userDto = new UserDto();
        userDto.setFirstName(userDetails.getFirstName());
        userDto.setLastName(userDetails.getLastName());
        userDto.setEmail(userDetails.getEmail());

        userDto = userService.createUser(userDto);

        return getUserResponse(userDto);
    }

    @PutMapping(path = "/{id}")
    public UserResponse updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) throws Exception{
        UserDto userDto = userService.getUserByUserId(id);
        userDto.setEmail(userDetails.getEmail());
        userDto.setFirstName(userDetails.getFirstName());
        userDto.setLastName(userDetails.getLastName());

        userDto = userService.updateUser(id, userDto);

        return getUserResponse(userDto);
    }

    @DeleteMapping(path = "/{id}")
    public OperationStatusModel deleteUser(@PathVariable String id) throws Exception{
        userService.deleteUser(id);
        return new OperationStatusModel();
    }

    @GetMapping()
    public List<UserResponse> getUsers(){
        List<UserDto>userDtoList = userService.getUsers();
        List<UserResponse> userResponses = new ArrayList<>();
        for (UserDto userDto: userDtoList){
            userResponses.add(getUserResponse(userDto));
        }

        return userResponses;
    }
}