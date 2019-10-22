package com.myself.collection.sqlite.entity;

import com.myself.collection.sqlite.annotation.DBTable;
import com.myself.collection.sqlite.annotation.DBField;

/**
 * @author Jams
 * @date 2019/10/22 15:31
 * @des todo
 * @
 * @upAuthor
 * @upDate 2019/10/22 15:31
 * @upDes todo
 */
@DBTable("tb_person")
public class Person {
    @DBField("tb_name")
    public String name;
    @DBField("tb_password")
    public long password;
    @DBField("tb_photo")
    public byte[] photo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPassword() {
        return password;
    }

    public void setPassword(long password) {
        this.password = password;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}
