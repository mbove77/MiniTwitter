
package com.bove.martin.minitwitter.retrofit.response;

import com.bove.martin.minitwitter.model.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeletedTweet {

    @SerializedName("mensaje")
    @Expose
    private String mensaje;
    @SerializedName("user")
    @Expose
    private User user;

    /**
     * No args constructor for use in serialization
     * 
     */
    public DeletedTweet() {
    }

    /**
     * 
     * @param mensaje
     * @param user
     */
    public DeletedTweet(String mensaje, User user) {
        super();
        this.mensaje = mensaje;
        this.user = user;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
