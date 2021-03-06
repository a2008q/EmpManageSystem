package com.a2008q.crud.utils;

import org.springframework.core.io.ClassPathResource;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 应用模块名称<p>
 * 代码描述<p>
 *
 * @author a2008q
 * @since 2021/8/22 11:45
 */
public class MailUtils {

    /**
     * @param toUser   发送邮件给谁
     * @param title    邮件的标题
     * @param emailMsg 邮件信息
     */
    public static void sendMail(String toUser, String title, String emailMsg) {
        // 1.创建一个程序与邮件服务器会话对象 Session
        Properties props = new Properties();
        InputStream in = null;
        System.out.println(new ClassPathResource("email.properties"));
        try {
            in = new ClassPathResource("email.properties").getInputStream();
            props.load(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String account = props.getProperty("mail.account");
        String password = props.getProperty("mail.password");


        // 创建验证器
        Authenticator auth = new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                //设置发送人的帐号和密码      帐号		   授权码
                return new PasswordAuthentication(account, password);
            }
        };
        try {
            //会话
            Session session = Session.getInstance(props, auth);

            // 2.创建一个Message，它相当于是邮件内容
            Message message = new MimeMessage(session);

            //设置发送者
            message.setFrom(new InternetAddress(account));

            //设置发送方式与接收者
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(toUser));

            //设置邮件主题
            message.setSubject(title);
            // message.setText("这是一封激活邮件，请<a href='#'>点击</a>");

            //设置邮件内容
            message.setContent(emailMsg, "text/html;charset=utf-8");

            // 3.创建 Transport用于将邮件发送
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
