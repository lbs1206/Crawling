package com.hanil.fluxus.user.service;

import com.hanil.fluxus.user.mapper.UserMapper;
import com.hanil.fluxus.user.model.UserLoginToken;
import com.hanil.fluxus.user.model.UserSignUp;
import com.hanil.fluxus.util.JWTUtils;
import com.hanil.fluxus.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserService implements UserDetailsService {

    @Autowired
    private UserMapper mapper;



    StringUtil util = new StringUtil();


    // 회원가입
    @Transactional
    public String signUp(UserSignUp usu) {
        Map<String,Object> inputs = new HashMap<String,Object>();
        inputs.put("user_id",usu.getUserid());
        inputs.put("user_name",usu.getUsername());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        inputs.put("password",passwordEncoder.encode(usu.getPassword()));
        // password를 암호화 한 뒤 db에 저장
        int insert = 0;

        insert = mapper.insertUserJoin(inputs);

        return usu.getUserid();

    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 로그인을 하기 위해 가입된 user정보를 조회하는 메서드
        Map<String,Object> inputs = new HashMap<String,Object>();
        inputs.put("user_id",username);
        Map<String, Object> getLoginInfo = mapper.getLoginInfo(inputs);

        List<GrantedAuthority> authorities = new ArrayList<>();

        if("admin".equals(username)){
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
        }

        // 아이디, 비밀번호, 권한리스트를 매개변수로 User를 만들어 반환해준다.
        User user = new User(getLoginInfo.get("USER_ID").toString(),
                             getLoginInfo.get("PASSWORD").toString()
                            ,authorities);


        return user;
    }




    public Map<String, Object> app_login(Map<String, Object> inputs) {
        // TODO Auto-generated method stub
        Map<String, Object> result = new HashMap<String, Object>();
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss");
        String NEW_TEMP_KEY = util.encryptionSHA256(StringUtil.stringNullChg(inputs.get("user_id"), "") + format.format(today));
        //String password = util.encryptionSHA256(StringUtil.stringNullChg(inputs.get("password"), ""));
        String password = inputs.get("password").toString();
//		if(mapper.getUserCnt(inputs) > 0) {
        //로그인 정보 조회
        Map<String, Object> getLoginInfo = mapper.getLoginInfo(inputs);
        UserSignUp user = new UserSignUp(getLoginInfo.get("USER_ID").toString(),
                                         getLoginInfo.get("USER_NAME").toString(),
                                         getLoginInfo.get("PASSWORD").toString());
        //가입유무 확인
        if(getLoginInfo.size() <= 0) {
            result.put("resultCode", "400");
            result.put("msg", "NOT_JOIN_USER");
        }else {
            //패스워드 일치 확인
            if(!password.equals(String.valueOf(getLoginInfo.get("PASSWORD")))) {
                result.put("resultCode", "400");
                result.put("msg", "PW_NOT_MATCH");
            }else {
                //자동로그인 임시키 발급
                inputs.put("temp_key", NEW_TEMP_KEY);
                UserLoginToken userLoginToken = JWTUtils.createToken(user);
                int temp_key_update = mapper.setTempKey(inputs);
                if(temp_key_update > 0) {
                    result.put("resultCode", "200");
                    result.put("msg", "OK");
                    result.put("temp_key", NEW_TEMP_KEY);
                    result.put("user_key", StringUtil.stringNullChg(getLoginInfo.get("USER_KEY"), ""));
                    result.put("push_key", StringUtil.stringNullChg(getLoginInfo.get("PUSH_KEY"), ""));
                    result.put("jwt_token",userLoginToken.getToken());
                }else {
                    result.put("resultCode", "400");
                    result.put("msg", "TEMP_KEY_ADD_FALSE");
                }
            }
        }
//		}else {
//			result.put("resultCode", "406");
//			result.put("msg", "NOT_JOIN_USER");
//		}

        return result;
    }


}
