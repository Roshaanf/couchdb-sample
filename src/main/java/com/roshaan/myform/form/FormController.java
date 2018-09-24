package com.roshaan.myform.form;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FormController {

	@Autowired
	CouchService service;

	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public String customerForm(Model model) {

		model.addAttribute("user", new User());

		return "form";
	}

	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String customerSubmit(@ModelAttribute User user, Model model) {

		model.addAttribute("user", user);

		try {

//			if fields are non empty
			if (user.getFirstname() != "" && user.getLastname() != "") {
				if (!service.checkDbExist(service.DATABASE_NAME)) {
					service.createDatabase(service.DATABASE_NAME);
				}

//			adding record to database
				service.addData(user.getFirstname(), user.getLastname());

			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String info = String.format("User Submission: firstname = %s, lastname = %s", user.getFirstname(),
				user.getLastname());

		System.out.println(info);

		return "result";
	}

}
