package com.xkit.ssm.handler;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.xkit.ssm.entity.Easybuy_user;
import com.xkit.ssm.service.Easybuy_userService;
import com.xkit.ssm.validator.UserLogin;
import com.xkit.ssm.validator.UserRegist;
@Controller
public class UserHandler {
	@Autowired
	private Easybuy_userService userService;
	
	@RequestMapping("/login")
	public String login(@Validated(value={UserLogin.class}) Easybuy_user param ,BindingResult r, Model m,HttpSession session) throws Exception{
		if (r.hasFieldErrors()) {
			return "login";
		}
		Easybuy_user user = userService.UserLogin(param.getLoginName(),param.getPassword());
		if (user==null) {
			m.addAttribute("msg","µÇÂ½Ê§°Ü£¡");
			return "login";
		}else{
			m.addAttribute(user);
			session.setAttribute("loginUser", user);
			return "welcome";
		}
	}
	@RequestMapping("/reg")
	public String regist(@Validated(value=UserRegist.class) Easybuy_user user,BindingResult r,Model m) throws Exception{
		if (r.hasFieldErrors()) {
			return "regist";
		}else{
			boolean isok=userService.addUser(user);
			if (isok) {
				m.addAttribute("msg","×¢²á³É¹¦£¡");
				return "login";
			}else{
				m.addAttribute("msg","×¢²áÊ§°Ü£¡");
				return "regist";
			}
		}
	}
	@RequestMapping(value="/checkLoginName")
	public @ResponseBody String checkLoginName(String zh) throws Exception{
		boolean isok=userService.checkLoginUsed(zh);
		String rs="{\"msg\":0}";
		if (isok) {
			rs="{\"msg\":1}";
		}
		return rs;
	}
	@RequestMapping(value="/showUserList")
	public @ResponseBody List<Easybuy_user> showUserList() throws Exception{
		List<Easybuy_user> list = userService.queryUser();
		return list;
	}
	
}
