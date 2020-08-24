package com.tai06.dothetai.fdapp.SendCode;


import com.tai06.dothetai.fdapp.URL.Link;

public class SendGmail {
    private static final String GMAIL_ADMIN = "dothetaind00@gmail.com";
    private static final String PASSWORD_GMAIL = "ductai2066";

    public void send(String msg, String gmail) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GmailSender mailSender = new GmailSender(GMAIL_ADMIN, PASSWORD_GMAIL);
                    mailSender.sendMail(Link.SUBJECT_SEND, msg,
                            GMAIL_ADMIN, gmail);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
