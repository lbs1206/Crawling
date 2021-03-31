package com.hanil.fluxus.user.controller;

import com.hanil.fluxus.user.mapper.UserMapper;
import com.hanil.fluxus.user.model.UserLogin;
import com.hanil.fluxus.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final HttpSession httpSession;
    private final UserMapper userMapper;

    static String setUrl="/api/user";


    @Autowired
    private UserService service;


    @PostMapping("/api/user/login")
    public Map<String,Object> login( @RequestBody @Valid UserLogin userLogin){
        //API 통계
        Map<String,Object> anal_map = new HashMap<String,Object>();
        anal_map.put("api_name", setUrl+"/login");
        anal_map.put("param", userLogin);

        Map<String,Object> inputs = new HashMap<>();
        Map<String,Object> result = new HashMap<>();

        try {
            inputs.put("user_id", userLogin.getEmail());
            inputs.put("password", userLogin.getPassword());
            result = service.app_login(inputs);

            return result;
        }catch (Exception e) {
            // TODO: handle exception
            result.put("resultCode", "400");
            result.put("msg", "FALSE");
            return result;
        }
    }

}
