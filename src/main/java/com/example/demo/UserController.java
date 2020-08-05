package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private  UserRepository userRepository;
    @PostMapping ("/users")
    public UsersResponse createNewUser(@RequestBody NewUserRequest request){
        // Validate input
        // Create new user into database =>
        User user = new User();
        user.setName(request.getName());
        user.setAge(request.getAge());
        user=userRepository.save(user);
        return new UsersResponse(user.getId(),request.getName()+request.getAge());
    }
    @GetMapping("/users")
    public PagingResponse getAllUser(
            @RequestParam (defaultValue = "1") int page,
            @RequestParam (name = "item_per_page" ,defaultValue = "10") int itemPerPage) {
        PagingResponse pagingResponse = new PagingResponse(page, itemPerPage);
        List<UsersResponse> usersResponseList = new ArrayList<>();
        // Pagination with Spring Data JPA
        // ทดสอบโดย post ข้อมูล แล้วดูผลที่ http://localhost:8080/users?page=0&item_per_page=5
        Pageable pageable = PageRequest.of(page, itemPerPage);
        // get data from repository
        Iterable<User> users = userRepository.findAll(pageable);

        for (User user: users){
            usersResponseList.add(new UsersResponse(user.getId(), user.getName(), user.getAge()));
        }
        pagingResponse.setUsersResponse(usersResponseList);
        return pagingResponse;
    }

    @GetMapping("/users/{id}")
    public  UsersResponse getUserById(@PathVariable int id){
        Optional<User> user = userRepository.findById(id);
        return new UsersResponse(user.get().getId(),user.get().getName());
    }



}
