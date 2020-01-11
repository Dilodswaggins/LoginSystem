package com.server.mail;
import java.util.Date;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class EmailSend {

    public String sendVerify(String email) {
        String dynamicCode = "" + (int)((Math.random() * 9 + 1) * 100000);
        String smsContent = "" +  dynamicCode;
        sendMail(email,smsContent,"验证码");
        return smsContent;
    }

    private static boolean sendMail(String emailTo,String content,String subject) {

        String fromEmail="772109738@qq.com";//你的QQ邮箱
        String eMailType="smtp.qq.com";
        String eMailAuthPassword="xbkqzlfmgxktbbia";//QQ邮箱授权码 开通POP3/SMTP服务 的授权码
        String body = content; //正文内容
        try {
            //创建会话
            Properties props = new Properties();
            props.put("mail.smtp.host",eMailType);//发件人使用发邮件的电子信箱服务器
            props.put("mail.password",eMailAuthPassword);
            props.put("mail.transport.protocol", "smtp");
            props.setProperty("mail.debug", "true");
            props.put("mail.smtp.auth","true"); //这样才能通过验证

            //下面这三句很重要，如果没有加入进去，qq邮箱会验证不成功，一直报503错误
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.port", "465");
            props.put("mail.smtp.socketFactory.port", "465");


            //获得默认的session对象
            Session mailSession = Session.getInstance(props);
            mailSession.setDebug(true);

            //构造消息
            MimeMessage mimeMessage = new MimeMessage(mailSession);

            InternetAddress from=new InternetAddress(fromEmail);
            mimeMessage.setFrom(from);
            InternetAddress to=new InternetAddress(emailTo); //设置收件人地址并规定其类型
            mimeMessage.setRecipient(Message.RecipientType.TO,to);

            mimeMessage.setSentDate(new Date()); 	//设置发信时间
            mimeMessage.setSubject(subject); 		//设置主题
            mimeMessage.setText(body); 				//设置 正文

            //给消息对象设置内容
            BodyPart bodyPart=new MimeBodyPart();					//新建一个存放信件内容的BodyPart对象
            bodyPart.setContent(body, "text/html;charset= GB2312");	//设置 发送邮件内容为HTML类型,并为中文编码

            Multipart multipart=new MimeMultipart();
            multipart.addBodyPart(bodyPart);

            mimeMessage.setContent(multipart);
            mimeMessage.saveChanges();

            //发送消息
            Transport transport=mailSession.getTransport("smtp");
            transport.connect(eMailType,fromEmail,eMailAuthPassword);//发邮件人帐户密码,此外是我的帐户密码，使用时请修改news.properties中的值 。
            transport.sendMessage(mimeMessage,mimeMessage.getAllRecipients());
            transport.close();
            mimeMessage.writeTo(System.out);//保存消息 并在控制台 显示消息对象中属性信息
            System.out.println("邮件已成功发送到 " + emailTo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            return false;
        }

    }
}