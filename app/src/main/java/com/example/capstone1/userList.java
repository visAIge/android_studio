package com.example.capstone1;

public class userList {

    private String id;
    private String password;
    private String name;
    private String otp_key;
    private String qr_code;

    public userList() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOtp_key() {
        return otp_key;
    }

    public void setOtp_key(String otp_key) {
        this.otp_key = otp_key;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }

    public userList(String id, String password, String name) {
        this.id = id;
        this.password = password;
        this.name = name;
    }
}
