package com.danis.servlet;

import com.danis.dto.CreateUserDto;
import com.danis.entity.Gender;
import com.danis.entity.Role;
import com.danis.service.UserService;
import com.danis.util.JSPHelper;
import com.danis.util.UrlPath;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {
    UserService userService = UserService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("roles", Role.values());
        req.setAttribute("genders", Gender.values());

        req.getRequestDispatcher(JSPHelper.getPath("registration"))
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        CreateUserDto userDto = CreateUserDto.builder()
                .username(req.getParameter("username"))
                .role(Role.findByName(req.getParameter("role")).orElse(null))
                .birthday(LocalDate.parse(req.getParameter("birthday")))
                .gender(Gender.findByName(req.getParameter("gender")).orElse(null))
                .email(req.getParameter("email"))
                .password(req.getParameter("password"))
                //.image(req.getPart("image"))
                .city(req.getParameter("city"))
                .build();

        userService.create(userDto);
        resp.sendRedirect(UrlPath.LOGIN);

        System.out.println(userDto.toString());
    }
}
